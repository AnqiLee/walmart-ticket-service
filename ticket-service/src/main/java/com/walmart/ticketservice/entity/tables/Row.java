package com.walmart.ticketservice.entity.tables;

public class Row {
	
	private int rowId;
	private char rowName;
    private int numSeats;
    
    public Row(int rowId, char rowName, int numSeats) {
    	this.rowId = rowId;
    	this.rowName = rowName;
    	this.numSeats = numSeats;
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

    public int getNumberOfSeat() {
        return numSeats;
    }

    public void setNumberOfSeat(int numSeats) {
        this.numSeats = numSeats;
    }

    @Override
    public String toString() {
        return "RowDetails{" +
                "rowId=" + rowId +
                "rowName=" + rowName +
                ", numSeats=" + numSeats +
                '}';
    }
}
