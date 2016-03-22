package com.app.spott.models;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

@ParseClassName("User")
public class User extends Model {

    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String AGE = "age";
    private static final String GENDER = "gender";
    private static final String OWNER = "owner";
    private static final String PROFILE_IMAGE_URL = "profile_image_url";
    private static final String COVER_IMAGE_URL = "cover_image_url";

    private static final String TAG = User.class.getSimpleName();

    private static ParseQuery<User> query;

    public User() {
        super();
    }

    public String getFirstName() {
        return getString(FIRST_NAME);
    }

    public String getProfileImageUrl() {
        return getString(PROFILE_IMAGE_URL);
    }

    public String getCoverImageUrl() {
        return getString(COVER_IMAGE_URL);
    }

    public void setFirstName(String firstName) {
        put(FIRST_NAME, firstName);
    }

    public String getLastName() {

        return getString(LAST_NAME);
    }

    public void setLastName(String lastName) {
        put(LAST_NAME, lastName);
    }

    public int getAge() {
        return getInt(AGE);
    }

    public void setAge(int age) {
        put(AGE, age);
    }

    public Gender getGender() {
        return Gender.valueOf(getString(GENDER));
    }

    public void setGender(Gender gender) {
        put(GENDER, gender.toString());
    }

    public String getId(String id) {
        return getObjectId();
    }

    public ParseUser getOwner() {
        return getParseUser(OWNER);
    }

    public void setOwner(ParseUser owner) {
        put(OWNER, owner);
    }

    @Override
    public String getLogTag() {
        return TAG;
    }

    public static void getByOwner(ParseUser owner, GetCallback<User> getCallback) {
        query = ParseQuery.getQuery(User.class);
        query.whereEqualTo(OWNER, owner);
        query.getFirstInBackground(getCallback);
    }

    public void getChosenAcitivities(FindCallback<Workout> findCallback) {
        ParseQuery<Workout> parseQuery = ParseQuery.getQuery(Workout.class);
        parseQuery.whereEqualTo(Workout.USER, this);
        parseQuery.orderByAscending("createdAt");
        parseQuery.include(Workout.USER);
        parseQuery.include(Workout.LOCATION);
        parseQuery.findInBackground(findCallback);
    }

    public static User findUserOnUIThread(String id) throws ParseException {
        query = ParseQuery.getQuery(User.class);
        query.whereEqualTo(objectId, id);
        return query.getFirst();
    }

}
