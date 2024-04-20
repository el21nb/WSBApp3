package com.example.wsbapp3.models;

import junit.framework.TestCase;


//Testing the model class works as expected
public class BusStopTest extends TestCase {
    private BusStop busStop;

    protected void setUp() throws Exception {
        super.setUp();
        busStop = new BusStop();
    }

    public void testGetLatitude() {
        busStop.setLatitude(12.3456);
        assertEquals(12.3456, busStop.getLatitude());
    }

    public void testSetLatitude() {
        busStop.setLatitude(12.3456);
        assertEquals(12.3456, busStop.getLatitude());
    }

    public void testGetLongitude() {
        busStop.setLongitude(-56.7890);
        assertEquals(-56.7890, busStop.getLongitude());
    }

    public void testSetLongitude() {
        busStop.setLongitude(-56.7890);
        assertEquals(-56.7890, busStop.getLongitude());
    }

    public void testGetId() {
        busStop.setId("test");
        assertEquals("test", busStop.getId());
    }

    public void testGetName() {
        busStop.setName("test name");
        assertEquals("test name", busStop.getName());
    }

    public void testGetAddress() {
        busStop.setAddress("test address");
        assertEquals("test address", busStop.getAddress());
    }

    public void testSetName() {
        busStop.setName("test");
        assertEquals("test", busStop.getName());
    }

    public void testSetId() {
        busStop.setId("test");
        assertEquals("test", busStop.getId());
    }

    public void testSetAddress() {
        busStop.setAddress("test address");
        assertEquals("test address", busStop.getAddress());
    }
}