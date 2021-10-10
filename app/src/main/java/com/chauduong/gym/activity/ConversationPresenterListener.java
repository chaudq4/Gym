package com.chauduong.gym.activity;

import com.chauduong.gym.model.Conversation;
import com.chauduong.gym.model.Inbox;

import java.util.List;

public interface ConversationPresenterListener {
    void onGetAllConversation(List<Conversation> conversations);
    void onGetItemInbox(Inbox inbox);
    void onGetAllItemInbox(List<Inbox> inboxList);
    void onNewInbox();
}
