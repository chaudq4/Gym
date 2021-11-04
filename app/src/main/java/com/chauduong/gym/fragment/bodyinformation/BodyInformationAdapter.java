package com.chauduong.gym.fragment.bodyinformation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.chauduong.gym.R;
import com.chauduong.gym.databinding.ItemBodyInforBinding;
import com.chauduong.gym.model.BodyInformation;

import java.util.List;

public class BodyInformationAdapter extends RecyclerView.Adapter<BodyInformationViewHolder> {

    private List<BodyInformation> bodyInformationList;
    private BodyInformationListener mListener;

    public BodyInformationAdapter(List<BodyInformation> bodyInformationList, BodyInformationListener mListener) {
        this.bodyInformationList = bodyInformationList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public BodyInformationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBodyInforBinding itemBodyInforBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_body_infor, parent, false);
        return new BodyInformationViewHolder(itemBodyInforBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BodyInformationViewHolder holder, int position) {
        BodyInformation b = bodyInformationList.get(position);
        holder.itemBodyInforBinding.setBody(b);
        holder.itemBodyInforBinding.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onBodyInformationClick(b);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bodyInformationList == null ? 0 : bodyInformationList.size();
    }

    public void setBodyInformationList(List<BodyInformation> bodyInformationList) {
        this.bodyInformationList = bodyInformationList;
    }
}

class BodyInformationViewHolder extends RecyclerView.ViewHolder {
    ItemBodyInforBinding itemBodyInforBinding;

    public BodyInformationViewHolder(@NonNull ItemBodyInforBinding itemBodyInforBinding) {
        super(itemBodyInforBinding.getRoot());
        this.itemBodyInforBinding = itemBodyInforBinding;
    }
}
