package com.app.spott.interfaces;

import com.app.spott.models.User;

public interface ProfileFragmentListener {
    User getUser();
    boolean isLoggedInUser();
}
