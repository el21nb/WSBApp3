package com.example.wsbapp3;

public class Ticket {
    private boolean direction; //0 for to school, 1 for from school
    private String childId;
    private String busStopId;
    private String journeyId;

    public Ticket() {
    }

    public Ticket(boolean direction, String childId, String busStopId, String journeyId) {
        this.direction = direction;
        this.childId = childId;
        this.busStopId = busStopId;
        this.journeyId = journeyId;
    }

    public boolean getDirection() {
        return direction;
    }

    public void setDirection(boolean direction) {
        this.direction = direction;
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
