package com.chauduong.gym.fragment.mess;


import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chauduong.gym.R;
import com.chauduong.gym.adapter.ContactAdapter;
import com.chauduong.gym.adapter.ContactListener;
import com.chauduong.gym.databinding.FragmentMessBinding;
import com.chauduong.gym.manager.dialog.DialogManager;
import com.chauduong.gym.model.User;
import com.chauduong.gym.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class MessFragment extends Fragment implements MessPresenterListener, ContactListener {
    private static final String TAG = "MessFragment";
    FragmentMessBinding mFragmentMessBinding;
    ContactAdapter mContactAdapter;
    List<User> contactList;
    private MessPresenter mMessPresenter;

    public MessFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentMessBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_mess, container, false);
        return mFragmentMessBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initPresenter();
        initData();
    }

    private void initData() {
        Util.setVisibilityView(mFragmentMessBinding.pbContact, true);
        mMessPresenter.getAllListUser();
    }

    private void initPresenter() {
        if (mMessPresenter == null) {
            mMessPresenter = new MessPresenter(getContext(), this);
        }
    }

    private void initView() {
        contactList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mContactAdapter = new ContactAdapter(getContext(), contactList);
        mContactAdapter.setmContactListener(this);
        mFragmentMessBinding.rvListAccount.setAdapter(mContactAdapter);
        mFragmentMessBinding.rvListAccount.setLayoutManager(linearLayoutManager);
        mFragmentMessBinding.svContact.setIconifiedByDefault(false);
        mFragmentMessBinding.svContact.setQueryHint(getString(R.string.search));
    }

    @Override
    public void onGetAllUserSuccess(List<User> userList) {
        mContactAdapter.getUserList().clear();
        mContactAdapter.getUserList().addAll(userList);
        mContactAdapter.notifyDataSetChanged();
        Util.setVisibilityView(mFragmentMessBinding.pbContact, false);
    }

    @Override
    public void onGetAllUserError(String msg) {
        DialogManager.getInstance(getContext()).dissmissProgressDialog();
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onContactClick(User user) {
        Toast.makeText(getContext(), user.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onContactTouch(User user) {

    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart: ");
        super.onStart();
    }

    @Override
    public void onStop() {
        Log.i(TAG, "onStop: ");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy: ");
        super.onDestroy();
    }

}