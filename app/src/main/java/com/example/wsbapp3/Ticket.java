package com.example.wsbapp3;

import java.io.Serializable;

public class Ticket {
    private boolean schoolTicket; //1 for school ticket, 0 for stop ticket
    private boolean pickUp; //1 for pick up, 0 for drop off
    private String ticketId;
    private String childId;
    private String busStopId;
    private String journeyId;

    public Ticket() {
    }

    public Ticket(String ticketId, boolean schoolTicket, boolean pickUp, String childId, String busStopId, String journeyId) {
        this.ticketId = ticketId;
        this.schoolTicket = schoolTicket;
        this.pickUp = pickUp;
        this.childId = childId;
        this.busStopId = busStopId;
        this.journeyId = journeyId;
    }

    public boolean isPickUp() {
        return pickUp;
    }

    public void setPickUp(boolean pickUp) {
        this.pickUp = pickUp;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public boolean isSchoolTicket() {
        return this.schoolTicket;
    }

    public void setSchoolTicket(boolean schoolTicket) {
        this.schoolTicket = schoolTicket;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public String getBusStopId() {
        return busStopId;
    }

    public void setBusStopId(String busStopId) {
        this.busStopId = busStopId;
    }

    public String getJourneyId() {
        return journeyId;
    }

    public void setJourneyId(String journeyId) {
        this.journeyId = journeyId;
    }
}
