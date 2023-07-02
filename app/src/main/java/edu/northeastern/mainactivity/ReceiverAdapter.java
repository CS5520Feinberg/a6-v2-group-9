package edu.northeastern.mainactivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import edu.northeastern.mainactivity.modals.Message;

public class ReceiverAdapter extends RecyclerView.Adapter<ReceiverAdapter.ViewHolder> {

    private List<Message> messages;

    public ReceiverAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_history_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.nameTextView.setText(message.getsender());
        Glide.with(holder.stickerImageView.getContext())
                .load(message.getImageUrl())
                .into(holder.stickerImageView);
    }

    @Override
    public int getItemCount() {
        return messages != null ? messages.size() : 0;
    }

    public void updateData(List<Message> newMessages) {
        this.messages = newMessages;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView stickerImageView;
        TextView nameTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            stickerImageView = itemView.findViewById(R.id.history_sticker);
            nameTextView = itemView.findViewById(R.id.history_name);
        }
    }
}
