package com.chauduong.gym.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chauduong.gym.R;
import com.chauduong.gym.adapter.InboxAdapter;
import com.chauduong.gym.adapter.LoadMoreListener;
import com.chauduong.gym.databinding.ActivityConversationBinding;
import com.chauduong.gym.model.Conversation;
import com.chauduong.gym.model.Inbox;
import com.chauduong.gym.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConversationActivity extends AppCompatActivity implements View.OnClickListener, ConversationPresenterListener, TextWatcher, LoadMoreListener {
    public static final String USER = "key_user";
    private static final String TAG = "ConversationActivity";
    ActivityConversationBinding mActivityConversationBinding;
    User toUser;
    InboxAdapter mInboxAdapter;
    ConversationPresenter mConversationPresenter;
    List<Inbox> inboxList;
    List<Inbox> newList;
    boolean isScrollEnd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityConversationBinding = DataBindingUtil.setContentView(this, R.layout.activity_conversation);
        initData();
        initPresenter();
        initView();
        isScrollEnd = true;
        receiveDataFromDatabase();

    }

    private void receiveDataFromDatabase() {
        if (mConversationPresenter != null && toUser != null) {
            mConversationPresenter.getAllInbox(toUser);
            if (newList == null) newList = new ArrayList<>();
            else
                newList.clear();
        }
    }

    private void initPresenter() {
        mConversationPresenter = new ConversationPresenter(this, this);
    }

    private void initView() {
        mActivityConversationBinding.txtName.setText(toUser.getName());
        mActivityConversationBinding.txtStatus.setText(toUser.isOnline() ? getString(R.string.online) : getString(R.string.offline));
        mActivityConversationBinding.btnBack.setOnClickListener(this);
        mActivityConversationBinding.btnSend.setOnClickListener(this);
        mActivityConversationBinding.btnLoad.setOnClickListener(this);
        mActivityConversationBinding.edtMsg.addTextChangedListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mActivityConversationBinding.rvListInbox.setLayoutManager(linearLayoutManager);
        mInboxAdapter = new InboxAdapter(inboxList, this, mActivityConversationBinding.rvListInbox);
        mActivityConversationBinding.rvListInbox.setAdapter(mInboxAdapter);
        mInboxAdapter.setmLoadMoreListener(this);
    }


    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            toUser = (User) intent.getSerializableExtra(USER);
        }
        inboxList = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnSend:
                sentInbox();
                break;
            case R.id.btnLoad:
                isScrollEnd = false;
                receiveDataFromDatabase();
            default:
                break;
        }
    }

    private void sentInbox() {
        if (mActivityConversationBinding.edtMsg.getText() == null || mActivityConversationBinding.edtMsg.getText().toString().length() == 0) {

        } else {
            String msg = mActivityConversationBinding.edtMsg.getText().toString();
            String time = new Date().toString();
            mConversationPresenter.sentInbox(msg, "", time, toUser);
            mActivityConversationBinding.edtMsg.setText("");
        }
    }

    @Override
    public void onGetAllConversation(List<Conversation> conversations) {

    }

    @Override
    public void onGetItemInbox(Inbox inbox) {
        if (mInboxAdapter.getInboxList().size() != 0 && mInboxAdapter.getInboxList().get(0) == null) {
            mInboxAdapter.getInboxList().remove(0);
            mInboxAdapter.notifyItemRemoved(0);
        }
        newList.add(inbox);
        Log.i(TAG, "onGetItemInbox: " + inbox.getId() + newList.size());
        mInboxAdapter.setInboxList(newList);
        if (!isScrollEnd)
            mActivityConversationBinding.rvListInbox.scrollToPosition(0);
        else
            mActivityConversationBinding.rvListInbox.smoothScrollToPosition(mInboxAdapter.getInboxList().size() - 1);

    }

    @Override
    public void onGetAllItemInbox(List<Inbox> inboxList) {
        mInboxAdapter.setInboxList(inboxList);
    }

    @Override
    public void onNewInbox() {
        isScrollEnd = true;
        mActivityConversationBinding.rvListInbox.smoothScrollToPosition(mInboxAdapter.getInboxList().size() - 1);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() == 0) {
            mActivityConversationBinding.imgFavorite.setImageResource(R.drawable.ic_baseline_favorite_24);
        } else {
            mActivityConversationBinding.imgFavorite.setImageResource(R.drawable.ic_baseline_send_24);
        }
    }

    @Override
    public void onLoadMore() {
        Log.i(TAG, "onLoadMore: ");
        isScrollEnd = false;
        mInboxAdapter.getInboxList().add(0, null);
        mInboxAdapter.notifyItemInserted(0);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                receiveDataFromDatabase();
                mInboxAdapter.setLoading(false);
            }
        }, 1000);

    }
}
