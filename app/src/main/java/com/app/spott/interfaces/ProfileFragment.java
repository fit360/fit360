package com.app.spott.interfaces;

import com.app.spott.models.User;

public interface ProfileFragment {
    public abstract User getUser();
    public abstract boolean isLoggedInUser();
}
