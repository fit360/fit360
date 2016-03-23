package com.app.spott.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.app.spott.R;
import com.app.spott.SpottApplication;
import com.app.spott.fragments.PostsListFragment;
import com.app.spott.models.Gender;
import com.app.spott.models.Post;
import com.app.spott.models.User;
import com.bumptech.glide.Glide;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class CommunityFeedActivity extends AppCompatActivity implements PostsListFragment.OnFragmentInteractionListener {
    private SpottApplication app;
    private PostsListFragment fragmentPostsList;

    private SwipeRefreshLayout swipeContainer;
    private static final String USER = "user";

    @Bind(R.id.pbLoading) ProgressBar pbLoading;
    MenuItem miActionProgressItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community_feed);
        ButterKnife.bind(this);
        app = (SpottApplication) getApplicationContext();
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchFeeds();
            }
        });
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null){
            fragmentPostsList = (PostsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_community_feed);
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabCreatePost);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CommunityFeedActivity.this, CreatePostActivity.class);
                startActivity(intent);
            }
        });
        fetchFeeds();
    }
    void fetchFeeds(){
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(USER);
        query.orderByDescending("updatedAt");
        //TODO: get posts for this user
        pbLoading.setVisibility(View.VISIBLE);
        query.findInBackground(new FindCallback<Post>() {
            public void done(List<Post> posts, ParseException e) {
                if (e == null) {
                    pbLoading.setVisibility(View.GONE);
                    fragmentPostsList.addAll((ArrayList) posts);
                    swipeContainer.setRefreshing(false);
                } else {
                    // something went wrong
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_community_feed_activity, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        miActionProgressItem = menu.findItem(R.id.miActionProgress);
        // Extract the action-view from the menu item
        ProgressBar v =  (ProgressBar) MenuItemCompat.getActionView(miActionProgressItem);

        MenuItem actionViewItem = menu.findItem(R.id.action_profile_pic);
        View view = MenuItemCompat.getActionView(actionViewItem);
        ImageView imageView  = (ImageView)view.findViewById(R.id.ivProfilePic);

        if(app.getCurrentUser() != null){
            Glide.with(this).load(app.getCurrentUser().getProfileImageUrl()).error(R.drawable.ic_action_profile).error(R.drawable.ic_action_profile).dontAnimate().into(imageView);
        } else {
            User.getByOwner(ParseUser.getCurrentUser(), new GetCallback<User>() {
                @Override
                public void done(User user, ParseException e) {
                    if (e == null) {
                        app.setCurrentUser(user);
                    } else {
                    }
                }
            });
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CommunityFeedActivity.this, ProfileActivity.class);
                startActivity(i);
            }
        });

        return super.onPrepareOptionsMenu(menu);

    }
    public void showProgressBar() {
        // Show progress item
        miActionProgressItem.setVisible(true);
    }

    public void hideProgressBar() {
        // Hide progress item
        miActionProgressItem.setVisible(false);
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
        } else if (id == R.id.action_profile_pic) {
            Intent i = new Intent(this, ProfileActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void hasPosts(Boolean hasPosts) {
    }
}
