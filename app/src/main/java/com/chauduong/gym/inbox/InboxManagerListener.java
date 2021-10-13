package com.chauduong.gym.inbox;

import com.chauduong.gym.model.Inbox;
import com.chauduong.gym.model.User;

public interface InboxManagerListener {
    void onGetItemSuccessInbox(Inbox inbox);

    void onAddNewInbox();

    void onToUserChange(User user);
}
