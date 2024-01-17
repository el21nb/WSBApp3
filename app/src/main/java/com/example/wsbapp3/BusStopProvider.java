package com.example.wsbapp3;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class BusStopProvider {
        public BusStopProvider() {
        }
        private final FirebaseFirestore db = FirebaseFirestore.getInstance();
        private final CollectionReference busStopCollection = db.collection("BusStops");
        private final CollectionReference journeysCollection = db.collection("Journeys");


    public void createBusStopChildren(String journeyId, Map<String, String> childrenWithBusStops) {
        BusStopProvider busStopProvider = new BusStopProvider();
        ChildProvider childProvider = new ChildProvider();
        DocumentReference journeyDocumentRef = journeysCollection.document(journeyId);
        CollectionReference journeyBusStopsCollectionRef = journeyDocumentRef.collection("JourneyBusStops");

        for (Map.Entry<String, String> entry : childrenWithBusStops.entrySet()) {
            String childId = entry.getKey();
            String busStopId = entry.getValue();
            DocumentReference journeyBusStopRef = journeyBusStopsCollectionRef.document(busStopId);
            CollectionReference busStopChildrenRef = journeyBusStopRef.collection("busStopChildren");
            Log.d("CBSC", "assigning child" + childId + busStopId);
            // Fetch the Child object
            childProvider.fetchChildById(childId, new ChildProvider.FetchChildCallback() {
                @Override
                public void onChildFetched(Child child) {
                    Log.d("CBSC", "main fun tried to fetch child" );

                    if (child != null) {
                        DocumentReference busStopChildRef = busStopChildrenRef.document(child.getId());

                        Log.d("CBSC", "main fun fetched Child");

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
                        // Handle the case when the Child is not found
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


    public interface FetchBusStopCallback {
            void onBusStopFetched(BusStop busStop);
            void onBusStopNotFound();
            void onFetchFailed(String errorMessage);
        }


        public void addBusStop(BusStop busStop) {
            // Set the document ID to the busStop name
            DocumentReference busStopRef = busStopCollection.document(busStop.getId());

            // Add the BusStop object to the specified document reference
            busStopRef.set(busStop)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("addBusStop", "BusStop document added with ID: " + busStop.getName());
                            } else {
                                Log.e("addBusStop", "Error adding BusStop document: " + task.getException().getMessage());
                                // Handle the exception as needed
                            }
                        }
                    });
        }

    }

