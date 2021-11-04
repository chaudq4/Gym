package com.chauduong.gym.fragment.bodyinformation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.chauduong.gym.fragment.personal.PersonalManager;
import com.chauduong.gym.fragment.personal.PersonalManagerListener;
import com.chauduong.gym.model.BodyInformation;

import java.util.Collections;
import java.util.List;

public class BodyInformationViewModel extends AndroidViewModel implements PersonalManagerListener {

    private MutableLiveData<List<BodyInformation>> bodyListMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isUpdateSuccess = new MutableLiveData<>();
    private PersonalManager personalManager;

    public BodyInformationViewModel(@NonNull Application application) {
        super(application);
        personalManager = new PersonalManager(getApplication(), this);
    }

    public MutableLiveData<Boolean> getIsUpdateSuccess() {
        return isUpdateSuccess;
    }

    public MutableLiveData<List<BodyInformation>> getBodyListMutableLiveData() {
        return bodyListMutableLiveData;
    }

    public void getAllBodyInformation() {
        if (personalManager != null) {
            personalManager.getAllBodyInformation();
        }
    }

    @Override
    public void onAddBodyInformationSuccess() {

    }

    @Override
    public void onAddBodyInformationFail() {

    }

    @Override
    public void onGetAllBodyInformationSuccess(List<BodyInformation> bodyInformations) {
        Collections.sort(bodyInformations);
        bodyListMutableLiveData.setValue(bodyInformations);
    }

    @Override
    public void onGetAllBodyInformationError(String message) {

    }

    @Override
    public void onSearchBodyInformationSuccess(List<BodyInformation> bodyInformations) {

    }

    @Override
    public void onSearchBodyInformationError(String message) {

    }

    @Override
    public void onUpdateBodySuccess() {
        isUpdateSuccess.setValue(true);
    }

    @Override
    public void onUpdateBodyError(String msg) {
        isUpdateSuccess.setValue(false);
    }

    public void updateBodyInformation(BodyInformation bodyInformation) {
        personalManager.updateBodyInformation(bodyInformation);
    }

    public void deleteBodyInformation(BodyInformation body) {
        personalManager.deleteBodyInformation(body);
    }
}
