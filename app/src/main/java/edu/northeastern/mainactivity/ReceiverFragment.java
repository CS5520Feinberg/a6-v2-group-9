package edu.northeastern.mainactivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import edu.northeastern.mainactivity.apis.MainAPI;
import edu.northeastern.mainactivity.dbmanager.FirebaseManager;
import edu.northeastern.mainactivity.modals.Message;

public class ReceiverFragment extends Fragment {

    private List<Message> messages;
    private RecyclerView recyclerView;
    private ReceiverAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String uid = FirebaseManager.getInstance().getLoggedInUser().getUid();
        MainAPI api = new MainAPI();
        CompletableFuture<List<Message>> future = api.getReceivedMessagesSingleUser(uid);

        future.thenAccept(this::handleMessages);
    }

    private void handleMessages(List<Message> messages) {
        this.messages = messages;

        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                adapter.updateData(messages);
                adapter.notifyDataSetChanged();
            });
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_view_history_received, container, false);

        recyclerView = view.findViewById(R.id.received_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new ReceiverAdapter(messages);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
