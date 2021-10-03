package com.chauduong.gym.fragment.signup;

import android.content.Context;

import com.chauduong.gym.model.User;

public class SignUpPresenter implements SignUpManagerListener {
    private Context mContext;
    private SignUpPresenterListener mSignUpPresenterListener;
    private SignUpManager mSignUpManager;
    public SignUpPresenter(Context mContext, SignUpPresenterListener mSignUpPresenterListener) {
        this.mContext = mContext;
        this.mSignUpPresenterListener = mSignUpPresenterListener;
        mSignUpManager = new SignUpManager(this, mContext);
    }

    public void signUp(String name, String phoneNumber, String passWord) {
        User user = new User();
        user.setName(name);
        user.setPhoneNumber(phoneNumber);
        user.setPassword(passWord);
        mSignUpManager.signUp(user);

    }

    @Override
    public void onSignUpSuccess(User user) {
        mSignUpPresenterListener.onSignUpSuccess(user);
    }

    @Override
    public void onSignUpError(String msg) {
        mSignUpPresenterListener.onSignUpError(msg);

    }
}
