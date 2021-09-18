package com.chauduong.gym.model;

import java.io.Serializable;
import java.util.List;

public class Type implements Serializable {
    private String id;
    private int icon;
    private String name;
    private List<Exercise> mExerciseList;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type(int icon, String name) {
        this.icon = icon;
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
                ", icon=" + icon +
                ", name='" + name + '\'' +
                ", mExerciseList=" + mExerciseList +
                '}';
    }
}
