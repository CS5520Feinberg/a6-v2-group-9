package edu.northeastern.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;

import java.util.List;
import java.util.Map;

import edu.northeastern.mainactivity.dbmanager.FirebaseManager;
import edu.northeastern.mainactivity.interfaces.StickerGroupsCallback;

public class MainActivity_A8 extends AppCompatActivity {

    private String username;
    private Button signUpBtn;
    private Button signInBtn;
    private EditText usernameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_a8);

        signUpBtn = findViewById(R.id.signUpBtn);
        signInBtn = findViewById(R.id.signInBtn);
        usernameEditText = findViewById(R.id.usernameEditText);

        signUpBtn.setOnClickListener(v -> {
            if (!(usernameEditText.getText().toString().trim().matches(""))) {
                username = usernameEditText.getText().toString();
                LoginActivity.registerOrAuthUser(username);
                Intent intent = new Intent(MainActivity_A8.this, StickerActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Enter a username", Toast.LENGTH_SHORT).show();
            }
        });

        signInBtn.setOnClickListener(v -> {
            if (!(usernameEditText.getText().toString().trim().matches(""))) {
                username = usernameEditText.getText().toString();
                LoginActivity.authenticateUser(username);
                Intent intent = new Intent(MainActivity_A8.this, StickerActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Enter a username", Toast.LENGTH_SHORT).show();
            }
        });
    }


}