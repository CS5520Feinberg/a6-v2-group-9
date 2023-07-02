package edu.northeastern.mainactivity.apis;

import android.util.Log;

import com.google.firebase.database.DatabaseError;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import edu.northeastern.mainactivity.dbmanager.FirebaseManager;
import edu.northeastern.mainactivity.interfaces.ReceivedMessagesCallback;
import edu.northeastern.mainactivity.modals.Message;

public class GetReceivedMessageAPI implements ReceivedMessagesCallback {
    public void getMessages(String receiverId) {
        FirebaseManager firebaseManager = FirebaseManager.getInstance();
        firebaseManager.getReceivedMessagesForUser(receiverId, this);
    }
    @Override
    public void onReceivedMessagesLoaded(Map<String, Message> receivedMessages) {
        Log.d("Sticker Groups", "Messages received " + receivedMessages);
    }

    @Override
    public void onReceivedMessagesLoadFailed(DatabaseError databaseError) {

    }

    public CompletableFuture<Map<String, Message>> getAllReceivedMessagesSingleUser(String receiverId) {
        CompletableFuture<Map<String, Message>> future = new CompletableFuture<>();

        FirebaseManager firebaseManager = FirebaseManager.getInstance();
        firebaseManager.getAllReceivedMessagesForUser(receiverId, new ReceivedMessagesCallback() {
            @Override
            public void onReceivedMessagesLoaded(Map<String, Message> receivedMessages) {
                future.complete(receivedMessages);
            }

            @Override
            public void onReceivedMessagesLoadFailed(DatabaseError databaseError) {
                future.completeExceptionally(databaseError.toException());
            }
        });
        return future;
    }
}
