package com.chauduong.gym.manager;

import com.chauduong.gym.model.User;

public interface SignInManagerListener {
    void onSignInSuccess(User user);
    void onSignInError(String msg);
}
