package edu.northeastern.mainactivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.northeastern.mainactivity.modals.Message;

public class SenderAdapter extends RecyclerView.Adapter<SenderAdapter.ViewHolder> {

    private List<Message> messages;

    public SenderAdapter(List<Message> messages) {
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
        holder.nameTextView.setText(message.getreceiver());
        long timestamp = message.getTimestamp();
        Date date = new Date(timestamp);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDate = sdf.format(date);
        holder.timestampTextView.setText(formattedDate);
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
        TextView timestampTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            stickerImageView = itemView.findViewById(R.id.history_sticker);
            nameTextView = itemView.findViewById(R.id.history_name);
            timestampTextView = itemView.findViewById(R.id.timestamp);
        }
    }
}
