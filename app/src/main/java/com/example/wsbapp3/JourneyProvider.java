package com.example.wsbapp3;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;

import java.util.HashMap;
import java.util.Map;

public class JourneyProvider {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference journeysCollection = db.collection("Journeys");

    public JourneyProvider() {
    }

    public void assignJacket(String ticketId, String jacketId){


    };
    public void removePassenger(String journeyId, Child child) { //add a Journey object to the top level Journeys collection
        CollectionReference onboardChildren = journeysCollection.document(journeyId).collection("Passengers");
        onboardChildren.add(child)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            DocumentReference childDocument = task.getResult();
                            Log.d("addedPassenger", "Child document added with ID: " + childDocument.getId());
                        } else {
                            Log.e("addedPassenger", "Error adding child document: " + task.getException().getMessage());
                            // Handle the exception as needed
                        }
                    }
                });
    }
    public void addPassenger(String journeyId, Child child) {
        // Create a map with fields: Child, Jacket, OnboardTime
        Map<String, Object> passengerData = new HashMap<>();
        passengerData.put("Child", child);
        // Set the OnboardTime to the current timestamp
        passengerData.put("OnboardTime", FieldValue.serverTimestamp());

        passengerData.put("Jacket", null);
        passengerData.put("OffboardTime", null);
        // Set the document id to the child id
        String childId = child.getId();
        // Add the map to the "Passengers" collection
        CollectionReference onboardChildren = journeysCollection.document(journeyId).collection("Passengers");
        DocumentReference passengerDocumentRef = onboardChildren.document(childId);

        passengerDocumentRef.set(passengerData)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("addedPassenger", "Passenger document added with ID: " + childId);
                        } else {
                            Log.e("addedPassenger", "Error adding passenger document: " + task.getException().getMessage());
                            // Handle the exception as needed
                        }
                    }
                });
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
                    DocumentReference busStopRef = journeyBusStopsCollectionRef.document(busStopId);

                            // Add the Map to the subcollection
                    busStopRef.set(busStopData)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        String busStopId = busStop.getId();
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
        // Set the ID of the Journey document to the Journey object's ID
        DocumentReference journeyDocumentRef = journeysCollection.document(journey.getId());

        journeyDocumentRef.set(journey)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("addJourney", "Journey document added with ID: " + journey.getId());
                        } else {
                            Log.e("addJourney", "Error adding journey document: " + task.getException().getMessage());
                            // Handle the exception as needed
                        }
                    }
                });
    }

        public void fetchJourneyById(String journeyId, FetchJourneyCallback callback) {
            DocumentReference journeyRef = db.collection("Journeys").document(journeyId);

            journeyRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Journey journey = document.toObject(Journey.class);
                            callback.onJourneyFetched(journey);
                        } else {
                            callback.onJourneyNotFound();
                        }
                    } else {
                        callback.onFetchFailed(task.getException().getMessage());
                    }
                }
            });
        }

        // Other methods and classes

        public interface FetchJourneyCallback {
            void onJourneyFetched(Journey journey);

            void onJourneyNotFound();

            void onFetchFailed(String errorMessage);
        }
    }
