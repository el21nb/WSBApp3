package com.example.wsbapp3.models;

/**
 * Model class representing a Contact
 * Used in Child object, and for Drivers' contact in database
 */
public class Contact {
    private String name;
    private String contactDetail;

    public Contact() {
    }

    public Contact(String name, String contactDetail) {
        this.name = name;
        this.contactDetail = contactDetail;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getContactDetail() {

        return contactDetail;
    }

    public void setContactDetail(String contactDetail) {

        this.contactDetail = contactDetail;
    }
}
