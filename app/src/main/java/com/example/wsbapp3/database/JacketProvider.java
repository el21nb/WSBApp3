package com.example.wsbapp3.database;

import android.util.Log;

import com.example.wsbapp3.models.Jacket;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Provider class for interacting with jacket objects in Firestore
 */
public class JacketProvider {
    public JacketProvider() {
        //empty constructor
    }

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Reference to top level Jackets Collection
    private final CollectionReference jacketCollection = db.collection("Jackets");

    /**
     * Fetches a jacket object from top level jackets collection, using its Id
     *
     * @param jacketId
     * @param callback
     */
    public void fetchJacketById(String jacketId, JacketProvider.FetchJacketCallback callback) {
        DocumentReference jacketDocumentRef = jacketCollection.document(jacketId);

        jacketDocumentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Jacket jacket = document.toObject(Jacket.class);
                        Log.d("CBSC", "Fetched jacket: " + jacketId);
                        callback.onJacketFetched(jacket);
                    } else {
                        Log.d("CBSC", "Jacket not found: " + jacketId);
                        callback.onJacketNotFound();
                    }
                } else {
                    Exception exception = task.getException();
                    Log.d("CBSC", "Exception on fetch failed: " + exception.getMessage());
                    callback.onFetchFailed(exception.getMessage());
                }
            }
        });
    }

    /**
     * Callback interface for handling fetch result
     */

    public interface FetchJacketCallback {
        void onJacketFetched(Jacket jacket);

        void onJacketNotFound();

        void onFetchFailed(String errorMessage);
    }

    /**
     * Adds a jacket object to top level jackets collection
     * Used during initial database population
     *
     * @param jacket Jacket to be added
     */
    public void addJacket(Jacket jacket) { //add a jacket object to the top level jackets collection
        DocumentReference jacketRef = jacketCollection.document(jacket.getIdentifier());
        jacketRef.set(jacket)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("addJacket", "Jacket document added with ID: " + jacketRef);
                        } else {
                            Log.e("addJacket", "Error adding Jacket document: " + task.getException().getMessage());
                            // Handle the exception as needed
                        }
                    }
                });
    }
}