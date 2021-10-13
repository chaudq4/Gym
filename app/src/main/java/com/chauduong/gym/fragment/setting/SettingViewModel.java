package com.chauduong.gym.fragment.setting;

import android.util.Log;

import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.chauduong.gym.BR;
import com.chauduong.gym.model.User;

public class SettingViewModel extends ViewModel implements SettingDatabaseListener, Observable {
    private static final String TAG = "SettingViewModel";

    private PropertyChangeRegistry registry = new PropertyChangeRegistry();
    private MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    private SettingDatabaseManager settingDatabaseManager = new SettingDatabaseManager(this);

    @Bindable
    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }


    public void setUserMutableLiveData(User user) {
        this.userMutableLiveData.setValue(user);
        registry.notifyChange(this, BR.userMutableLiveData);
    }

    public void setListenerUser(User user) {
        settingDatabaseManager.listenUserChange(user);
    }

    @Override
    public void onUserChange(User user) {
        Log.i(TAG, "onUserChange: " + user.toString());
        setUserMutableLiveData(user);
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
