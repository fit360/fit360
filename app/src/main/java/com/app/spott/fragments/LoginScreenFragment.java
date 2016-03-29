package com.app.spott.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.app.spott.R;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginScreenFragment extends Fragment {

    @Bind(R.id.etUserName)
    EditText etUserName;

    @Bind(R.id.etPassword)
    EditText etPassword;

    @Bind(R.id.btnLogin)
    Button btnLogin;

    private LoginAdapter mListener;
    private static final String TAG = LoginScreenFragment.class.getSimpleName();

    public LoginScreenFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_screen, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryLogin();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (LoginAdapter) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void tryLogin() {
        String username = etUserName.getText().toString();
        String password = etPassword.getText().toString();
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser owner, ParseException e) {
                if (e == null)
                    mListener.onLoginSuccess(owner);
                else {
                    Log.e(TAG, "User login failed: "+ e.getMessage());
                    mListener.onLoginFailure(e);
                }
            }
        });
    }

    private void temp_signup(){
        ParseUser user = new ParseUser();
        user.setUsername("police");
        user.setPassword("thulla");
        user.setEmail("ansari.adil20@gmail.com");
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e!=null)
                    Log.e(TAG, "Signup fail" + e.getMessage());
            }
        });
    }

    public interface LoginAdapter {
        void onLoginSuccess(ParseUser logedInUser);
        void onLoginFailure(Exception e);
    }
}
