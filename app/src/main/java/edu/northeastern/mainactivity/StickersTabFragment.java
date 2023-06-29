package edu.northeastern.mainactivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class StickersTabFragment extends BottomSheetDialogFragment {

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

        int[] stickerIds = new int[] {
                R.drawable.ic_launcher_background,
                R.drawable.ic_launcher_foreground
        };

        StickerAdapter adapter = new StickerAdapter(stickerIds, new StickerAdapter.OnStickerClickListener() {
            @Override
            public void onStickerClick(int stickerId) {
                ((StickerActivity)getActivity()).onStickerSelected(stickerId);
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
