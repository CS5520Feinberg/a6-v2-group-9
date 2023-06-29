package edu.northeastern.mainactivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.StickerViewHolder> {

    private final int[] stickerIds;
    private final OnStickerClickListener listener;

    public StickerAdapter(int[] stickerIds, OnStickerClickListener listener) {
        this.stickerIds = stickerIds;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_item, parent, false);
        return new StickerViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull StickerViewHolder holder, int position) {
        holder.bind(stickerIds[position]);
    }

    @Override
    public int getItemCount() {
        return stickerIds.length;
    }


    public class StickerViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;

        public StickerViewHolder(@NonNull View itemView, OnStickerClickListener listener) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            imageView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onStickerClick(stickerIds[position]);
                }
            });
        }

        public void bind(int id) {
            imageView.setImageResource(id);
        }
    }

    public interface OnStickerClickListener {
        void onStickerClick(int stickerId);
    }
}
