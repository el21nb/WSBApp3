package com.example.wsbapp3;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.wsbapp3.Journey;
import com.example.wsbapp3.JourneyProvider;
import com.example.wsbapp3.databinding.ActivityMainBinding;
import com.example.wsbapp3.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private String currentJourneyId;

    private ActivityMainBinding binding;

    public String getCurrentJourneyId() {
        return currentJourneyId;
    }

    public void setCurrentJourneyId(String currentJourneyId) {
        this.currentJourneyId = currentJourneyId;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.home) {
                replaceFragment(new HomeFragment());
            } else if (itemId == R.id.route) {
                replaceFragment(new RouteFragment());
            } else if (itemId == R.id.scan) {
                replaceFragment(new ScanFragment());
            }
            return true;
        });

        initialiseDatabase();


    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.addToBackStack(null); // Add to back stack
        fragmentTransaction.commit();
    }

        private void initialiseDatabase(){ //populates the database, including one fully populated journey with matching tickets
        currentJourneyId = "KelmpE01102024AM"; //journeyId = driver surname + driver initial + date + AM/PM
        makeJourneys();
        makeBusStops();
        makeChildren();
        makeTickets();
        makeJackets();
        makeJourneyBusStops();
        makeBusStopChildren();
    }

    private void makeJackets(){
        Log.d("jacket", "making jackets");
        JacketProvider provider = new JacketProvider();
        for(int i =0; i<15; i++){
            Jacket jacket = new Jacket(String.format("JA%02d",i+1));
            provider.addJacket(jacket);
        }
    }
        private void makeJourneys() { //Creates top level collection of Journeys
            JourneyProvider provider = new JourneyProvider();
            provider.addJourney(new Journey("KelmpE01102024AM","Evan Kelmp", "123215634", "09:00 01/10/2024", true));
            provider.addJourney(new Journey("AppelbeesK102102024AM","Kristen Appelbees", "7384294527", "09:00 02/10/2024", true));
            provider.addJourney(new Journey("GooseT03102024PM","Timothy Goose", "8784338381", "15:00 03/10/2024", false));
            provider.addJourney(new Journey("MooreM04102024PM","Misty Moore", "36354756522", "15:00 04/10/2024", false));
        }

        private void makeBusStops() { //Creates top level collection of BusStops
            BusStopProvider provider = new BusStopProvider();
            provider.addBusStop(new BusStop("BPS1", "Brentwood Police Station", "Brentwood Police Station, Wallesey Rd, SP2 6YU"));
                provider.addBusStop(new BusStop("CS1","Central Station", "Central Station, Main St, AB1 2CD"));
                provider.addBusStop(new BusStop("GP1", "Green Park", "Green Park, Park Ave, XY3 4ZW"));
                provider.addBusStop(new BusStop("SM1","Sunset Mall", "Sunset Mall, Sunset Blvd, CD5 6EF"));
                provider.addBusStop(new BusStop("TH1","Tech Hub", "Tech Hub, Innovation St, EF6 7GH"));
                provider.addBusStop(new BusStop("RS1", "Riverfront Square", "Riverfront Square, Riverside Rd, GH8 9IJ"));
                provider.addBusStop(new BusStop("HV1","Hilltop View", "Hilltop View, Summit Hill, IJ2 3KL"));
                provider.addBusStop(new BusStop("OB1","Ocean Breeze", "Ocean Breeze, Coastal Ave, KL4 5MN"));

            }
        private void makeChildren() { //Creates top level collection of Children
            ChildProvider provider = new ChildProvider();
            provider.addChild(new Child("CallowayF3AS", "Fearne", "Calloway", "Birdie Calloway (Mother)", "3578438311", "3AS"));
            provider.addChild(new Child("SmithL4GH","Liam", "Smith", "Emma Smith (Mother)", "1234567890", "4GH"));
            provider.addChild(new Child("JohnsonO3AS","Olivia", "Johnson", "Sophia Johnson (Mother)", "2345678901","3AS"));
            provider.addChild(new Child("WilliamsN6PW","Noah", "Williams", "Ava Williams (Mother)", "3456789012", "6PW"));
            provider.addChild(new Child("JonesE5HW","Emma", "Jones", "Isabella Jones (Mother)", "4567890123","5HW"));
            provider.addChild(new Child("BrownA5HW","Aiden", "Brown", "Olivia Brown (Mother)", "5678901234", "5HW"));
            provider.addChild(new Child("DavisS5SM","Sophia", "Davis", "Sophia Davis (Mother)", "6789012345","5SM"));
            provider.addChild(new Child("MillerJ2IB","Jackson", "Miller", "Amelia Miller (Mother)", "7890123456","2IB"));
            provider.addChild(new Child("GarciaO2IB","Olivia", "Garcia", "Sophia Garcia (Mother)", "8901234567","2IB"));
            provider.addChild(new Child("MartinezL1EF", "Lucas", "Martinez", "Isabella Martinez (Mother)", "9012345678", "1EF"));
            provider.addChild(new Child("RodriguezA2EF", "Ava", "Rodriguez", "Olivia Rodriguez (Mother)", "0123456789", "2EF"));
            provider.addChild(new Child("LopezE3GH", "Emma", "Lopez", "Sophia Lopez (Mother)", "1122334455", "3GH"));
            provider.addChild(new Child("ClarkL4GH", "Logan", "Clark", "Emma Clark (Mother)", "2233445566", "4GH"));
            provider.addChild(new Child("HallS5GH", "Sophia", "Hall", "Ava Hall (Mother)", "3344556677", "5GH"));
            provider.addChild(new Child("FisherL1CD", "Liam", "Fisher", "Sophia Fisher (Mother)", "4455667788", "1CD"));
            provider.addChild(new Child("YoungI2CD", "Isabella", "Young", "Olivia Young (Mother)", "5566778899", "2CD"));
           }

           private void makeJourneyBusStops(){ //Goes into a journey document within Journeys, and creates a subCollection of busStops
               JourneyProvider provider = new JourneyProvider();
               //public void createJourneyBusStopsSubcollection(String journeyId, Map<String, Long> busStopsWithTimes) {
               Map<String, String> journeyMap = new HashMap<>(); //map of busStop Id (from BusStops colleciton) versus arrival time
               journeyMap.put("BPS1", "0750");
               journeyMap.put("TH1","0800");
               journeyMap.put("HV1","0807");
               journeyMap.put("CS1","0814");
               journeyMap.put("OB1","0829");
               journeyMap.put("RS1","0840");
               provider.createJourneyBusStops(currentJourneyId,journeyMap);
        }

        private void makeBusStopChildren() {
        BusStopProvider provider = new BusStopProvider();
        Map<String, String> busStopChildMap = new HashMap<>();
        busStopChildMap.put("JonesE5HW", "BPS1");
        busStopChildMap.put("GarciaO2IB", "BPS1");
        busStopChildMap.put("DavisS5SM", "RS1");
        busStopChildMap.put("ClarkL4GH", "OB1");
        busStopChildMap.put("JohnsonO3AS", "OB1");
        busStopChildMap.put("FisherL1CD", "CS1");
        busStopChildMap.put("RodriguezA2EF", "CS1");
        busStopChildMap.put("YoungI2CD", "TH1");
        busStopChildMap.put("LopezE3GH", "HV1");
        provider.createBusStopChildren("KelmpE01102024AM",busStopChildMap);
        }

    private void makeTickets() {
        TicketProvider provider = new TicketProvider();
        provider.addTicket(new Ticket("JonesE5HWKelmpE01102024AM",true,"JonesE5HW", "BPS1","KelmpE01102024AM"));
        provider.addTicket(new Ticket("GarciaO2IBKelmpE01102024AM",true,"GarciaO2IB", "BPS1","KelmpE01102024AM"));
        provider.addTicket(new Ticket("DavisS5SMKelmpE01102024AM",true,"DavisS5SM", "RS1","KelmpE01102024AM"));
        provider.addTicket(new Ticket("ClarkL4GHKelmpE01102024AM",true,"ClarkL4GH", "OB1","KelmpE01102024AM"));
        provider.addTicket(new Ticket("JohnsonO3ASKelmpE01102024AM",true,"JohnsonO3AS","OB1","KelmpE01102024AM"));
        provider.addTicket(new Ticket("FisherL1CDKelmpE01102024AM",true,"FisherL1CD", "BPS1","KelmpE01102024AM")); //ticked Id = childId + journeyId
        provider.addTicket(new Ticket("RodriguezA2EFKelmpE01102024AM",true,"RodriguezA2EF", "CS1","KelmpE01102024AM"));
        provider.addTicket(new Ticket("YoungI2CDKelmpE01102024AM",true,"YoungI2CD", "TH1","KelmpE01102024AM"));
        provider.addTicket(new Ticket("LopezE3GHKelmpE01102024AM",true,"LopezE3GH", "HV1","KelmpE01102024AM"));
    }
    }









