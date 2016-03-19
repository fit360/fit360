package com.app.spott.interfaces;

import com.app.spott.models.User;
import com.app.spott.models.Workout;

public interface WorkoutEditFragmentListener {
    Workout getWorkout();
    User getCurrentUser();
    boolean isLoggedInUser();
    boolean isNewWorkout();
    void setLatLng();
    void notifyListenerActivity(Workout w);
}
