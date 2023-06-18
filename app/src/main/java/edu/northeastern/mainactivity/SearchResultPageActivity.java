package edu.northeastern.mainactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.mainactivity.adapters.SearchArticleAdapter;
import edu.northeastern.mainactivity.entity.Link;

public class SearchResultPageActivity extends AppCompatActivity {

    private static final String KEY_LINKS_LIST = "linksList";

    RecyclerView linksArticleRecyclerView;
    List<Link> links = new ArrayList<>();

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_LINKS_LIST,new ArrayList<>(links));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page3);
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_LINKS_LIST)) {
            links = (List<Link>) savedInstanceState.getSerializable(KEY_LINKS_LIST);
        }
        linksArticleRecyclerView = findViewById(R.id.recycler_view_result_page);
        linksArticleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        SearchArticleAdapter searchArticleAdapter = new SearchArticleAdapter(getLinks(),this);
        linksArticleRecyclerView.setAdapter(searchArticleAdapter);


    }
    // Whoever implemented the thread has to get the links here
    private List<Link> getLinks() {
        Link link1 = new Link("The Great Java","https://www.google.com","The best book ever wrriten on Java");
        Link link2 = new Link("The Great Java","https://www.google.com","The best book ever wrriten on Java");
        Link link3 = new Link("The Great Java","https://www.google.com","The best book ever wrriten on Java");

        List<Link> lists = new ArrayList<>();
        lists.add(link1);
        lists.add(link2);
        lists.add(link3);
        return lists;
    }
}