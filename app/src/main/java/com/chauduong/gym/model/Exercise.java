package com.chauduong.gym.model;

import java.io.Serializable;
import java.util.List;

public class Exercise implements Serializable {
    private String id;
    private String name;
    private String main;
    private String other;
    private String level;
    private String type;
    private String force;
    private String equipment;
    private String instruction;
    private List<String> imageUrlList;
    private String videoUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getForce() {
        return force;
    }

    public void setForce(String force) {
        this.force = force;
    }

    public String getEquipment() {
        return equipment;
    }

    public void setEquipment(String equipment) {
        this.equipment = equipment;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public Exercise() {
    }


    public List<String> getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(List<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", main='" + main + '\'' +
                ", other='" + other + '\'' +
                ", level='" + level + '\'' +
                ", type='" + type + '\'' +
                ", force='" + force + '\'' +
                ", equipment='" + equipment + '\'' +
                ", instruction='" + instruction + '\'' +
                ", imageUrlList=" + imageUrlList +
                ", videoUrl='" + videoUrl + '\'' +
                '}';
    }
}
