package com.walmart.ticketservice.entity.packets;

public class SeatReserveResponse {

	private final int seatHoldId;
	private final String confirmationCode;
    private final String customerEmail;
    
    public SeatReserveResponse(int seatHoldId, String confirmationCode, String customerEmail) {
        this.seatHoldId = seatHoldId;
        this.confirmationCode = confirmationCode;
        this.customerEmail = customerEmail;
    }

    public int getSeatHoldId() {
        return seatHoldId;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }
    
    public String getCustomerEmail() {
        return customerEmail;
    }

    @Override
    public String toString() {
        return "SeatReserveResponse{" +
                "seatHoldId=" + seatHoldId +
                ", confirmationCode='" + confirmationCode + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                '}';
    }
}
