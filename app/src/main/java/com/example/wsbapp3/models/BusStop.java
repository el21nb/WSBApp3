package com.example.wsbapp3.models;

import java.util.List;

/**
 * Model class representing a bus stop and its properties.
 */
public class BusStop {
    private String id;
    private String name;
    private String address;

    /**
     * Default constructor
     */
    public BusStop() {
    }

    public BusStop(String id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setId(String id) {
        this.id = id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
