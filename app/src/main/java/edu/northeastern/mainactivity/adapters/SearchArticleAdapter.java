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
import edu.northeastern.mainactivity.entity.Link;

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
        // return new LinkViewHolder(LayoutInflater.from(context).inflate(R.layout.links_fragment2,null),context,this);

    }
    @Override
    public void onBindViewHolder(@NonNull SearchArticleViewHolder holder, int position) {
        JsonObject article = searchResultList.get(position);
        holder.title.setText(article.get("title").getAsString());
        holder.description.setText(article.get("description").getAsString());
    }

    @Override
    public int getItemCount() {
        return searchResultList.size();
    }
    
    public class SearchArticleViewHolder extends RecyclerView.ViewHolder {
        public TextView title, description;;
        
        public SearchArticleViewHolder (@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.textView_title_daily);
            description = itemView.findViewById(R.id.textView_title_daily_description);

        }
    }
}
