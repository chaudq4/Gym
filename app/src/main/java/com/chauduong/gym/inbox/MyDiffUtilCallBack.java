package com.chauduong.gym.inbox;

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
        return oldInbox == null ? 0 : oldInbox.size();
    }

    @Override
    public int getNewListSize() {
        return newInbox == null ? 0 : newInbox.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        if (newInbox.get(newItemPosition).getId() == null||oldInbox.get(oldItemPosition).getId()==null) return false;
        return newInbox.get(newItemPosition).getId().equalsIgnoreCase(oldInbox.get(oldItemPosition).getId());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        if (newInbox.get(newItemPosition).getId() == null||oldInbox.get(oldItemPosition).getId()==null) return false;
        return newInbox.get(newItemPosition).getId().equalsIgnoreCase(oldInbox.get(oldItemPosition).getId());
    }
}
