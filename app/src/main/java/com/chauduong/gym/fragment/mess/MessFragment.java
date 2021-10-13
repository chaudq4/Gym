package com.chauduong.gym.fragment.mess;


import android.content.Intent;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chauduong.gym.R;
import com.chauduong.gym.inbox.InboxActivity;
import com.chauduong.gym.adapter.ContactAdapter;
import com.chauduong.gym.adapter.ContactListener;
import com.chauduong.gym.databinding.FragmentMessBinding;
import com.chauduong.gym.manager.dialog.DialogManager;
import com.chauduong.gym.model.User;
import com.chauduong.gym.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class MessFragment extends Fragment implements ContactListener {
    private static final String TAG = "MessFragment";
    public static final String USER = "key_user";
    FragmentMessBinding mFragmentMessBinding;
    ContactAdapter mContactAdapter;
    List<User> contactList;
    private MessViewModel messViewModel;

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
        initViewModel();
        initData();
    }

    private void initData() {
        Util.setVisibilityView(mFragmentMessBinding.pbContact, true);
        messViewModel.getAllUserForChar();
    }

    private void initViewModel() {
        messViewModel = ViewModelProviders.of(getActivity()).get(MessViewModel.class);
        messViewModel.getListUserForChar().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> userList) {
                mContactAdapter.getUserList().clear();
                mContactAdapter.getUserList().addAll(userList);
                mContactAdapter.notifyDataSetChanged();
                Util.setVisibilityView(mFragmentMessBinding.pbContact, false);
            }
        });
        messViewModel.getMsgGetAllUserError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                DialogManager.getInstance(getContext()).dissmissProgressDialog();
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

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
    public void onContactClick(User user) {
        Intent intent = new Intent(getContext(), InboxActivity.class);
        intent.putExtra(USER, user);
        startActivity(intent);
    }

    @Override
    public void onContactTouch(User user) {

    }

}