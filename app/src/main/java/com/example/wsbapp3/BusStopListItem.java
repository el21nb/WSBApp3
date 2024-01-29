package com.example.wsbapp3;

import java.util.ArrayList;

public class BusStopListItem {
    String arrivalTime;
    String busStopName;
    String busStopAddress;
    String busStopId;
    ArrayList<ChildListItem> childList;


    Boolean expanded;

    public BusStopListItem() {
    }

    public BusStopListItem(String busStopName, String busStopAddress) {
        this.busStopName = busStopName;
        this.busStopAddress = busStopAddress;
    }

    public ArrayList<ChildListItem> getChildList() {
        return childList;
    }

    public void setChildList(ArrayList<ChildListItem> childList) {
        this.childList = childList;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getBusStopName() {
        return busStopName;
    }

    public void setBusStopName(String busStopName) {
        this.busStopName = busStopName;
    }

    public String getBusStopAddress() {
        return busStopAddress;
    }

    public void setBusStopAddress(String busStopAddress) {
        this.busStopAddress = busStopAddress;
    }

    public String getBusStopId() {
        return busStopId;
    }

    public void setBusStopId(String busStopId) {
        this.busStopId = busStopId;
    }

    public void setExpanded(boolean expanded){
        this.expanded = expanded;
    }
    public boolean isExpanded(){
        return expanded;
    }
}
