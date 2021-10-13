package com.chauduong.gym.inbox;

import android.content.Context;
import android.util.Log;

import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.chauduong.gym.BR;
import com.chauduong.gym.model.Inbox;
import com.chauduong.gym.model.User;

import java.util.Date;


public class InboxViewModel extends ViewModel implements InboxManagerListener, Observable {
    private static final String TAG = "InboxViewModel";
    private static int currentPage = 1;

    private PropertyChangeRegistry registry = new PropertyChangeRegistry();

    private MutableLiveData<Inbox> mListMutableLiveDataInbox;
    private MutableLiveData<Boolean> isNewInbox;
    private MutableLiveData<User> toUser = new MutableLiveData<>();

    private InboxManager inboxManager;

    private String msg;
    private boolean isFavorite = true;

    @Bindable
    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        this.isFavorite = favorite;
        registry.notifyChange(this, BR.favorite);
    }

    @Bindable
    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
        registry.notifyChange(this, BR.msg);
    }

    @Bindable
    public MutableLiveData<User> getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser.setValue(toUser);
        registry.notifyChange(this, BR.toUser);
    }

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

    public void sentInbox(String link) {
        Inbox inbox = new Inbox();
        inbox.setTo(toUser.getValue());
        inbox.setMsg(msg);
        inbox.setTime(new Date().toString());
        inbox.setLink(link);
        inboxManager.sentInbox(inbox);
        setMsg("");
        setFavorite(true);
    }

    public MutableLiveData<Inbox> getmListMutableLiveDataInbox() {
        return mListMutableLiveDataInbox;
    }

    public MutableLiveData<Boolean> getIsNewInbox() {
        return isNewInbox;
    }

    public void listenToUserChange(User toUser) {
        inboxManager.listenToUserChange(toUser);
    }

    @Override
    public void onGetItemSuccessInbox(Inbox inbox) {
        mListMutableLiveDataInbox.setValue(inbox);
    }


    @Override
    public void onAddNewInbox() {
        isNewInbox.setValue(true);
    }

    @Override
    public void onToUserChange(User user) {
        if (user != null) {
            Log.i(TAG, "onToUserChange: " + user.toString());
            setToUser(user);
        }
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        registry.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        registry.remove(callback);
    }
}
