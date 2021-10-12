package com.chauduong.gym.inbox;

import com.chauduong.gym.model.Inbox;

public interface InboxManagerListener {
    void onGetItemSuccessInbox(Inbox inbox);

    void onAddNewInbox();
}
