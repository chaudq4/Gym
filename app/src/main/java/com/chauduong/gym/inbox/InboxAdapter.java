package com.chauduong.gym.inbox;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.chauduong.gym.R;
import com.chauduong.gym.databinding.ItemImageFromBinding;
import com.chauduong.gym.databinding.ItemImageToBinding;
import com.chauduong.gym.databinding.ItemInboxFromBinding;
import com.chauduong.gym.databinding.ItemInboxToBinding;
import com.chauduong.gym.databinding.ItemInboxTypingBinding;
import com.chauduong.gym.manager.session.SessionManager;
import com.chauduong.gym.model.Inbox;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class InboxAdapter extends RecyclerView.Adapter {
    private static final String TAG = "InboxAdapter";
    List<Inbox> inboxList;
    Context mContext;
    private InboxListener mInboxListener;
    static final int VIEW_FROM = 0;
    static final int VIEW_TO = 1;
    static final int IMAGE_FROM = 2;
    static final int IMAGE_TO = 3;
    static final int VIEW_TYPING = -1;

    public InboxAdapter(Context mContext, List<Inbox> inboxList, InboxListener mInboxListener) {
        this.mContext = mContext;
        this.inboxList = inboxList;
        this.mInboxListener = mInboxListener;
    }

    public List<Inbox> getInboxList() {
        return inboxList;
    }

    public void setInboxList(List<Inbox> inboxList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MyDiffUtilCallBack(inboxList, this.inboxList));
        diffResult.dispatchUpdatesTo(this);
        this.inboxList.clear();
        this.inboxList.addAll(inboxList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_FROM) {
            ItemInboxFromBinding mItemInboxFromBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_inbox_from, parent, false);
            return new InboxFromViewHolder(mItemInboxFromBinding);
        }
        if (viewType == VIEW_TO) {
            ItemInboxToBinding itemInboxToBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_inbox_to, parent, false);
            return new InboxToViewHolder(itemInboxToBinding);
        }
        if (viewType == VIEW_TYPING) {
            ItemInboxTypingBinding mItemTypingBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_inbox_typing, parent, false);
            return new TypingViewHolder(mItemTypingBinding);
        }
        if (viewType == IMAGE_FROM) {
            ItemImageFromBinding itemImageFromBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_image_from, parent, false);
            return new ImageViewHolderFrom(itemImageFromBinding);
        }
        if (viewType == IMAGE_TO) {
            ItemImageToBinding itemImageToBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_image_to, parent, false);
            return new ImageViewHolderTo(itemImageToBinding);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Inbox inbox = inboxList.get(position);
        if (holder instanceof InboxFromViewHolder) {
            ((InboxFromViewHolder) holder).mItemInboxFromBinding.setInbox(inbox);
        }
        if (holder instanceof InboxToViewHolder) {
            ((InboxToViewHolder) holder).mItemInboxToBinding.setInbox(inbox);
        }
        if (holder instanceof ImageViewHolderFrom) {
            ((ImageViewHolderFrom) holder).itemImageFromBinding.setInbox(inbox);
        }
        if (holder instanceof ImageViewHolderTo) {
            ((ImageViewHolderTo) holder).itemImageToBinding.setInbox(inbox);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInboxListener.onInboxClick(inbox);
            }
        });
    }


    @Override
    public int getItemCount() {
        return inboxList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (inboxList != null) {
            if (inboxList.get(position).getFrom().getPhoneNumber().equalsIgnoreCase(new SessionManager(mContext).getUser().getPhoneNumber())) {
                if (inboxList.get(position).getLink() != null && inboxList.get(position).getLink().length() != 0) {
                    return IMAGE_FROM;
                } else {
                    return VIEW_FROM;
                }

            } else {
                if (inboxList.get(position).getLink() != null && inboxList.get(position).getLink().length() != 0) {
                    return IMAGE_TO;
                } else {
                    return VIEW_TO;
                }
            }
        }
        return VIEW_TYPING;
    }
}

class InboxFromViewHolder extends RecyclerView.ViewHolder {
    ItemInboxFromBinding mItemInboxFromBinding;

    public InboxFromViewHolder(@NonNull ItemInboxFromBinding mItemInboxFromBinding) {
        super(mItemInboxFromBinding.getRoot());
        this.mItemInboxFromBinding = mItemInboxFromBinding;

    }
}

class InboxToViewHolder extends RecyclerView.ViewHolder {
    ItemInboxToBinding mItemInboxToBinding;

    public InboxToViewHolder(@NonNull ItemInboxToBinding mItemInboxToBinding) {
        super(mItemInboxToBinding.getRoot());
        this.mItemInboxToBinding = mItemInboxToBinding;
    }
}

class TypingViewHolder extends RecyclerView.ViewHolder {
    ItemInboxTypingBinding mItemTypingBinding;

    public TypingViewHolder(@NonNull ItemInboxTypingBinding mItemTypingBinding) {
        super(mItemTypingBinding.getRoot());
        this.mItemTypingBinding = mItemTypingBinding;
    }
}

class ImageViewHolderFrom extends RecyclerView.ViewHolder {
    ItemImageFromBinding itemImageFromBinding;

    public ImageViewHolderFrom(@NonNull ItemImageFromBinding itemImageFromBinding) {
        super(itemImageFromBinding.getRoot());
        this.itemImageFromBinding = itemImageFromBinding;
    }
}

class ImageViewHolderTo extends RecyclerView.ViewHolder {
    ItemImageToBinding itemImageToBinding;

    public ImageViewHolderTo(@NonNull ItemImageToBinding itemImageToBinding) {
        super(itemImageToBinding.getRoot());
        this.itemImageToBinding = itemImageToBinding;
    }
}

interface InboxListener {
    void onInboxClick(Inbox inbox);
}


