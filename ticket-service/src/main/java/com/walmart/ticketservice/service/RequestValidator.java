package com.walmart.ticketservice.service;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.walmart.ticketservice.exception.EmailNotValidException;
import com.walmart.ticketservice.exception.NumberOfSeatsNotValidException;
import com.walmart.ticketservice.exception.SeatHoldNotValidException;

@Component
public class RequestValidator {

	private static final Logger LOGGER = LogManager.getLogger(RequestValidator.class);
	
	private static final Pattern CUSTOMER_EMAIL_PATTERN = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
	
	public void throwExceptionIfSeatHoldBadRequest(int numSeats, String customerEmail) {
		LOGGER.debug("Validating Seat Hold request.");
		
		if(StringUtils.isEmpty(customerEmail) || !CUSTOMER_EMAIL_PATTERN.matcher(customerEmail).matches()) {
			throw new EmailNotValidException("Customer email is not valid.");
		}
		
		if(numSeats < 0) {
			throw new NumberOfSeatsNotValidException("Number of seats is not valid.");
		}
		
		LOGGER.debug("Hold request is valid.");
	}
	
	public void throwExceptionIfSeatReserveBadRequest(int seatHoldId, String customerEmail) {
		LOGGER.debug("Validating Seat Reserve request.");
		
		if(StringUtils.isEmpty(customerEmail) || !CUSTOMER_EMAIL_PATTERN.matcher(customerEmail).matches()) {
			throw new EmailNotValidException("Customer email is not valid.");
		}
		
		if(seatHoldId < 0) {
			throw new SeatHoldNotValidException("Seat Hold is not valid.");
		}
		
		LOGGER.debug("Reserve request is valid.");
	}
}
