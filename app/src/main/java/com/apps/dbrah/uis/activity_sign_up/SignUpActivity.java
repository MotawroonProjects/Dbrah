package com.apps.dbrah.uis.activity_sign_up;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.apps.dbrah.R;
import com.apps.dbrah.databinding.ActivitySignUpBinding;
import com.apps.dbrah.model.SignUpModel;
import com.apps.dbrah.preferences.Preferences;
import com.apps.dbrah.share.Common;
import com.apps.dbrah.uis.activity_base.BaseActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class SignUpActivity extends BaseActivity {
    private ActivitySignUpBinding binding;
    private SignUpModel model;
    private Preferences preferences;
    private String phone_code, phone;
    private ActivityResultLauncher<Intent> launcher;
    private Uri uri = null;
    private int req;

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
        preferences = Preferences.getInstance();
        model = new SignUpModel();
        model.setPhone_code(phone_code);
        model.setPhone(phone);
        binding.setModel(model);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK) {

            }
        });


        binding.flImage.setOnClickListener(v -> checkPhotoPermission());
        binding.btnSignup.setOnClickListener(view -> {

        });

    }


    public void checkPhotoPermission() {


        if (ContextCompat.checkSelfPermission(this, WRITE_PERM) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, CAM_PERM) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, READ_PERM) == PackageManager.PERMISSION_GRANTED
        ) {
            SelectImage();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{READ_PERM, WRITE_PERM, CAM_PERM}, req);
        }
    }

    private void SelectImage() {
        Intent intentGallery = new Intent();
        intentGallery.setAction(Intent.ACTION_GET_CONTENT);
        intentGallery.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intentGallery.setType("image/*");

        Intent intentCamera = new Intent();
        intentCamera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri outPutUri = null;
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT,uri);




    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == req) {

            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                        grantResults[2] == PackageManager.PERMISSION_GRANTED
                ) {
                    SelectImage();

                } else {
                    Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

                }
            } else {
                Toast.makeText(this, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
            }

        }
    }

    private Uri getUriFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        return Uri.parse(MediaStore.Images.Media.insertImage(this.getContentResolver(), bitmap, "", ""));
    }


}