package com.app.spott.fragments;

import android.content.Context;
import android.content.Intent;
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
import com.app.spott.activities.CommunityFeedActivity;
import com.app.spott.exceptions.ModelException;
import com.app.spott.models.Post;
import com.app.spott.models.User;
import com.app.spott.utils.Utils;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PostComposeFragment extends Fragment {

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
        View view = inflater.inflate(R.layout.fragment_post_compose, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        final Uri imageUri = Utils.getPhotoFileUri(mContext, PostImageCaptureFragment.FINAL_IMAGE_NAME);
        ivPostImage.setImageURI(imageUri);
        etPostMessage.requestFocus();

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    final String imageUrl = Utils.saveImageToCloud(imageUri.getPath());
                    User.getByOwner(ParseUser.getCurrentUser(), new GetCallback<User>() {
                        @Override
                        public void done(User user, ParseException e) {
                            Post post = new Post();
                            post.setUser(user);
                            post.setImageUrl(imageUrl);
                            post.setBody(etPostMessage.getText().toString());
                            try {
                                post.saveModel();
                            } catch (ModelException e1) {
                                e1.printStackTrace();
                            }

                            Intent intent = new Intent(mContext, CommunityFeedActivity.class);
                            mContext.startActivity(intent);
                        }
                    });

                } catch (ParseException e) {
                    e.printStackTrace();
                }
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
