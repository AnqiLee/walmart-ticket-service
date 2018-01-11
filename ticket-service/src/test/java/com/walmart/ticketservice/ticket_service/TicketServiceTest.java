package com.walmart.ticketservice.ticket_service;


import java.sql.Timestamp;
import java.time.Instant;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.*;

import com.walmart.ticketservice.dao.FakeData;
import com.walmart.ticketservice.dao.WalmartTicketServiceDao;
import com.walmart.ticketservice.entity.tables.Customer;
import com.walmart.ticketservice.entity.tables.SeatHold;
import com.walmart.ticketservice.exception.EmailNotValidException;
import com.walmart.ticketservice.exception.SeatHoldNotValidException;
import com.walmart.ticketservice.service.RequestValidator;
import com.walmart.ticketservice.service.WalmartTicketService;


public class TicketServiceTest {

	WalmartTicketService walmartTicketService;
	private WalmartTicketServiceDao walmartTicketServiceDao;
	private RequestValidator requestValidator;
	
	@Before
	public void init() {
		
		this.walmartTicketServiceDao = Mockito.mock(WalmartTicketServiceDao.class);
		this.requestValidator = Mockito.mock(RequestValidator.class);
		this.walmartTicketService = new WalmartTicketService(this.walmartTicketServiceDao, this.requestValidator);
	}
	
	@Test
	public void testNumSeatsAvailable() {
		
		doNothing().when(this.walmartTicketServiceDao).removeExpiredSeats();
		when(this.walmartTicketServiceDao.findAvailableSeats()).thenReturn(160);
		
		this.walmartTicketService.numSeatsAvailable();
		
		verify(this.walmartTicketServiceDao, times(1)).removeExpiredSeats();
		verify(this.walmartTicketServiceDao, times(1)).findAvailableSeats();
	}
	
	@Test
	public void testNumSeatsAvailableByRow() {
		
		doNothing().when(this.walmartTicketServiceDao).removeExpiredSeats();
		when(this.walmartTicketServiceDao.findAvailableSeatsByRow('C')).thenReturn(160);
		
		this.walmartTicketService.numSeatsAvailableByRow('C');
		
		verify(this.walmartTicketServiceDao, times(1)).removeExpiredSeats();
		verify(this.walmartTicketServiceDao, times(1)).findAvailableSeatsByRow('C');
	}
	
	@Test
	public void testFindAndHoldSeatsReturnEmail() {
		String email = "armin@gmail.com";
		Customer customer = new Customer();
		customer.setEmail(email);
		customer.setId(0);
		
		doNothing().when(this.requestValidator).throwExceptionIfSeatHoldBadRequest(20, email);
		when(this.walmartTicketServiceDao.findCustomer(email)).thenReturn(customer);
		
		when(this.walmartTicketServiceDao.getAllRows()).thenReturn(FakeData.rows);
		when(this.walmartTicketServiceDao.findAvailableSeatsByRow('C')).thenReturn(20);
		
		doNothing().when(this.walmartTicketServiceDao).takeSeatsFromRow(20, 'C');
		
		doNothing().when(this.walmartTicketServiceDao).addSeatHold(Mockito.any(SeatHold.class));
		
		this.walmartTicketService.findAndHoldSeats(20, email);
		
		verify(this.requestValidator, times(1)).throwExceptionIfSeatHoldBadRequest(20, email);
		verify(this.walmartTicketServiceDao, times(1)).getAllRows();
		verify(this.walmartTicketServiceDao, times(3)).findAvailableSeatsByRow(Mockito.anyChar());
	}
	
	@Test
	public void testFindAndHoldSeatsSaveEmail() {
		String email = "armin@gmail.com";
		Customer customer = new Customer();
		customer.setEmail(email);
		customer.setId(0);
		
		doNothing().when(this.requestValidator).throwExceptionIfSeatHoldBadRequest(20, email);
		when(this.walmartTicketServiceDao.findCustomer(email)).thenReturn(null);
		doNothing().when(this.walmartTicketServiceDao).saveCustomer(customer);
		when(this.walmartTicketServiceDao.getAllRows()).thenReturn(FakeData.rows);
		when(this.walmartTicketServiceDao.findAvailableSeatsByRow('C')).thenReturn(20);
		
		doNothing().when(this.walmartTicketServiceDao).takeSeatsFromRow(20, 'C');
		
		doNothing().when(this.walmartTicketServiceDao).addSeatHold(Mockito.any(SeatHold.class));
		
		this.walmartTicketService.findAndHoldSeats(20, email);
		
		verify(this.requestValidator, times(1)).throwExceptionIfSeatHoldBadRequest(20, email);
		verify(this.walmartTicketServiceDao, times(1)).getAllRows();
		verify(this.walmartTicketServiceDao, times(3)).findAvailableSeatsByRow(Mockito.anyChar());
	}
	
