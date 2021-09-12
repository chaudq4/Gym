package com.chauduong.gym.model;

import java.io.Serializable;

public class Type implements Serializable {
    private int icon;
    private String name;

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
}
