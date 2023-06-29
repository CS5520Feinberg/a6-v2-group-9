package edu.northeastern.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
            } else {
                Toast.makeText(this, "Enter a username", Toast.LENGTH_SHORT).show();
            }
        });

        signInBtn.setOnClickListener(v -> {
            if (!(usernameEditText.getText().toString().trim().matches(""))) {
                username = usernameEditText.getText().toString();
                LoginActivity.authenticateUser(username);
            } else {
                Toast.makeText(this, "Enter a username", Toast.LENGTH_SHORT).show();
            }
        });
    }
}