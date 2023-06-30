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
import java.util.concurrent.CompletableFuture;

import edu.northeastern.mainactivity.apis.GetReceivedMessageAPI;
import edu.northeastern.mainactivity.apis.GetSentMessagesAPI;
import edu.northeastern.mainactivity.apis.MainAPI;
import edu.northeastern.mainactivity.dbmanager.FirebaseManager;
import edu.northeastern.mainactivity.interfaces.StickerGroupsCallback;
import edu.northeastern.mainactivity.modals.Message;

public class StickerActivity extends AppCompatActivity {

    private Spinner usersDropdown;
    private FloatingActionButton stickersBtn;
    private ImageView img;

    // This is important please don't create a new instance of firebaseManager , use the instance by ,getInstance()
    private FirebaseManager firebaseManager = FirebaseManager.getInstance();



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


        /***
         * Testing some more things
         */




        /***
         * End of backend related statements in on-Create function
         */


        usersDropdown = findViewById(R.id.usersDropdown);
        stickersBtn = findViewById(R.id.stickersBtn);
        img = findViewById(R.id.img);


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



    /**
     * Fetching the stickers
     * */



    // to get stickers
    private void getStickerGroups() {
        mainAPI = new MainAPI();
        CompletableFuture<Map<String, List<String>>> stickerGroupsFuture = mainAPI.fetchStickerGroups();

        stickerGroupsFuture.thenAccept(stickerGroups -> {
            // Access the fetched sticker groups here
            Log.d("HAHAHA WORKEDDDDD", "PRINTING STICKERS" + stickerGroups);

            // Call any other methods
            // eg. useStickers(stickerGroups)
        }).exceptionally(ex -> {
            // Handle error if sticker groups loading fails
            Log.e("Sticker Groups", "Error loading sticker groups", ex);
            return null;
        });

        // Continue with other operations or return from the method without waiting
    }


    private void getEntireConversationUSerAUserB() {
        mainAPI = new MainAPI();
        CompletableFuture<List<Message>> messagesFuture = mainAPI.getCombinedMessages("Zaq7p6ZL8aaGy4J5rKnUKZDtPwf1", "U5fcRJ4fvyZ5L6YKMPsdCT79AG92");

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
    }


    private void getAllSentMessageSingleUser() {
        mainAPI = new MainAPI();
        CompletableFuture<List<Message>> messagesFuture = mainAPI.getSentMessagesSingleUser("Zaq7p6ZL8aaGy4J5rKnUKZDtPwf1");

        messagesFuture.thenAccept(messages -> {
            // Access the fetched messages here
            Log.d("SINGE MESSAGES", "HAHAHAH" + messages);
//            handleMessages(messages);

            // Call any other methods or perform actions based on messages
            // ...
        }).exceptionally(ex -> {
            // Handle error if message loading fails
            Log.e("Messages", "Error loading messages", ex);
            return null;
        });

        // Continue with other operations or return from the method without waiting
    }


    private void getCountStickers() {
        mainAPI = new MainAPI();
        CompletableFuture<Map<String, List<String>>> stickerGroupsFuture = mainAPI.fetchStickerGroups();
        CompletableFuture<List<Message>> messagesFuture = mainAPI.getSentMessagesSingleUser("Zaq7p6ZL8aaGy4J5rKnUKZDtPwf1");

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(stickerGroupsFuture, messagesFuture);

        combinedFuture.thenRun(() -> {
            Map<String, List<String>> stickerGroups = stickerGroupsFuture.join();
            List<Message> sentMessages = messagesFuture.join();

            // Call the getStickerCount method with the fetched sticker groups and sent messages
            Map<String, Integer> count = mainAPI.getStickerCount(stickerGroups, sentMessages);

            // Print the sticker count for each group
            for (String stickerGroup : count.keySet()) {
                int c = count.get(stickerGroup);
                Log.d("Sticker Count", "Sticker Group: " + stickerGroup + ", Count: " + c);
            }
        }).exceptionally(ex -> {
            // Handle error if fetching sticker groups or messages fails
            Log.e("Fetch Error", "Error fetching sticker groups or messages", ex);
            return null;
        });

        // Continue with other operations or return from the method without waiting
    }

}