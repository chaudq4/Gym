package com.chauduong.gym.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.chauduong.gym.R;
import com.chauduong.gym.databinding.ItemSliderImageBinding;
import com.chauduong.gym.model.SliderItem;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderAdapter extends SliderViewAdapter<SliderViewHolder> {

    private Context context;
    private List<SliderItem> mSliderItems = new ArrayList<>();

    public SliderAdapter(Context context) {
        this.context = context;
    }

    public void renewItems(List<SliderItem> sliderItems) {
        this.mSliderItems = sliderItems;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        this.mSliderItems.remove(position);
        notifyDataSetChanged();
    }

    public void addItem(SliderItem sliderItem) {
        this.mSliderItems.add(sliderItem);
        notifyDataSetChanged();
    }
    @Override
    public SliderViewHolder onCreateViewHolder(ViewGroup parent) {
        ItemSliderImageBinding itemSliderImageBinding= DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_slider_image,parent,false);
        return new SliderViewHolder(itemSliderImageBinding);
    }

    @Override
    public void onBindViewHolder(SliderViewHolder viewHolder, int position) {
        SliderItem sliderItem = mSliderItems.get(position);
        CircularProgressDrawable drawable = new CircularProgressDrawable(context);
        drawable.setColorSchemeColors(R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimary);
        drawable.setCenterRadius(30f);
        drawable.setStrokeWidth(5f);
        // set all other properties as you would see fit and start it
        drawable.start();
        viewHolder.mItemSliderImageBinding.tvAutoImageSlider.setText(sliderItem.getDescription());
        viewHolder.mItemSliderImageBinding.tvAutoImageSlider.setTextSize(16);
        viewHolder.mItemSliderImageBinding.tvAutoImageSlider.setTextColor(Color.WHITE);
        Glide.with(viewHolder.itemView)
                .load(sliderItem.getUrl())
                .fitCenter()
                .placeholder(drawable)
                .transition(DrawableTransitionOptions.with(new DrawableAlwaysCrossFadeFactory()))
                .into(viewHolder.mItemSliderImageBinding.ivAutoImageSlider);

    }



    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return mSliderItems.size();
    }


}
class SliderViewHolder extends SliderViewAdapter.ViewHolder {
    ItemSliderImageBinding mItemSliderImageBinding;
    public SliderViewHolder(ItemSliderImageBinding mItemSliderImageBinding) {
        super(mItemSliderImageBinding.getRoot());
        this.mItemSliderImageBinding=mItemSliderImageBinding;
    }
}
