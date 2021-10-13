package com.chauduong.gym.fragment.signin;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.chauduong.gym.BR;
import com.chauduong.gym.model.User;

public class SignInViewModel extends AndroidViewModel implements SignInManagerListener, Observable {
    PropertyChangeRegistry propertyChangeRegistry = new PropertyChangeRegistry();
    private String phoneNumber;
    private String password;
    private SignInManager mSignInManager;
    private MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<String> msgSignError= new MutableLiveData<>();

    public SignInViewModel(@NonNull Application application) {
        super(application);
        mSignInManager = new SignInManager(application, this);
    }

    @Bindable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        propertyChangeRegistry.notifyChange(this, BR.phoneNumber);
    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        propertyChangeRegistry.notifyChange(this, BR.password);
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public MutableLiveData<String> getMsgSignError() {
        return msgSignError;
    }

    public void login() {
        User user = new User();
        user.setPassword(password);
        user.setPhoneNumber(phoneNumber);
        mSignInManager.login(user);
    }


    @Override
    public void onSignInSuccess(User user) {
        userMutableLiveData.setValue(user);
    }

    @Override
    public void onSignInError(String msg) {
        msgSignError.setValue(msg);
    }

    @Override
    public void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        propertyChangeRegistry.add(callback);
    }

    @Override
    public void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        propertyChangeRegistry.remove(callback);
    }
}

