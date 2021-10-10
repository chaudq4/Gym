package com.chauduong.gym.activity;

import com.chauduong.gym.model.Conversation;
import com.chauduong.gym.model.Inbox;

import java.util.List;

public interface ConversationManagerListener {
    void onGetAllSuccessConversation(List<Conversation> conversationList);
    void onGetItemSuccessInbox(Inbox inbox);
    void onCancelGetAllTye(String message);
    void onGetAllSuccessInbox(List<Inbox> inboxList);
    void onAddNewInbox();
}
