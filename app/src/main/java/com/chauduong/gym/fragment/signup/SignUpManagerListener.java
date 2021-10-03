package com.chauduong.gym.fragment.signup;

import com.chauduong.gym.model.User;

public interface SignUpManagerListener {
    void onSignUpSuccess(User user);
    void onSignUpError(String msg);
}
