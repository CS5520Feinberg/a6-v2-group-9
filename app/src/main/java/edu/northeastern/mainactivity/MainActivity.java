package edu.northeastern.mainactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;
import android.widget.TextView;
import com.google.gson.JsonArray;
import edu.northeastern.mainactivity.helpers.FeelingLucky;

public class MainActivity extends AppCompatActivity {
    private Handler stringHandler = new Handler();
    private static String dailyString;
    private Button searchButton;
    private Button luckyButton;
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

        APIThread runnableAPIThread = new APIThread();
        new Thread(runnableAPIThread).start();
    }

    class APIThread implements Runnable {
        // NOTE @shashankmanjunath: This APIThread class is just for testing
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void run() {
            // JsonObject dailyArticle = APIMiddleware.getDailyArticle(getApplicationContext());
            //JsonArray jsonSearch = APIMiddleware.searchArticles("cantor set", 3, getApplicationContext());
            // JsonObject json = (JsonObject) JsonParser.parseString(dailyString);
            //Log.d("API Response", jsonSearch.get(0).toString());

            JsonObject daily = APIMiddleware.getDailyArticle(getApplicationContext());
            runOnUiThread(() -> {
                dailyList.add(daily);
                dailyArticleAdapter.notifyDataSetChanged();
            });
        }
    }


    public void goToResultPage(View view){
        Intent intent = new Intent(MainActivity.this, SearchResultPageActivity.class);
        startActivity(intent);
    }

    public void feelingLucky(View view){
        FeelingLucky lucky  = new FeelingLucky();
        Thread thread = new Thread(lucky);
        thread.start();

    }

}

