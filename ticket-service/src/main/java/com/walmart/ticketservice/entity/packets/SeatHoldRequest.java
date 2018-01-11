package com.walmart.ticketservice.entity.packets;

public class SeatHoldRequest {
	
	private String numSeats;
    private String customerEmail;
    
    public String getNumSeats() {
        return numSeats;
    }

    public void setNumSeats(String numSeats) {
        this.numSeats = numSeats;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
    
    @Override
    public String toString() {
        return "SeatHoldRequest{" +
                "numSeats='" + numSeats + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                '}';
    }
}
