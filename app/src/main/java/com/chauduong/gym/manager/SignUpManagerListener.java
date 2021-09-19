package com.chauduong.gym.manager;

import com.chauduong.gym.model.User;

public interface SignUpManagerListener {
    void onSignUpSuccess(User user);
    void onSignUpError(String msg);
}
