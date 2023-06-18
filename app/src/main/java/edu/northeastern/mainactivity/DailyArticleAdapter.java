package edu.northeastern.mainactivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import java.util.List;

public class DailyArticleAdapter extends RecyclerView.Adapter<DailyArticleAdapter.DailyArticleViewHolder> {

    private List<JsonObject> dailyArticles;
    private Context context;

    public DailyArticleAdapter(Context context, List<JsonObject> dailyArticles) {
        this.context = context;
        this.dailyArticles = dailyArticles;
    }

    @NonNull
    @Override
    public DailyArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_main, parent, false);
        return new DailyArticleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyArticleViewHolder holder, int position) {
        JsonObject article = dailyArticles.get(position);
        holder.title.setText(article.get("title").getAsString());
        holder.description.setText(article.get("description").getAsString());
    }

    @Override
    public int getItemCount() {
        return dailyArticles.size();
    }

    public class DailyArticleViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description;

        public DailyArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_text);
            description = itemView.findViewById(R.id.description_text);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    String link = dailyArticles.get(position)
                            .getAsJsonObject("content_urls")
                            .getAsJsonObject("mobile")
                            .get("page")
                            .getAsString();

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(link));
                    context.startActivity(intent);
                }
            });
        }
    }
}
