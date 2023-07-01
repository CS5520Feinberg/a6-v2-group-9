package edu.northeastern.mainactivity.interfaces;

import com.google.firebase.database.DatabaseError;

import java.util.Map;

import edu.northeastern.mainactivity.modals.Message;

public interface SentMessagesCallback {
    void onSentMessagesLoaded(Map<String, Message> receivedMessages);
    void onSentMessagesLoadFailed(DatabaseError databaseError);
}



