package edu.northeastern.mainactivity.dbmanager;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.northeastern.mainactivity.LoginActivity;
import edu.northeastern.mainactivity.interfaces.ReceivedMessagesCallback;
import edu.northeastern.mainactivity.interfaces.SentMessagesCallback;
import edu.northeastern.mainactivity.interfaces.StickerGroupsCallback;
import edu.northeastern.mainactivity.modals.Message;
import edu.northeastern.mainactivity.modals.StickerGroup;

public class FirebaseManager {

    private static FirebaseManager instance;
    private static FirebaseAuth auth = FirebaseAuth.getInstance();
    private DatabaseReference stickersRef;
    private DatabaseReference messagesRef;

    private static FirebaseUser LoggedInUser;

    // tokens
    private String cry1 = "https://firebasestorage.googleapis.com/v0/b/a6group9.appspot.com/o/crying1.png?alt=media&token=12cb5542-3ccb-4ed7-a197-7aefc9f4c8dd";
    private String cry2 = "https://firebasestorage.googleapis.com/v0/b/a6group9.appspot.com/o/crying2.png?alt=media&token=d86c4d6b-61b7-4379-890e-04d7af1b294e";
    private String laugh1 = "https://firebasestorage.googleapis.com/v0/b/a6group9.appspot.com/o/laughing1.png?alt=media&token=ac8b7920-c262-4fc3-9be1-e59c4e56d508";
    private String laugh2 = "https://firebasestorage.googleapis.com/v0/b/a6group9.appspot.com/o/laughing2.png?alt=media&token=55036057-2543-441d-8948-46fa38015358";

    private String dance1 = "https://firebasestorage.googleapis.com/v0/b/a6group9.appspot.com/o/dancing1.webp?alt=media&token=51e33408-3976-4297-9714-8d82833934cc";
    private String dance2 = "https://firebasestorage.googleapis.com/v0/b/a6group9.appspot.com/o/dancing2.webp?alt=media&token=d3fa4b71-e40d-4ff7-b1a5-d6f4bd28b4f9";

    private String emoji1 = "https://firebasestorage.googleapis.com/v0/b/a6group9.appspot.com/o/emoji1.webp?alt=media&token=baf20ed2-489b-4927-9c96-2e37b7170006";
    private String emoji2 = "https://firebasestorage.googleapis.com/v0/b/a6group9.appspot.com/o/emoji2.png?alt=media&token=e1e09316-5a43-4059-9448-56b301a8edce";

    private FirebaseManager() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        stickersRef = database.getReference().child("Stickers");
        messagesRef = database.getReference().child("Messages");
    }

    public static FirebaseManager getInstance() {
        if (instance == null) {
            instance = new FirebaseManager();
        }
        return instance;
    }

    public static FirebaseAuth getAuthInstance() {
        return auth;
    }

    // set user
    public void setLoggedInUser(FirebaseUser firebaseUser) {
        LoggedInUser = firebaseUser;
    }

    // get user
    public FirebaseUser getLoggedInUser() {
        return LoggedInUser;
    }

    public void initializeDefaultStickerGroups() {
        stickersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    addStickerGroup("Emoji", Arrays.asList(emoji1, emoji2));
                    addStickerGroup("Laughing", Arrays.asList(laugh1, laugh2));
                    addStickerGroup("Crying", Arrays.asList(cry1,cry2));
                    addStickerGroup("Dancing", Arrays.asList(dance1,dance2));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error if needed
            }
        });
    }


    public void addStickerGroup(String groupName, List<String> imageUrls) {
        DatabaseReference groupRef = stickersRef.child(groupName);
        groupRef.setValue(new StickerGroup(groupName, imageUrls));
    }

    // get stickers group wise in HashMap
    public void getStickerGroups(final StickerGroupsCallback callback) {
        stickersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, List<String>> stickerGroups = new HashMap<>();

                for (DataSnapshot groupSnapshot : dataSnapshot.getChildren()) {
                    String groupName = groupSnapshot.getKey();
                    List<String> stickerUrls = new ArrayList<>();
                    for (DataSnapshot stickerSnapshot : groupSnapshot.getChildren()) {
                        Object stickerValue = stickerSnapshot.getValue();
                        String stickerUrl = (stickerValue != null) ? stickerValue.toString() : "";
                        stickerUrls.add(stickerUrl);
                        Log.d("StickerActivity", "Sticker URL: " + stickerUrl + ", Data Type: " + (stickerValue != null ? stickerValue.getClass().getSimpleName() : "null"));
                    }

                    stickerGroups.put(groupName, stickerUrls);
                }

                callback.onStickerGroupsLoaded(stickerGroups);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onStickerGroupsLoadFailed(databaseError);
            }
        });
    }


    public void removeStickerGroup(String groupName) {
        DatabaseReference groupRef = stickersRef.child(groupName);
        groupRef.removeValue();
    }

    public void getSentMessagesForUser(String senderId, String receiverId, final SentMessagesCallback callback) {
        DatabaseReference sentRef = messagesRef.child("sent").child(senderId);
        Query query = sentRef.orderByChild("receiver").equalTo(receiverId);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Message> sentMessages = new HashMap<>();

                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    String messageId = messageSnapshot.getKey();
                    Message message = messageSnapshot.getValue(Message.class);
                    sentMessages.put(messageId, message);
                    Log.d("Sticker Groups", "Message: " + message.getsender());
                }

                callback.onSentMessagesLoaded(sentMessages);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onSentMessagesLoadFailed(databaseError);
            }
        });
    }


    public void getReceivedMessagesForUser(String receiverId, final ReceivedMessagesCallback callback) {
        DatabaseReference receivedRef = messagesRef.child("received").child(receiverId);
        Log.d("DBCONSOLE", "received" + receivedRef);
        receivedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, Message> receivedMessages = new HashMap<>();

                for (DataSnapshot messageSnapshot : dataSnapshot.getChildren()) {
                    String messageId = messageSnapshot.getKey();
                    Message message = messageSnapshot.getValue(Message.class);
                    receivedMessages.put(messageId, message);
                }

                callback.onReceivedMessagesLoaded(receivedMessages);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onReceivedMessagesLoadFailed(databaseError);
            }
        });
}



    public void addMessage(Message message) {
        DatabaseReference messageRef = messagesRef.child("sent").child(message.getsender()).push();
        messageRef.setValue(message);

        DatabaseReference receivedRef = messagesRef.child("received").child(message.getreceiver()).push();
        receivedRef.setValue(message);
    }

    // Other methods for removing, updating, and managing messages

    // ...
}
