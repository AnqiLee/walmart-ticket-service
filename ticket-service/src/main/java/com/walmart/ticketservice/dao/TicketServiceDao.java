package com.walmart.ticketservice.dao;

import java.util.List;

import com.walmart.ticketservice.entity.tables.Customer;
import com.walmart.ticketservice.entity.tables.Row;
import com.walmart.ticketservice.entity.tables.SeatHold;
import com.walmart.ticketservice.entity.tables.SeatReserve;

interface TicketServiceDao {
	
	/**
     * Find all available seats
     * @return number of seats available
     */
	public int findAvailableSeats();
	
	/**
     * Find available seats by row
     * @param row Name of the row
     * @return  number of seats available in the specified row
     */
	public int findAvailableSeatsByRow(char row);
	
	/**
     * Finding all the rows
     * @return  List<Row> list of rows
     */
	public List<Row> getAllRows();
	
	/**
     * Find row object by id
     * @param rowId 
     * @return  Row object containing details of the row
     */
	public Row findRowById(int rowId);
	
	/**
     * Remove seats from the specified row
     * @param numSeats    number of seats to be removed
     * @param row	name of the row
     */
	public void takeSeatsFromRow(int numSeats, char row);
	
	/**
     * Add Seat Hold 
     * @param newSeatHold    SeatHold to be added
     */
	public void addSeatHold(SeatHold newSeatHold);
	
	/**
     * Saving a Seat Hold 
     * @param seatHold    SeatHold with new values to be saved
     */
	public void saveSeatHold(SeatHold seatHold);
	
	/**
     * Delete specified SeatHold
     * @param seatHold    Seat Hold to be deleted
     */
	public void deleteSeatHold(SeatHold seatHold);
	
	/**
     * Find seat hold by its id
     * @param seatHoldId    Seat Hold id 
     * @return  SeatHold 
     */
	public SeatHold findSeatHoldById(int seatHoldId);
	
	/**
     * Remove all the SeatHold objects that are expired
     */
	public void removeExpiredSeats();
	
	/**
     * Find customer
     * @param customerEmail    
     * @return  Customer 
     */
	public Customer findCustomer(String customerEmail);
	
	/**
     * Find customer by its id
     * @param customerId    
     * @return  Customer 
     */
	public Customer findCustomerById(int customerId);
	
	/**
     * Save the customer
     * @param customer    
     */
	public void saveCustomer(Customer customer);
	
	/**
     * Add the SeatReserve object containing details about rows 
     * @param newSeatReserve    
     */
	public void addSeatReserve(SeatReserve newSeatReserve);
	
	/**
     * Save the SeatReserve object containing details about rows 
     * @param seatReserve    
     */
	public void saveSeatReserve(SeatReserve seatReserve);
	
	/**
     * Delete the specified SeatReserve object
     * @param seatReserve    
     */
	public void deleteSeatReserve(SeatReserve seatReserve);
}
