package com.example.wsbapp3;

import java.util.List;

public class BusStop {
    private String id;
    private String name;
    private String address;

    public BusStop() {
    }
    public BusStop(String name, String address) {
        this.name = name;
        this.address = address;
    }
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setId(String id) {
        this.id = id;
    }
    public void setAddress(String address) {
        this.address = address;
    }

}
