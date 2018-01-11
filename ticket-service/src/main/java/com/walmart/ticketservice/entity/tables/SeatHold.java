package com.walmart.ticketservice.entity.tables;

import java.sql.Timestamp;
import java.util.List;

public class SeatHold {

	private long id;
    private long customerId;
    private String customerEmail;
    private String confirmationCode;
    private Timestamp expireTime;
    private List<SeatReserve> seatsInRows;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public Timestamp getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Timestamp expireTime) {
        this.expireTime = expireTime;
    }

    public List<SeatReserve> getSeatInRows() {
        return seatsInRows;
    }

    public void setSeatInRows(List<SeatReserve> seatsInRows) {
        this.seatsInRows = seatsInRows;
    }

    @Override
    public String toString() {
        return "SeatHold{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", customerEmail='" + customerEmail + '\'' +
                ", confirmationCode='" + confirmationCode + '\'' +
                ", expireTime=" + expireTime +
                ", seatsInRows=" + seatsInRows +
                '}';
    }
}
