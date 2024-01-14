package com.example.wsbapp3;

public class Journey {

    public String getId() {
        return id;
    }

    private String id;
    private Contact driverContact;

    private String journeyDateTime;

    private boolean outwardJourney;

    public Journey() {
    }

    public Journey(String id) {
        id = id;
    }

    public Journey(String driverName, String driverPhone, String journeyDateTime, boolean outwardJourney) {
        Contact contact = new Contact(driverName, driverPhone);
        this.driverContact = contact;
        this.journeyDateTime = journeyDateTime;
        this.outwardJourney = outwardJourney;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Contact getDriverContact() {
        return driverContact;
    }

    public void setDriverContact(Contact driverContact) {
        this.driverContact = driverContact;
    }

    public String getJourneyDateTime() {
        return journeyDateTime;
    }

    public void setJourneyDateTime(String journeyDateTime) {
        this.journeyDateTime = journeyDateTime;
    }

    public boolean is0utwardJourney() {
        return outwardJourney;
    }

    public void setOutwardJourney(boolean outwardJourney) {
        this.outwardJourney = outwardJourney;
    }

}