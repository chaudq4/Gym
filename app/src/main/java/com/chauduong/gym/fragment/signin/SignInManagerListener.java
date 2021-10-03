package com.chauduong.gym.fragment.signin;

import com.chauduong.gym.model.User;

public interface SignInManagerListener {
    void onSignInSuccess(User user);
    void onSignInError(String msg);
}
