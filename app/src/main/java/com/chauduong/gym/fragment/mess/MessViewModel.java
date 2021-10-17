package com.chauduong.gym.fragment.mess;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.chauduong.gym.model.Conversation;
import com.chauduong.gym.model.User;

import java.util.ArrayList;
import java.util.List;

public class MessViewModel extends AndroidViewModel implements MessManagerListener {
    private MutableLiveData<List<User>> listUserForChar = new MutableLiveData<>();
    private MutableLiveData<String> msgGetAllUserError = new MutableLiveData<>();
    private MutableLiveData<List<Conversation>> listConversation = new MutableLiveData<>();
    private MessManager mMessManager = new MessManager(this, getApplication());

    public MessViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getMsgGetAllUserError() {
        return msgGetAllUserError;
    }

    public MutableLiveData<List<User>> getListUserForChar() {
        return listUserForChar;
    }

    public MutableLiveData<List<Conversation>> getListConversation() {
        return listConversation;
    }

    public void getAllConversation() {
        mMessManager.getAllConversation();
    }

    public void getAllUserForChar() {
        mMessManager.getAllListUserForChat();
    }

    @Override
    public void onGetAllUserSuccess(List<User> userList) {
        listUserForChar.setValue(userList);
    }

    @Override
    public void onGetAllUserError(String msg) {
        msgGetAllUserError.setValue(msg);
    }

    @Override
    public void onGetItemConversation(List<Conversation> conversations) {
        listConversation.setValue(conversations);
    }

}
