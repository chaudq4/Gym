package com.chauduong.gym.adapter;

import com.chauduong.gym.model.User;

public interface ContactListener {
    void onContactClick(User user);
    void onContactTouch(User user);
}
