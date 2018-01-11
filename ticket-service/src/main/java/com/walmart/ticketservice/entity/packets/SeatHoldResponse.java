package com.walmart.ticketservice.entity.packets;

import java.util.List;

import com.walmart.ticketservice.entity.tables.Row;
import com.walmart.ticketservice.entity.tables.SeatReserve;

public class SeatHoldResponse {
	
	private final long holdId;
    private final String customerEmail;
    private final List<SeatReserve> seatHoldRowList;
    
    public SeatHoldResponse(long holdId, String customerEmail, List<SeatReserve> seatHoldRowList) {
        this.holdId = holdId;
        this.customerEmail = customerEmail;
        this.seatHoldRowList = seatHoldRowList;
    }
    
    public long getHoldId() {
        return holdId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public List<SeatReserve> getSeatHoldRowList() {
        return seatHoldRowList;
    }

    @Override
    public String toString() {
        return "SeatHoldResponse{" +
                "holdId=" + holdId +
                ", customerEmail='" + customerEmail + '\'' +
                ", seatHoldRowList=" + seatHoldRowList +
                '}';
    }
}
