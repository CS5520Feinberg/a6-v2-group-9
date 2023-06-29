package edu.northeastern.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class StickerActivity extends AppCompatActivity {

    private Spinner usersDropdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker);

        usersDropdown = findViewById(R.id.usersDropdown);
        // Replace by usernames/emails
        String[] temp = new String[] {
                "1", "2", "3", "4", "5"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, temp);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        usersDropdown.setAdapter(adapter);
    }
}