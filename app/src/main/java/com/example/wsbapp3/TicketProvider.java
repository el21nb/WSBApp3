package com.example.wsbapp3;

import android.util.Log;

import com.example.wsbapp3.Ticket;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TicketProvider {
    public TicketProvider() {
    }
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference ticketCollection = db.collection("Tickets");
    public void fetchTicketById(String ticketId, TicketProvider.FetchTicketCallback callback) {
        DocumentReference busStopDocumentRef = ticketCollection.document(ticketId);
        Log.d("CBSC","fetchTicketbyId"+ticketId);
        busStopDocumentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Ticket ticket = document.toObject(Ticket.class);
                        Log.d("CBSC", "fetched ticket");

                        callback.onTicketFetched(ticket);
                    } else {
                        callback.onTicketNotFound();
                        Log.d("CBSC", "ticket not found");

                    }
                } else {
                    Exception exception = task.getException();
                    callback.onFetchFailed(exception.getMessage());
                    Log.d("CBSC", "exception on fetch failed");
                }
            }
        });

    }
    public interface FetchTicketCallback {
        void onTicketFetched(Ticket ticket);
        void onTicketNotFound();
        void onFetchFailed(String errorMessage);
    }


    public void addTicket(Ticket ticket) { //add a ticket object to the top level tickets collection
        DocumentReference ticketRef = ticketCollection.document();
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

