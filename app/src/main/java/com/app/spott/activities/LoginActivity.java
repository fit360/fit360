package com.app.spott.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.app.spott.R;
import com.app.spott.SpottApplication;
import com.app.spott.models.User;
import com.parse.ParseException;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {
    @Bind(R.id.etUserName)
    EditText etUserName;
    @Bind(R.id.etPwd)
    EditText etPwd;
    @Bind(R.id.btnLogin)
    Button btnLogin;
    @Bind(R.id.btnSignUp)
    Button btnSignUp;
    private SpottApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setupNewUser();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            }
        });
    }

    private User setupNewUser() {
        User user = new User();
        user.setFirstName(etUserName.getText().toString());
//        user.setAge(25);
//        user.setLastName("BAR");
//        user.setGender(Gender.MALE);
//        user.setOwner(ParseUser.getCurrentUser());
        try {
            user.save();
            app = (SpottApplication) getApplicationContext();
            app.setCurrentUser(user);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return user;
    }

}
