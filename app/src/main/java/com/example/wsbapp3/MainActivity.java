package com.example.wsbapp3;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wsbapp3.Journey;
import com.example.wsbapp3.JourneyProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            //makeJourneys();
            //makeBusStops();
            //makeChildren();
            makeJourneyBusStops();
            makeBusStopChildren();

        }
        private void makeJourneys() { //Creates top level collection of Journeys
            JourneyProvider provider = new JourneyProvider();
            provider.addJourney(new Journey("Evan Kelmp", "123215634", "09:00 01/10/2024", true));
            provider.addJourney(new Journey("Fearne Calloway", "7384294527", "09:00 02/10/2024", true));
            provider.addJourney(new Journey("Timothy Goose", "8784338381", "15:00 03/10/2024", false));
            provider.addJourney(new Journey("Misty Moore", "36354756522", "15:00 04/10/2024", false));
        }

        private void makeBusStops() { //Creates top level collection of BusStops
            BusStopProvider provider = new BusStopProvider();
            provider.addBusStop(new BusStop("Brentwood Police Station", "Brentwood Police Station, Wallesey Rd, SP2 6YU"));
                provider.addBusStop(new BusStop("Central Station", "Central Station, Main St, AB1 2CD"));
                provider.addBusStop(new BusStop("Green Park", "Green Park, Park Ave, XY3 4ZW"));
                provider.addBusStop(new BusStop("Sunset Mall", "Sunset Mall, Sunset Blvd, CD5 6EF"));
                provider.addBusStop(new BusStop("Tech Hub", "Tech Hub, Innovation St, EF6 7GH"));
                provider.addBusStop(new BusStop("Riverfront Square", "Riverfront Square, Riverside Rd, GH8 9IJ"));
                provider.addBusStop(new BusStop("Hilltop View", "Hilltop View, Summit Hill, IJ2 3KL"));
                provider.addBusStop(new BusStop("Ocean Breeze", "Ocean Breeze, Coastal Ave, KL4 5MN"));

            }
        private void makeChildren() { //Creates top level collection of Children
            ChildProvider provider = new ChildProvider();
            provider.addChild(new Child("Fearne", "Calloway", "Birdie Calloway (Mother)", "3578438311"));
            provider.addChild(new Child("Liam", "Smith", "Emma Smith (Mother)", "1234567890"));
            provider.addChild(new Child("Olivia", "Johnson", "Sophia Johnson (Mother)", "2345678901"));
            provider.addChild(new Child("Noah", "Williams", "Ava Williams (Mother)", "3456789012"));
            provider.addChild(new Child("Emma", "Jones", "Isabella Jones (Mother)", "4567890123"));
            provider.addChild(new Child("Aiden", "Brown", "Olivia Brown (Mother)", "5678901234"));
            provider.addChild(new Child("Sophia", "Davis", "Sophia Davis (Mother)", "6789012345"));
            provider.addChild(new Child("Jackson", "Miller", "Amelia Miller (Mother)", "7890123456"));
            provider.addChild(new Child("Olivia", "Garcia", "Sophia Garcia (Mother)", "8901234567"));
            provider.addChild(new Child("Lucas", "Martinez", "Isabella Martinez (Mother)", "9012345678"));
            provider.addChild(new Child("Ava", "Rodriguez", "Olivia Rodriguez (Mother)", "0123456789"));
            provider.addChild(new Child("Emma", "Lopez", "Sophia Lopez (Mother)", "1122334455"));
            provider.addChild(new Child("Logan", "Clark", "Emma Clark (Mother)", "2233445566"));
            provider.addChild(new Child("Sophia", "Hall", "Ava Hall (Mother)", "3344556677"));
            provider.addChild(new Child("Liam", "Fisher", "Sophia Fisher (Mother)", "4455667788"));
            provider.addChild(new Child("Isabella", "Young", "Olivia Young (Mother)", "5566778899"));
           }

           private void makeJourneyBusStops(){ //Goes into a journey document within Journeys, and creates a subCollection of busStops
            JourneyProvider provider = new JourneyProvider();
            //public void createJourneyBusStopsSubcollection(String journeyId, Map<String, Long> busStopsWithTimes) {
               Map<String, String> journeyMap = new HashMap<>(); //map of busStop Id (from BusStops colleciton) versus arrival time
               journeyMap.put("Brentwood Police Station", "0750");
               journeyMap.put("Tech Hub","0800");
               journeyMap.put("Hilltop View","0807");
               journeyMap.put("Central Station","0814");
               journeyMap.put("Ocean Breeze","0829");
               journeyMap.put("Riverfront Square","0840");
               provider.createJourneyBusStops("MBa1QzWDDB3hFxpDKZSn",journeyMap);
        }

        private void makeBusStopChildren() {
            BusStopProvider provider = new BusStopProvider();
        Map<String, String> busStopChildMap = new HashMap<>();
        busStopChildMap.put("LiamSmith", "Brentwood Police Station");
        busStopChildMap.put("JacksonMiller", "Brentwood Police Station");
        busStopChildMap.put("SophiaDavis", "Riverfront Square");
        busStopChildMap.put("IsabellaYoung", "Ocean Breeze");
        busStopChildMap.put("OliviaJohnson", "Central Station");
        busStopChildMap.put("AvaRodriguez", "Central Station");
        busStopChildMap.put("NoahWilliams", "Central Station");
        busStopChildMap.put("OliviaGarcia", "Tech Hub");
        busStopChildMap.put("EmmaLopez", "Hilltop View");
        provider.createBusStopChildren("MBa1QzWDDB3hFxpDKZSn",busStopChildMap);
        }

    }









