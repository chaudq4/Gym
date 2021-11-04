package com.chauduong.gym.fragment.personal;

import com.chauduong.gym.model.BodyInformation;

import java.util.List;

public interface PersonalManagerListener {
    void onAddBodyInformationSuccess();

    void onAddBodyInformationFail();

    void onGetAllBodyInformationSuccess(List<BodyInformation> bodyInformations);

    void onGetAllBodyInformationError(String message);

    void onSearchBodyInformationSuccess(List<BodyInformation> bodyInformations);

    void onSearchBodyInformationError(String message);

    void onUpdateBodySuccess();
    void onUpdateBodyError(String msg);
}
