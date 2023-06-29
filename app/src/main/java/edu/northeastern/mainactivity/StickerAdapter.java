package edu.northeastern.mainactivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class StickerAdapter extends RecyclerView.Adapter<StickerAdapter.StickerViewHolder> {

    private final List<String> stickerUrls;
    private final OnStickerClickListener listener;

    public interface OnStickerClickListener {
        void onStickerClick(String stickerUrl);
    }

    public StickerAdapter(List<String> stickerUrls, OnStickerClickListener listener) {
        this.stickerUrls = stickerUrls;
        this.listener = listener;
    }

    @NonNull
    @Override
    public StickerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_item, parent, false);
        return new StickerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StickerViewHolder holder, int position) {
        Glide.with(holder.stickerView.getContext())
                .load(stickerUrls.get(position))
                .into(holder.stickerView);

        holder.stickerView.setOnClickListener(v ->  {
            listener.onStickerClick(stickerUrls.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return stickerUrls.size();
    }

    public static class StickerViewHolder extends RecyclerView.ViewHolder {
        ImageView stickerView;

        public StickerViewHolder(@NonNull View itemView) {
            super(itemView);
            stickerView = itemView.findViewById(R.id.stickerView);
        }
    }
}
