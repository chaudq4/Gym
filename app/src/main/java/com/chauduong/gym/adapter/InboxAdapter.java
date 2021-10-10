package com.chauduong.gym.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chauduong.gym.R;
import com.chauduong.gym.databinding.ItemInboxFromBinding;
import com.chauduong.gym.databinding.ItemInboxToBinding;
import com.chauduong.gym.databinding.ItemInboxTypingBinding;
import com.chauduong.gym.databinding.ItemLoadingMoreBinding;
import com.chauduong.gym.manager.session.SessionManager;
import com.chauduong.gym.model.Inbox;

import java.util.List;

public class InboxAdapter extends RecyclerView.Adapter {
    private static final String TAG = "InboxAdapter";
    List<Inbox> inboxList;
    Context mContext;
    static final int VIEW_FROM = 0;
    static final int VIEW_TO = 1;
    static final int VIEW_TYPING = -1;
    static final int VIEW_LOADING = 2;
    LoadMoreListener mLoadMoreListener;
    boolean isLoading;
    int visibleThreshold = 10;
    int firstVisibleItem, totalItemCount;
    RecyclerView recyclerView;


    public InboxAdapter(List<Inbox> inboxList, Context mContext, RecyclerView recyclerView) {
        this.inboxList = inboxList;
        this.mContext = mContext;
        this.recyclerView = recyclerView;
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                firstVisibleItem = linearLayoutManager.findFirstVisibleItemPosition();
                Log.i(TAG, "onScrollStateChanged: " + firstVisibleItem + " " + newState);
                if (!isLoading && firstVisibleItem == 0 && newState == 1) {
                    if (mLoadMoreListener != null)
                        mLoadMoreListener.onLoadMore();
                    isLoading = true;
                }
            }
        });
    }

    public InboxAdapter(Context mContext, List<Inbox> inboxList) {
        this.mContext = mContext;
        this.inboxList = inboxList;
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
        if (viewType == VIEW_LOADING) {
            ItemLoadingMoreBinding itemLoadingMoreBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_loading_more, parent, false);
            return new LoadingMoreViewHolder(itemLoadingMoreBinding);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Inbox inbox = inboxList.get(position);
        if (holder instanceof InboxFromViewHolder) {
            ((InboxFromViewHolder) holder).mItemInboxFromBinding.txtMsg.setText(inbox.getMsg());
            ((InboxFromViewHolder) holder).mItemInboxFromBinding.txtTime.setText(inbox.getTime());

        }
        if (holder instanceof InboxToViewHolder) {
            ((InboxToViewHolder) holder).mItemInboxToBinding.txtMsg.setText(inbox.getMsg());
            ((InboxToViewHolder) holder).mItemInboxToBinding.txtTime.setText(inbox.getTime());
        }
        if (holder instanceof LoadingMoreViewHolder) {
            ((LoadingMoreViewHolder) holder).mItemLoadingMoreBinding.progressBar.setIndeterminate(true);
        }
    }

    public void setmLoadMoreListener(LoadMoreListener mLoadMoreListener) {
        this.mLoadMoreListener = mLoadMoreListener;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    @Override
    public int getItemCount() {
        return inboxList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (inboxList.get(position) == null) return VIEW_LOADING;
        if (inboxList != null) {
            if (inboxList.get(position).getFrom().getPhoneNumber().equalsIgnoreCase(new SessionManager(mContext).getUser().getPhoneNumber())) {
                return VIEW_FROM;
            } else {
                return VIEW_TO;
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

class LoadingMoreViewHolder extends RecyclerView.ViewHolder {
    ItemLoadingMoreBinding mItemLoadingMoreBinding;

    public LoadingMoreViewHolder(@NonNull ItemLoadingMoreBinding mItemLoadingMoreBinding) {
        super(mItemLoadingMoreBinding.getRoot());
        this.mItemLoadingMoreBinding = mItemLoadingMoreBinding;

    }
}

