package com.example.wsbapp3;

import java.io.Serializable;

public class Ticket {
    private boolean OutwardJourney; //1 for to school, - for from school
    private String ticketId;
    private String childId;
    private String busStopId;
    private String journeyId;

    public Ticket() {
    }

    public Ticket(String ticketId, boolean outwardJourney, String childId, String busStopId, String journeyId) {
        this.ticketId = ticketId;
        this.OutwardJourney = outwardJourney;
        this.childId = childId;
        this.busStopId = busStopId;
        this.journeyId = journeyId;
    }

    public String getTicketId() {
        return ticketId;
    }

    public void setTicketId(String ticketId) {
        this.ticketId = ticketId;
    }

    public boolean getOutwardJourney() {
        return this.OutwardJourney;
    }

    public void setOutwardJourney(boolean outwardJourney) {
        this.OutwardJourney = outwardJourney;
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
