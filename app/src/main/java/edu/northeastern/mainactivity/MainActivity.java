package edu.northeastern.mainactivity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Handler stringHandler = new Handler();
    private static String dailyString;
    private Button searchButton;
    private Button luckyButton;
    private String luckyURL;
    private EditText searchBar;
    private RecyclerView dailyRV;
    private DailyArticleAdapter dailyArticleAdapter;
    private List<JsonObject> dailyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // writing data to database (firebase realtime)
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");


        // reading the data
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



        searchButton = findViewById(R.id.search_button);
        luckyButton = findViewById(R.id.lucky_button);
        searchBar = findViewById(R.id.search_bar);
        dailyRV = findViewById(R.id.dailyRV);

        dailyArticleAdapter = new DailyArticleAdapter(this, dailyList);
        dailyRV.setLayoutManager(new LinearLayoutManager(this));
        dailyRV.setAdapter(dailyArticleAdapter);

        searchButton.setOnClickListener(v -> {
            String query = searchBar.getText().toString();
            Intent intent = new Intent(MainActivity.this, SearchResultPageActivity.class);
            intent.putExtra("queryString", query);
            startActivity(intent);
        });

        luckyButton.setOnClickListener(v -> {
            LuckyThread lucky = new LuckyThread();
            Thread getURLThread = new Thread(lucky);
            getURLThread.start();

            try {
                getURLThread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Uri luckyURI = Uri.parse(luckyURL);
            Intent intent = new Intent(Intent.ACTION_VIEW, luckyURI);
            startActivity(intent);
        });

        APIThread runnableAPIThread = new APIThread();
        new Thread(runnableAPIThread).start();
    }

    class LuckyThread implements Runnable {
        public void run() {
            luckyURL = APIMiddleware.getRandomArticle(getApplicationContext());
        }
    }

    class APIThread implements Runnable {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void run() {
            ArrayList<JsonObject> daily = APIMiddleware.getDailyArticle(getApplicationContext(), 3);
            runOnUiThread(() -> {
                dailyList.add(daily.get(0));
                dailyArticleAdapter.notifyDataSetChanged();
            });
        }
    }

}

