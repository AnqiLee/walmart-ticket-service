package com.walmart.ticketservice.exception;

public class EmailNotValidException extends RuntimeException{

	public EmailNotValidException(String message) {
        super(message);
    }
}
