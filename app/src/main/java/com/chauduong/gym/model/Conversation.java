package com.chauduong.gym.model;

import java.io.Serializable;
import java.util.List;

public class Conversation implements Serializable {
    String id;
    User toUser;
    List<Inbox> inboxList;

    public Conversation() {
    }

    @Override
    public String toString() {
        return "Conversation{" +
                "id='" + id + '\'' +
                ", toUser=" + toUser +
                ", inboxList=" + inboxList +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getToUser() {
        return toUser;
    }

    public void setToUser(User toUser) {
        this.toUser = toUser;
    }

    public List<Inbox> getInboxList() {
        return inboxList;
    }

    public void setInboxList(List<Inbox> inboxList) {
        this.inboxList = inboxList;
    }
}
