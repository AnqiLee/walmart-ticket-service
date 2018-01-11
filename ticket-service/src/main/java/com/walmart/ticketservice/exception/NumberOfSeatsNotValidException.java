package com.walmart.ticketservice.exception;

public class NumberOfSeatsNotValidException extends RuntimeException{

	public NumberOfSeatsNotValidException(String message) {
        super(message);
    }
}
