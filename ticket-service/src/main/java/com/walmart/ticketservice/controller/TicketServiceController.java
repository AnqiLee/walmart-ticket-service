package com.walmart.ticketservice.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.walmart.ticketservice.service.TicketService;
import com.walmart.ticketservice.entity.packets.AvailableSeatsResponse;
import com.walmart.ticketservice.entity.packets.SeatHoldRequest;
import com.walmart.ticketservice.entity.packets.SeatHoldResponse;
import com.walmart.ticketservice.entity.packets.SeatReserveRequest;
import com.walmart.ticketservice.entity.packets.SeatReserveResponse;
import com.walmart.ticketservice.entity.tables.SeatHold;
import com.walmart.ticketservice.service.RequestValidator;

@RestController
@EnableAutoConfiguration
public class TicketServiceController {
	
	private static final Logger LOGGER = LogManager.getLogger(TicketServiceController.class);
	
	private final TicketService walmartTicketService;
	
	public TicketServiceController(final TicketService walmartTicketService) {
        this.walmartTicketService = walmartTicketService;
    }
	
	/**
	 * @return Number of seats available.
	 */
	@RequestMapping(value = "/ticketservice/availableSeats", method = RequestMethod.GET)
	public ResponseEntity<AvailableSeatsResponse> numSeatsAvailable() {
		LOGGER.debug("Request: Finidng available seats.");
		
		final int numSeatsAvailable = walmartTicketService.numSeatsAvailable();
		
		final ResponseEntity<AvailableSeatsResponse> response
				= new ResponseEntity<AvailableSeatsResponse>(new AvailableSeatsResponse(numSeatsAvailable), HttpStatus.OK);
		
		LOGGER.debug("Response: Available seats: {}", response);
		return response;
	}

	/**
	 * @param seatHoldRequest
	 *            JSON request to hold seats contains numSeats, customerEmail
	 * @return SeatHoldResponse contains id, customerEmail and list of seats on
	 *         hold.
	 */
	@RequestMapping(value = "/ticketservice/holdSeats", method = RequestMethod.POST)
	public ResponseEntity<SeatHoldResponse> findAndHoldSeats(@RequestBody final SeatHoldRequest seatHoldRequest) { 
		LOGGER.debug("Request: Find and Hold seats. {}", seatHoldRequest);
		
		final SeatHold seatHold = walmartTicketService.findAndHoldSeats(Integer.valueOf(seatHoldRequest.getNumSeats()), seatHoldRequest.getCustomerEmail());
		final ResponseEntity<SeatHoldResponse> response;
		
		if(seatHold == null) {
			response = new ResponseEntity<>(HttpStatus.OK);
		}else {
			response = new ResponseEntity<SeatHoldResponse>(
					new SeatHoldResponse(seatHold.getId(),
	                seatHold.getCustomerEmail(), seatHold.getSeatInRows()), HttpStatus.OK);
		}
		
		LOGGER.debug("Response: Seats on hold {} for {}", response, seatHoldRequest);
		return response;
	}

	/**
	 *
	 * @param reserveRequest
	 *            JSON request to commit hold seats. Contains holdId and
	 *            customerEmail
	 * @return SeatReserverResponse contains confirmation code, customerEmail, Hold id
	 */
	@RequestMapping(value = "/ticketservice/reserve", method = RequestMethod.POST)
	public ResponseEntity<SeatReserveResponse> reserveSeats(@RequestBody final SeatReserveRequest seatReserveRequest) {
		LOGGER.debug("Request: Reserve seats for {}", seatReserveRequest);
		
		int seatHoldId = Integer.valueOf(seatReserveRequest.getSeatHoldId());
		final String confirmationCode = walmartTicketService.reserveSeats(seatHoldId, seatReserveRequest.getCustomerEmail());
		ResponseEntity<SeatReserveResponse> response;
		
		if(StringUtils.isEmpty(confirmationCode)) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}else {
			response = new ResponseEntity<SeatReserveResponse>(new SeatReserveResponse(seatHoldId,
					seatReserveRequest.getCustomerEmail(), confirmationCode), HttpStatus.OK);
		}
		
		LOGGER.debug("Response: Reserved seats {} for {}", response, seatReserveRequest);
		return response;
	}

}
