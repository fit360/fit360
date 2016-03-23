package com.app.spott.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.app.spott.R;
import com.app.spott.SpottApplication;
import com.app.spott.adapters.ChatListAdapter;
import com.app.spott.models.Message;
import com.app.spott.models.User;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class ChatActivity extends AppCompatActivity implements ChatListAdapter.ChatAssetsProvider {

    static final String USER_ID_KEY = "userId";
    static final String BODY_KEY = "body";
    static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;
    String otherUserId = "";
    Handler handler = new Handler();

    @Bind(R.id.etMessage)
    EditText etMessage;

    @Bind(R.id.btSend)
    Button btSend;

    @Bind(R.id.lvChat)
    ListView lvChat;

    ArrayList<Message> mMessages;
    ChatListAdapter mAdapter;
    private User selfUser;
    private User otherUser;
    private SpottApplication app;
    boolean mFirstLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_36dp);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        ButterKnife.bind(this);
        otherUserId = getIntent().getStringExtra(ProfileActivity.CHAT_WITH_USERID_KEY);
        app = (SpottApplication) getApplicationContext();
        selfUser = app.getCurrentUser();
        if(otherUser == null){
            try {
                otherUser = User.findUserOnUIThread(otherUserId);
                getSupportActionBar().setTitle(otherUser.getFirstName());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        setupMessagePosting();
        handler.post(runnableCode);
    }

    // Setup button event handler which posts the entered message to Parse
    void setupMessagePosting() {
        // Find the text field and button
        mMessages = new ArrayList<>();
        // Automatically scroll to the bottom when a data set change notification is received and only if the last item is already visible on screen. Don't scroll to the bottom otherwise.
        mFirstLoad = true;
        mAdapter = new ChatListAdapter(ChatActivity.this, mMessages);
        lvChat.setAdapter(mAdapter);
        // When send button is clicked, create message object on Parse
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etMessage.getText().toString();
                ParseObject message = ParseObject.create("Message");
                message.put(Message.USER_ID_KEY, selfUser.getObjectId());
                message.put(Message.TO_USER_ID_KEY, otherUserId);
                message.put(Message.BODY_KEY, data);
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        refreshMessages();
                    }
                });
                etMessage.setText(null);
            }
        });


    }

    void refreshMessages() {
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        query.whereEqualTo("userId", selfUser.getObjectId());
        query.whereEqualTo("toUserId", otherUserId);

        ParseQuery<Message> theirMessagesQuery = ParseQuery.getQuery(Message.class);
        theirMessagesQuery.whereEqualTo("userId", otherUserId);
        theirMessagesQuery.whereEqualTo("toUserId", selfUser.getObjectId());


        List<ParseQuery<Message>> queries = new ArrayList<ParseQuery<Message>>();
        queries.add(query);
        queries.add(theirMessagesQuery);

        ParseQuery<Message> mainQuery = ParseQuery.or(queries);

        // Execute query to fetch all messages from Parse asynchronously
        // This is equivalent to a SELECT query with SQL
        mainQuery.findInBackground(new FindCallback<Message>() {
            public void done(List<Message> messages, ParseException e) {
                if (e == null) {
                    mMessages.clear();
                    mMessages.addAll(messages);
                    mAdapter.notifyDataSetChanged(); // update adapter
                    // Scroll to the bottom of the list on initial load
                    if (mFirstLoad) {
                        lvChat.setSelection(mAdapter.getCount() - 1);
                        mFirstLoad = false;
                    }
                } else {
                    Log.e("message", "Error Loading Messages" + e);
                }
            }
        });
    }

    private Runnable runnableCode = new Runnable() {
        @Override
        public void run() {
            // Do something here on the main thread
            refreshMessages();
            Log.d("Handlers", "Called on main thread");
            // Repeat this the same runnable code block again another 2 seconds
            handler.postDelayed(runnableCode, 2000);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnableCode);
    }

    @Override
    public User selfUser() {
        return this.selfUser;
    }

    @Override
    public User otherUser() {
        return this.otherUser;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
