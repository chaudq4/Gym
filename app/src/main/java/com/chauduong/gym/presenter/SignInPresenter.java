package com.chauduong.gym.presenter;

import android.content.Context;

import com.chauduong.gym.manager.SignInManager;
import com.chauduong.gym.manager.SignInManagerListener;
import com.chauduong.gym.model.User;

public class SignInPresenter implements SignInManagerListener {
    private SignInPresenterListener mSignInPresenterListener;
    private Context mContext;
    private SignInManager mSignInManager;

    public SignInPresenter(SignInPresenterListener mSignInPresenterListener, Context mContext) {
        this.mSignInPresenterListener = mSignInPresenterListener;
        this.mContext = mContext;
        mSignInManager = new SignInManager(mContext, this);
    }

    public void login(String phoneNumber, String passWord) {
        User user = new User();
        user.setPassword(passWord);
        user.setPhoneNumber(phoneNumber);
        mSignInManager.login(user);
    }

    public void signUp() {
        mSignInPresenterListener.onSignUpClick();
    }

    @Override
    public void onSignInSuccess(User user) {
        mSignInPresenterListener.onSignInSuccess(user);
    }

    @Override
    public void onSignInError(String msg) {
        mSignInPresenterListener.onSignInError(msg);

    }
}

