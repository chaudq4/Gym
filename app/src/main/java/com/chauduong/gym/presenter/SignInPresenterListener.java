package com.chauduong.gym.presenter;


import com.chauduong.gym.model.User;

public interface SignInPresenterListener {
    void onSignInSuccess(User user);
    void onSignInError(String msg);
    void onSignUpClick();
}
