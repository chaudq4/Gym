package com.chauduong.gym.ui;


import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.chauduong.gym.BR;
import com.chauduong.gym.model.User;

public class AccountViewModel extends AndroidViewModel implements AccountManagerListener, Observable {
    private PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();
    private AccountManager accountManager;
    private MutableLiveData<String[]> isUpdatePassSuccess = new MutableLiveData<>();
    private MutableLiveData<Double> progressUpload = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUpdateAvatarSuccess = new MutableLiveData<>();
    private User user = new User();


    public MutableLiveData<Boolean> getIsUpdateAvatarSuccess() {
        return isUpdateAvatarSuccess;
    }

    public MutableLiveData<Double> getProgressUpload() {
        return progressUpload;
    }

    public AccountViewModel(@NonNull Application application) {
        super(application);
        accountManager = new AccountManager(getApplication(), this);
    }

    public MutableLiveData<String[]> getIsUpdatePassSuccess() {
        return isUpdatePassSuccess;
    }


    @Bindable
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        propertyChangeRegistry.notifyChange(this, BR.user);
    }


    public void listenChangeUser(User user) {
        accountManager.listenerAccount(user);
    }

    public void uploadFile(Uri filePath) {
        accountManager.uploadImage(filePath);
    }

    @Override
    public void onUserChange(User user) {
        setUser(user);

    }

    @Override
    public void onChangePassSuccess(String newPass) {
        String[] result = new String[2];
        result[0] = "1";
        result[1] = newPass;
        isUpdatePassSuccess.setValue(result);
    }

    @Override
    public void onChangePassFail() {
        String[] result = new String[2];
        result[0] = "0";
        isUpdatePassSuccess.setValue(result);
    }

    @Override
    public void onUploadImageAccountSuccess(String url) {
        accountManager.updateLinkAvatar(url);
    }

    @Override
    public void onUpdateImageSuccess() {
        isUpdateAvatarSuccess.setValue(true);
    }

    @Override
    public void onUploadImageAccountFail() {
        isUpdateAvatarSuccess.setValue(false);
    }

    @Override
    public void onProgressUpload(double progress) {
        progressUpload.setValue(progress);
    }

    @Override
    public void onUpdateInformationAccountSuccess() {

    }

    @Override
    public void onUpdateInformationAccountFail() {

    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        propertyChangeRegistry.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        propertyChangeRegistry.remove(callback);
    }

    public void updatePassword(User user, String oldPass, String newPass) {
        accountManager.changePass(user, oldPass, newPass);
    }

    public void updateInformationAccount(User user) {
        accountManager.updateInformationAccount(user);
    }
}
