package com.chauduong.gym.fragment.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.chauduong.gym.R;
import com.chauduong.gym.adapter.SliderAdapter;
import com.chauduong.gym.databinding.FragmentDetailBinding;
import com.chauduong.gym.manager.dialog.DialogManager;
import com.chauduong.gym.model.SliderItem;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;

public class DetailFragment extends Fragment implements View.OnClickListener {
    FragmentDetailBinding mFragmentDetailBinding;
    SliderAdapter mSliderAdapter;
    DialogManager mDialogManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentDetailBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false);
        return mFragmentDetailBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
        initSlider();
    }

    private void initSlider() {

        mSliderAdapter = new SliderAdapter(getContext());
        SliderItem sliderItem = new SliderItem();
        sliderItem.setUrl("https://scontent.fhan5-4.fna.fbcdn.net/v/t31.18172-8/20280536_699198310271382_8101066472111972321_o.png?_nc_cat=104&ccb=1-5&_nc_sid=cdbe9c&_nc_ohc=Cr805an7RjMAX_UPSvK&_nc_ht=scontent.fhan5-4.fna&oh=b56cd69ac61b527ae4fc12d4a108183b&oe=6197D38B");
        mSliderAdapter.addItem(sliderItem);
        mSliderAdapter.addItem(sliderItem);
        mFragmentDetailBinding.imageSlider.setSliderAdapter(mSliderAdapter);
        mFragmentDetailBinding.imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        mFragmentDetailBinding.imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        mFragmentDetailBinding.imageSlider.setScrollTimeInSec(4); //set scroll delay in seconds :
        mFragmentDetailBinding.imageSlider.setIndicatorEnabled(true);
        mFragmentDetailBinding.imageSlider.setAutoCycle(false);
        mFragmentDetailBinding.imageSlider.stopAutoCycle();


    }


    private void initView() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("abcd");
        }
        mFragmentDetailBinding.txtInstruction.setText(sb.toString());
        mFragmentDetailBinding.rlWatchingVideo.setOnClickListener(this);

    }

    private void initData() {
        if (mDialogManager == null) mDialogManager = DialogManager.getInstance(getContext());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public DetailFragment() {
    }

    @Override
    public void onClick(View v) {
        if (mDialogManager.isShow()) return;
        switch (v.getId()) {
            case R.id.rlWatchingVideo:
                String uri = "Z4HivEWoXGE";
                mDialogManager.showPlayVideo(mFragmentDetailBinding.rlDetailParent, uri);
                break;
            default:
                break;
        }
    }
}
