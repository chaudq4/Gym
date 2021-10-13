package com.chauduong.gym.inbox;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.chauduong.gym.R;
import com.chauduong.gym.databinding.ActivityConversationBinding;
import com.chauduong.gym.model.Inbox;
import com.chauduong.gym.model.User;
import com.chauduong.gym.utils.DataBindingAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class InboxActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher, Observer<Inbox>, SwipeRefreshLayout.OnRefreshListener {
    public static final String USER = "key_user";
    private static final String TAG = "ConversationActivity";
    ActivityConversationBinding mActivityConversationBinding;
    User toUser;
    InboxAdapter mInboxAdapter;
    List<Inbox> inboxList;
    List<Inbox> newList;
    InboxViewModel inboxViewModel;
    boolean isScrollEnd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityConversationBinding = DataBindingUtil.setContentView(this, R.layout.activity_conversation);
        initData();
        initView();
        isScrollEnd = true;
        initViewModel();
        receiveDataFromViewModel();
        mActivityConversationBinding.setViewModel(inboxViewModel);

    }

    private void receiveDataFromViewModel() {
        inboxViewModel.getAllInbox(toUser);
        if (newList == null) newList = new ArrayList<>();
        else
            newList.clear();
    }

    private void initViewModel() {
        inboxViewModel = ViewModelProviders.of(this).get(InboxViewModel.class);
        inboxViewModel.init(this);
        inboxViewModel.getmListMutableLiveDataInbox().observe(this, this);
        inboxViewModel.getIsNewInbox().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isScrollEnd = true;
                mActivityConversationBinding.rvListInbox.smoothScrollToPosition(mInboxAdapter.getInboxList().size() - 1);
            }
        });
        inboxViewModel.listenToUserChange(toUser);

    }

    private void initView() {
        mActivityConversationBinding.btnBack.setOnClickListener(this);
        mActivityConversationBinding.btnSend.setOnClickListener(this);
        mActivityConversationBinding.swLayout.setOnRefreshListener(this);
        mActivityConversationBinding.edtMsg.addTextChangedListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mActivityConversationBinding.rvListInbox.setLayoutManager(linearLayoutManager);
        mInboxAdapter = new InboxAdapter(this, inboxList);
        mActivityConversationBinding.rvListInbox.setAdapter(mInboxAdapter);
        mActivityConversationBinding.swLayout.setColorSchemeColors(getColor(R.color.colorPrimary));
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
            default:
                break;
        }
    }

    private void sentInbox() {
        String urlImage = "";
        inboxViewModel.sentInbox(urlImage);
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
            inboxViewModel.setFavorite(true);
        } else {
            inboxViewModel.setFavorite(false);
        }
        if (mActivityConversationBinding.edtMsg.getLineCount() >= 1 && mActivityConversationBinding.edtMsg.getLineCount() < 7) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mActivityConversationBinding.layoutMsg.getLayoutParams();
            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            layoutParams.height = getResources().getDimensionPixelSize(R.dimen.bottom_conversation_height) + (getResources().getDimensionPixelSize(R.dimen.bottom_conversation_height) / 2 * (mActivityConversationBinding.edtMsg.getLineCount() - 1));
            int margin = getResources().getDimensionPixelSize(R.dimen.margin_edt_msg);
            int padding = getResources().getDimensionPixelSize(R.dimen.padding_edt_msg);
            layoutParams.setMargins(margin, margin, margin, margin);
            mActivityConversationBinding.layoutMsg.setPadding(padding, padding, padding, padding);
            mActivityConversationBinding.layoutMsg.setLayoutParams(layoutParams);
        }

    }

    public void loadMore() {
        Log.i(TAG, "onLoadMore: ");
        isScrollEnd = false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                receiveDataFromViewModel();
                mActivityConversationBinding.swLayout.setRefreshing(false);
            }
        }, 1000);

    }

    @Override
    public void onChanged(Inbox inbox) {
        newList.add(inbox);
        mInboxAdapter.setInboxList(newList);
        if (!isScrollEnd)
            mActivityConversationBinding.rvListInbox.scrollToPosition(0);
        else
            mActivityConversationBinding.rvListInbox.smoothScrollToPosition(mInboxAdapter.getInboxList().size() - 1);
    }

    @Override
    public void onRefresh() {
        isScrollEnd = false;
        mActivityConversationBinding.swLayout.setRefreshing(true);
        loadMore();
    }
}
