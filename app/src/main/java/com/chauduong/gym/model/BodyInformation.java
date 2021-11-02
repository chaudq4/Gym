package com.chauduong.gym.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.chauduong.gym.BR;

import java.io.Serializable;

public class BodyInformation extends BaseObservable implements Serializable, Comparable {
    private String id;
    private float date;
    private String height;
    private String weight;
    private String muscle;
    private String fat;
    private String protein;
    private String mineral;
    private String water;

    public BodyInformation() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Bindable
    public float getDate() {
        return date;
    }

    public void setDate(float date) {
        this.date = date;
        notifyPropertyChanged(BR.date);
    }

    @Bindable
    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
        notifyPropertyChanged(BR.height);
    }

    @Bindable
    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
        notifyPropertyChanged(BR.weight);
    }

    @Bindable
    public String getMuscle() {
        return muscle;
    }

    public void setMuscle(String muscle) {
        this.muscle = muscle;
        notifyPropertyChanged(BR.muscle);
    }

    @Bindable
    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
        notifyPropertyChanged(BR.fat);
    }

    @Bindable
    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
        notifyPropertyChanged(BR.protein);
    }

    @Bindable
    public String getMineral() {
        return mineral;
    }

    public void setMineral(String mineral) {
        this.mineral = mineral;
        notifyPropertyChanged(BR.mineral);
    }

    @Bindable
    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
        notifyPropertyChanged(BR.water);
    }

    public BodyInformation(String height, String weight, String muscle, String fat, String protein, String mineral, String water, float date) {
        this.date = date;
        this.height = height;
        this.weight = weight;
        this.muscle = muscle;
        this.fat = fat;
        this.protein = protein;
        this.mineral = mineral;
        this.water = water;
    }

    @Override
    public String toString() {
        return "BodyInformation{" +
                "id='" + id + '\'' +
                ", date='" + date + '\'' +
                ", height='" + height + '\'' +
                ", weight='" + weight + '\'' +
                ", muscle='" + muscle + '\'' +
                ", fat='" + fat + '\'' +
                ", protein='" + protein + '\'' +
                ", mineral='" + mineral + '\'' +
                ", water='" + water + '\'' +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof BodyInformation) {
            BodyInformation b = (BodyInformation) o;
            if (((BodyInformation) o).getDate() == this.date) return 0;
            if (((BodyInformation) o).getDate() > date) return -1;
            return 1;
        }
        return 0;
    }
}
