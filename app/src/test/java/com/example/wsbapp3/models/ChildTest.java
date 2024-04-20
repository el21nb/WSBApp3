package com.example.wsbapp3.models;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

//Testing the model class works as expected
public class ChildTest extends TestCase {

    private Child child;
    private List<Contact> contactList;


    @Override
    public void setUp() throws Exception {
        super.setUp();
        child = new Child();
        Contact contact1 = new Contact("Name", "0123456");
        Contact contact2 = new Contact("Name2", "78901234");
        contactList = new ArrayList<>();
        contactList.add(contact1);
        contactList.add(contact2);
    }

    public void testGetParentId() {
        child.setParentId("parentId");
        assertEquals("parentId", child.getParentId());
    }

    public void testSetParentId() {
        child.setParentId("parentId");
        assertEquals("parentId", child.getParentId());
    }

    public void testGetId() {
        child.setId("Id");
        assertEquals("Id", child.getId());
    }

    public void testGetFirstName() {
        child.setFirstName("name");
        assertEquals("name", child.getFirstName());
    }

    public void testSetId() {
        child.setId("Id");
        assertEquals("Id", child.getId());
    }

    public void testSetFirstName() {
        child.setFirstName("name");
        assertEquals("name", child.getFirstName());
    }

    public void testGetLastName() {
        child.setLastName("name2");
        assertEquals("name2", child.getLastName());
    }

    public void testSetLastName() {
        child.setLastName("name2");
        assertEquals("name2", child.getLastName());
    }

    public void testGetChildContacts() {
        child.setChildContacts(contactList);
        assertEquals(child.getChildContacts(), contactList);
    }

    public void testSetChildContacts() {
        child.setChildContacts(contactList);
        assertEquals(child.getChildContacts(), contactList);
    }

    public void testGetClassCode() {
        child.setClassCode("101");
        assertEquals("101", child.getClassCode());
    }

    public void testSetClassCode() {
        child.setClassCode("101");
        assertEquals("101", child.getClassCode());
    }
}