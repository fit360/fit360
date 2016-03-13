package com.app.spott.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.app.spott.R;
import com.app.spott.adapters.PostsAdapter;
import com.app.spott.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class CommunityFeedActivity extends AppCompatActivity {
    PostsAdapter aPosts;
    ArrayList<Post> mPosts;
    RecyclerView rvPosts;
    LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout swipeContainer;
    private static final String USER = "user";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_feed);

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchFeeds();
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCreatePost);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mPosts = new ArrayList<>();
        aPosts = new PostsAdapter(this, mPosts);
        rvPosts = (RecyclerView)findViewById(R.id.rvFeed);
        rvPosts.setAdapter(aPosts);
        linearLayoutManager = new LinearLayoutManager(this);
        rvPosts.setLayoutManager(linearLayoutManager);
        fetchFeeds();
    }

    void fetchFeeds(){
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(USER);
        //TODO: get posts for this user
        query.findInBackground(new FindCallback<Post>() {
            public void done(List<Post> posts, ParseException e) {
                if (e == null) {
                    // Access data using the `getGender` methods for the object
                    mPosts.addAll((ArrayList)posts);
                    aPosts.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                } else {
                    // something went wrong
                }
            }
        });
    }

}
