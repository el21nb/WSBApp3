package com.example.wsbapp3.database;

import android.util.Log;

import com.example.wsbapp3.models.Child;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Provider class for interacting with Child objects within Firestore
 */
public class ChildProvider {
    public ChildProvider() {
        //Empty constructor
    }

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Collection reference for top level Children collection
    private final CollectionReference childCollection = db.collection("Children");

    /**
     * fetches a child object by its id from the top level Children collection
     * @param childId id of child to be fetched
     * @param callback callback to handle fetch result
     */
    public void fetchChildById(String childId, ChildProvider.FetchChildCallback callback) {
        DocumentReference childDocumentRef = childCollection.document(childId);

        childDocumentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Child child = document.toObject(Child.class);
                        Log.d("CBSC", "Fetched child: " + child.getFirstName());

                        callback.onChildFetched(child);
                    } else {
                        callback.onChildNotFound();
                        Log.d("CBSC", "Child not found");
                    }
                } else {
                    Exception exception = task.getException();
                    callback.onFetchFailed(exception.getMessage());
                    Log.d("CBSC", "Exception on fetch failed");
                }
            }
        });
    }

    public interface FetchChildCallback {
        void onChildFetched(Child child);

        void onChildNotFound();

        void onFetchFailed(String errorMessage);
    }

    /**
     * adds a child object to the top level children collection
     * @param child Child object to be added
     */
    public void addChild(Child child) {
        DocumentReference childRef = childCollection.document(child.getId());
        childRef.set(child)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("addChild", "Child document added with ID: " + childRef);
                        } else {
                            Log.e("addChild", "Error adding Child document: " + task.getException().getMessage());
                            // Handle the exception as needed
                        }
                    }
                });
    }
}

