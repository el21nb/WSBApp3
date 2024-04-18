package com.example.wsbapp3.models;

/**
 * Model class representing an item in the recyclerview list of bus stops, in BusStopFragments
 */
public class UpdatesListItem {
    String title;
    String message;
    Boolean expanded;

    /**
     * Default constructor
     */
    public UpdatesListItem() {
    }

    /**
     * Parameterized constructor
     * @param title
     * @param message
     */
    public UpdatesListItem(String title, String message) {
        this.title = title;
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getExpanded() {
        return expanded;
    }

    public void setExpanded(Boolean expanded) {
        this.expanded = expanded;
    }

    public boolean isExpanded() {
        return expanded;
    }
}
