package edu.northeastern.mainactivity.viewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.mainactivity.R;

public class SearchArticleViewHolder extends RecyclerView.ViewHolder {
    TextView link_name;
    TextView link_description;
    public SearchArticleViewHolder(@NonNull View itemView) {
        super(itemView);
        this.link_name = itemView.findViewById(R.id.textView_title_daily);
        this.link_description = itemView.findViewById(R.id.textView_title_daily_description);


    }
}
