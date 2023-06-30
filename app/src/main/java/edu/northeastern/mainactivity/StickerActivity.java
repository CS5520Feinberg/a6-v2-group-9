package edu.northeastern.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

//import com.bumptech.glide.Glide;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.northeastern.mainactivity.apis.GetReceivedMessageAPI;
import edu.northeastern.mainactivity.apis.GetSentMessagesAPI;
import edu.northeastern.mainactivity.apis.MainAPI;
import edu.northeastern.mainactivity.dbmanager.FirebaseManager;
import edu.northeastern.mainactivity.interfaces.StickerGroupsCallback;
import edu.northeastern.mainactivity.modals.Message;

public class StickerActivity extends AppCompatActivity implements StickerGroupsCallback {

    private Spinner usersDropdown;
    private FloatingActionButton stickersBtn;
    private ImageView img;

    private FirebaseManager firebaseManager = FirebaseManager.getInstance();

    private GetReceivedMessageAPI receivedMessageAPI;

    private GetSentMessagesAPI sentMessagesAPI;

    private MainAPI mainAPI;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //lin.kevin1@northeastern.edu

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker);

        /**
         * All the functions to be used for fetching from DB are explained below - follow the same
         * */

        // initializing the stickers from the DB
        firebaseManager.initializeDefaultStickerGroups();

        // to get the stickers from the DB - There are 3 helper methods below that completes this function
        fetchStickerGroups();

        // For fetching the entire conversation history between user A and user B
        // I am sending the  user-tokens instead of usernames so you need to access those. - I need to ask @Shashank to create a helper method for this
        mainAPI = new MainAPI();
        ArrayList<Message> messageList = new ArrayList<>();
        mainAPI.getCombinedMessages("Zaq7p6ZL8aaGy4J5rKnUKZDtPwf1", "U5fcRJ4fvyZ5L6YKMPsdCT79AG92").thenApply(messages -> {
                    // Process the messages here
                    Log.d("Messages future ", "Messages future" + messages);
                    messageList.addAll(messages);
                    for(Message message : messages) {
                        Log.d("MessageValue", message.getImageUrl());
                        Log.d("MessageValue", message.getsender());
                        Log.d("MessageValue", "" + message.getTimestamp());
                        Log.d("MessageValue", message.getImageUrl());

                    }
                    return messages;
                })
                .exceptionally(ex -> {
                    // Handle any exceptions that occurred during message loading
                    Log.e("Messages", "Error loading messages", ex);
                    return null;
                }).thenRun(() -> {
                    // Access the messages after they have been fetched
                    Log.d("Fetched messages", "Fetched: " + messageList);
                });


        /***
         * End of backend related statements in on-Create function
         */


        usersDropdown = findViewById(R.id.usersDropdown);
        stickersBtn = findViewById(R.id.stickersBtn);
        img = findViewById(R.id.img);

//        sendMessage();

        // Replace by usernames/emails
        String[] temp = new String[] {
                "1", "2", "3", "4", "5"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, temp);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        usersDropdown.setAdapter(adapter);

        stickersBtn.setOnClickListener(v -> {
            StickersTabFragment stickersTabFragment = new StickersTabFragment();
            stickersTabFragment.show(getSupportFragmentManager(), stickersTabFragment.getTag());
        });
    }

    public void onStickerSelected(String stickerUrl) {
        Log.d("StickerActivity", "Selected sticker URL: " + stickerUrl);
        Glide.with(this)
                .load(stickerUrl)
                .into(img);
    }

    /***
     * To send a message from userA to userB
     */

    // Send message
    public void sendMessage(String sender, String receiver, String stickerTokenURL) {
//        Message message = new Message( "JcvHYO1eEkftiVELHEER07BkWO22", "Zaq7p6ZL8aaGy4J5rKnUKZDtPwf1", System.currentTimeMillis(), "https://firebasestorage.googleapis.com/v0/b/a6group9.appspot.com/o/crying1.png?alt=media&token=12cb5542-3ccb-4ed7-a197-7aefc9f4c8dd" );
        Message message = new Message(sender, receiver, System.currentTimeMillis(), stickerTokenURL);
        firebaseManager.addMessage(message);
    }

    // get received messages


    /**
     * Fetching the stickers
     * */

    // Async function handled through callbacks

    // to get the stickers
    public void fetchStickerGroups() {
        firebaseManager.getStickerGroups(this);
    }

    @Override
    public void onStickerGroupsLoaded(Map<String, List<String>> stickerGroups) {
        Log.d("Sticker Groups", "Stickers got " + stickerGroups);
    }

    @Override
    public void onStickerGroupsLoadFailed(DatabaseError databaseError) {
        Log.d("Sticker Groups", "Error " + databaseError);
    }


}