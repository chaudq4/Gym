package com.chauduong.gym.fragment.personal;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.chauduong.gym.R;
import com.chauduong.gym.model.BodyInformation;
import com.chauduong.gym.utils.Util;
import com.github.mikephil.charting.charts.Chart;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PersonalViewModel extends AndroidViewModel implements PersonalManagerListener, SaveChartListener {
    private PersonalManager personalManager;
    private MutableLiveData<Boolean> isAddSuccess = new MutableLiveData<>();
    private MutableLiveData<List<BodyInformation>> listBodyInformation = new MutableLiveData<>();
    private boolean resultSave;
    private int countChartSave;
    private int sizeListChartRequest;
    private MutableLiveData<Boolean> isSaveSuccess = new MutableLiveData<>();

    public MutableLiveData<Boolean> getIsSaveSuccess() {
        return isSaveSuccess;
    }

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

    @Override
    public void onUpdateBodySuccess() {

    }

    @Override
    public void onUpdateBodyError(String msg) {

    }

    public void searchBodyInformation(long millisFromDate, long millisToDate) {
        if (personalManager != null) {
            personalManager.searchBodyInformation(millisFromDate, millisToDate);
        }
    }

    public void saveChartGallery(List<Chart> chartList) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        sizeListChartRequest = chartList.size();
        resultSave = true;
        countChartSave = 0;
        for (Chart chart : chartList) {
            executorService.execute(new SaveChartThread(chart, this));
        }
        executorService.shutdown();
    }

    @Override
    public void onComplete(boolean result) {
        countChartSave++;
        resultSave = resultSave && result;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if (countChartSave == sizeListChartRequest && resultSave) {
                    isSaveSuccess.setValue(true);
                }
                if (countChartSave == sizeListChartRequest && !resultSave) {
                    isSaveSuccess.setValue(false);
                }
            }
        });


    }

}
