package com.app.spott.models;

import com.parse.ParseClassName;
import com.parse.ParseUser;

/**
 * Created by aparnajain on 3/10/16.
 */
@ParseClassName("Post")
public class Post extends Model {
    // Ensure that your subclass has a public default constructor

    private static final String USER = "user";
    private static final String IMAGE_URL = "image_url";
    private static final String BODY = "body";



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

    // Get the user for this item
    public ParseUser getUser()  {
        return getParseUser("User");
    }

    // Associate each item with a user
    public void setOwner(ParseUser user) {
        put("User", user);
    }

    public void setUser(User user) {
        put(USER, user);
    }

    public void setImageUrl(String imageUrl){
        put(IMAGE_URL, imageUrl);
    }

    @Override
    public String getLogTag() {
        return null;
    }
}
