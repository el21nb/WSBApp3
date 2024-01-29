package com.example.wsbapp3.database;

import android.util.Log;

import com.example.wsbapp3.models.Child;
import com.example.wsbapp3.models.BusStop;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

/**
 * Provider class for interacting with BusStops in Firestore
 */
public class BusStopProvider {
    public BusStopProvider() {
        //required empty constructor
    }

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Reference to Top Level BusStops collection
    private final CollectionReference busStopCollection = db.collection("BusStops");

    //Reference to Top Level Journeys Collection
    private final CollectionReference journeysCollection = db.collection("Journeys");

    /**
     * Database initial population, called by intialise database in DatabaseProvider
     * TODO: change to a cloud function?
     * Adds a subcollection busStopChildren to each BusStop document in JourneyBusStops
     * to store where a child is booked to be
     *
     * @param journeyId            journey Id to add bus stop children
     * @param childrenWithBusStops map of key value pairs. key: BusStopID, value: Child Id
     */
    public void createBusStopChildren(String journeyId, Map<String, String> childrenWithBusStops) {
        BusStopProvider busStopProvider = new BusStopProvider();
        ChildProvider childProvider = new ChildProvider();

        //Document reference for the journey based on input journey Id
        DocumentReference journeyDocumentRef = journeysCollection.document(journeyId);
        //Collection reference for the JourneyBusStops subcollection in the journey doc
        CollectionReference journeyBusStopsCollectionRef = journeyDocumentRef.collection("JourneyBusStops");

        //iterate though map of bus stop and child Ids
        for (Map.Entry<String, String> entry : childrenWithBusStops.entrySet()) {
            String childId = entry.getKey();
            String busStopId = entry.getValue();
            //Fetch doc reference for the journeyBusStop using busStopId from current Map pair
            DocumentReference journeyBusStopRef = journeyBusStopsCollectionRef.document(busStopId);
            //Fetch or create collection reference for busStopChildren within that journeyBusStop
            CollectionReference busStopChildrenRef = journeyBusStopRef.collection("busStopChildren");

            // Fetch the Child object using the child Id
            childProvider.fetchChildById(childId, new ChildProvider.FetchChildCallback() {
                @Override
                public void onChildFetched(Child child) {

                    if (child != null) {
                        //Create busStopChild document with Id = child Id
                        DocumentReference busStopChildRef = busStopChildrenRef.document(child.getId());
                        //Add child object to document
                        busStopChildRef.set(child)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("CBSC", "Child added to BusStop");
                                        } else {
                                            Exception exception = task.getException();
                                            Log.e("CBSC", "Error adding Child to BusStop");
                                        }
                                    }
                                });
                    } else {
                        // Handle case when Child not found
                        Log.d("CBSC", "Child not found");
                    }
                }

                @Override
                public void onChildNotFound() {
                    // Handle the case when the Child is not found
                    Log.d("CBSC", "Child not found");
                }

                @Override
                public void onFetchFailed(String errorMessage) {
                    // Handle the fetch failure
                    Log.e("CBSC", "Fetch failed: " + errorMessage);
                }
            });
        }
    }

    /**
     * fetches a BusStop object from the top level BusStops collection, based on its Id
     *
     * @param busStopId ID of busStop to fetch
     * @param callback  for handling the fetch result
     */
    public void fetchBusStopById(String busStopId, FetchBusStopCallback callback) {
        DocumentReference busStopDocumentRef = busStopCollection.document(busStopId);

        busStopDocumentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        BusStop busStop = document.toObject(BusStop.class);
                        callback.onBusStopFetched(busStop);
                    } else {
                        callback.onBusStopNotFound();
                    }
                } else {
                    Exception exception = task.getException();
                    callback.onFetchFailed(exception.getMessage());
                }
            }
        });
    }

    /**
     * Callback interface for handling result of fetchBusStopById
     */
    public interface FetchBusStopCallback {
        void onBusStopFetched(BusStop busStop);

        void onBusStopNotFound();

        void onFetchFailed(String errorMessage);
    }


    /**
     * Adds a busStop object to the top level BusStops collection
     *
     * @param busStop busStop object to be added
     */
    public void addBusStop(BusStop busStop) {
        // Set the document ID to the busStop Id
        DocumentReference busStopRef = busStopCollection.document(busStop.getId());

        busStopRef.set(busStop)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("addBusStop", "BusStop document added with ID: " + busStop.getName());
                        } else {
                            Log.e("addBusStop", "Error adding BusStop document: " + task.getException().getMessage());
                        }
                    }
                });
    }

}

