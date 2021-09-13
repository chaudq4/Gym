package com.chauduong.gym.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.chauduong.gym.R;
import com.chauduong.gym.databinding.ItemExerciseBinding;
import com.chauduong.gym.model.Exercise;

import java.util.List;

public class ExcersiseAdapter extends RecyclerView.Adapter<ExerciseViewHolder> {
    private List<Exercise> mExerciseList;
    private Context mContext;
    private ExcersieListener mExcersieListener;

    public ExcersiseAdapter(List<Exercise> mExerciseList, Context mContext) {
        this.mExerciseList = mExerciseList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemExerciseBinding itemExcersiceBinding = DataBindingUtil.inflate(LayoutInflater.from(mContext), R.layout.item_exercise, parent, false);
        return new ExerciseViewHolder(itemExcersiceBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.mItemExcersiceBinding.txtName.setText( mExerciseList.get(position).getName());
        holder.mItemExcersiceBinding.txtType.setText(mContext.getString(R.string.type) + mExerciseList.get(position).getType());
        holder.mItemExcersiceBinding.txtLevel.setText(mContext.getString(R.string.level) + mExerciseList.get(position).getLevel());
        holder.mItemExcersiceBinding.txtMain.setText(mContext.getString(R.string.main) + mExerciseList.get(position).getMain());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mExcersieListener != null) {
                    mExcersieListener.onExerciseClick(mExerciseList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mExerciseList.size();
    }

    public void setmExcersieListener(ExcersieListener mExcersieListener) {
        this.mExcersieListener = mExcersieListener;
    }
}

class ExerciseViewHolder extends RecyclerView.ViewHolder {
    ItemExerciseBinding mItemExcersiceBinding;

    public ExerciseViewHolder(@NonNull ItemExerciseBinding mItemExcersiceBinding) {
        super(mItemExcersiceBinding.getRoot());
        this.mItemExcersiceBinding = mItemExcersiceBinding;
    }
}
