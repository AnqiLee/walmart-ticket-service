package com.walmart.ticketservice.dao;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.walmart.ticketservice.entity.tables.Customer;
import com.walmart.ticketservice.entity.tables.Row;
import com.walmart.ticketservice.entity.tables.SeatHold;
import com.walmart.ticketservice.entity.tables.SeatReserve;

@Repository
public class WalmartTicketServiceDao implements TicketServiceDao {

	private static final Logger LOGGER = LogManager.getLogger(WalmartTicketServiceDao.class);
	
	public int findAvailableSeats() {
		
		int totalSeats = 0;
		for(Row row : FakeData.rows) {
			totalSeats += row.getNumberOfSeat();
		}
		
		return totalSeats;
	}
	
	public int findAvailableSeatsByRow(char row) {
		LOGGER.debug("Finding available seats for row {}.", row);
		int totalSeats = 0;
		for(Row rowObj : FakeData.rows) {
			if(row == rowObj.getRowName()) {
				return rowObj.getNumberOfSeat();
			}
		}
		
		return totalSeats;
	}
	
	public Row findRowById(int rowId) {
		for(Row row : FakeData.rows) {
			if(rowId == row.getRowId())
				return row; 
		}
		return null;
	}
	
	public void takeSeatsFromRow(int numSeats, char row) {
		LOGGER.debug("Taking {} seats from row {}.", numSeats, row);
		
		for(Row rowObj : FakeData.rows) {
			if(row == rowObj.getRowName()) {
				int numSeatsAvailable = rowObj.getNumberOfSeat();
				rowObj.setNumberOfSeat(numSeatsAvailable-numSeats);
			}
		}
	}
	
	public List<Row> getAllRows(){
		return FakeData.rows;
	}
	
	public void addSeatHold(SeatHold newSeatHold) {
		
		int holdId = FakeData.holdId;
		FakeData.holdId++;
		newSeatHold.setId(holdId);
		
		for(SeatReserve seatReserve : newSeatHold.getSeatInRows()) {
			seatReserve.setHoldId(holdId);
			seatReserve.setId(FakeData.seatReserveId++);
		}
		
		LOGGER.debug("Adding SeatHold {}.", newSeatHold);
		FakeData.seatHoldList.add(newSeatHold);
	}
	
	public void saveSeatHold(SeatHold seatHold) {
		LOGGER.debug("Saving SeatHold {}.", seatHold);
		FakeData.seatHoldList.add(seatHold);
	}
	
	public void deleteSeatHold(SeatHold seatHold) {
		LOGGER.debug("Deleting SeatHold {}.", seatHold);
		FakeData.seatHoldList.remove(seatHold);
	}
	
	public SeatHold findSeatHoldById(int seatHoldId) {
		LOGGER.debug("Finding SeatHold by id {}.", seatHoldId);
		for(SeatHold seatHold : FakeData.seatHoldList) {
			if(seatHold.getId() == seatHoldId) {
				return seatHold;
			}
		}
		
		return null;
	}
	
	
	public void removeExpiredSeats() {
		LOGGER.debug("Removing expired seats on hold.");
		
		Timestamp currenTime = new Timestamp(System.currentTimeMillis());

		// go through each seatHold object and find the expired ones
		Iterator<SeatHold> seatHoldIter = FakeData.seatHoldList.iterator();
		while(seatHoldIter.hasNext()) {
			SeatHold seatHold = seatHoldIter.next();
			if(currenTime.after(seatHold.getExpireTime())) {
				// remove all the seats on hold and add them back to Rows
				Iterator<SeatReserve> seatReserveIter = seatHold.getSeatInRows().iterator();
				while(seatReserveIter.hasNext()) {
					SeatReserve seatReserve = seatReserveIter.next();
					Row row = findRowById(seatReserve.getRowId());
					int numSeatsOnHold = seatReserve.getNumSeats();
					row.setNumberOfSeat(row.getNumberOfSeat()+numSeatsOnHold);
					seatReserveIter.remove();
				}
				
				seatHoldIter.remove();
			}
		}
		
	}
	
	public Customer findCustomer(String customerEmail) {
		LOGGER.debug("Finding customer {}", customerEmail);
		
		for(Customer customer : FakeData.custumerList) {
			if(customerEmail.contentEquals(customer.getEmail())) {
				return customer;
			}
		}
		
		return null;
	}
	
	public Customer findCustomerById(int customerId) {
		LOGGER.debug("Finding customer {}", customerId);
		for(Customer customer : FakeData.custumerList) {
			if(customerId == customer.getId()) {
				return customer;
			}
		}
		return null;
	}
	
	public void saveCustomer(Customer customer) {
		customer.setId(FakeData.customerId++);
		LOGGER.debug("Adding customer {}", customer.getEmail());
		FakeData.custumerList.add(customer);
	}
	
	public void addSeatReserve(SeatReserve newSeatReserve) {
		LOGGER.debug("Adding SeatReserve {}", newSeatReserve.getNumSeats());
		FakeData.seatReserveList.add(newSeatReserve);
	}
	
	public void saveSeatReserve(SeatReserve seatReserve) {
		LOGGER.debug("Saving SeatReserve {}", seatReserve.getNumSeats());
		FakeData.seatReserveList.add(seatReserve);
	}
	
	public void deleteSeatReserve(SeatReserve seatReserve) {
		LOGGER.debug("Deleting SeatReserve {}", seatReserve.getNumSeats());
		FakeData.seatReserveList.remove(seatReserve);
	}
	
}
