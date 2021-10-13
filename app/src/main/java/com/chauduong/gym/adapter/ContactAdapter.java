package com.chauduong.gym.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chauduong.gym.R;
import com.chauduong.gym.databinding.ItemContactBinding;
import com.chauduong.gym.model.User;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactViewHolder> {
    private static final String TAG = "ContactAdapter";
    ContactListener mContactListener;
    Context mContext;
    List<User> userList;

    public ContactAdapter(Context mContext, List<User> userList) {
        this.mContext = mContext;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemContactBinding itemContactBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_contact, parent, false);
        return new ContactViewHolder(itemContactBinding);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        User user = userList.get(position);
        holder.mItemContactBinding.setContact(user);
        Log.i(TAG, "onBindViewHolder: "+user.toString());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mContactListener != null) {
                    mContactListener.onContactClick(user);
                }
                Log.i(TAG, "onClick: ");
            }
        });
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.i(TAG, "onTouch: ");
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) v.findViewById(R.id.imgAvatar).getLayoutParams();
                //Code to convert height and width in dp.
                int height = mContext.getResources().getDimensionPixelSize(R.dimen.image_contact_width);
                int width = mContext.getResources().getDimensionPixelSize(R.dimen.image_contact_width);

                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Log.i(TAG, "onTouch: down");
                    layoutParams.width = width - 15;
                    layoutParams.height = height - 15;
                    holder.mItemContactBinding.imgAvatar.setLayoutParams(layoutParams);
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Log.i(TAG, "onTouch: up");
                    layoutParams.width = width;
                    layoutParams.height = height;
                    holder.mItemContactBinding.imgAvatar.setLayoutParams(layoutParams);
                }
                return false;
            }
        });
    }

    public List<User> getUserList() {
        return userList;
    }


    public void setmContactListener(ContactListener mContactListener) {
        this.mContactListener = mContactListener;
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}

class ContactViewHolder extends RecyclerView.ViewHolder {
    ItemContactBinding mItemContactBinding;

    public ContactViewHolder(@NonNull ItemContactBinding mItemContactBinding) {
        super(mItemContactBinding.getRoot());
        this.mItemContactBinding = mItemContactBinding;

    }
}
