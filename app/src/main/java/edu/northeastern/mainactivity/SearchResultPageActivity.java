package edu.northeastern.mainactivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import edu.northeastern.mainactivity.adapters.SearchArticleAdapter;
import edu.northeastern.mainactivity.entity.Link;

public class SearchResultPageActivity extends AppCompatActivity {

    private TextView searchResultTV;
    private static final String KEY_LINKS_LIST = "linksList";

    RecyclerView linksArticleRecyclerView;
    List<Link>  links = new ArrayList<>();

    List<JsonObject> searchResultList = new ArrayList<>();
    private String query;
    private SearchArticleAdapter searchArticleAdapter;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(KEY_LINKS_LIST,new ArrayList<>(links));
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page3);
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_LINKS_LIST)) {
            links = (List<Link>) savedInstanceState.getSerializable(KEY_LINKS_LIST);
        }

        linksArticleRecyclerView = findViewById(R.id.recycler_view_result_page);
        linksArticleRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchArticleAdapter = new SearchArticleAdapter(searchResultList,this);
        linksArticleRecyclerView.setAdapter(searchArticleAdapter);

        searchResultTV = findViewById(R.id.result_details_text_view);
        this.query = getIntent().getStringExtra("queryString");
        searchResultTV.setText(query);
        testThread run = new testThread();
        Thread iterTestThread = new Thread(run);

        iterTestThread.start();

        try {
            iterTestThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        searchArticleAdapter.notifyDataSetChanged();
    }

    class testThread implements Runnable {

        @Override
        public void run() {
            JsonArray jsonSearch = APIMiddleware.searchArticles(query, 50, getApplicationContext());

            for (int i = 0; i < jsonSearch.size(); i++) {
                JsonObject article = (JsonObject) jsonSearch.get(i);
                searchResultList.add(article);
            }
        }
    }
}