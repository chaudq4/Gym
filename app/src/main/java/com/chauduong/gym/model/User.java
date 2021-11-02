package com.chauduong.gym.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.chauduong.gym.BR;

import java.io.Serializable;

public class User extends BaseObservable implements Serializable {
    private String id;
    private String name;
    private String phoneNumber;
    private String password;
    private String dob;
    private boolean male;
    private String avatar;
    private String email;
    private String address;
    private boolean isOnline;


    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getAddress() {
        return address;

    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", dob='" + dob + '\'' +
                ", sex='" + male + '\'' +
                ", avatar='" + avatar + '\'' +
                ", isOnline=" + isOnline +
                '}';
    }

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        notifyPropertyChanged(BR.phoneNumber);

    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);

    }

    @Bindable
    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
        notifyPropertyChanged(BR.dob);

    }

    @Bindable
    public boolean isMale() {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
        notifyPropertyChanged(BR.male);
    }

    public User() {
        isOnline = false;
    }

    @Bindable
    public String getAvatar() {
        return avatar;

    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
        notifyPropertyChanged(BR.avatar);

    }

    public User(String name, String phoneNumber, String password, String dob, boolean sex) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.dob = dob;
        this.male = sex;
    }
}
