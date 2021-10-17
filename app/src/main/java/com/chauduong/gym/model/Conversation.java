package com.chauduong.gym.model;


import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.chauduong.gym.BR;

import java.io.Serializable;
import java.util.List;
import java.util.Observable;

public class Conversation extends BaseObservable implements Serializable, Comparable {
    private String id;
    private Inbox inbox;

    @Override
    public String toString() {
        return "Conversation{" +
                "id='" + id + '\'' +
                ", inbox=" + inbox +
                '}';
    }

    @Bindable
    public Inbox getInbox() {
        return inbox;
    }

    public void setInbox(Inbox inbox) {
        this.inbox = inbox;
        notifyPropertyChanged(BR.inbox);
    }

    public Conversation() {
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
    public int compareTo(Object o) {
        Conversation conversation = (Conversation) o;
//        if (conversation.inbox.getTime().)
        return 0;
    }
}
