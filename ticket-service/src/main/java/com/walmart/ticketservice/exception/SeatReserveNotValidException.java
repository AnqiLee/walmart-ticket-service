package com.walmart.ticketservice.exception;

public class SeatReserveNotValidException extends RuntimeException{
	public SeatReserveNotValidException(String message) {
        super(message);
    }
}
