package com.app.spott.models;

import com.parse.FindCallback;
import com.parse.ParseClassName;
import com.parse.ParseQuery;

/**
 * Created by aparnajain on 3/10/16.
 */
@ParseClassName("Post")
public class Post extends Model {
    private static final String USER = "user";
    private static final String IMAGE_URL = "image_url";
    private static final String BODY = "body";
    private static final String CREATED_AT = "createdAt";
    private static final String LIKES = "likes";
    private static final String LOVES = "loves";
    private static final String WOWS = "wows";

    public Post() {
        super();
    }

    // Add a constructor that contains core properties
    public Post(String body) {
        super();
        setBody(body);
    }

    // Use getString and others to access fields
    public String getBody() {
        return getString("body");
    }
    public String getImageUrl() {
        return getString("image_url");
    }
    // Use put to modify field values
    public void setBody(String value) {
        put(BODY, value);
    }

    public User getUser() {
        return (User) get(USER);
    }

    public void setUser(User user) {
        put(USER, user);
    }

    public void setImageUrl(String imageUrl){
        put(IMAGE_URL, imageUrl);
    }
    public int getLikes() {
        return getInt(LIKES);
    }
    public void setLikes(int value){
        put(LIKES, value);
    }

    public int getLoves() {
        return getInt(LOVES);
    }
    public void setLoves(int value){
        put(LOVES, value);
    }

    public int getWows() {
        return getInt(WOWS);
    }
    public void setWows(int value){
        put(WOWS, value);
    }

    @Override
    public String getLogTag() {
        return null;
    }

    public static void fetchUserPosts(User user, FindCallback<Post> findCallback){
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo(USER, user);
        query.include(USER);
        query.orderByDescending("updatedAt");
        query.findInBackground(findCallback);
    }
}
