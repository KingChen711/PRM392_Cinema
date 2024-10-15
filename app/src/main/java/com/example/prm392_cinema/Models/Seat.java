package com.example.prm392_cinema.Models;

public class Seat {
    public static final int STATUS_AVAILABLE = 0;
    public static final int STATUS_BOOKED = 1;
    public static final int STATUS_SELECTED = 2;

    private final boolean isEmpty;
    private int status;  //checked, ordered, ready
    private int index;  //checked, ordered, ready

    public Seat(boolean isEmpty) {
        this.isEmpty = isEmpty;
        this.status = STATUS_AVAILABLE;
        this.index = -1;
    }

    public Seat(int status, int index) {
        this.isEmpty = false;
        this.status = status;
        this.index = index;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isBooked() {
        return status == STATUS_BOOKED;
    }

    public boolean isAvailable() {
        return status == STATUS_AVAILABLE;
    }

    public boolean isSelected() {
        return status == STATUS_SELECTED;
    }
}