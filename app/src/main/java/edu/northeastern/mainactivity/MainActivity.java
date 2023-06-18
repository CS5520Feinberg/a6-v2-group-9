package edu.northeastern.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MainActivity extends AppCompatActivity {
    private Handler stringHandler = new Handler();
    private static String dailyString;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        APIThread runnableAPIThread = new APIThread();
        new Thread(runnableAPIThread).start();
    }

    class APIThread implements Runnable {
        // NOTE @shashankmanjunath: This APIThread class is just for testing
        @Override
        public void run() {
            // JsonObject dailyArticle = APIMiddleware.getDailyArticle(getApplicationContext());
            JsonArray jsonSearch = APIMiddleware.searchArticles("cantor set", 3, getApplicationContext());
            // JsonObject json = (JsonObject) JsonParser.parseString(dailyString);
            Log.d("API Response", jsonSearch.get(0).toString());
        }
    }
}

