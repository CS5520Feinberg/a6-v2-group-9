package edu.northeastern.mainactivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        getEmailFromUid(message.getsender(), email -> {
            if (email != null) {
                holder.nameTextView.setText(String.format("From: %s", email));
            } else {
                holder.nameTextView.setText("From: ");
            }
        });
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


    private void getEmailFromUid(String targetUid, SenderAdapter.EmailCallback callback) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference userInfoRef = db.getReference("UserInfo");

        userInfoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String email = null;
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String uid = userSnapshot.child("userID").getValue(String.class);
                    if (targetUid.equals(uid)) {
                        email = userSnapshot.child("email").getValue(String.class);
                        break;
                    }
                }
                callback.onEmailReceived(email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.onEmailReceived(null);
            }
        });
    }

    interface EmailCallback {
        void onEmailReceived(String email);
    }
}
