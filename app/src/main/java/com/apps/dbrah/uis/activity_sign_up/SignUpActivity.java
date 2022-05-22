package com.apps.dbrah.uis.activity_sign_up;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.apps.dbrah.BuildConfig;
import com.apps.dbrah.R;
import com.apps.dbrah.databinding.ActivitySignUpBinding;
import com.apps.dbrah.databinding.DialogChooseImageBinding;
import com.apps.dbrah.databinding.DialogInformationBinding;
import com.apps.dbrah.model.SettingDataModel;
import com.apps.dbrah.model.SignUpModel;
import com.apps.dbrah.mvvm.ActivitySignupMvvm;
import com.apps.dbrah.preferences.Preferences;
import com.apps.dbrah.share.Common;
import com.apps.dbrah.uis.activity_base.BaseActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SignUpActivity extends BaseActivity {
    private ActivitySignUpBinding binding;
    private SignUpModel model;
    private ActivitySignupMvvm activitySignupMvvm;
    private String phone_code, phone;
    private ActivityResultLauncher<String[]> permissions;
    private ActivityResultLauncher<Intent> launcher;
    private Uri outPutUri = null;
    private String imagePath = "";
    private int req;
    private SettingDataModel.Data setting;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        phone_code = intent.getStringExtra("phone_code");
        phone = intent.getStringExtra("phone");

    }

    private void initView() {
        activitySignupMvvm = ViewModelProviders.of(this).get(ActivitySignupMvvm.class);
        activitySignupMvvm.getOnDataSuccess().observe(this, model -> {
            setting = model;
            if(setting!=null){
                binding.imageInfo.setVisibility(View.VISIBLE);
            }
        });
        activitySignupMvvm.getUserData().observe(this, userModel -> {
            if (userModel != null) {
                setUserModel(userModel);
                setResult(RESULT_OK);
                finish();
            }
        });
        String title = getString(R.string.sign_up);
        model = new SignUpModel();


        if (getUserModel()!=null){
            title = getString(R.string.edit_profile);
            model.setName(getUserModel().getData().getName());
            model.setEmail(getUserModel().getData().getEmail());
            model.setPhone_code(getUserModel().getData().getPhone_code());
            model.setPhone(getUserModel().getData().getPhone());
            model.setVat(getUserModel().getData().getVat_number());
            binding.llVat.setVisibility(View.GONE);
            binding.llSpinner.setEnabled(false);
            binding.edtName.setEnabled(false);
            binding.llPhone.setVisibility(View.VISIBLE);
            if (getUserModel().getData().getImage()!=null){
                Glide.with(this).asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .placeholder(R.drawable.circle_avatar)
                        .load(getUserModel().getData().getImage())
                        .centerCrop()
                        .into(binding.image);

            }
        }else {
            model.setPhone_code(phone_code);
            model.setPhone(phone);
            binding.edtName.setEnabled(true);
            binding.llVat.setVisibility(View.VISIBLE);
            binding.llPhone.setVisibility(View.GONE);
        }
        setUpToolbar(binding.toolbar, title, R.color.white, R.color.black, R.drawable.small_rounded_grey4, true);


        binding.setModel(model);
        permissions = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), permissions -> {
            if (req == 1) {
                if (permissions.containsValue(false)) {
                    Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
                } else {
                    openCamera();
                }
            } else if (req == 2) {
                if (permissions.containsValue(false)) {
                    Toast.makeText(this, R.string.perm_image_denied, Toast.LENGTH_SHORT).show();
                } else {
                    openGallery();
                }
            }
        });
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 2 && result.getResultCode() == RESULT_OK && result.getData() != null) {
                Uri uri = result.getData().getData();
                model.setImage_uri(uri.toString());
                model.setImage_path(Common.getImagePath(this, uri));

                Glide.with(this)
                        .asBitmap()
                        .load(uri)
                        .into(binding.image);
            } else if (req == 1 && result.getResultCode() == RESULT_OK) {
                model.setImage_uri(outPutUri.toString());
                model.setImage_path(imagePath);
                Log.e("uri",outPutUri+"__");

                Glide.with(this)
                        .asBitmap()
                        .load(outPutUri)
                        .into(binding.image);

            }
        });


        binding.imageInfo.setOnClickListener(v -> {
            openSheetInfo();

        });
        binding.flImage.setOnClickListener(v -> openSheet());
        binding.btnSignup.setOnClickListener(view -> {
            if (getUserModel()==null){
                activitySignupMvvm.signUp(model, this);

            }else {
                activitySignupMvvm.update(model,getUserModel().getData().getId(),this);

            }
        });
        activitySignupMvvm.getSettings(this);

    }

    private void openSheet() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg);
        DialogChooseImageBinding imageBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_choose_image, null, false);
        dialog.setView(imageBinding.getRoot());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        imageBinding.tvCamera.setOnClickListener(v -> {
            checkCameraPermission();
            dialog.dismiss();
        });
        imageBinding.tvGallery.setOnClickListener(v -> {
            checkPhotoPermission();
            dialog.dismiss();
        });
        imageBinding.tvCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    private void openSheetInfo() {
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_window_bg2);
        DialogInformationBinding informationBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_information, null, false);
        dialog.setView(informationBinding.getRoot());
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        informationBinding.tvDetails.setText(Html.fromHtml(setting.getUser_info()));
        informationBinding.tvCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }


    public void checkCameraPermission() {
        req = 1;
        String[] permissions = new String[]{WRITE_PERM, CAM_PERM};
        if (ContextCompat.checkSelfPermission(this, WRITE_PERM) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, CAM_PERM) == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            this.permissions.launch(permissions);
        }
    }

    public void checkPhotoPermission() {
        req = 2;
        String[] permissions = new String[]{READ_PERM};
        if (ContextCompat.checkSelfPermission(this, READ_PERM) == PackageManager.PERMISSION_GRANTED

        ) {
            openGallery();
        } else {
            this.permissions.launch(permissions);
        }
    }

    private void openCamera() {
        req = 1;
        outPutUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", getCameraOutPutFile());

        if (outPutUri != null) {
            Intent intentCamera = new Intent();
            intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
            intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, outPutUri);
            launcher.launch(intentCamera);
        } else {
            Toast.makeText(this, "You don't allow to access photos", Toast.LENGTH_SHORT).show();
        }


    }

    private void openGallery() {
        req = 2;
        Intent intentGallery = new Intent();
        intentGallery.setAction(Intent.ACTION_GET_CONTENT);
        intentGallery.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intentGallery.setType("image/*");
        launcher.launch(Intent.createChooser(intentGallery, "Choose photos"));


    }

    private File getCameraOutPutFile() {
        File file = null;
        String stamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageName = "JPEG_" + stamp + "_";

        File appFile = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        try {
            file = File.createTempFile(imageName, ".jpg", appFile);
            imagePath = file.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

}