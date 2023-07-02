package edu.northeastern.mainactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import edu.northeastern.mainactivity.modals.Message;

public class StickerCountAdapter extends RecyclerView.Adapter<StickerCountAdapter.ViewHolder> {

    private List<Message> messages;
    private Map<String, Integer> stickerCountMap;

    public StickerCountAdapter(Context context) {
        messages = new ArrayList<>();
        stickerCountMap = new HashMap<>();
    }

    public void setData(List<Message> newMessages) {
        messages.clear();
        stickerCountMap.clear();

        for (Message message : newMessages) {
            String stickerUrl = message.getImageUrl();

            if (!stickerCountMap.containsKey(stickerUrl)) {
                stickerCountMap.put(stickerUrl, 0);
                messages.add(message);
            }

            int count = stickerCountMap.get(stickerUrl);
            stickerCountMap.put(stickerUrl, count + 1);
        }

        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_sticker_count_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        String stickerUrl = message.getImageUrl();
        int stickerCount = getStickerCount(stickerUrl);
        holder.countTextView.setText(String.valueOf(stickerCount));
        Glide.with(holder.itemView)
                .load(stickerUrl)
                .into(holder.stickerCountImageView);
    }

    private int getStickerCount(String stickerUrl) {
        if (stickerCountMap.containsKey(stickerUrl)) {
            return stickerCountMap.get(stickerUrl);
        } else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        return messages != null? messages.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView stickerCountImageView;
        TextView countTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            stickerCountImageView = itemView.findViewById(R.id.stickerCountImageView);
            countTextView = itemView.findViewById(R.id.countTextView);
        }
    }
}
