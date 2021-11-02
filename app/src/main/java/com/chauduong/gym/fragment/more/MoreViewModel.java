package com.chauduong.gym.fragment.more;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.Bindable;
import androidx.databinding.Observable;
import androidx.databinding.PropertyChangeRegistry;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.chauduong.gym.BR;
import com.chauduong.gym.fragment.signin.SignInManager;
import com.chauduong.gym.manager.session.SessionManager;
import com.chauduong.gym.model.BodyInformation;
import com.chauduong.gym.model.User;

public class MoreViewModel extends AndroidViewModel implements MoreDatabaseListener, Observable {
    private static final String TAG = "SettingViewModel";

    private PropertyChangeRegistry registry = new PropertyChangeRegistry();
    private MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    private MoreDatabaseManager moreDatabaseManager = new MoreDatabaseManager(this);
    private SessionManager mSessionManager = new SessionManager(getApplication());
    private MutableLiveData<BodyInformation> bodyInformationMutableLiveData = new MutableLiveData<>();

    @Bindable
    public MutableLiveData<BodyInformation> getBodyInformationMutableLiveData() {
        return bodyInformationMutableLiveData;
    }

    public void setBodyInformationMutableLiveData(BodyInformation bodyInformation) {
        bodyInformationMutableLiveData.setValue(bodyInformation);
        registry.notifyChange(this, BR.bodyInformationMutableLiveData);
    }

    public MoreViewModel(@NonNull Application application) {
        super(application);
    }

    @Bindable
    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }


    public void setUserMutableLiveData(User user) {
        this.userMutableLiveData.setValue(user);
        registry.notifyChange(this, BR.userMutableLiveData);
    }

    public void setListenerUser(User user) {
        moreDatabaseManager.listenUserChange(user);
    }

    public void setListenerBodyInformation(User user) {
        moreDatabaseManager.listenerBodyInformation(user);
    }

    public void updateStatusUser(User user) {
        moreDatabaseManager.updateStatusUser(user);
    }

    @Override
    public void onUserChange(User user) {
        setUserMutableLiveData(user);
        Log.i(TAG, "onUserChange: "+user.toString());
        mSessionManager.createSignIn(user);
    }

    @Override
    public void onUpdateLastBodyInformation(BodyInformation bodyInformation) {
        setBodyInformationMutableLiveData(bodyInformation);
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
