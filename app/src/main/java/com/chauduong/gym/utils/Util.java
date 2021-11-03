package com.chauduong.gym.utils;

import static androidx.core.app.ActivityCompat.requestPermissions;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;

import com.chauduong.gym.R;
import com.chauduong.gym.databinding.MySnackbarBinding;
import com.google.android.material.snackbar.Snackbar;

public class Util {
    public static final int TYPE_SNACK_BAR_SUCCESS = 0;
    public static final int TYPE_SNACK_BAR_WARNING = 1;
    public static final int TYPE_SNACK_BAR_WRONG = 2;
    public static final int TYPE_SNACK_BAR_NORMAL = 3;
    private static final int PERMISSION_STORAGE = 0;

    public static void setFullScreen(Context mContext) {
        ((Activity) mContext).requestWindowFeature(Window.FEATURE_NO_TITLE);
        ((Activity) mContext).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        ((Activity) mContext).getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Window window = ((Activity) mContext).getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);
    }

    public static void updateIconTab(ImageView view, int width, int height) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = height;
        view.setLayoutParams(layoutParams);
    }

    public static void setVisibilityView(View view, boolean isVisibility) {
        if (view != null) {
            view.setVisibility(isVisibility ? View.VISIBLE : View.GONE);
        }
    }

    public static String getRealPathFromURIForGallery(Context mContext, Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = mContext.getContentResolver().query(uri, projection, null,
                null, null);
        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        assert false;
        cursor.close();
        return uri.getPath();
    }

    public static Bitmap getBitmapFromGallery(Context mContext, Uri path, int SCALE) {
        String picturePath = getRealPathFromURIForGallery(mContext, path);
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(picturePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, options.outWidth / SCALE, options.outHeight / SCALE);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(picturePath, options);

    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static int getWindowHeight(Context mContext) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;

    }

    public static int getWindowWidth(Context mContext) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    @SuppressLint("RestrictedApi")
    public static void showSnackbar(View parent, String content, int type) {
        Snackbar snackbar = Snackbar.make(parent, "", Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        TextView textView = (TextView) layout.findViewById(R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);
        MySnackbarBinding mySnackbarBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.my_snackbar, null, false);


//If the view is not covering the whole snackbar layout, add this line
        Context mContext = parent.getContext();
        switch (type) {
            case TYPE_SNACK_BAR_SUCCESS:
                mySnackbarBinding.imgIcon.setImageResource(R.drawable.ic_baseline_done_24);
                mySnackbarBinding.snackLayoutParent.setBackgroundResource(R.drawable.snackbar_shape_success);

                break;
            case TYPE_SNACK_BAR_WARNING:
                mySnackbarBinding.imgIcon.setImageResource(R.drawable.ic_baseline_warning_24);
                mySnackbarBinding.snackLayoutParent.setBackgroundResource(R.drawable.snackbar_shape_warning);
                break;
            case TYPE_SNACK_BAR_WRONG:
                mySnackbarBinding.imgIcon.setImageResource(R.drawable.ic_baseline_info_24);
                mySnackbarBinding.snackLayoutParent.setBackgroundResource(R.drawable.snackbar_shape_wrong);
                break;
            case TYPE_SNACK_BAR_NORMAL:
                mySnackbarBinding.snackLayoutParent.setBackgroundResource(R.drawable.snackbar_shape_normal);
                break;
            default:
                break;
        }
        mySnackbarBinding.txtContent.setText(content);
        mySnackbarBinding.txtContent.setTextColor(mContext.getColor(R.color.white));
        layout.setPadding(0, 0, 0, 0);
        layout.addView(mySnackbarBinding.getRoot(), 0);
        layout.setBackground(new ColorDrawable(Color.TRANSPARENT));
        snackbar.show();
    }

    public static boolean checkValidEditText(View parent, EditText editText) {
        if (editText.getText().toString().length() == 0) {
            Util.showSnackbar(parent, parent.getContext().getString(R.string.please_add_information), TYPE_SNACK_BAR_WARNING);
            editText.requestFocus();
            return false;
        }
        return true;
    }

    public static void requestStoragePermission(View view, Activity activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            Snackbar.make(view, "Cấp quyền ghi dữ liệu vào thư viện", Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_STORAGE);
                        }
                    }).show();
        } else {
            Toast.makeText(activity, "Vui lòng cấp quyền!", Toast.LENGTH_SHORT)
                    .show();
            requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_STORAGE);
        }
    }
}
