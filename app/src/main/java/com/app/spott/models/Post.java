package com.app.spott.models;

import com.parse.ParseClassName;
import com.parse.ParseUser;

/**
 * Created by aparnajain on 3/10/16.
 */
@ParseClassName("Post")
public class Post extends Model {
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

    // Get the user for this item
    public ParseUser getUser()  {
        return getParseUser("User");
    }

    // Associate each item with a user
    public void setOwner(ParseUser user) {
        put("User", user);
    }

    @Override
    public String getLogTag() {
        return null;
    }
}
