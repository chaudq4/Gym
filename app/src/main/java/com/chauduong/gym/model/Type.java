package com.chauduong.gym.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.Observable;

import com.chauduong.gym.BR;

import java.io.Serializable;
import java.util.List;

public class Type extends BaseObservable implements Serializable {
    private String id;
    private String urlIcon;
    private String name;
    private List<Exercise> mExerciseList;

    public Type(String urlIcon, String name) {
        this.urlIcon = urlIcon;
        this.name = name;
    }

    public Type() {
    }

    @Bindable
    public String getUrlIcon() {
        return urlIcon;
    }

    public void setUrlIcon(String urlIcon) {
        this.urlIcon = urlIcon;
        notifyPropertyChanged(BR.urlIcon);
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
    public List<Exercise> getmExerciseList() {
        return mExerciseList;
    }

    public void setmExerciseList(List<Exercise> mExerciseList) {
        this.mExerciseList = mExerciseList;
        notifyPropertyChanged(BR.mExerciseList);
    }

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Override
    public String toString() {
        return "Type{" +
                "id='" + id + '\'' +
                ", icon=" + urlIcon +
                ", name='" + name + '\'' +
                ", mExerciseList=" + mExerciseList +
                '}';
    }

}
