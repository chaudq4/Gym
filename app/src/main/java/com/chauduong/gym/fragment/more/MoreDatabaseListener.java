package com.chauduong.gym.fragment.more;

import com.chauduong.gym.model.BodyInformation;
import com.chauduong.gym.model.User;

public interface MoreDatabaseListener {
    void onUserChange(User user);
    void onUpdateLastBodyInformation(BodyInformation bodyInformation);
}
