package com.chauduong.gym.fragment.signin;


import com.chauduong.gym.model.User;

public interface SignInPresenterListener {
    void onSignInSuccess(User user);
    void onSignInError(String msg);
    void onSignUpClick();
}
