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
    private String sex;
    private String avatar;
    private float high;
    private int weight;
    private boolean isOnline;

    @Bindable
    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
        notifyPropertyChanged(BR.high);
    }

    @Bindable
    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
        notifyPropertyChanged(BR.weight);
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
                ", sex='" + sex + '\'' +
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
    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
        notifyPropertyChanged(BR.sex);

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

    public User(String name, String phoneNumber, String password, String dob, String sex) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.dob = dob;
        this.sex = sex;
    }
}
