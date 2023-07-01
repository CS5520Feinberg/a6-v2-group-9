package edu.northeastern.mainactivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import edu.northeastern.mainactivity.modals.Message;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private List<Message> conversation;
    private String loggedInUserId;

    public ChatAdapter(List<Message> conversation, String loggedInUserId) {
        this.conversation = conversation;
        this.loggedInUserId = loggedInUserId;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_sticker_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        Message currentMessage = conversation.get(position);

        if (currentMessage.getsender().equals(loggedInUserId)) {
            Glide.with(holder.sentStickerImageView.getContext())
                    .load(currentMessage.getImageUrl())
                    .into(holder.sentStickerImageView);
            holder.receivedStickerImageView.setVisibility(View.GONE);
        } else {
            Glide.with(holder.receivedStickerImageView.getContext())
                    .load(currentMessage.getImageUrl())
                    .into(holder.receivedStickerImageView);
            holder.sentStickerImageView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return conversation.size();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        ImageView receivedStickerImageView;
        ImageView sentStickerImageView;

        public ChatViewHolder(View itemView) {
            super(itemView);
            receivedStickerImageView = itemView.findViewById(R.id.receivedStickerImageView);
            sentStickerImageView = itemView.findViewById(R.id.sentStickerImageView);
        }
    }
}
