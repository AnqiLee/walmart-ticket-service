package com.walmart.ticketservice.exception;

public class SeatHoldExpiredException extends RuntimeException{
	public SeatHoldExpiredException(String message) {
        super(message);
    }
}
