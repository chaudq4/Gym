package com.chauduong.gym.adapter;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.chauduong.gym.model.Inbox;

import java.util.List;

public class MyDiffUtilCallBack extends DiffUtil.Callback {
    List<Inbox> newInbox;
    List<Inbox> oldInbox;

    public MyDiffUtilCallBack(List<Inbox> newInbox, List<Inbox> oldInbox) {
        this.newInbox = newInbox;
        this.oldInbox = oldInbox;
    }



    @Override
    public int getOldListSize() {
        return oldInbox==null?0 :oldInbox.size();
    }

    @Override
    public int getNewListSize() {
        return newInbox==null?0 :newInbox.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return newInbox.get(newItemPosition).getId() == oldInbox.get(oldItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        int result = newInbox.get(newItemPosition).compareTo(oldInbox.get(oldItemPosition));
        return result==0;
    }
}
