package com.example.wsbapp3;

import android.text.TextUtils;
import android.util.Log;

import com.example.wsbapp3.Jacket;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class JacketProvider {
    public JacketProvider() {
    }
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference jacketCollection = db.collection("Jackets");
    public void fetchJacketById(String jacketId, JacketProvider.FetchJacketCallback callback) {
        Log.d("CBSC", "fetchJacketById: " + jacketId); // Log the jacketId for debugging

        if (TextUtils.isEmpty(jacketId)) {
            Log.d("CBSC", "Invalid jacketId: " + jacketId);
            callback.onJacketNotFound();
            return;
        }

        // Query the Jacket collection to find the document with the specified jacketId
        Query query = jacketCollection.whereEqualTo("id", jacketId);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot != null && !querySnapshot.isEmpty()) {
                        // Assuming there's only one document with the specified jacketId
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
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

    public interface FetchJacketCallback {
        void onJacketFetched(Jacket jacket);
        void onJacketNotFound();
        void onFetchFailed(String errorMessage);
    }


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