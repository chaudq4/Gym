package com.chauduong.gym.fragment.mess;

import com.chauduong.gym.model.Conversation;
import com.chauduong.gym.model.User;

import java.util.List;

public interface MessManagerListener {
    void onGetAllUserSuccess(List<User> userList);

    void onGetAllUserError(String msg);

    void onGetItemConversation(List<Conversation> conversations);
}
