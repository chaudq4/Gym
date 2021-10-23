package com.chauduong.gym.fragment.personal;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.chauduong.gym.model.BodyInformation;

public class PersonalViewModel extends AndroidViewModel implements PersonalManagerListener {
    private PersonalManager personalManager;
    private MutableLiveData<Boolean> isAddSuccess = new MutableLiveData<>();

    public MutableLiveData<Boolean> getIsAddSuccess() {
        return isAddSuccess;
    }


    public PersonalViewModel(@NonNull Application application) {
        super(application);
        personalManager = new PersonalManager(getApplication(), this);
    }

    public void addBodyInformation(BodyInformation bodyInformation) {
        if (personalManager != null) {
            personalManager.addBodyInformation(bodyInformation);
        }
    }

    @Override
    public void onAddBodyInformationSuccess() {
        isAddSuccess.setValue(true);
    }

    @Override
    public void onAddBodyInformationFail() {
        isAddSuccess.setValue(false);
    }
}
