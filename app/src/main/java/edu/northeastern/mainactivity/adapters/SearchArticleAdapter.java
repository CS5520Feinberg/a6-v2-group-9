package edu.northeastern.mainactivity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonObject;

import java.util.List;

import edu.northeastern.mainactivity.R;

public class SearchArticleAdapter extends RecyclerView.Adapter<SearchArticleAdapter.SearchArticleViewHolder> {
    List<JsonObject> searchResultList;
    Context context;

    public SearchArticleAdapter(List<JsonObject> searchResultList, Context context) {
        this.searchResultList = searchResultList;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_holder_result, parent, false);
        // Continue with any other view setup or operations
        return new SearchArticleViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull SearchArticleViewHolder holder, int position) {
        JsonObject article = searchResultList.get(position);
        holder.title.setText(article.get("title").getAsString());
        String curDescription;

        if (article.get("description").isJsonNull()) {
            curDescription = "No description found";
        } else {
            curDescription = article.get("description").getAsString();
        }
        holder.description.setText(curDescription);
    }

    @Override
    public int getItemCount() {
        return searchResultList.size();
    }
    
    public static class SearchArticleViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description;;
        
        public SearchArticleViewHolder (@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView_title_daily);
            description = itemView.findViewById(R.id.textView_title_daily_description);

        }
    }
}
