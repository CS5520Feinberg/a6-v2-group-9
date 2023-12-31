package edu.northeastern.mainactivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import edu.northeastern.mainactivity.apis.MainAPI;
import edu.northeastern.mainactivity.apis.SendNotification;
import edu.northeastern.mainactivity.dbmanager.FirebaseManager;
import edu.northeastern.mainactivity.entity.Notification;
import edu.northeastern.mainactivity.modals.Message;

public class StickerActivity extends AppCompatActivity {

    private Spinner usersDropdown;
    private FloatingActionButton stickersBtn;

    // This is important please don't create a new instance of firebaseManager , use the instance by ,getInstance()
    private FirebaseManager firebaseManager = FirebaseManager.getInstance();
    private FirebaseDatabase database = FirebaseDatabase.getInstance("https://a6group9-default-rtdb.firebaseio.com/");
    private DatabaseReference userInfoRef = database.getReference("UserInfo");
    private List<Message> conversation;
    private ChatAdapter chatAdapter;
    private RecyclerView chatRecyclerView;

    private Button historyBtn;
    private Button countBtn;

    private MainAPI mainAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker);

        chatRecyclerView = findViewById(R.id.stickerRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true);
        chatRecyclerView.setLayoutManager(layoutManager);
        usersDropdown = findViewById(R.id.usersDropdown);
        stickersBtn = findViewById(R.id.stickersBtn);
        historyBtn = findViewById(R.id.historyBtn);
        countBtn = findViewById(R.id.countBtn);

        List<String> userEmails = new ArrayList<>();
        userEmails.add("Select a user");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, userEmails);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        usersDropdown.setAdapter(adapter);

        userInfoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userEmails.subList(1, userEmails.size()).clear();
                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    String email = userSnapshot.child("email").getValue(String.class);
                    userEmails.add(email);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        usersDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String targetEmail = usersDropdown.getSelectedItem().toString();
                refreshConversation(targetEmail);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        stickersBtn.setOnClickListener(v -> {
            StickersTabFragment stickersTabFragment = new StickersTabFragment();
            stickersTabFragment.show(getSupportFragmentManager(), stickersTabFragment.getTag());
        });

        historyBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, HistoryActivity.class);
            startActivity(intent);
        });

        countBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, CountActivity.class);
            startActivity(intent);
        });
    }

    private CompletableFuture<String> getUidFromEmail(String targetEmail) {
        CompletableFuture<String> futureUid = new CompletableFuture<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference userInfoRef = database.getReference("UserInfo");
        userInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uid = null;
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String email = userSnapshot.child("email").getValue(String.class);
                    if (targetEmail.equals(email)) {
                        uid = userSnapshot.child("userID").getValue(String.class);
                        break;
                    }
                }
                futureUid.complete(uid);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                futureUid.completeExceptionally(error.toException());
            }
        });
        return futureUid;
    }

    public void onStickerSelected(String stickerUrl) {
        Log.d("StickerActivity", "Selected sticker URL: " + stickerUrl);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://a6group9-default-rtdb.firebaseio.com/");
        DatabaseReference userInfoRef = database.getReference("UserInfo");
        FirebaseUser firebaseUser = firebaseManager.getLoggedInUser();
        String senderID = firebaseUser.getUid();

        if (!usersDropdown.getSelectedItem().toString().equals("Select a user")) {
            String targetEmail = usersDropdown.getSelectedItem().toString();
            userInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String receiverID;
                    for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                        String email = userSnapshot.child("email").getValue(String.class);
                        if (targetEmail.equals(email)) {
                            receiverID = userSnapshot.child("userID").getValue(String.class);
                            sendMessage(senderID, receiverID, stickerUrl,email,firebaseUser.getEmail());
                            refreshConversation(targetEmail);
                            break;
                        }

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    private void refreshConversation(String targetEmail) {
        FirebaseUser firebaseUser = firebaseManager.getLoggedInUser();
        if (!targetEmail.equals("Select a user")) {
            getUidFromEmail(targetEmail).thenAccept(uid -> {
                if (uid != null && firebaseUser.getUid() != null) {
                    getEntireConversationUserAUserB(firebaseUser.getUid(), uid)
                            .thenAccept(messages -> {
                                runOnUiThread(() -> {
                                    conversation = messages;
                                    chatAdapter = new ChatAdapter(conversation, firebaseUser.getUid());
                                    chatRecyclerView.setAdapter(chatAdapter);
                                });
                            }).exceptionally(ex -> {
                                Log.e("Messages", "Error loading messages", ex);
                                return null;
                            });
                } else {
                    Log.e("UID", "Uid is null");
                }
            }).exceptionally(e -> {
                Log.e("UID", "Error getting uid", e);
                return null;
            });
        }
    }

    /***
     *
     * For users, use user-tokens (can be found in Firebase - storage ) for now, have to ask shashank to create a helper method to fetch user-tokens given the user-email
     *
     */
    /***
     * To send a message from userA to userB
     */

    // Send message
    public void sendMessage(String senderUid, String receiverUid, String stickerTokenURL, String receiverEmail, String senderEmail)  {
        Message message = new Message(senderUid, receiverUid, System.currentTimeMillis(), stickerTokenURL);
        Log.d("SENDING:", String.valueOf(message));
        SendNotification sendNotification = new SendNotification(new Notification(senderEmail,receiverEmail,""+System.currentTimeMillis(),stickerTokenURL));
        Log.d("SENDING:", String.valueOf(sendNotification));
        sendNotification.sendNotificationToFireBase();
        firebaseManager.addMessage(message);
    }

    /***
     * Fetch conversation between userA and userB
     */

    private CompletableFuture<List<Message>> getEntireConversationUserAUserB(String userA, String userB) {
        mainAPI = new MainAPI();
        CompletableFuture<List<Message>> messagesFuture = mainAPI.getCombinedMessages(userA, userB);

        messagesFuture.thenAccept(messages -> {
            // Access the fetched messages here
            Log.d("HAHAHA WORKEDDDDD", "PRINTING Messages Combined" + messages);
//            handleMessages(messages);

            // Call any other methods or perform actions based on messages
            // ...
        }).exceptionally(ex -> {
            // Handle error if message loading fails
            Log.e("Messages", "Error loading messages", ex);
            return null;
        });

        // Continue with other operations or return from the method without waiting
        return messagesFuture;
    }
}