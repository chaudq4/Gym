package com.chauduong.gym.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.chauduong.gym.R;
import com.chauduong.gym.databinding.ItemTypeBinding;
import com.chauduong.gym.model.Type;

import java.util.List;

public class TypeAdapter extends RecyclerView.Adapter<TypeViewHolder> {
    private List<Type> mTypeList;
    private TypeListener mTypeListener;
    private Context mContext;

    public TypeAdapter(List<Type> mTypeList, Context mContext) {
        this.mTypeList = mTypeList;
        this.mContext = mContext;
    }

    public void setmTypeList(List<Type> mTypeList) {
        this.mTypeList = mTypeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTypeBinding itemTypeBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_type, parent, false);
        return new TypeViewHolder(itemTypeBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.mItemTypeBinding.txtName.setText(mTypeList.get(position).getName());
        Log.i("chauanh", "onBindViewHolder: "+mTypeList.get(position).getUrlIcon());
        if (mTypeList.get(position).getUrlIcon() != null) {
            Glide.with(mContext.getApplicationContext())
                    .load(mTypeList.get(position).getUrlIcon())
                    .into(holder.mItemTypeBinding.imgType);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTypeListener != null) {
                    mTypeListener.onTypeClick(mTypeList.get(position));
                }
            }
        });
    }

    public List<Type> getmTypeList() {
        return mTypeList;
    }

    @Override
    public int getItemCount() {
        return mTypeList.size();
    }

    public void setmTypeListener(TypeListener mTypeListener) {
        this.mTypeListener = mTypeListener;
    }

    public  interface TypeListener {
        void onTypeClick(Type type);
    }
}

class TypeViewHolder extends RecyclerView.ViewHolder {
    public ItemTypeBinding mItemTypeBinding;

    public TypeViewHolder(@NonNull ItemTypeBinding mItemTypeBinding) {
        super(mItemTypeBinding.getRoot());
        this.mItemTypeBinding = mItemTypeBinding;
    }
}



