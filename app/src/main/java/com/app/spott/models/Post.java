package com.app.spott.models;

import com.parse.ParseClassName;

/**
 * Created by aparnajain on 3/10/16.
 */
@ParseClassName("Post")
public class Post extends Model {
    private static final String USER = "user";

    // Ensure that your subclass has a public default constructor
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
        put("body", value);
    }
    public void setImageUrl(String value) {
        put("image_url", value);
    }

    public User getUser() {
        return (User) get(USER);
    }

    public void setUser(User user) {
        put(USER, user);
    }

    // Associate each item with a user
    public void setOwner(User user) {
        put(USER, user);
    }

    @Override
    public String getLogTag() {
        return null;
    }
}
