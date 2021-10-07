package com.chauduong.gym.fragment.mess;

import com.chauduong.gym.model.User;

import java.util.List;

public interface MessPresenterListener {
    void onGetAllUserSuccess(List<User> userList);
    void onGetAllUserError(String msg);
}
