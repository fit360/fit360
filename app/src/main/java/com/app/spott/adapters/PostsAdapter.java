package com.app.spott.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.spott.R;
import com.app.spott.activities.ProfileActivity;
import com.app.spott.models.Post;
import com.app.spott.models.User;
import com.bumptech.glide.Glide;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.ArrayList;

/**
 * Created by aparnajain on 3/10/16.
 */
public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    Context mContext;
    private ArrayList<Post> mPosts;

    public PostsAdapter(Context context, ArrayList<Post> posts) {
        mPosts = posts;
        mContext = context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_community_feed, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(context, contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final PostsAdapter.ViewHolder holder, int position) {
        Post post = mPosts.get(position);
        final User user = post.getUser();
        if (user != null){
            holder.tvUserName.setText(user.getFirstName());
            if (user.getProfileImageUrl() != null){
                Glide.with(holder.context).load(user.getProfileImageUrl()).centerCrop().into(holder.ivProfilePic);
            }else {
                Glide.with(holder.context).load(getProfileUrl(post.getObjectId())).centerCrop().into(holder.ivProfilePic);
            }
            holder.ivProfilePic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, ProfileActivity.class);
                    i.putExtra("userId", user.getObjectId());
                    mContext.startActivity(i);
                }
            });
        }
        holder.tvCaption.setText(post.getBody());
        Glide.with(holder.context).load(post.getImageUrl()).centerCrop().into(holder.ivPhoto);
    }
    // Create a gravatar image based on the hash value obtained from userId
    private static String getProfileUrl(final String userId) {
        String hex = "";
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] hash = digest.digest(userId.getBytes());
            final BigInteger bigInt = new BigInteger(hash);
            hex = bigInt.abs().toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "http://www.gravatar.com/avatar/" + hex + "?d=identicon";
    }


    @Override
    public int getItemCount() {
        System.out.println("mPosts size" + String.valueOf(mPosts.size()));
        return mPosts.size();
    }
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvUserName;
        TextView tvCaption;
        ImageView ivPhoto;
        ImageView ivProfilePic;
        private Context context;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
//          ButterKnife.bind(this, itemView);
            tvCaption = (TextView)itemView.findViewById(R.id.tvCaption);
            tvUserName = (TextView)itemView.findViewById(R.id.tvUsername);
            ivPhoto = (ImageView)itemView.findViewById(R.id.ivPhoto);
            ivProfilePic = (ImageView)itemView.findViewById(R.id.ivProfilePic);
            // Store the context
            this.context = context;
            // Attach a click listener to the entire row view
            itemView.setOnClickListener(this);
        }

        // Handles the row being being clicked
        @Override
        public void onClick(View view) {
        }
    }
    public void addAll(ArrayList<Post> posts) {
        mPosts.addAll(posts);
    }
}
