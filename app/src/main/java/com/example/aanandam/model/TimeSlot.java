package com.example.aanandam.model;

public class TimeSlot {
    private int slot;
    private String type;

    // type = 0 (booked by doctor)
    //type = 1 (booked by patient)


    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
