package com.chauduong.gym.inbox;

import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.text.format.DateFormat;
import android.util.Log;

import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.chauduong.gym.BR;
import com.chauduong.gym.model.Inbox;
import com.chauduong.gym.model.User;

import java.time.LocalTime;
import java.util.Date;


public class InboxViewModel extends ViewModel implements InboxManagerListener, Observable {
    private static final String TAG = "InboxViewModel";
    private static int currentPage = 1;

    private PropertyChangeRegistry registry = new PropertyChangeRegistry();

    private MutableLiveData<Inbox> mListMutableLiveDataInbox;
    private MutableLiveData<Boolean> isNewInbox;
    private MutableLiveData<User> toUser = new MutableLiveData<>();
    private MutableLiveData<String> linkUpload = new MutableLiveData<>();
    private MutableLiveData<Double> progressUpload = new MutableLiveData<>();
    private MutableLiveData<String> msgErrorUpload = new MutableLiveData<>();
    private InboxManager inboxManager;

    private String msg;


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
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        Date date = new Date();
        System.out.println(dateFormat.format(date));
        inbox.setTime(dateFormat.format(date));
        inbox.setLink(link);
        inboxManager.sentInbox(inbox);
        setMsg("");
    }

    public void uploadFile(Uri filePath) {
        inboxManager.uploadImage(filePath);
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

    public MutableLiveData<String> getLinkUpload() {
        return linkUpload;
    }

    public MutableLiveData<Double> getProgressUpload() {
        return progressUpload;
    }

    public MutableLiveData<String> getMsgErrorUpload() {
        return msgErrorUpload;
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
    public void onProgressUpload(double percent) {
        progressUpload.setValue(percent);
    }

    @Override
    public void onFailed(String msg) {
        msgErrorUpload.setValue(msg);
    }

    @Override
    public void onSuccessUpload(String link) {
        linkUpload.setValue(link);
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
