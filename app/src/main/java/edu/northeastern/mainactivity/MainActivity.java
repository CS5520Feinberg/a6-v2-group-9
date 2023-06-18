package edu.northeastern.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonArray;

import edu.northeastern.mainactivity.helpers.FeelingLucky;


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

