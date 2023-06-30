package edu.northeastern.mainactivity.apis;

import android.util.Log;

import com.google.firebase.database.DatabaseError;

import java.util.Map;

import edu.northeastern.mainactivity.dbmanager.FirebaseManager;
import edu.northeastern.mainactivity.interfaces.ReceivedMessagesCallback;
import edu.northeastern.mainactivity.modals.Message;

public class GetReceivedMessageAPI implements ReceivedMessagesCallback {
    FirebaseManager firebaseManager = FirebaseManager.getInstance();
    public void getMessages(String receiverId) {
        firebaseManager.getReceivedMessagesForUser(receiverId, this);
    }
    @Override
    public void onReceivedMessagesLoaded(Map<String, Message> receivedMessages) {
        Log.d("Sticker Groups", "Messages received " + receivedMessages);
    }

    @Override
    public void onReceivedMessagesLoadFailed(DatabaseError databaseError) {

    }
}
