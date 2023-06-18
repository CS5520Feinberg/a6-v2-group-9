package edu.northeastern.mainactivity.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.northeastern.mainactivity.R;
import edu.northeastern.mainactivity.entity.Link;
import edu.northeastern.mainactivity.viewHolder.SearchArticleViewHolder;

public class SearchArticleAdapter extends RecyclerView.Adapter<SearchArticleViewHolder> {
    List<Link> links_article;
    Context context;

    public SearchArticleAdapter(List<Link> links_article, Context context) {
        this.links_article = links_article;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_holder_result, parent, false);

        // Set the layout parameters to match the parent's width
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(layoutParams);

        // Continue with any other view setup or operations

        return new SearchArticleViewHolder(view, context);
        // return new LinkViewHolder(LayoutInflater.from(context).inflate(R.layout.links_fragment2,null),context,this);

    }

    @Override
    public void onBindViewHolder(@NonNull SearchArticleViewHolder holder, int position) {
        holder.link_name.setText(links_article.get(position).getTitle());
        holder.link_description.setText(links_article.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return links_article.size();
    }
}
