package com.chauduong.gym.fragment.mess;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.chauduong.gym.model.User;

import java.util.List;

public class MessViewModel extends AndroidViewModel implements MessManagerListener {
    private MutableLiveData<List<User>> listUserForChar = new MutableLiveData<>();
    private MutableLiveData<String> msgGetAllUserError = new MutableLiveData<>();
    private MessManager mMessManager = new MessManager(this);

    public MessViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<String> getMsgGetAllUserError() {
        return msgGetAllUserError;
    }

    public MutableLiveData<List<User>> getListUserForChar() {
        return listUserForChar;
    }

    public void getAllUserForChar() {
        mMessManager.getAllListUserForChat(getApplication());
    }

    @Override
    public void onGetAllUserSuccess(List<User> userList) {
        listUserForChar.setValue(userList);
    }

    @Override
    public void onGetAllUserError(String msg) {
        msgGetAllUserError.setValue(msg);
    }
}
