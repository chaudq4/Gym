package com.chauduong.gym.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.chauduong.gym.model.User;

import java.net.IDN;

public class SessionManager {
    private Context mContext;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "GymGC";

    // All Shared Preferences Keys
    private static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    private static final String KEY_ID = "id";
    private static final String KEY_PHONE_NUMBER = "phoneNumber";
    private static final String KEY_PASSWORD = "password";

    private static final String KEY_LOGIN_PHONE = "keyLoginPhone";
    private static final String KEY_LOGIN_PASSWORD = "keyLoginPassword";
    private static final String KEY_LOGIN_ISREMEMBER = "keyLoginIsRemember";

    public SessionManager(Context mContext) {
        this.mContext = mContext;
        mSharedPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        mEditor = mSharedPreferences.edit();
    }

    public boolean isSignIn() {
        return mSharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);

    }

    public User getUser() {
        User user = new User();
        String id = mSharedPreferences.getString(KEY_ID, null);
        String phone = mSharedPreferences.getString(KEY_PHONE_NUMBER, null);
        String pass = mSharedPreferences.getString(KEY_PASSWORD, null);
        user.setPhoneNumber(phone);
        user.setId(id);
        user.setPassword(pass);
        return user;
    }

    public void createSignIn(User user) {
        mEditor.putString(KEY_ID, user.getId());
        mEditor.putString(KEY_PHONE_NUMBER, user.getPhoneNumber());
        mEditor.putString(KEY_PASSWORD, user.getPassword());
        mEditor.putBoolean(KEY_IS_LOGGED_IN, true);
        mEditor.commit();
    }

    public void clearSession() {
        mEditor.remove(KEY_ID);
        mEditor.remove(KEY_PHONE_NUMBER);
        mEditor.remove(KEY_PASSWORD);
        mEditor.remove(KEY_IS_LOGGED_IN);
        mEditor.commit();
    }

    public void setIsRemember(boolean isRemember){
        mEditor.putBoolean(KEY_LOGIN_ISREMEMBER, isRemember);
        mEditor.commit();
    }
    public void putRememberLogin(String phoneNumber, String password, boolean isRemember) {
        mEditor.putString(KEY_LOGIN_PHONE, phoneNumber);
        mEditor.putString(KEY_LOGIN_PASSWORD, password);
        mEditor.putBoolean(KEY_LOGIN_ISREMEMBER, isRemember);
        mEditor.commit();
    }

    public boolean isRememberLogin() {
        return mSharedPreferences.getBoolean(KEY_LOGIN_ISREMEMBER, false);
    }

    public String getLoginPhoneNumber() {
        return mSharedPreferences.getString(KEY_LOGIN_PHONE, null);
    }

    public String getLoginPassWord() {
        return mSharedPreferences.getString(KEY_LOGIN_PASSWORD, null);
    }

}
