package edu.northeastern.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StickerActivity extends AppCompatActivity {

    private Spinner usersDropdown;
    private FloatingActionButton stickersBtn;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //lin.kevin1@northeastern.edu

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sticker);

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
}