package com.app.spott.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_feed);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        //TODO: get posts for this user
        query.findInBackground(new FindCallback<Post>() {
            public void done(List<Post> posts, ParseException e) {
                if (e == null) {
                    // Access data using the `getGender` methods for the object
                    mPosts.addAll((ArrayList)posts);
                    aPosts.notifyDataSetChanged();
                    // Do whatever you want with the data...
                } else {
                    // something went wrong
                }
            }
        });
    }

}
