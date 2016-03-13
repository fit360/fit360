package com.app.spott.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.app.spott.R;
import com.app.spott.models.User;
import com.app.spott.utils.Utils;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PostComposeFragment extends Fragment {

    @Bind(R.id.ivIconPhoto)
    ImageView ivIconPhoto;

    @Bind(R.id.ivPostImage)
    ImageView ivPostImage;

    @Bind(R.id.etPostMessage)
    EditText etPostMessage;

    @Bind(R.id.btnPost)
    Button btnPost;

    private OnFragmentInteractionListener mListener;
    private Context mContext;

    public PostComposeFragment() {
        // Required empty public constructor
    }

    public static PostComposeFragment newInstance(Bitmap bitmap) {
        PostComposeFragment fragment = new PostComposeFragment();
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
        View view =  inflater.inflate(R.layout.fragment_post_compose, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final Uri imageUri = Utils.getPhotoFileUri(mContext, "post_image.png");
        ivPostImage.setImageURI(imageUri);
        etPostMessage.requestFocus();

        ivIconPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.startImageCaptureFragment();
            }
        });

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Utils.saveImageToCloud(imageUri.getPath());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                User.getByOwner(ParseUser.getCurrentUser(), new GetCallback<User>() {
                    @Override
                    public void done(User object, ParseException e) {
                        //save post
                    }
                });

            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void startImageCaptureFragment();
    }
}
