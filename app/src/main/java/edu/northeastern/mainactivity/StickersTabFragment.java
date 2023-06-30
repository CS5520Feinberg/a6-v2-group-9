package edu.northeastern.mainactivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StickersTabFragment extends BottomSheetDialogFragment {


    FirebaseDatabase database = FirebaseDatabase.getInstance("https://a6group9-default-rtdb.firebaseio.com/");
    DatabaseReference stickersRef = database.getReference("Stickers");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_view_stickers, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));

        fetchStickerUrls(recyclerView);
    }

    private void fetchStickerUrls(RecyclerView recyclerView) {

        stickersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> stickerUrls = new ArrayList<>();
                for (DataSnapshot categorySnapshot: dataSnapshot.getChildren()) {
                    for (DataSnapshot urlSnapshot: categorySnapshot.child("imageUrls").getChildren()) {
                        String stickerUrl = urlSnapshot.getValue(String.class);
                        stickerUrls.add(stickerUrl);
                    }
                }
                StickerAdapter adapter = new StickerAdapter(stickerUrls, stickerUrl -> ((StickerActivity)getActivity()).onStickerSelected(stickerUrl));
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
