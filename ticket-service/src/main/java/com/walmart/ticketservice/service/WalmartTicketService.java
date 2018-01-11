package com.walmart.ticketservice.service;

import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;

import com.walmart.ticketservice.dao.WalmartTicketServiceDao;
import com.walmart.ticketservice.entity.tables.Customer;
import com.walmart.ticketservice.entity.tables.Row;
import com.walmart.ticketservice.entity.tables.SeatHold;
import com.walmart.ticketservice.entity.tables.SeatReserve;
import com.walmart.ticketservice.exception.EmailNotValidException;
import com.walmart.ticketservice.exception.NoSeatsAvailableException;
import com.walmart.ticketservice.exception.SeatHoldExpiredException;
import com.walmart.ticketservice.exception.SeatHoldNotValidException;
import com.walmart.ticketservice.exception.SeatReserveNotValidException;

@Service
public class WalmartTicketService implements TicketService{

	private static final Logger LOGGER = LogManager.getLogger(WalmartTicketService.class);
	private static final int EXPIRE_TIME_SECONDS = 120;
	
	private WalmartTicketServiceDao walmartTicketServiceDao;
	private RequestValidator requestValidator;
	
	public WalmartTicketService(WalmartTicketServiceDao walmartTicketServiceDao, RequestValidator requestValidator) {
		this.walmartTicketServiceDao = walmartTicketServiceDao;
		this.requestValidator = requestValidator;
	}
	
	@Override
	public int numSeatsAvailable() {
		LOGGER.debug("Finding number of available seats.");
		walmartTicketServiceDao.removeExpiredSeats();
		final int availableSeats = walmartTicketServiceDao.findAvailableSeats();
		LOGGER.debug("Number of available seats are {}.", availableSeats);
		return availableSeats;
	}
	
	public int numSeatsAvailableByRow(char row) {
		LOGGER.debug("Finding number of available seats by row.");
		walmartTicketServiceDao.removeExpiredSeats();
		final int availableSeats = walmartTicketServiceDao.findAvailableSeatsByRow(row);
		LOGGER.debug("Number of available seats for row {} is {}.", row, availableSeats);
		return availableSeats;
	}
	

	@Override
	public SeatHold findAndHoldSeats(int numSeats, String customerEmail) {
		LOGGER.debug("Finding and holding best available seats. {} requested {} seats.", customerEmail, numSeats);
		
		// validate request
		requestValidator.throwExceptionIfSeatHoldBadRequest(numSeats, customerEmail);
		
		// register customer
		Customer customer = walmartTicketServiceDao.findCustomer(customerEmail);
		if(customer == null) {
			customer = new Customer();
			customer.setEmail(customerEmail);
			walmartTicketServiceDao.saveCustomer(customer);
		}
		
		SeatHold seatHold = new SeatHold();
		seatHold.setCustomerEmail(customerEmail);
		seatHold.setCustomerId(customer.getId());
		
		// Go through each row, find as many seats as you can
		int numSeatsOnHoldSoFar = 0;
		List<SeatReserve> listOfSeatsOnHold = new ArrayList<SeatReserve>();
		List<Row> listOfRows = walmartTicketServiceDao.getAllRows();
		for(Row row : listOfRows) {
			SeatReserve seatReserve = new SeatReserve();
			int availableSeatsInRow = walmartTicketServiceDao.findAvailableSeatsByRow(row.getRowName());
			if(availableSeatsInRow > 0) {
				int neededSeats = numSeats - numSeatsOnHoldSoFar;
				if(availableSeatsInRow >= neededSeats) {
					walmartTicketServiceDao.takeSeatsFromRow(neededSeats, row.getRowName());
					seatReserve.setNumSeats(neededSeats);
					numSeatsOnHoldSoFar += neededSeats;
				}else {
					walmartTicketServiceDao.takeSeatsFromRow(availableSeatsInRow, row.getRowName());
					seatReserve.setNumSeats(availableSeatsInRow);
					numSeatsOnHoldSoFar = numSeatsOnHoldSoFar + availableSeatsInRow;
				}
				seatReserve.setRowName(row.getRowName());
				seatReserve.setRowId(row.getRowId());
				listOfSeatsOnHold.add(seatReserve);
			}
			if(numSeatsOnHoldSoFar == numSeats)
				break;
		}
		
		if(numSeatsOnHoldSoFar == 0) {
			throw new NoSeatsAvailableException("There are no seats available.");
		}
		
		seatHold.setSeatInRows(listOfSeatsOnHold);
		seatHold.setExpireTime(expireTime());
		walmartTicketServiceDao.addSeatHold(seatHold);
		
		LOGGER.debug("Number of seats on hold: {} for {}.", numSeats, customerEmail);
		return seatHold;
	}

	@Override
	public String reserveSeats(int seatHoldId, String customerEmail) {
		LOGGER.debug("Reserving seats for {} with Hold Id {}", customerEmail, seatHoldId);
		requestValidator.throwExceptionIfSeatReserveBadRequest(seatHoldId, customerEmail);
		
		SeatHold seatHold = walmartTicketServiceDao.findSeatHoldById(seatHoldId);
		if(seatHold == null || StringUtils.isNotEmpty(seatHold.getConfirmationCode())) {
			throw new SeatHoldNotValidException("SeatHold: " + seatHoldId + " was not found.");
		}
		
		Customer customer = walmartTicketServiceDao.findCustomer(customerEmail);
		if(customer == null) {
			throw new EmailNotValidException("Email: " + customerEmail + " was not found.");
		}
		
		if(!customerEmail.equalsIgnoreCase(seatHold.getCustomerEmail())) {
			throw new SeatReserveNotValidException("Email: " + customerEmail + ", does not have a valid hold id: " + seatHoldId +".") ;
		}
		
		if(isExpired(seatHold)) {
			throw new SeatHoldExpiredException("SeatHold " + seatHoldId + " is expired.");
		}
		
		String confirmationCode = generateConfirmationId(); 
		seatHold.setConfirmationCode(confirmationCode);
		walmartTicketServiceDao.saveSeatHold(seatHold);
		
		LOGGER.debug("Reserved seats for {} with Confirmation code: ", customerEmail, confirmationCode);
		return confirmationCode;
	}
	
	public boolean isExpired(SeatHold seatHold) {
		if(seatHold.getExpireTime() == null)
			return false;
		
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		Timestamp expiredTime = seatHold.getExpireTime();
		return currentTime.after(expiredTime);
	}
	
	public Timestamp expireTime() {
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		Instant toBeExpireTime = currentTime.toInstant().plusSeconds(EXPIRE_TIME_SECONDS);
		Timestamp expireTime = Timestamp.from(toBeExpireTime);
		return expireTime;
	}
	
	public String generateConfirmationId() {
		return UUID.randomUUID().toString();
	}

}
