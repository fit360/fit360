package com.app.spott.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.app.spott.R;
import com.app.spott.SpottApplication;
import com.app.spott.adapters.ProfileWorkoutsAdapter;
import com.app.spott.fragments.PostsListFragment;
import com.app.spott.fragments.ProfileWorkoutsFragment;
import com.app.spott.interfaces.ProfileFragmentListener;
import com.app.spott.models.Post;
import com.app.spott.models.User;
import com.app.spott.models.Workout;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity implements ProfileWorkoutsAdapter.AdapterOnClickListener,
        ProfileFragmentListener,
        ProfileWorkoutsFragment.AddWorkoutListener,
        PostsListFragment.OnFragmentInteractionListener{

    private User user;
    private boolean isLoggedInUser;
    public static final String WORKOUT_ID_INTENT_KEY = "workout_id";
    public static final int WORKOUT_EDIT_REQUEST_CODE = 1;
    private PostsListFragment mFragmentPostsList;

    @Bind(R.id.tvPosts)
    TextView tvPosts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SpottApplication app = (SpottApplication) this.getApplicationContext();
        setContentView(R.layout.activity_profile);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabStartChat);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
                intent.putExtra("theirUserId", user.getObjectId());
                startActivity(intent);
            }
        });
        Intent intent = getIntent();
        if (intent.hasExtra(MapActivity.INTENT_USER_ID)) {
//            setUser(intent.getStringExtra(MapActivity.INTENT_USER_ID));
            setUserOnUIThread(intent.getStringExtra(MapActivity.INTENT_USER_ID));
            isLoggedInUser = false;
        } else {
            user = app.getCurrentUser();
            isLoggedInUser = true;
            fab.setVisibility(View.GONE);
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ButterKnife.bind(this);

        mFragmentPostsList = (PostsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_community_feed);
        fetchUserPosts();
    }

    private void fetchUserPosts(){
        Post.fetchUserPosts(getUser(), new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                mFragmentPostsList.addAll((ArrayList) posts);
            }
        });
    }


    @Override
    public User getUser() {
        return user;
    }

    @Override
    public boolean isLoggedInUser() {
        return isLoggedInUser;
    }

    public void setUser(String user_id) {
        User.findOne(User.class, user_id, new GetCallback<User>() {
            @Override
            public void done(User object, ParseException e) {
                if (e == null)
                    user = object;
                else
                    e.printStackTrace();
            }
        });
    }

    public void setUserOnUIThread(String user_id){
        try {
            user = User.findUserOnUIThread(user_id);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void editWorkout(Workout workout) {
//        start activity for result
        Intent intent = new Intent(this, WorkoutEditActivity.class);
        intent.putExtra(WORKOUT_ID_INTENT_KEY, workout.getObjectId());
        startActivityForResult(intent, WORKOUT_EDIT_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == WORKOUT_EDIT_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
                String workoutId = data.getStringExtra(WORKOUT_ID_INTENT_KEY);
                final ProfileWorkoutsFragment frag = (ProfileWorkoutsFragment) getSupportFragmentManager().findFragmentById(R.id.fragProfileWorkouts);
                Workout.findOne(workoutId, true, new GetCallback<Workout>() {
                    @Override
                    public void done(Workout object, ParseException e) {
                        if (e == null)
                            frag.onWorkoutSave(object);
                    }
                });
            }
        }
    }

    @Override
    public void addWorkout() {
        Intent intent = new Intent(this, WorkoutEditActivity.class);
        startActivityForResult(intent, WORKOUT_EDIT_REQUEST_CODE);
    }

    @Override
    public void hasPosts(Boolean hasPosts) {
        if(hasPosts) {
            tvPosts.setVisibility(View.VISIBLE);
        } else {
            tvPosts.setVisibility(View.GONE);
        }
    }
}
