package com.example.wsbapp3;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;

import java.util.HashMap;
import java.util.Map;

public class JourneyProvider {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference journeysCollection = db.collection("Journeys");

    public JourneyProvider() {
    }

    public void createJourneyBusStops(String journeyId, Map<String, String> busStopsWithTimes) {
        BusStopProvider busStopProvider = new BusStopProvider();
        DocumentReference journeyDocumentRef = journeysCollection.document(journeyId);
        CollectionReference journeyBusStopsCollectionRef = journeyDocumentRef.collection("JourneyBusStops");

        for (Map.Entry<String, String> entry : busStopsWithTimes.entrySet()) {

            String busStopId = entry.getKey();
            String arrivalTime = entry.getValue();

            // Fetch the BusStop object
            busStopProvider.fetchBusStopById(busStopId, new BusStopProvider.FetchBusStopCallback() {
                @Override
                public void onBusStopFetched(BusStop busStop) {
                    // Create a Map to represent the data you want to store in the document
                    Map<String, Object> busStopData = new HashMap<>();
                    busStopData.put("busStop", busStop);
                    busStopData.put("arrivalTime", arrivalTime); // Convert DateTime to Date
                    DocumentReference busStopRef = journeyBusStopsCollectionRef.document(busStop.getName());

                            // Add the Map to the subcollection
                    busStopRef.set(busStopData)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        String busStopId = busStop.getName();
                                        Log.d("JourneyProvider", "Bus Stop added to BusStops subcollection with ID: " + busStopId);
                                    } else {
                                        Exception exception = task.getException();
                                        Log.e("JourneyProvider", "Error adding Bus Stop to BusStops subcollection: " + exception.getMessage());
                                    }
                                }
                            });
                }
                @Override
                public void onBusStopNotFound() {
                    // Handle the case when the BusStop is not found
                    Log.d("YourActivity", "BusStop not found");
                }

                @Override
                public void onFetchFailed(String errorMessage) {
                    // Handle the fetch failure
                    Log.e("YourActivity", "Fetch failed: " + errorMessage);
                }
            });
        }
    }

    public void addJourney(Journey journey) { //add a Journey object to the top level Journeys collection
        journeysCollection.add(journey)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            DocumentReference journeyDocument = task.getResult();
                            Log.d("addJourney", "Journey document added with ID: " + journeyDocument.getId());
                        } else {
                            Log.e("addJourney", "Error adding journey document: " + task.getException().getMessage());
                            // Handle the exception as needed
                        }
                    }
                });
    }
}