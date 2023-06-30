package edu.northeastern.mainactivity.interfaces;

import com.google.firebase.database.DatabaseError;

import java.util.Map;

import edu.northeastern.mainactivity.modals.Message;

public interface ReceivedMessagesCallback {
    void onReceivedMessagesLoaded(Map<String, Message> receivedMessages);
    void onReceivedMessagesLoadFailed(DatabaseError databaseError);
}

