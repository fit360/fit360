package com.app.spott.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.app.spott.R;
import com.app.spott.SpottApplication;
import com.app.spott.adapters.PostsAdapter;
import com.app.spott.models.Gender;
import com.app.spott.models.Post;
import com.app.spott.models.User;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class CommunityFeedActivity extends AppCompatActivity {
    private SpottApplication app;

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
        app = (SpottApplication) getApplicationContext();

        if (ParseUser.getCurrentUser() != null) { // start with existing user
            startWithCurrentUser();
        } else { // If not logged in, login as a new anonymous user
            login();
        }

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
                Intent intent = new Intent(CommunityFeedActivity.this, CreatePostActivity.class);
                startActivity(intent);
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
    // Get the userId from the cached currentUser object
    void startWithCurrentUser() {
        ParseUser loggedInUser = ParseUser.getCurrentUser();
        Log.d("login", loggedInUser.getSessionToken());
        User.getByOwner(loggedInUser, new GetCallback<User>() {
            @Override
            public void done(User user, ParseException e) {
                if (e == null) {
                    app.setCurrentUser(user);
                } else {
                    app.setCurrentUser(setupAdil());
                }
            }
        });
    }

    private User setupNewUser() {
        User user = new User();
        user.setFirstName("FOO");
        user.setAge(25);
        user.setLastName("BAR");
        user.setGender(Gender.MALE);
        user.setOwner(ParseUser.getCurrentUser());
        try {
            user.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return user;
    }

    private Post randomPost() {
        Post post = new Post();
        post.setBody("We're having so much fun!");
        post.setImageUrl("http://www.imagesource.com/Doc/IS0/Media/TR16/a/5/d/c/38YDP0062RMG.jpg");
        try {
            post.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return post;
    }

    private User setupAdil() {
        User user = new User();
        user.setFirstName("Adil");
        user.setAge(25);
        user.setLastName("Ansari");
        user.setGender(Gender.MALE);
        user.setOwner(ParseUser.getCurrentUser());
        try {
            user.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return user;
    }
    private User setupAJ() {
        User user = new User();
        user.setFirstName("A");
        user.setAge(20);
        user.setLastName("J");
        user.setGender(Gender.FEMALE);
        user.setOwner(ParseUser.getCurrentUser());
        try {
            user.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return user;
    }

    // Create an anonymous user using ParseAnonymousUtils and set sUserId
    void login() {
        ParseAnonymousUtils.logIn(new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Log.e("DEBUG", "Anonymous login failed: ", e);
                } else {
                    startWithCurrentUser();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_community_feed_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_map) {
            Intent mapIntent = new Intent(this, MapActivity.class);
            startActivity(mapIntent);
            return true;
        } else if (id == R.id.action_profile) {
            Intent i = new Intent(this, ProfileActivity.class);
            startActivity(i);
        } else if (id == R.id.action_chat) {
            Intent i = new Intent(this, ChatActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
