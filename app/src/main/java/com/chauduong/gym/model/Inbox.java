package com.chauduong.gym.model;

import java.io.Serializable;

public class Inbox implements Serializable, Comparable, Cloneable {
    String id;
    User from;
    User to;
    String msg;
    String link;
    String time;
    boolean isRead;

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Inbox() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    @Override
    public String toString() {
        return "Inbox{" +
                "id='" + id + '\'' +
                ", from=" + from +
                ", to=" + to +
                ", msg='" + msg + '\'' +
                ", link='" + link + '\'' +
                ", time='" + time + '\'' +
                ", isRead=" + isRead +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Inbox) {
            Inbox ib = (Inbox) o;
            if (ib.getId().equalsIgnoreCase(((Inbox) o).getId()))
                return 1;
        }
        return 0;
    }

    @Override
    public Inbox clone() {

        Inbox inbox;
        try {
            inbox = (Inbox) super.clone();

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); //should not happen
        }

        return inbox;
    }
}
