package com.walmart.ticketservice.entity.tables;

public class SeatReserve {

	private long id;
    private long holdId;
    private int rowId;
    private char rowName;
    private int numSeats;
    

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getHoldId() {
        return holdId;
    }

    public void setHoldId(long holdId) {
        this.holdId = holdId;
    }

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }
    
    public char getRowName() {
        return rowName;
    }

    public void setRowName(char rowName) {
        this.rowName = rowName;
    }

    public int getNumSeats() {
        return numSeats;
    }

    public void setNumSeats(int numSeats) {
        this.numSeats = numSeats;
    }

    @Override
    public String toString() {
        return "SeatBooking{" +
                "id=" + id +
                ", holdId=" + holdId +
                ", rowId=" + rowId +
                ", numSeats=" + numSeats +
                '}';
    }
}
