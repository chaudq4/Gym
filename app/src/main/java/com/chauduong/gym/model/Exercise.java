package com.chauduong.gym.model;

import java.io.Serializable;

public class Exercise implements Serializable {
    private String name;
    private String main;
    private String other;
    private String level;
    private String type;
    private String force;
    private String equipment;
    private String instruction;

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

    public Exercise(String name, String main, String other, String level, String type, String force, String equipment, String instruction) {
        this.name = name;
        this.main = main;
        this.other = other;
        this.level = level;
        this.type = type;
        this.force = force;
        this.equipment = equipment;
        this.instruction = instruction;
    }
}
