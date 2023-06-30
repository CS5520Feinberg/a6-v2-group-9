package edu.northeastern.mainactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;

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

