package com.chauduong.gym.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

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

    @NonNull
    @Override
    public TypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemTypeBinding itemTypeBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_type, parent, false);
        return new TypeViewHolder(itemTypeBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TypeViewHolder holder, int position) {
        holder.mItemTypeBinding.txtName.setText(mTypeList.get(position).getName());
        holder.mItemTypeBinding.imgType.setImageResource(mTypeList.get(position).getIcon());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTypeListener != null) {
                    mTypeListener.onTypeClick(mTypeList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTypeList.size();
    }

    public void setmTypeListener(TypeListener mTypeListener) {
        this.mTypeListener = mTypeListener;
    }
}

class TypeViewHolder extends RecyclerView.ViewHolder {
    public ItemTypeBinding mItemTypeBinding;

    public TypeViewHolder(@NonNull ItemTypeBinding mItemTypeBinding) {
        super(mItemTypeBinding.getRoot());
        this.mItemTypeBinding = mItemTypeBinding;
    }
}



