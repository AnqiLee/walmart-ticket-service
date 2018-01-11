package com.walmart.ticketservice.exception;

public class NoSeatsAvailableException extends RuntimeException{
	public NoSeatsAvailableException(String message) {
        super(message);
    }
}
