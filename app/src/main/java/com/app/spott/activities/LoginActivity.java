package com.app.spott.activities;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.VideoView;

import com.app.spott.R;
import com.app.spott.SpottApplication;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
//    @Bind(R.id.etUserName)
//    EditText etUserName;
//    @Bind(R.id.etPwd)
//    EditText etPwd;
//    @Bind(R.id.btnLogin)
//    Button btnLogin;
//    @Bind(R.id.btnSignUp)
//    Button btnSignUp;

    @Bind(R.id.vvLoginVideo)
    VideoView vvLoginVideo;

    private SpottApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        vvLoginVideo.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.loginvid));
        vvLoginVideo.setMediaController(null);
        vvLoginVideo.requestFocus();
        vvLoginVideo.start();

//        btnSignUp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setupNewUser();
//                Intent i = new Intent(LoginActivity.this, CommunityFeedActivity.class);
//                startActivity(i);
//            }
//        });
    }

//    private User setupNewUser() {
//        User user = new User();
//        user.setFirstName(etUserName.getText().toString());
////        user.setAge(25);
////        user.setLastName("BAR");
////        user.setGender(Gender.MALE);
////        user.setOwner(ParseUser.getCurrentUser());
//        try {
//            user.save();
//            app = (SpottApplication) getApplicationContext();
//            app.setCurrentUser(user);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return user;
//    }

}
