package edu.northeastern.mainactivity;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.auth.FirebaseAuth;

import edu.northeastern.mainactivity.dbmanager.FirebaseManager;

public class LoginActivity {

    private static FirebaseManager firebaseManager = FirebaseManager.getInstance();
    private static FirebaseAuth auth = firebaseManager.getAuthInstance();
    private static FirebaseUser user = null;

    // We use the same password for all users to avoid having to worry about passwords
    private static String pass = "test_pass";

    public static FirebaseUser getUser() {
        if (user == null) {
            Log.e(TAG, "User has not been authenticated!");
        }
        return user;
    }
    public static void dbTest() {
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
    }

    public static void registerOrAuthUser(String user_email) {
        // Logic used is from example in FirebaseAuth documentation
        Log.d(TAG, "User Email for registration: " + user_email);
        auth.createUserWithEmailAndPassword(user_email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    user = auth.getCurrentUser();

                    firebaseManager.setLoggedInUser(user);
                } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                    Log.d(TAG, "Email already exists! Trying standard login...");
                    authenticateUser(user_email);
                } else {
                    Log.w(TAG, "User creation failure!", task.getException());
                }
            }
        });
    }

    public static void authenticateUser (String user_email) {
        // Logic used is from example in FirebaseAuth documentation
        Log.d(TAG, "User Email for login: " + user_email);
        auth.signInWithEmailAndPassword(user_email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Login Successful!");
                    user = auth.getCurrentUser();
                    Log.d(TAG, "User created!" + user.getEmail());
                    firebaseManager.setLoggedInUser(user);
                } else {
                    Log.w(TAG, "Login failed!", task.getException());
                }
            }
        });
    }
}
