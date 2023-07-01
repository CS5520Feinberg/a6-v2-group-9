package edu.northeastern.mainactivity.apis;
import android.os.AsyncTask;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.northeastern.mainactivity.entity.Notification;

public class SendNotification {
    static Notification notification;
    public SendNotification(Notification notification){
        this.notification = notification;
    }
    public void sendNotificationToFireBase() {
        DatabaseReference dbUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(notification.getReceiver());

        dbUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String deviceToken = dataSnapshot.child("deviceToken").getValue(String.class);
                    if (deviceToken != null) {
                        new SendNotificationTask().execute( deviceToken);
                    } else {
                        System.out.println("No device token retrieved");
                    }
                } else {
                    System.out.println("User data does not exist");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Firebase database read operation cancelled");
            }
        });
    }

    private static class SendNotificationTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground( String... deviceTokens) {
            String fcmUrl = "https://fcm.googleapis.com/fcm/send";
            String serverKey = "AAAAficsrrs:APA91bEhq_jHGhTS1AW0tZDMHFy32N2tLwiC-Jjok7VSjPBwDVGvx2hjunNTC8QIDH-91TNHPuPL2Fkqk813TpHXGvi4EyYkukkCd71jwPocWS9X__Hex4wmOkYxU2zGQoHKlvhvgZts";
            String deviceToken = deviceTokens[0];
            System.out.println("Device token: "+ deviceToken );
            String notificationTitle = "Sticker From :"+ notification.getSender();
            String payload = String.format("{\"to\":\"%s\",\"notification\":{\"title\":\"%s\",\"body\":\"%s\",\"timeStamp\":\"%s\"}}", deviceToken, notificationTitle, notification.getStickerUrl(),notification.getTimeStamp() );
            try {
                URL url = new URL(fcmUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Authorization", "key=" + serverKey);
                connection.setDoOutput(true);

                OutputStream outputStream = connection.getOutputStream();
                outputStream.write(payload.getBytes("UTF-8"));
                outputStream.close();

                int responseCode = connection.getResponseCode();
                System.out.println("Notification sent. Response Code: " + responseCode);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
