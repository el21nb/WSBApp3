package com.example.wsbapp3.models;

/**
 * Model class representing an item in the nested RV of busStopChildren in BusStopFragment
 */
public class ChildListItem {
    String childName;
    String childId;

    public ChildListItem(String childName, String childId) {
        this.childName = childName;
        this.childId = childId;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }
}
