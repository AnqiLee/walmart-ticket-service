package com.walmart.ticketservice.entity.packets;

public class AvailableSeatsResponse {
	
	private final int availableSeats;
	
	public AvailableSeatsResponse(int availableSeats) {
		this.availableSeats = availableSeats;
	}
	
	public int getAvailableSeats() {
        return availableSeats;
    }

	@Override
    public String toString() {
        return "Number of available seats{" +
                "availableSeats=" + availableSeats +
                '}';
    }
}
