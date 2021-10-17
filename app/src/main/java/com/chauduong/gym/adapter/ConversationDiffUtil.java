package com.chauduong.gym.adapter;

import androidx.recyclerview.widget.DiffUtil;

import com.chauduong.gym.model.Conversation;
import com.chauduong.gym.model.Inbox;

import java.util.List;

//public class ConversationDiffUtil extends DiffUtil.Callback {
//    List<Conversation> newConversation;
//    List<Conversation> oldConversation;
//
//    public ConversationDiffUtil(List<Conversation> newConversation, List<Conversation> oldConversation) {
//        this.newConversation = newConversation;
//        this.oldConversation = oldConversation;
//    }
//
//
//    @Override
//    public int getOldListSize() {
//        return oldConversation == null ? 0 : oldConversation.size();
//    }
//
//    @Override
//    public int getNewListSize() {
//        return newConversation == null ? 0 : newConversation.size();
//    }
//
//    @Override
//    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
//        if (newConversation.get(newItemPosition).getLastMsg() == null || oldConversation.get(oldItemPosition).getLastMsg() == null)
//            return false;
//        return newConversation.get(newItemPosition).getLastMsg().equalsIgnoreCase(oldConversation.get(oldItemPosition).getLastMsg());
//    }
//
//    @Override
//    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
//        if (newConversation.get(newItemPosition).getLastMsg() == null || oldConversation.get(oldItemPosition).getLastMsg() == null)
//            return false;
//        return newConversation.get(newItemPosition).getLastMsg().equalsIgnoreCase(oldConversation.get(oldItemPosition).getLastMsg());
//    }
//}

