package com.app.spott.models;

import com.parse.ParseClassName;

/**
 * Created by aparnajain on 3/14/16.
 */
@ParseClassName("Message")
public class Message extends Model{
    public static final String USER_ID_KEY = "userId";
    public static final String BODY_KEY = "body";

    public String getUserId() {
        return getString(USER_ID_KEY);
    }

    public String getBody() {
        return getString(BODY_KEY);
    }

    public void setUserId(String userId) {
        put(USER_ID_KEY, userId);
    }

    public void setBody(String body) {
        put(BODY_KEY, body);
    }

    @Override
    public String getLogTag() {
        return null;
    }
}
