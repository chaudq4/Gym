package com.chauduong.gym.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.Observable;

import com.chauduong.gym.BR;

import java.io.Serializable;

public class Inbox extends BaseObservable implements Serializable, Comparable, Cloneable {
    private String id;
    private User from;
    private User to;
    private String msg;
    private String link;
    private long time;
    private boolean isRead;

    @Bindable
    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
        notifyPropertyChanged(BR.read);
    }

    @Bindable
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
        notifyPropertyChanged(BR.time);
    }

    public Inbox() {
    }

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public User getFrom() {
        return from;

    }

    public void setFrom(User from) {
        this.from = from;
        notifyPropertyChanged(BR.from);
    }

    @Bindable
    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
        notifyPropertyChanged(BR.to);
    }

    @Bindable
    public String getMsg() {
        return msg;

    }

    public void setMsg(String msg) {
        this.msg = msg;
        notifyPropertyChanged(BR.msg);
    }

    @Bindable
    public String  getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
        notifyPropertyChanged(BR.link);
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
