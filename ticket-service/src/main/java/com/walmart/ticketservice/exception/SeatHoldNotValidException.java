package com.walmart.ticketservice.exception;

public class SeatHoldNotValidException extends RuntimeException{

	public SeatHoldNotValidException(String message) {
        super(message);
    }
}
