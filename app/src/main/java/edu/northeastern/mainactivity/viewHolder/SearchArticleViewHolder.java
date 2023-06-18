package edu.northeastern.mainactivity.viewHolder;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.mainactivity.R;

public class SearchArticleViewHolder extends RecyclerView.ViewHolder {
    public TextView link_name;
    public TextView link_description;
    private Context context;
    public SearchArticleViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.link_name = itemView.findViewById(R.id.textView_title_daily);
        this.link_description = itemView.findViewById(R.id.textView_title_daily_description);
        this.context = context;
    }
}
