package edu.northeastern.mainactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import com.google.firebase.auth.FirebaseUser;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import edu.northeastern.mainactivity.apis.MainAPI;
import edu.northeastern.mainactivity.dbmanager.FirebaseManager;
import edu.northeastern.mainactivity.modals.Message;

public class CountActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StickerCountAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);

        recyclerView = findViewById(R.id.count_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new StickerCountAdapter(this);
        recyclerView.setAdapter(adapter);

        FirebaseUser currentUser = FirebaseManager.getInstance().getLoggedInUser();
        if (currentUser != null) {
            MainAPI api = new MainAPI();
            CompletableFuture<List<Message>> future = api.getSentMessagesSingleUser(currentUser.getUid());
            future.thenAccept(messages -> {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(() -> adapter.setData(messages), 100);
            });
        }
    }
}