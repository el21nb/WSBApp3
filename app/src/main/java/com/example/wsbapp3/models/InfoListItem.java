package com.example.wsbapp3.models;

import java.util.ArrayList;

/**
 * Model class representing an item in the recyclerview list of bus stops, in BusStopFragments
 */
public class InfoListItem {
    String heading;
    String body;
    Boolean expanded;

    /**
     * Default constructor
     */
    public InfoListItem() {
    }

    /**
     * Parameterized constructor
     * @param heading
     * @param body
     */
    public InfoListItem(String heading, String body) {
        this.heading = heading;
        this.body = body;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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
