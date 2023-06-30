package edu.northeastern.mainactivity.apis;

import android.util.Log;

import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import edu.northeastern.mainactivity.dbmanager.FirebaseManager;
import edu.northeastern.mainactivity.interfaces.SentMessagesCallback;
import edu.northeastern.mainactivity.interfaces.StickerGroupsCallback;
import edu.northeastern.mainactivity.modals.Message;

public class MainAPI {
    FirebaseManager firebaseManager;
    GetSentMessagesAPI getSentMessagesAPI;
    List<Message> messages;
    private List<Message> getSortedMessages(Map<String, Message> allMessages) {
        // Combine the values from both maps into a single list
        List<Message> combinedMessages = new ArrayList<>(allMessages.values());

        // Sort the combined list based on timestamps using a custom comparator
        Collections.sort(combinedMessages, new Comparator<Message>() {
            @Override
            public int compare(Message message1, Message message2) {
                long timestamp1 = message1.getTimestamp();
                long timestamp2 = message2.getTimestamp();

                // Ascending order (oldest to newest)
                return Long.compare(timestamp1, timestamp2);

                // Descending order (newest to oldest)
                // return Long.compare(timestamp2, timestamp1);
            }
        });

        return combinedMessages;
    }


    public CompletableFuture<List<Message>> getCombinedMessages(String userA, String userB) {
        CompletableFuture<Map<String, Message>> messagesA = CompletableFuture.supplyAsync(() -> {
            GetSentMessagesAPI getSentMessagesAPI = new GetSentMessagesAPI();
            return getSentMessagesAPI.getMessages(userA, userB).join();
        });

        CompletableFuture<Map<String, Message>> messagesB = CompletableFuture.supplyAsync(() -> {
            GetSentMessagesAPI getSentMessagesAPI = new GetSentMessagesAPI();
            return getSentMessagesAPI.getMessages(userB, userA).join();
        });

        CompletableFuture<List<Message>> combined = messagesA.thenCombine(messagesB, (mapA, mapB) -> {
            Map<String, Message> combinedMessages = new HashMap<>(mapA);
            combinedMessages.putAll(mapB);
            return getSortedMessages(combinedMessages);
        });

        return combined;
    }


    public CompletableFuture<List<Message>> getSentMessagesSingleUser(String userA) {
        CompletableFuture<Map<String, Message>> messages = CompletableFuture.supplyAsync(() -> {
            GetSentMessagesAPI getSentMessagesAPI = new GetSentMessagesAPI();
            return getSentMessagesAPI.getAllSentMessagesSingleUser(userA).join();
        });

        CompletableFuture<List<Message>> combined = messages.thenApply(map -> {
            return getSortedMessages(map);
        });

        return combined;
    }




//    public void getCombinedMessages(String userA, String userB) {
//        GetSentMessagesAPI getSentMessagesAPI = new GetSentMessagesAPI(new SentMessagesCallback() {
//            private Map<String, Message> combinedMessages = new HashMap<>();
//
//
//            @Override
//            public void onSentMessagesLoaded(Map<String, Message> sentMessages) {
//                // Add the messages from sentMessages to combinedMessages
//                combinedMessages.putAll(sentMessages);
//
//                // Check if both users' messages have been loaded
//                if (combinedMessages.size() == 2) {
//                    List<Message> sortedMessages = getSortedMessages(combinedMessages);
//                    messages = sortedMessages;
//                    // Iterate over the sorted messages
//                    for (Message message : sortedMessages) {
//                        // Access the message properties (e.g., message.getsender(), message.getImageUrl(), etc.)
//                        // Process the message as needed
//                        String sender = message.getsender();
//                        String imageUrl = message.getImageUrl();
//                        // Process sender and imageUrl here
//                        Log.d("Sticker Groups", "Sender: " + sender + ", Image URL: " + imageUrl);
//                    }
//                }
//
//            }
//
//            @Override
//            public void onSentMessagesLoadFailed(DatabaseError databaseError) {
//                // Handle failure scenario
//            }
//        });
//
//        // Load messages for userA to userB
//        getSentMessagesAPI.getMessages(userA, userB);
//
//        // Load messages for userB to userA
//        getSentMessagesAPI.getMessages(userB, userA);
//
//    }


    public CompletableFuture<Map<String, List<String>>> fetchStickerGroups() {
        CompletableFuture<Map<String, List<String>>> stickerGroupsFuture = new CompletableFuture<>();
        firebaseManager = FirebaseManager.getInstance();
        firebaseManager.getStickerGroups(new StickerGroupsCallback() {
            @Override
            public void onStickerGroupsLoaded(Map<String, List<String>> stickerGroups) {
                stickerGroupsFuture.complete(stickerGroups);
            }

            @Override
            public void onStickerGroupsLoadFailed(DatabaseError databaseError) {
                stickerGroupsFuture.completeExceptionally(databaseError.toException());
            }
        });

        return stickerGroupsFuture;
    }


    public List<Message> getMessages() {
        return messages;
    }


    public Map<String, Integer> getStickerCount(Map<String, List<String>> stickerGroups, List<Message> sentMessages) {
        Map<String, Integer> stickerCountMap = new HashMap<>();
        if (stickerGroups != null && sentMessages != null) {


            // Initialize the count for each sticker group to 0
            for (String stickerGroup : stickerGroups.keySet()) {
                stickerCountMap.put(stickerGroup, 0);
            }

            // Iterate over sent messages
            for (Message message : sentMessages) {
                String imageUrl = message.getImageUrl();
                System.out.println("URL" + imageUrl);

                // Check if the imageUrl belongs to any sticker group
                for (String stickerGroup : stickerGroups.keySet()) {

                    List<String> groupStickers = stickerGroups.get(stickerGroup);
                    System.out.println(groupStickers.get(1));
                    if (groupStickers.get(1).contains(imageUrl)) {
                        // Increment the count for the sticker group
                        int count = stickerCountMap.get(stickerGroup);
                        stickerCountMap.put(stickerGroup, count + 1);
                        break; // No need to check other sticker groups
                    }
                }
            }

//            // Print the sticker count for each group
//            for (String stickerGroup : stickerCountMap.keySet()) {
//                int count = stickerCountMap.get(stickerGroup);
//                Log.d("Sticker Count", "Sticker Group: " + stickerGroup + ", Count: " + count);
//            }
        }
        return stickerCountMap;
    }




}
