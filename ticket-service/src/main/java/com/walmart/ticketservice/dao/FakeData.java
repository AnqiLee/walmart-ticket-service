package com.walmart.ticketservice.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.walmart.ticketservice.entity.tables.Customer;
import com.walmart.ticketservice.entity.tables.Row;
import com.walmart.ticketservice.entity.tables.SeatHold;
import com.walmart.ticketservice.entity.tables.SeatReserve;

/**
 * 
 * Fake data to be used to show functionality.
 * 
 */
public class FakeData {
	
	private static final int NUM_SEATS_PER_ROW = 20;
	
	public static int customerId;
	public static int holdId;
	public static int seatReserveId;
	public static int rowId;
	
	public static List<Row> rows;
	public static List<SeatHold> seatHoldList;
	public static List<Customer> custumerList;
	public static List<SeatReserve> seatReserveList;
	
	
	
	static {
		rows = new ArrayList<Row>();
		for(char row = 'A'; row < 'I'; row++) {
			rows.add(new Row(rowId++, row, NUM_SEATS_PER_ROW));
		}
		
		seatHoldList = new ArrayList<SeatHold>();
		custumerList = new ArrayList<Customer>();
		seatReserveList = new ArrayList<SeatReserve>();
		
		customerId = 0;
		holdId = 0;
		seatReserveId = 0;
	}
}
