package com.app.spott.fragments;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.VideoView;

import com.app.spott.R;
import com.app.spott.activities.CommunityFeedActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginVideoFragment extends Fragment {

    @Bind(R.id.vvLoginVideo)
    VideoView vvLoginVideo;

    @Bind(R.id.btnLogin)
    Button btnLogin;

    @Bind(R.id.btnSignup)
    Button btnSignup;

    private Context mContext;
    private OnFragmentInteractionListener mListener;

    public LoginVideoFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LoginVideoFragment newInstance(String param1, String param2) {
        LoginVideoFragment fragment = new LoginVideoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login_video, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        vvLoginVideo.setVideoURI(Uri.parse("android.resource://" + mContext.getPackageName() + "/" + R.raw.loginvid));
        vvLoginVideo.setMediaController(null);
        vvLoginVideo.requestFocus();
        vvLoginVideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
        vvLoginVideo.start();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mListener.startLogin();
                Intent intent = new Intent(mContext, CommunityFeedActivity.class);
                mContext.startActivity(intent);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        mListener = (OnFragmentInteractionListener) context;
    }

        @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void startLogin();

        void startSignup();
    }
}
