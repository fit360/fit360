package com.app.spott.fragments;

import android.support.v4.app.Fragment;
import android.widget.Button;
import android.widget.EditText;

import com.app.spott.R;

import butterknife.Bind;

public class SignUpFragment extends Fragment {

    @Bind(R.id.etFirstName)
    EditText etFirstName;

    @Bind(R.id.etLastName)
    EditText etLastname;

    @Bind(R.id.etEmail)
    EditText etEmail;

    @Bind(R.id.etUserName)
    EditText etUserName;

    @Bind(R.id.etPassword)
    EditText etPassword;

    @Bind(R.id.etConfirmPassword)
    EditText etConfirmPassword;

    @Bind(R.id.btnSignUp)
    Button btnSignUp;

    private SignUpAdapter adapter;
    private static final String TAG = SignUpFragment.class.getSimpleName();

    public interface SignUpAdapter {
        void onSignUpSuccess();
        void onSignUpFailure();
    }

    public SignUpFragment(){}
}
