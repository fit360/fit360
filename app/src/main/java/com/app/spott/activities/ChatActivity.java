package com.app.spott.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.app.spott.R;
import com.app.spott.adapters.ChatListAdapter;
import com.app.spott.models.Message;
import com.app.spott.models.User;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;


public class ChatActivity extends AppCompatActivity {

    static final String USER_ID_KEY = "userId";
    static final String BODY_KEY = "body";
    static final int MAX_CHAT_MESSAGES_TO_SHOW = 50;
    final String userId = "";
    String theirUserId = "";
    Handler handler = new Handler();

    EditText etMessage;
    Button btSend;

    ListView lvChat;
    ArrayList<Message> mMessages;
    ChatListAdapter mAdapter;
    private User mUser;
    // Keep track of initial load to scroll to the bottom of the ListView
    boolean mFirstLoad;

    // Get the userId from the cached currentUser object
    void startWithCurrentUser() {
        setupMessagePosting();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        theirUserId = getIntent().getStringExtra("theirUserId");
//        theirUserId = "J90tjqnxYc";
        if(mUser == null){
            User.getByOwner(ParseUser.getCurrentUser(), new GetCallback<User>() {
                @Override
                public void done(User user, ParseException e) {
                    mUser = user;
                    setupMessagePosting();
                    handler.post(runnableCode);
                }
            });
        }
    }
    // Setup button event handler which posts the entered message to Parse
    void setupMessagePosting() {
        // Find the text field and button
        etMessage = (EditText) findViewById(R.id.etMessage);
        btSend = (Button) findViewById(R.id.btSend);
        lvChat = (ListView) findViewById(R.id.lvChat);
        mMessages = new ArrayList<>();
        // Automatically scroll to the bottom when a data set change notification is received and only if the last item is already visible on screen. Don't scroll to the bottom otherwise.
        mFirstLoad = true;
        mAdapter = new ChatListAdapter(ChatActivity.this, userId, mMessages);
        lvChat.setAdapter(mAdapter);
        // When send button is clicked, create message object on Parse
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data = etMessage.getText().toString();
                ParseObject message = ParseObject.create("Message");
                message.put(Message.USER_ID_KEY, mUser.getObjectId());
                message.put(Message.TO_USER_ID_KEY, theirUserId);
                message.put(Message.BODY_KEY, data);
                message.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        Toast.makeText(ChatActivity.this, "Successfully created message on Parse",
                                Toast.LENGTH_SHORT).show();
                        refreshMessages();
                    }
                });
                etMessage.setText(null);
            }
        });


    }
    // Query messages from Parse so we can load them into the chat adapter
    void refreshMessages() {
        // Construct query to execute
        ParseQuery<Message> query = ParseQuery.getQuery(Message.class);
        query.whereEqualTo("userId", mUser.getObjectId());
        query.whereEqualTo("toUserId", theirUserId);

        Log.d("DEBUG", "Parse user ids ours: " + userId + " theirs: " + theirUserId);

        ParseQuery<Message> theirMessagesQuery = ParseQuery.getQuery(Message.class);
        theirMessagesQuery.whereEqualTo("userId", theirUserId);
        theirMessagesQuery.whereEqualTo("toUserId", mUser.getObjectId());


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
}
