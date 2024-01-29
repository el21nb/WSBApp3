package com.example.wsbapp3.database;

import android.util.Log;

import com.example.wsbapp3.database.BusStopProvider;
import com.example.wsbapp3.models.BusStop;
import com.example.wsbapp3.models.Child;
import com.example.wsbapp3.models.Journey;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class JourneyProvider {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference journeysCollection = db.collection("Journeys");

    /**
     * Provider for interacting with the Journeys Collection, and its nested data, within Firestore
     */
    public JourneyProvider() {
        //empty constructor
    }

    /**
     * Adds a jacket deassignment timestamp to a child in the passengers subcollection within a journey document
     *
     * @param journeyId current journey id
     * @param childId   id of the child passenger to deassign jacket from
     * @param jacketId  id of the jacket to be deassigned
     */
    public void deassignJacket(String journeyId, String childId, String jacketId) {
        DocumentReference passengerDocumentRef = journeysCollection.document(journeyId)
                .collection("Passengers")
                .document(childId);

        Map<String, Object> jacketUpdates = new HashMap<>();
        jacketUpdates.put("JacketDeassignTime", FieldValue.serverTimestamp());
        passengerDocumentRef.update(jacketUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("dropOffPassenger", "Drop-off time updated for passenger: " + childId);
                        } else {
                            Log.e("dropOffPassenger", "Error updating drop-off time: " + task.getException().getMessage());
                            // Handle the exception as needed
                        }
                    }
                });
    }

    /**
     * Adds a drop off timestamp to a child in the passengers subcollection within a journey document
     *
     * @param journeyId Id of journey to access
     * @param childId   id of passenger to drop off
     */
    public void dropOffPassenger(String journeyId, String childId) {
        // Reference to the passenger document
        DocumentReference passengerDocumentRef = journeysCollection.document(journeyId)
                .collection("Passengers")
                .document(childId);

        // Update the dropOffTime field with the current timestamp
        Map<String, Object> updates = new HashMap<>();
        updates.put("dropOffTime", FieldValue.serverTimestamp());
        passengerDocumentRef.update(updates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("dropOffPassenger", "Drop-off time updated for passenger: " + childId);
                        } else {
                            Log.e("dropOffPassenger", "Error updating drop-off time: " + task.getException().getMessage());
                            // Handle the exception as needed
                        }
                    }
                });
    }

    /**
     * Adds a jacket code and assignment time to the passenger document within a
     * journey's passengers subcollection
     *
     * @param journeyId id of current journey
     * @param childId   id of passenger to assign jacket to
     * @param jacketId  id of jacket to be assigned
     */
    public void assignJacket(String journeyId, String childId, String jacketId) {
        DocumentReference passengerDocumentRef = journeysCollection.document(journeyId)
                .collection("Passengers")
                .document(childId);

        Map<String, Object> jacketUpdates = new HashMap<>();
        jacketUpdates.put("JacketAssignTime", FieldValue.serverTimestamp());
        jacketUpdates.put("Jacket", jacketId);
        passengerDocumentRef.update(jacketUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("dropOffPassenger", "Drop-off time updated for passenger: " + childId);
                        } else {
                            Log.e("dropOffPassenger", "Error updating drop-off time: " + task.getException().getMessage());
                            // Handle the exception as needed
                        }
                    }
                });

    }

    /**
     * Adds a new document to the Passengers subcollection of a journey.
     * Creates fields for Child, pick up time, drop off time, jacket assignment and deassigment times
     * populates Child and pick up time fields. Sets others to null
     *
     * @param journeyId current journey Id
     * @param child     Child object to be added to Passengers
     */
    public void pickUpPassenger(String journeyId, Child child) {
        // Create a map with all passenger fields
        Map<String, Object> passengerData = new HashMap<>();
        //Add child object to map
        passengerData.put("Child", child);
        // Set the OnboardTime to the current timestamp
        passengerData.put("pickUpTime", FieldValue.serverTimestamp());
        //Set other fields to null
        passengerData.put("dropOffTime", null);
        passengerData.put("Jacket", null);
        passengerData.put("JacketDeassignTime", null);
        passengerData.put("JacketAssignTime", null);

        // Set  new passengers document id to the child id
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

    /**
     * Creates a journeyBusStops collection within a journey.
     * Adds BusStop objects and their arrival times to documents within.
     *
     * @param journeyId         current journey Id
     * @param busStopsWithTimes map of bus stop Ids and arrival times
     */
    public void createJourneyBusStops(String journeyId, Map<String, String> busStopsWithTimes) {
        BusStopProvider busStopProvider = new BusStopProvider();
        DocumentReference journeyDocumentRef = journeysCollection.document(journeyId);
        CollectionReference journeyBusStopsCollectionRef = journeyDocumentRef.collection("JourneyBusStops");

        for (Map.Entry<String, String> entry : busStopsWithTimes.entrySet()) {

            String busStopId = entry.getKey();
            String arrivalTime = entry.getValue();

            // Fetch the BusStop object by Id
            busStopProvider.fetchBusStopById(busStopId, new BusStopProvider.FetchBusStopCallback() {
                @Override
                public void onBusStopFetched(BusStop busStop) {
                    // Create a map of the data to be added to the journeyBusStop document
                    Map<String, Object> busStopData = new HashMap<>();
                    busStopData.put("busStop", busStop);
                    busStopData.put("arrivalTime", arrivalTime); // Convert DateTime to Date
                    DocumentReference busStopRef = journeyBusStopsCollectionRef.document(busStopId);

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

    /**
     * Adds a journey object to the top level journeys collection.
     * Called during initial database population.
     *
     * @param journey Journey object to be added
     */
    public void addJourney(Journey journey) {
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

    /**
     * Fetches a journey object from the top level journeys collection, based on its Id
     *
     * @param journeyId Id of journey to be fetched
     * @param callback  Callback to handle fetch results
     */
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

    /**
     * Callback to handle journey fetch result
     */
    public interface FetchJourneyCallback {
        void onJourneyFetched(Journey journey);

        void onJourneyNotFound();

        void onFetchFailed(String errorMessage);
    }
}
