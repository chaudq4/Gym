package com.chauduong.gym.ui;

import com.chauduong.gym.model.User;

public interface AccountManagerListener {
    void onUserChange(User user);

    void onChangePassSuccess(String newPass);

    void onChangePassFail();

    void onUploadImageAccountSuccess(String url);

    void onUpdateImageSuccess();

    void onUploadImageAccountFail();

    void onProgressUpload(double progress);

    void onUpdateInformationAccountSuccess();
    void onUpdateInformationAccountFail();

}
