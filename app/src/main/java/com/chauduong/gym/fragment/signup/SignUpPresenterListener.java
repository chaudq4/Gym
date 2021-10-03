package com.chauduong.gym.fragment.signup;

import com.chauduong.gym.model.User;

public interface SignUpPresenterListener {
    void onSignUpSuccess(User user);
    void onSignUpError(String msg);
}
