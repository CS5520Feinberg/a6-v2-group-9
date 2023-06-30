package edu.northeastern.mainactivity.apis;

import android.util.Log;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import edu.northeastern.mainactivity.dbmanager.FirebaseManager;
import edu.northeastern.mainactivity.interfaces.SentMessagesCallback;
import edu.northeastern.mainactivity.modals.Message;

//public class GetSentMessagesAPI implements SentMessagesCallback {

    public class GetSentMessagesAPI {
        public CompletableFuture<Map<String, Message>> getMessages(String senderId, String receiverId) {
            CompletableFuture<Map<String, Message>> future = new CompletableFuture<>();

            FirebaseManager firebaseManager = FirebaseManager.getInstance();
            firebaseManager.getSentMessagesForUser(senderId, receiverId, new SentMessagesCallback() {
                @Override
                public void onSentMessagesLoaded(Map<String, Message> sentMessages) {
                    future.complete(sentMessages);
                }

                @Override
                public void onSentMessagesLoadFailed(DatabaseError databaseError) {
                    future.completeExceptionally(databaseError.toException());
                }
            });

            return future;
        }
    }


//public class GetSentMessagesAPI implements SentMessagesCallback {
//
//    private FirebaseManager firebaseManager = FirebaseManager.getInstance();
//    private SentMessagesCallback callback;
//
//    public GetSentMessagesAPI(SentMessagesCallback callback) {
//        this.callback = callback;
//    }
//
//    public void getMessages(String senderId, String receiverId) {
//        firebaseManager.getSentMessagesForUser(senderId, receiverId, this);
//    }
//
//    @Override
//    public void onSentMessagesLoaded(Map<String, Message> sentMessages) {
//        List<Message> messageList = new ArrayList<>(sentMessages.values());
//        callback.onSentMessagesLoaded(messageList);
//    }
//
//    @Override
//    public void onSentMessagesLoadFailed(DatabaseError databaseError) {
//        callback.onSentMessagesLoadFailed(databaseError);
//    }
//}

