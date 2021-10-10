package com.chauduong.gym.activity;

import android.content.Context;

import com.chauduong.gym.model.Conversation;
import com.chauduong.gym.model.Inbox;
import com.chauduong.gym.model.User;

import java.util.List;

public class ConversationPresenter implements ConversationManagerListener {
    private ConversationManager mConversationManager;
    private Context context;
    private ConversationPresenterListener mPresenterListener;
    private static int index = 1;

    public ConversationPresenter(Context context, ConversationPresenterListener mPresenterListener) {
        this.context = context;
        this.mPresenterListener = mPresenterListener;
        mConversationManager = new ConversationManager(context, this);
    }

    public void getAllConversation(User user) {
        mConversationManager.getAllConversation(user);
    }

    public void getAllInbox(User toUser) {
        mConversationManager.getAllInbox(toUser, index++);
    }

    public void sentInbox(String msg, String link, String time, User toUser) {
        Inbox inbox = new Inbox();
        inbox.setTo(toUser);
        inbox.setMsg(msg);
        inbox.setTime(time);
        inbox.setLink(link);
        mConversationManager.sentInbox(inbox);
    }

    @Override
    public void onGetAllSuccessConversation(List<Conversation> conversationList) {
        mPresenterListener.onGetAllConversation(conversationList);
    }

    @Override
    public void onCancelGetAllTye(String message) {

    }

    @Override
    public void  onGetItemSuccessInbox(Inbox inbox) {
        mPresenterListener.onGetItemInbox(inbox);
    }

    @Override
    public void onGetAllSuccessInbox(List<Inbox> inboxList) {
        mPresenterListener.onGetAllItemInbox(inboxList);
    }

    @Override
    public void onAddNewInbox() {
        mPresenterListener.onNewInbox();
    }
}
