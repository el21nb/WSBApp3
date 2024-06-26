package com.example.wsbapp3.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Model class representing a Child and its properties
 */
public class Child {

    private String id;
    private String firstName;
    private String lastName;

    List<Contact> childContacts;
    private String classCode;

    private String parentId;
    public Child() {
    }

    public Child(String id, String firstName, String lastName, String contactName, String contactDetail, String classCode, String parentId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.childContacts = new ArrayList<>(); // Initialize the childContacts list
        Contact childContact = new Contact(contactName, contactDetail);
        this.childContacts.add(childContact);
        this.classCode = classCode;
        this.parentId = parentId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Contact> getChildContacts() {
        return childContacts;
    }

    public void setChildContacts(List<Contact> childContacts) {
        this.childContacts = childContacts;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }
}
