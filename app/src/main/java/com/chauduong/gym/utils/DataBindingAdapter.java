package com.chauduong.gym.utils;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;

import java.text.DecimalFormat;

public class DataBindingAdapter {

    @BindingAdapter("android:src")
    public static void setImageUri(ImageView view, String imageUri) {
        if (imageUri == null) {
            view.setImageURI(null);
        } else {
            view.setImageURI(Uri.parse(imageUri));
        }
    }

    @BindingAdapter("bind:imageUrl")
    public static void loadImage(ImageView view, String url) {
        if (url != null && url.length() != 0)
            Glide.with(view.getContext())
                    .load(url)
                    .into(view);
    }

    @BindingAdapter("android:src")
    public static void setImageUri(ImageView view, Uri imageUri) {
        view.setImageURI(imageUri);
    }

    @BindingAdapter("android:src")
    public static void setImageDrawable(ImageView view, Drawable drawable) {
        view.setImageDrawable(drawable);
    }

    @BindingAdapter("bind:imageSrc")
    public static void setImageResource(ImageView imageView, int resource) {
        imageView.setImageResource(resource);
    }

    @BindingAdapter({"bind:high", "bind:weight"})
    public static void setBMI(TextView textView, float high, int weight) {
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        textView.setText(String.valueOf(df.format(weight / (high * high))));
    }

    @BindingAdapter("bind:sethigh")
    public static void setTextHigh(TextView textView, float value) {
        textView.setText(value + "m");
    }

    @BindingAdapter("bind:setweight")
    public static void setTextWeight(TextView textView, float value) {
        textView.setText(value + "kg");
    }


}