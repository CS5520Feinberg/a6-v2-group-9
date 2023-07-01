package edu.northeastern.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

//        LoginActivity.registerOrAuthUser("manjunath.sh@northeastern.edu");

        Button launchA6 = (Button) findViewById(R.id.launch_a6_button);
        Button about = findViewById(R.id.about_button);
        Button launchA8 = findViewById(R.id.launch_a8_button);

        launchA6.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), MainActivity.class);
            view.getContext().startActivity(intent);
        });

        about.setOnClickListener(v -> {
            Intent intent = new Intent(LaunchActivity.this, AboutActivity.class);
            startActivity(intent);
        });

        launchA8.setOnClickListener(v -> {
            Intent intent = new Intent(LaunchActivity.this, MainActivity_A8.class);
            startActivity(intent);
        });
    }
}