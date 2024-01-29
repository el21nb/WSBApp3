package com.example.wsbapp3.database;

import android.text.TextUtils;
import android.util.Log;

import com.example.wsbapp3.models.Ticket;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Provider for interacting with Ticket objects in the Firestore db
 */
public class TicketProvider {
    public TicketProvider() {
        //empty constructor
    }

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    //Collection reference to top level Ticekts collection
    private final CollectionReference ticketCollection = db.collection("Tickets");

    /**
     * Fetch ticket object from top level Tickets collection, based on id
     *
     * @param ticketId id of ticket to be fetched
     * @param callback callback to handle fetch result
     */
    public void fetchTicketById(String ticketId, TicketProvider.FetchTicketCallback callback) {
        if (TextUtils.isEmpty(ticketId)) {
            callback.onTicketNotFound();
            return;
        }
        DocumentReference busStopDocumentRef = ticketCollection.document(ticketId);

        busStopDocumentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Ticket ticket = document.toObject(Ticket.class);
                        Log.d("CBSC", "Fetched ticket: " + ticketId);
                        callback.onTicketFetched(ticket);
                    } else {
                        Log.d("CBSC", "Ticket not found: " + ticketId);
                        callback.onTicketNotFound();
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
     * Callback interface to handle ticket fetch result
     */
    public interface FetchTicketCallback {
        void onTicketFetched(Ticket ticket);

        void onTicketNotFound();

        void onFetchFailed(String errorMessage);
    }

    /**
     * adds a ticket object to the top level Tickets collection
     *
     * @param ticket object to be added
     */
    public void addTicket(Ticket ticket) { //add a ticket object to the top level tickets collection
        DocumentReference ticketRef = ticketCollection.document(ticket.getTicketId());
        ticketRef.set(ticket)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("addTicket", "Ticket document added with ID: " + ticketRef);
                        } else {
                            Log.e("addTicket", "Error adding Ticket document: " + task.getException().getMessage());
                            // Handle the exception as needed
                        }
                    }
                });
    }
}

