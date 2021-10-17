package com.chauduong.gym.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.chauduong.gym.R;
import com.chauduong.gym.databinding.ItemConversationBinding;
import com.chauduong.gym.model.Conversation;
import com.chauduong.gym.utils.DataBindingAdapter;

import java.util.List;

public class ConversationApdater extends RecyclerView.Adapter<ConversationViewHolder> {
    private Context mContext;
    private List<Conversation> conversationList;
    private ConversationListener mConversationListener;

    public ConversationApdater(Context mContext, List<Conversation> conversationList, ConversationListener mConversationListener) {
        this.mContext = mContext;
        this.conversationList = conversationList;
        this.mConversationListener = mConversationListener;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemConversationBinding itemConversationBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_conversation, parent, false);
        return new ConversationViewHolder(itemConversationBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        Conversation conversation = conversationList.get(position);
        holder.itemConversationBinding.setConversation(conversation);
    }

    public List<Conversation> getConversationList() {
        return conversationList;
    }

    @Override
    public int getItemCount() {
        return conversationList == null ? 0 : conversationList.size();
    }
}

class ConversationViewHolder extends RecyclerView.ViewHolder {
    ItemConversationBinding itemConversationBinding;

    public ConversationViewHolder(ItemConversationBinding itemConversationBinding) {
        super(itemConversationBinding.getRoot());
        this.itemConversationBinding = itemConversationBinding;
    }

}

