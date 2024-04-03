package com.example.wsbapp3.database;

import android.util.Log;

import com.example.wsbapp3.models.Jacket;
import com.example.wsbapp3.models.Journey;
import com.example.wsbapp3.models.Ticket;
import com.example.wsbapp3.models.BusStop;
import com.example.wsbapp3.models.Child;

import java.util.HashMap;
import java.util.Map;

public class DatabaseProvider {
    public DatabaseProvider() {
    }
}


    /**
     * Top level function to add all the initial data to the database.
     * Only needs to be called once- maybe should be a cloud function.
     * Currently provides some data for each journey, but only fully populates
     * journey with Id KelmpE01102024AM
     *
     * @param currentJourneyId journey Id to add all the data
     */
//    public void initialiseDatabase(String currentJourneyId) { //populates the database, including one fully populated journey with matching tickets
//        makeJourneys();
//        makeBusStops();
//        makeChildren();
//        makeTickets();
//        makeJackets();
//        makeJourneyBusStops(currentJourneyId);
//        makeBusStopChildren();
//    }
//
//    /**
//     * Populate top level jackets collection
//     */
//    public void makeJackets() {
//        Log.d("jacket", "making jackets");
//        JacketProvider provider = new JacketProvider();
//        for (int i = 0; i < 15; i++) {
//            Jacket jacket = new Jacket(String.format("JA%02d", i + 1));
//            provider.addJacket(jacket);
//        }
//    }
//
//    /**
//     * Populate top level Journeys collection
//     */
//    public void makeJourneys() { //Creates top level collection of Journeys
//        JourneyProvider provider = new JourneyProvider();
//        provider.addJourney(new Journey("KelmpE01102024AM", "Evan Kelmp", "123215634", "09:00 01/10/2024", true));
//        provider.addJourney(new Journey("AppelbeesK102102024AM", "Kristen Appelbees", "7384294527", "09:00 02/10/2024", true));
//        provider.addJourney(new Journey("GooseT03102024PM", "Timothy Goose", "8784338381", "15:00 03/10/2024", false));
//        provider.addJourney(new Journey("MooreM04102024PM", "Misty Moore", "36354756522", "15:00 04/10/2024", false));
//        provider.addJourney(new Journey("KelmpE01102024PM", "Evan Kelmp", "123215634", "15:00 01/10/2024", false));
//
//    }
//
//    /**
//     * Populate top level BusStops collection
//     */
//    public void makeBusStops() { //Creates top level collection of BusStops
//        BusStopProvider provider = new BusStopProvider();
//        provider.addBusStop(new BusStop("BPS1", "Brentwood Police Station", "Brentwood Police Station, Wallesey Rd, SP2 6YU"));
//        provider.addBusStop(new BusStop("CS1", "Central Station", "Central Station, Main St, AB1 2CD"));
//        provider.addBusStop(new BusStop("GP1", "Green Park", "Green Park, Park Ave, XY3 4ZW"));
//        provider.addBusStop(new BusStop("SM1", "Sunset Mall", "Sunset Mall, Sunset Blvd, CD5 6EF"));
//        provider.addBusStop(new BusStop("TH1", "Tech Hub", "Tech Hub, Innovation St, EF6 7GH"));
//        provider.addBusStop(new BusStop("RS1", "Riverfront Square", "Riverfront Square, Riverside Rd, GH8 9IJ"));
//        provider.addBusStop(new BusStop("HV1", "Hilltop View", "Hilltop View, Summit Hill, IJ2 3KL"));
//        provider.addBusStop(new BusStop("OB1", "Ocean Breeze", "Ocean Breeze, Coastal Ave, KL4 5MN"));
//        provider.addBusStop(new BusStop("SCH", "Primary School", "Primary School, Some Road, KL4 5OP"));
//
//    }
//
//    /**
//     * Populate top level Children collection
//     */
//    public void makeChildren() { //Creates top level collection of Children
//        ChildProvider provider = new ChildProvider();
//        provider.addChild(new Child("CallowayF3AS", "Fearne", "Calloway", "Birdie Calloway (Mother)", "3578438311", "3AS"));
//        provider.addChild(new Child("SmithL4GH", "Liam", "Smith", "Emma Smith (Mother)", "1234567890", "4GH"));
//        provider.addChild(new Child("JohnsonO3AS", "Olivia", "Johnson", "Sophia Johnson (Mother)", "2345678901", "3AS"));
//        provider.addChild(new Child("WilliamsN6PW", "Noah", "Williams", "Ava Williams (Mother)", "3456789012", "6PW"));
//        provider.addChild(new Child("JonesE5HW", "Emma", "Jones", "Isabella Jones (Mother)", "4567890123", "5HW"));
//        provider.addChild(new Child("BrownA5HW", "Aiden", "Brown", "Olivia Brown (Mother)", "5678901234", "5HW"));
//        provider.addChild(new Child("DavisS5SM", "Sophia", "Davis", "Sophia Davis (Mother)", "6789012345", "5SM"));
//        provider.addChild(new Child("MillerJ2IB", "Jackson", "Miller", "Amelia Miller (Mother)", "7890123456", "2IB"));
//        provider.addChild(new Child("GarciaO2IB", "Olivia", "Garcia", "Sophia Garcia (Mother)", "8901234567", "2IB"));
//        provider.addChild(new Child("MartinezL1EF", "Lucas", "Martinez", "Isabella Martinez (Mother)", "9012345678", "1EF"));
//        provider.addChild(new Child("RodriguezA2EF", "Ava", "Rodriguez", "Olivia Rodriguez (Mother)", "0123456789", "2EF"));
//        provider.addChild(new Child("LopezE3GH", "Emma", "Lopez", "Sophia Lopez (Mother)", "1122334455", "3GH"));
//        provider.addChild(new Child("ClarkL4GH", "Logan", "Clark", "Emma Clark (Mother)", "2233445566", "4GH"));
//        provider.addChild(new Child("HallS5GH", "Sophia", "Hall", "Ava Hall (Mother)", "3344556677", "5GH"));
//        provider.addChild(new Child("FisherL1CD", "Liam", "Fisher", "Sophia Fisher (Mother)", "4455667788", "1CD"));
//        provider.addChild(new Child("YoungI2CD", "Isabella", "Young", "Olivia Young (Mother)", "5566778899", "2CD"));
//    }
//
//    /**
//     * Populates current journey (KelmpE01102024AM) with BusStops and Arrival times
//     */
//    public void makeJourneyBusStops(String currentJourneyId) { //Goes into a journey document within Journeys, and creates a subCollection of busStops
//        JourneyProvider provider = new JourneyProvider();
//        //public void createJourneyBusStopsSubcollection(String journeyId, Map<String, Long> busStopsWithTimes) {
//        Map<String, String> journeyMap = new HashMap<>(); //map of busStop Id (from BusStops colleciton) versus arrival time
//        journeyMap.put("BPS1", "0750");
//        journeyMap.put("TH1", "0800");
//        journeyMap.put("HV1", "0807");
//        journeyMap.put("CS1", "0814");
//        journeyMap.put("OB1", "0829");
//        journeyMap.put("RS1", "0840");
//        journeyMap.put("SCH", "0900");
//        provider.createJourneyBusStops(currentJourneyId, journeyMap);
//    }
//
//    /**
//     * Adds 'booked' children to 'booked' bus stops in current journey's journeyBusStops
//     */
//    public void makeBusStopChildren() {
//        BusStopProvider provider = new BusStopProvider();
//        Map<String, String> busStopChildMap = new HashMap<>();
//        busStopChildMap.put("JonesE5HW", "BPS1");
//        busStopChildMap.put("GarciaO2IB", "BPS1");
//        busStopChildMap.put("DavisS5SM", "RS1");
//        busStopChildMap.put("ClarkL4GH", "OB1");
//        busStopChildMap.put("JohnsonO3AS", "OB1");
//        busStopChildMap.put("FisherL1CD", "CS1");
//        busStopChildMap.put("RodriguezA2EF", "CS1");
//        busStopChildMap.put("YoungI2CD", "TH1");
//        busStopChildMap.put("LopezE3GH", "HV1");
//        provider.createBusStopChildren("KelmpE01102024AM", busStopChildMap);
//    }
//
//    /**
//     * Populates top level tickets collection.
//     * Creates tickets for inbound and outbound journeys on one day, for all 4 Drivers
//     */
//    public void makeTickets() {
//        TicketProvider provider = new TicketProvider();
//        //morning pick ups:
//        provider.addTicket(new Ticket("JonesE5HWKelmpE01102024AMPU", false, true, "JonesE5HW", "BPS1", "KelmpE01102024AM"));
//        provider.addTicket(new Ticket("GarciaO2IBKelmpE01102024AMPU", false, true, "GarciaO2IB", "BPS1", "KelmpE01102024AM"));
//        provider.addTicket(new Ticket("DavisS5SMKelmpE01102024AMPU", false, true, "DavisS5SM", "RS1", "KelmpE01102024AM"));
//        provider.addTicket(new Ticket("ClarkL4GHKelmpE01102024AMPU", false, true, "ClarkL4GH", "OB1", "KelmpE01102024AM"));
//        provider.addTicket(new Ticket("JohnsonO3ASKelmpE01102024AMPU", false, true, "JohnsonO3AS", "OB1", "KelmpE01102024AM"));
//        provider.addTicket(new Ticket("FisherL1CDKelmpE01102024AMPU", false, true, "FisherL1CD", "BPS1", "KelmpE01102024AM")); //ticked Id = childId + journeyId
//        provider.addTicket(new Ticket("RodriguezA2EFKelmpE01102024AMPU", false, true, "RodriguezA2EF", "CS1", "KelmpE01102024AM"));
//        provider.addTicket(new Ticket("YoungI2CDKelmpE01102024AMPU", false, true, "YoungI2CD", "TH1", "KelmpE01102024AM"));
//        provider.addTicket(new Ticket("LopezE3GHKelmpE01102024AMPU", false, true, "LopezE3GH", "HV1", "KelmpE01102024AM"));
//
//        //morning drop off
//        provider.addTicket(new Ticket("GarciaO2IBKelmpE01102024AMDO", true, false, "GarciaO2IB", "SCH", "KelmpE01102024AM"));
//        provider.addTicket(new Ticket("DavisS5SMKelmpE01102024AMDO", true, false, "DavisS5SM", "SCH", "KelmpE01102024AM"));
//        provider.addTicket(new Ticket("ClarkL4GHKelmpE01102024AMDO", true, false, "ClarkL4GH", "SCH", "KelmpE01102024AM"));
//        provider.addTicket(new Ticket("JohnsonO3ASKelmpE01102024AMDO", true, false, "JohnsonO3AS", "SCH", "KelmpE01102024AM"));
//        provider.addTicket(new Ticket("FisherL1CDKelmpE01102024AMDO", true, false, "FisherL1CD", "SCH", "KelmpE01102024AM"));
//        provider.addTicket(new Ticket("RodriguezA2EFKelmpE01102024AMDO", true, false, "RodriguezA2EF", "SCH", "KelmpE01102024AM"));
//        provider.addTicket(new Ticket("YoungI2CDKelmpE01102024AMDO", true, false, "YoungI2CD", "SCH", "KelmpE01102024AM"));
//        provider.addTicket(new Ticket("LopezE3GHKelmpE01102024AMDO", true, false, "LopezE3GH", "SCH", "KelmpE01102024AM"));
//
//        //afternoon pick up
//        provider.addTicket(new Ticket("JonesE5HWKelmpE01102024PMPU", true, true, "JonesE5HW", "SCH", "KelmpE01102024PM"));
//        provider.addTicket(new Ticket("GarciaO2IBKelmpE01102024PMPU", true, true, "GarciaO2IB", "SCH", "KelmpE01102024PM"));
//        provider.addTicket(new Ticket("DavisS5SMKelmpE01102024PMPU", true, true, "DavisS5SM", "SCH", "KelmpE0110202PM"));
//        provider.addTicket(new Ticket("ClarkL4GHKelmpE01102024PMPU", true, true, "ClarkL4GH", "SCH", "KelmpE01102024PM"));
//        provider.addTicket(new Ticket("JohnsonO3ASKelmpE01102024PMPU", true, true, "JohnsonO3AS", "SCH", "KelmpE01102024PM"));
//        provider.addTicket(new Ticket("FisherL1CDKelmpE01102024PMPU", true, true, "FisherL1CD", "SCH", "KelmpE01102024PM")); //ticked Id = childId + journeyId
//        provider.addTicket(new Ticket("RodriguezA2EFKelmpE01102024PMPU", true, true, "RodriguezA2EF", "SCH", "KelmpE01102024PM"));
//        provider.addTicket(new Ticket("YoungI2CDKelmpE01102024PMPU", true, true, "YoungI2CD", "SCH", "KelmpE01102024PM"));
//        provider.addTicket(new Ticket("LopezE3GHKelmpE01102024PMPU", true, true, "LopezE3GH", "SCH", "KelmpE01102024PM"));
//
//        //afternoon drop off
//        provider.addTicket(new Ticket("JonesE5HWKelmpE01102024PMDO", false, false, "JonesE5HW", "BPS1", "KelmpE01102024AM"));
//        provider.addTicket(new Ticket("GarciaO2IBKelmpE01102024PMDO", false, false, "GarciaO2IB", "BPS1", "KelmpE01102024AM"));
//        provider.addTicket(new Ticket("DavisS5SMKelmpE01102024PMDO", false, false, "DavisS5SM", "RS1", "KelmpE01102024AM"));
//        provider.addTicket(new Ticket("ClarkL4GHKelmpE01102024PMDO", false, false, "ClarkL4GH", "OB1", "KelmpE01102024AM"));
//        provider.addTicket(new Ticket("JohnsonO3ASKelmpE01102024PMDO", false, false, "JohnsonO3AS", "OB1", "KelmpE01102024AM"));
//        provider.addTicket(new Ticket("FisherL1CDKelmpE01102024PMDO", false, false, "FisherL1CD", "BPS1", "KelmpE01102024AM")); //ticked Id = childId + journeyId
//        provider.addTicket(new Ticket("RodriguezA2EFKelmpE01102024PMDO", false, false, "RodriguezA2EF", "CS1", "KelmpE01102024AM"));
//        provider.addTicket(new Ticket("YoungI2CDKelmpE01102024PMDO", false, false, "YoungI2CD", "TH1", "KelmpE01102024AM"));
//        provider.addTicket(new Ticket("LopezE3GHKelmpE01102024PMDO", false, false, "LopezE3GH", "HV1", "KelmpE01102024AM"));
//
//    }
//}
