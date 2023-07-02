package edu.northeastern.mainactivity.dbmanager;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

//import edu.northeastern.mainactivity.LoginActivity;
import edu.northeastern.mainactivity.interfaces.CompletionHandler;
import edu.northeastern.mainactivity.interfaces.ReceivedMessagesCallback;
import edu.northeastern.mainactivity.interfaces.SentMessagesCallback;
import edu.northeastern.mainactivity.interfaces.StickerGroupsCallback;
import edu.northeastern.mainactivity.modals.Message;
import edu.northeastern.mainactivity.modals.StickerGroup;
import edu.northeastern.mainactivity.modals.UserInfo;

public class FirebaseManager {

    private static FirebaseManager instance;
    private static FirebaseAuth auth = FirebaseAuth.getInstance();

    private DatabaseReference stickersRef;
    private DatabaseReference messagesRef;

    private static FirebaseUser LoggedInUser;

    // We use the same password for all users to avoid having to worry about passwords
    private static String pass = "test_pass";

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
        Log.d("LOGGED IN ", "FB " + LoggedInUser);
    }

    // get user
    public FirebaseUser getLoggedInUser() {
        Log.d("LOGGED IN ", "FB " + LoggedInUser);
        return LoggedInUser;
    }

    //Auth and register

    public static CompletableFuture<FirebaseUser> registerOrAuthUser(String user_email) {
        CompletableFuture<FirebaseUser> future = new CompletableFuture<>();
        // Logic used is from example in FirebaseAuth documentation
        Log.d("REGISTERED USER", "User Email for registration: " + user_email);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userInfoRef = database.getReference("UserInfo");
        auth.createUserWithEmailAndPassword(user_email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser loggedInUser = auth.getCurrentUser(); // Store the registered/authenticated user in the static variable
                    LoggedInUser = loggedInUser;
                    saveDeviceToken(new CompletionHandler<String>() {
                        @Override
                        public void onSuccess(String deviceToken) {
                            UserInfo userInfo = new UserInfo(user_email, LoggedInUser.getUid(),deviceToken);

                            userInfoRef.child(user_email.replace('.','t')).setValue(userInfo);
                            future.complete(loggedInUser);
                        }

                        @Override
                        public void onError(Exception exception) {
                           System.out.println("GotError");
                        }
                    });

                } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                    Log.d("ERROR", "Email already exists! Trying standard login...");
                    authenticateUser(user_email)
                            .thenAccept(user -> future.complete(user)) // Complete the future with the logged-in user
                            .exceptionally(ex -> {
                                future.completeExceptionally(ex); // Complete the future exceptionally with the authentication exception
                                return null;
                            });
                } else {
                    Log.w("ERROR", "User creation failure!", task.getException());
                    future.completeExceptionally(task.getException()); // Complete the future exceptionally with the task exception
                }
            }
        });
        return future;
    }

    public void setupMessagesListener(String currentUserId) {
        DatabaseReference messagesRef = FirebaseDatabase.getInstance().getReference("messages");

        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Message message = snapshot.getValue(Message.class);
                String senderId = message.getsender();

                if (senderId.equals(currentUserId)) {
                    // Update the view with the received message
                    // Add the message to your conversation list or update the UI
                    // ...

                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle changes to existing messages if necessary
                // This can include updates to properties or nested child nodes
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // Handle removal of messages if necessary
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // Handle moving of messages if necessary
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle any error that occurred during the listener
            }
        };

        // Attach the child event listener to the "received" node under "messages"
        DatabaseReference receivedMessagesRef = messagesRef.child("received");
        receivedMessagesRef.addChildEventListener(childEventListener);
    }


    public static CompletableFuture<FirebaseUser> authenticateUser(String user_email) {
        CompletableFuture<FirebaseUser> future = new CompletableFuture<>();
        // Logic used is from example in FirebaseAuth documentation
        Log.d("LOGGEDIN", "User Email for login: " + user_email);
        auth.signInWithEmailAndPassword(user_email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d("SUCCESS", "Login Successful!");
                    FirebaseUser loggedInUser = auth.getCurrentUser(); // Store the authenticated user in the static variable
                    LoggedInUser = loggedInUser;
                    Log.d("LOGGEDIN", "User created! User: " + loggedInUser + " email: " + loggedInUser.getEmail() + " uid: " + loggedInUser.getUid());
                    future.complete(loggedInUser); // Complete the future with the logged-in user
                } else {
                    Log.w("LOGIN FAILED", "Login failed!", task.getException());
                    future.completeExceptionally(task.getException()); // Complete the future exceptionally with the task exception
                }
            }
        });
        return future;
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

    public void getAllSentMessagesForUser(String senderId, final SentMessagesCallback callback) {
        DatabaseReference sentRef = messagesRef.child("sent").child(senderId);
//        Query query = sentRef.orderByChild("receiver").equalTo(receiverId);

        sentRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
    private static void saveDeviceToken(CompletionHandler<String> completionHandler) {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String deviceToken = task.getResult();
                        completionHandler.onSuccess(deviceToken);
                    } else {
                        completionHandler.onError(task.getException());
                    }
                });
    }


}
