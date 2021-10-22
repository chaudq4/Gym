package com.chauduong.gym.inbox;

import static com.chauduong.gym.utils.Util.getRealPathFromURIForGallery;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chauduong.gym.R;
import com.chauduong.gym.databinding.ActivityInboxBinding;
import com.chauduong.gym.databinding.DialogProgressBinding;
import com.chauduong.gym.databinding.DialogProgressUploadBinding;
import com.chauduong.gym.manager.dialog.ImagePopupWindow;
import com.chauduong.gym.model.Inbox;
import com.chauduong.gym.model.User;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class InboxActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher, Observer<Inbox>, SwipeRefreshLayout.OnRefreshListener, InboxListener {
    public static final String USER = "key_user";
    private static final String TAG = "ConversationActivity";
    private static final int REQUEST_PICK_IMAGE = 1;
    private static final int MY_CAMERA_PERMISSION_CODE = 2;
    private static final int CAMERA_REQUEST = 3;
    ActivityInboxBinding mActivityInboxBinding;
    User toUser;
    InboxAdapter mInboxAdapter;
    List<Inbox> inboxList;
    List<Inbox> newList;
    InboxViewModel inboxViewModel;
    boolean isScrollEnd;
    AlertDialog progressUploadDialog;
    DialogProgressUploadBinding dialogProgressBinding;
    ImagePopupWindow imagePopupWindow;
    private Uri imageUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityInboxBinding = DataBindingUtil.setContentView(this, R.layout.activity_inbox);
        initData();
        initView();
        initPopupImage();
        isScrollEnd = true;
        initViewModel();
        receiveDataFromViewModel();
        mActivityInboxBinding.setViewModel(inboxViewModel);
        updateEdtMessage(mActivityInboxBinding.edtMsg.getText().toString());

    }

    private void initPopupImage() {
        imagePopupWindow = new ImagePopupWindow(this);
    }

    private void receiveDataFromViewModel() {
        inboxViewModel.getAllInbox(toUser);
        if (newList == null) newList = new ArrayList<>();
        else
            newList.clear();
    }

    private void initViewModel() {
        inboxViewModel = ViewModelProviders.of(this).get(InboxViewModel.class);
        inboxViewModel.init(this);
        inboxViewModel.getmListMutableLiveDataInbox().observe(this, this);
        inboxViewModel.getIsNewInbox().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isScrollEnd = true;
                mActivityInboxBinding.rvListInbox.smoothScrollToPosition(mInboxAdapter.getInboxList().size() - 1);
            }
        });
        inboxViewModel.listenToUserChange(toUser);
        listenUploadFile();

    }

    private void listenUploadFile() {
        inboxViewModel.getLinkUpload().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (progressUploadDialog != null && progressUploadDialog.isShowing())
                    progressUploadDialog.dismiss();
                sentInbox(s);
            }
        });
        inboxViewModel.getProgressUpload().observe(this, new Observer<Double>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(Double aDouble) {
                Log.i(TAG, "onChanged: pro" + aDouble);
                dialogProgressBinding.pbUpload.setProgress((int) Math.round(aDouble));
                dialogProgressBinding.txtProgress.setText((int) Math.round(aDouble) + "%");
            }
        });
        inboxViewModel.getMsgErrorUpload().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Log.i(TAG, "onChanged: error" + s);
                if (progressUploadDialog != null && progressUploadDialog.isShowing())
                    progressUploadDialog.dismiss();
                Toast.makeText(InboxActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        mActivityInboxBinding.btnCamera.setOnClickListener(this);
        mActivityInboxBinding.btnBack.setOnClickListener(this);
        mActivityInboxBinding.btnSend.setOnClickListener(this);
        mActivityInboxBinding.swLayout.setOnRefreshListener(this);
        mActivityInboxBinding.btnImage.setOnClickListener(this);
        mActivityInboxBinding.edtMsg.addTextChangedListener(this);
        mActivityInboxBinding.btnCamera.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        mActivityInboxBinding.rvListInbox.setLayoutManager(linearLayoutManager);
        mInboxAdapter = new InboxAdapter(this, inboxList, this);
        mActivityInboxBinding.rvListInbox.setAdapter(mInboxAdapter);
        mActivityInboxBinding.swLayout.setColorSchemeColors(getColor(R.color.colorPrimary));

    }

    private void updateEdtMessage(String s) {
        if (s.length() == 0) {
            mActivityInboxBinding.edtMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
            updateLayoutSend(false);
        } else {
            mActivityInboxBinding.edtMsg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            updateLayoutSend(true);
        }
        if (mActivityInboxBinding.edtMsg.getLineCount() >= 1 && mActivityInboxBinding.edtMsg.getLineCount() < 7) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mActivityInboxBinding.layoutMsg.getLayoutParams();
            layoutParams.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            layoutParams.height = getResources().getDimensionPixelSize(R.dimen.bottom_conversation_height) + (getResources().getDimensionPixelSize(R.dimen.bottom_conversation_height) / 2 * (mActivityInboxBinding.edtMsg.getLineCount() - 1));
            int margin = getResources().getDimensionPixelSize(R.dimen.margin_edt_msg);
            int padding = getResources().getDimensionPixelSize(R.dimen.padding_edt_msg);
            layoutParams.setMargins(margin, margin, margin, margin);
            mActivityInboxBinding.layoutMsg.setPadding(padding, padding, padding, padding);
            mActivityInboxBinding.layoutMsg.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams llParam = (RelativeLayout.LayoutParams) mActivityInboxBinding.layoutMsgParent.getLayoutParams();
            llParam.width = RelativeLayout.LayoutParams.MATCH_PARENT;
            llParam.height = getResources().getDimensionPixelSize(R.dimen.bottom_conversation_height_parent) + (getResources().getDimensionPixelSize(R.dimen.bottom_conversation_height) / 2 * (mActivityInboxBinding.edtMsg.getLineCount() - 1));
            mActivityInboxBinding.layoutMsgParent.setLayoutParams(llParam);

        }
    }


    private void initData() {
        Intent intent = getIntent();
        if (intent != null) {
            toUser = (User) intent.getSerializableExtra(USER);
        }
        inboxList = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBack:
                finish();
                break;
            case R.id.btnSend:
                sentInbox("");
                break;
            case R.id.btnImage:
                openSelecImage();
                break;
            case R.id.btnCamera:
                openCamera();
            default:
                break;
        }
    }

    private void openCamera() {
        if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
        } else {
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
            imageUri = getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, CAMERA_REQUEST);
        }
    }


    private void sentInbox(String link) {
        String urlImage = link;
        inboxViewModel.sentInbox(urlImage);
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        updateEdtMessage(s.toString());
    }

    public void loadMore() {
        Log.i(TAG, "onLoadMore: ");
        isScrollEnd = false;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                receiveDataFromViewModel();
                mActivityInboxBinding.swLayout.setRefreshing(false);
            }
        }, 1000);

    }

    @Override
    public void onChanged(Inbox inbox) {
        newList.add(inbox);
        mInboxAdapter.setInboxList(newList);
        if (!isScrollEnd)
            mActivityInboxBinding.rvListInbox.scrollToPosition(0);
        else
            mActivityInboxBinding.rvListInbox.smoothScrollToPosition(mInboxAdapter.getInboxList().size() - 1);
    }

    @Override
    public void onRefresh() {
        isScrollEnd = false;
        mActivityInboxBinding.swLayout.setRefreshing(true);
        loadMore();
    }

    private void updateLayoutSend(boolean isSend) {
        if (isSend) {
            mActivityInboxBinding.btnSend.setVisibility(View.VISIBLE);
            mActivityInboxBinding.btnImage.setVisibility(View.GONE);
            mActivityInboxBinding.btnAudio.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mActivityInboxBinding.layoutMsg.getLayoutParams();
            layoutParams.addRule(RelativeLayout.START_OF, R.id.btnSend);

        } else {
            mActivityInboxBinding.btnSend.setVisibility(View.GONE);
            mActivityInboxBinding.btnImage.setVisibility(View.VISIBLE);
            mActivityInboxBinding.btnAudio.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mActivityInboxBinding.layoutMsg.getLayoutParams();
            layoutParams.addRule(RelativeLayout.START_OF, mActivityInboxBinding.btnAudio.getId());
        }
    }

    private void openSelecImage() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_image)), REQUEST_PICK_IMAGE);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            inboxViewModel.uploadFile(data.getData());
            dialogProgressBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_progress_upload, null, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.upload_image));
            builder.setView(dialogProgressBinding.getRoot());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap tmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                dialogProgressBinding.imgUpload.setImageBitmap(tmp);
                bitmap.recycle();
            } catch (IOException e) {
                e.printStackTrace();
            }
            progressUploadDialog = builder.create();
            dialogProgressBinding.pbUpload.setProgress(0);
            dialogProgressBinding.txtProgress.setText(0 + "%");
            progressUploadDialog.show();
        }
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            inboxViewModel.uploadFile(imageUri);
            dialogProgressBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_progress_upload, null, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.upload_image));
            builder.setView(dialogProgressBinding.getRoot());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                Bitmap tmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
                dialogProgressBinding.imgUpload.setImageBitmap(tmp);
                bitmap.recycle();
            } catch (IOException e) {
                e.printStackTrace();
            }
            progressUploadDialog = builder.create();
            dialogProgressBinding.pbUpload.setProgress(0);
            dialogProgressBinding.txtProgress.setText(0 + "%");
            progressUploadDialog.show();
        }
    }


    @Override
    public void onInboxClick(Inbox inbox) {
        if (inbox.getLink() != null && inbox.getLink().length() != 0) {
            Log.i(TAG, "onInboxClick: " + inbox.getLink());
            imagePopupWindow.showImagePopup(mActivityInboxBinding.layoutInbox, inbox.getLink());
        }
    }

    @Override
    public void onBackPressed() {
        if (imagePopupWindow != null && imagePopupWindow.isShow()) {
            imagePopupWindow.dissmiss();
        } else {
            super.onBackPressed();
        }

    }

}
