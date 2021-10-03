package com.chauduong.gym.model;

import java.io.Serializable;
import java.util.List;

public class Type implements Serializable {
    private String id;
    private String urlIcon;
    private String name;
    private List<Exercise> mExerciseList;

    public String getUrlIcon() {
        return urlIcon;
    }

    public void setUrlIcon(String urlIcon) {
        this.urlIcon = urlIcon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type(String urlIcon, String name) {
        this.urlIcon = urlIcon;
        this.name = name;
    }

    public Type() {
    }

    public List<Exercise> getmExerciseList() {
        return mExerciseList;
    }

    public void setmExerciseList(List<Exercise> mExerciseList) {
        this.mExerciseList = mExerciseList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