	@Test
	public void testReserveSeatsTrue() {
		
		String email = "armin@gmail.com";
		int seatHoldId = 1;
		
		Customer customer = new Customer();
		customer.setEmail(email);
		customer.setId(0);
		
		SeatHold seatHold = new SeatHold();
		seatHold.setCustomerEmail(email);
		seatHold.setCustomerId(0);
		seatHold.setId(seatHoldId);
		seatHold.setExpireTime(expireTime());
		
		doNothing().when(this.requestValidator).throwExceptionIfSeatReserveBadRequest(seatHoldId, email);
		when(this.walmartTicketServiceDao.findSeatHoldById(seatHoldId)).thenReturn(seatHold);
		when(this.walmartTicketServiceDao.findCustomer(email)).thenReturn(customer);
		
		doNothing().when(this.walmartTicketServiceDao).saveSeatHold(seatHold);
		
		this.walmartTicketService.reserveSeats(seatHoldId, email);
		
		verify(this.requestValidator, times(1)).throwExceptionIfSeatReserveBadRequest(seatHoldId, email);
		verify(this.walmartTicketServiceDao, times(1)).findSeatHoldById(seatHoldId);
		verify(this.walmartTicketServiceDao, times(1)).saveSeatHold(seatHold);
	}
	
	@Test
	public void testReserveSeatsSeatHoldIdException() {
		
		String email = "armin@gmail.com";
		int seatHoldId = 1;
		
		Customer customer = new Customer();
		customer.setEmail(email);
		customer.setId(0);
		
		SeatHold seatHold = new SeatHold();
		seatHold.setCustomerEmail(email);
		seatHold.setCustomerId(0);
		seatHold.setId(seatHoldId);
		seatHold.setExpireTime(expireTime());
		
		doNothing().when(this.requestValidator).throwExceptionIfSeatReserveBadRequest(seatHoldId, email);
		when(this.walmartTicketServiceDao.findSeatHoldById(seatHoldId)).thenReturn(null);
		
		try {
			this.walmartTicketService.reserveSeats(seatHoldId, email);
		}catch(Exception e){
			
			assertTrue(e.getClass().toString().equals(SeatHoldNotValidException.class.toString()));
		}
		
		verify(this.requestValidator, times(1)).throwExceptionIfSeatReserveBadRequest(seatHoldId, email);
		verify(this.walmartTicketServiceDao, times(1)).findSeatHoldById(seatHoldId);
	}
	
	@Test
	public void testReserveSeatsEmailNotValidException() {
		
		String email = "armin@gmail.com";
		int seatHoldId = 1;
		
		Customer customer = new Customer();
		customer.setEmail(email);
		customer.setId(0);
		
		SeatHold seatHold = new SeatHold();
		seatHold.setCustomerEmail(email);
		seatHold.setCustomerId(0);
		seatHold.setId(seatHoldId);
		seatHold.setExpireTime(expireTime());
		
		doNothing().when(this.requestValidator).throwExceptionIfSeatReserveBadRequest(seatHoldId, email);
		when(this.walmartTicketServiceDao.findSeatHoldById(seatHoldId)).thenReturn(seatHold);
		when(this.walmartTicketServiceDao.findCustomer(email)).thenReturn(null);
		
		try {
			this.walmartTicketService.reserveSeats(seatHoldId, email);
		}catch(Exception e){
			assertTrue(e.getClass().toString().equals(EmailNotValidException.class.toString()));
		}
		
		verify(this.requestValidator, times(1)).throwExceptionIfSeatReserveBadRequest(seatHoldId, email);
		verify(this.walmartTicketServiceDao, times(1)).findSeatHoldById(seatHoldId);
	}
	
	public Timestamp expireTime() {
		Timestamp currentTime = new Timestamp(System.currentTimeMillis());
		Instant toBeExpireTime = currentTime.toInstant().plusSeconds(120);
		Timestamp expireTime = Timestamp.from(toBeExpireTime);
		return expireTime;
	}
}
