package com.chauduong.gym.fragment.personal;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.chauduong.gym.model.BodyInformation;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PersonalViewModel extends AndroidViewModel implements PersonalManagerListener {
    private PersonalManager personalManager;
    private MutableLiveData<Boolean> isAddSuccess = new MutableLiveData<>();
    private MutableLiveData<List<BodyInformation>> listBodyInformation = new MutableLiveData<>();

    public MutableLiveData<List<BodyInformation>> getListBodyInformation() {
        return listBodyInformation;
    }

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

    public void getAllBodyInformation() {
        if (personalManager != null) {
            personalManager.getAllBodyInformation();
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

    @Override
    public void onGetAllBodyInformationSuccess(List<BodyInformation> bodyInformations) {
        Collections.sort(bodyInformations);
        listBodyInformation.setValue(bodyInformations);
    }

    @Override
    public void onGetAllBodyInformationError(String message) {

    }

    @Override
    public void onSearchBodyInformationSuccess(List<BodyInformation> bodyInformations) {
        Collections.sort(bodyInformations);
        listBodyInformation.setValue(bodyInformations);
    }

    @Override
    public void onSearchBodyInformationError(String message) {

    }

    public void searchBodyInformation(long millisFromDate, long millisToDate) {
        if (personalManager != null) {
            personalManager.searchBodyInformation(millisFromDate, millisToDate);
        }
    }
}
