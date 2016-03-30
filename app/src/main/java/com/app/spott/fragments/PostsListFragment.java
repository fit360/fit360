package com.app.spott.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.spott.R;
import com.app.spott.adapters.PostsAdapter;
import com.app.spott.models.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aparnajain on 3/20/16.
 */
public class PostsListFragment extends Fragment {
    PostsAdapter aPosts;
    ArrayList<Post> mPosts;
    RecyclerView rvPosts;
    LinearLayoutManager linearLayoutManager;
    OnFragmentInteractionListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_posts_lists, parent, false);

        rvPosts = (RecyclerView)v.findViewById(R.id.rvFeed);
        rvPosts.setAdapter(aPosts);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvPosts.setLayoutManager(linearLayoutManager);
        rvPosts.setNestedScrollingEnabled(true);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPosts = new ArrayList<>();
        aPosts = new PostsAdapter(getActivity(), mPosts);
    }

    public void addAll(ArrayList<Post> posts) {
        ArrayList dePosts = getDedupedPosts(posts);
        if(dePosts.size() > 0){
            mListener.hasPosts(true);
        }
        aPosts.addAll(dePosts);
        aPosts.notifyDataSetChanged();
    }

    private ArrayList getDedupedPosts(List<Post> posts) {//compare all new posts against existing ones
        if (mPosts.size() == 0){
            return (ArrayList)posts;
        }
        boolean dupe = false;

        ArrayList freshPosts = new ArrayList();
        for (int i = 0; i < posts.size(); i++){
            Post fetchedPost = posts.get(i);
            //new post date is after the array date
            //if(post.getUpdatedAt().after(mPosts.get(0).getUpdatedAt())){
            for (int j = 0; j < mPosts.size(); j++){
                Post arrayPost = mPosts.get(j);
                if(fetchedPost.getObjectId().equals(arrayPost.getObjectId())){
                    //it's a duplicate post
                    dupe = true;
                    break;
                }
            }
            if (!dupe){
                freshPosts.add(fetchedPost);
            }else{
                dupe = false;
            }
        }
        return freshPosts;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LoginAdapter");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void hasPosts(Boolean hasPosts);
    }

}
