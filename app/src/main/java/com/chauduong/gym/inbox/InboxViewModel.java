package com.chauduong.gym.inbox;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.chauduong.gym.model.Inbox;
import com.chauduong.gym.model.User;


public class InboxViewModel extends ViewModel implements InboxManagerListener {
    private static final String TAG = "InboxViewModel";
    private MutableLiveData<Inbox> mListMutableLiveDataInbox;
    private InboxManager inboxManager;
    private static int currentPage = 1;
    private MutableLiveData<Boolean> isNewInbox;

    public void init(Context mContext) {
        inboxManager = new InboxManager(mContext, this);
        mListMutableLiveDataInbox = new MutableLiveData<>();
        isNewInbox = new MutableLiveData<>();
    }

    public void getAllInbox(User toUser) {
        if (inboxManager != null) {
            inboxManager.getPagingInbox(toUser, currentPage++);
        }
    }

    public void sentInbox(String msg, String link, String time, User toUser) {
        Inbox inbox = new Inbox();
        inbox.setTo(toUser);
        inbox.setMsg(msg);
        inbox.setTime(time);
        inbox.setLink(link);
        inboxManager.sentInbox(inbox);
    }

    public MutableLiveData<Inbox> getmListMutableLiveDataInbox() {
        return mListMutableLiveDataInbox;
    }

    public MutableLiveData<Boolean> getIsNewInbox() {
        return isNewInbox;
    }

    @Override
    public void onGetItemSuccessInbox(Inbox inbox) {
        mListMutableLiveDataInbox.setValue(inbox);
    }


    @Override
    public void onAddNewInbox() {
        isNewInbox.setValue(true);
    }
}
