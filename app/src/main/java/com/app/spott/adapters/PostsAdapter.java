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

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Seconds;
import org.joda.time.Weeks;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

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
            holder.tvUserName.setText("@" + user.getFirstName() + user.getLastName());
            holder.tvRealName.setText(user.getFirstName() + " " + user.getLastName());
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
        if (post.getCreatedAt() != null){
            holder.tvCreatedAt.setText(formatCreatedAtString(post.getCreatedAt().toString()));
        }
        holder.tvCaption.setText(post.getBody());
        Glide.with(holder.context).load(post.getImageUrl()).centerCrop().into(holder.ivPhoto);
    }

    private String formatCreatedAtString(String rawJsonDate) {
        String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
        SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
        sf.setLenient(true);

        String relativeDate = "";
        try {
            DateTime createdDateTime = new DateTime(sf.parse(rawJsonDate).getTime());
            DateTime currentDateTime = new DateTime();

            int secondDifference = Seconds.secondsBetween(createdDateTime.toLocalDateTime(), currentDateTime.toLocalDateTime()).getSeconds();
            if (secondDifference < 60) {
                relativeDate = Integer.toString(secondDifference) + "s";
            } else if (Minutes.minutesBetween(createdDateTime.toLocalDateTime(), currentDateTime.toLocalDateTime()).getMinutes() < 60) {
                relativeDate = Integer.toString(Minutes.minutesBetween(createdDateTime.toLocalDateTime(), currentDateTime.toLocalDateTime()).getMinutes())+"m";
            } else if (Hours.hoursBetween(createdDateTime.toLocalDateTime(), currentDateTime.toLocalDateTime()).getHours() < 24) {
                relativeDate = Integer.toString(Hours.hoursBetween(createdDateTime.toLocalDateTime(),currentDateTime.toLocalDateTime()).getHours())+"h";
            } else if (Weeks.weeksBetween(createdDateTime.toLocalDateTime(), currentDateTime.toLocalDateTime()).getWeeks() < 1) {
                relativeDate = Integer.toString(Days.daysBetween(createdDateTime.toLocalDateTime(), currentDateTime.toLocalDateTime()).getDays()) + "d";
            } else if (Months.monthsBetween(createdDateTime.toLocalDateTime(), currentDateTime.toLocalDateTime()).getMonths() < 1) {
                relativeDate = Integer.toString(Weeks.weeksBetween(createdDateTime.toLocalDateTime(), currentDateTime.toLocalDateTime()).getWeeks()) + "w";
            } else {
//                relativeDate = Integer.toString(Months.monthsBetween(createdDateTime.toLocalDateTime(), currentDateTime.toLocalDateTime()).getMonths())+"M";
                String dateFormat = "MM/dd/yyyy";
                SimpleDateFormat nf = new SimpleDateFormat(dateFormat);
                relativeDate = nf.format(sf.parse(rawJsonDate));

            }
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return relativeDate;
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
        TextView tvRealName;
        TextView tvCreatedAt;
        TextView tvCaption;
        ImageView ivPhoto;
        ImageView ivProfilePic;


        private Context context;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
//          ButterKnife.bind(this, itemView);
            tvCaption = (TextView)itemView.findViewById(R.id.tvCaption);
            tvUserName = (TextView)itemView.findViewById(R.id.tvUsername);
            tvRealName = (TextView)itemView.findViewById(R.id.tvRealName);
            tvCreatedAt = (TextView)itemView.findViewById(R.id.tvCreatedAt);
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
