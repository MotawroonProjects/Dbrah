package com.app.dbrah.uis.activity_base;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.app.dbrah.databinding.ToolbarBinding;
import com.app.dbrah.language.Language;
import com.app.dbrah.model.ChatUserModel;
import com.app.dbrah.model.UserModel;
import com.app.dbrah.model.UserSettingsModel;
import com.app.dbrah.preferences.Preferences;

import io.paperdb.Paper;


public class BaseActivity extends AppCompatActivity {

    public static final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String WRITE_PERM = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String CAM_PERM = Manifest.permission.CAMERA;
    public static final String FINELOCPerm = Manifest.permission.ACCESS_FINE_LOCATION;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language.updateResources(newBase, Paper.book().read("lang", "ar")));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }

    private void initViews() {


    }

    protected String getLang() {
        Paper.init(this);
        String lang = Paper.book().read("lang", "ar");
        return lang;
    }

    protected UserModel getUserModel() {
        Preferences preferences = Preferences.getInstance();
        return preferences.getUserData(this);
    }

    protected void setUserModel(UserModel userModel) {
        Preferences preferences = Preferences.getInstance();
        preferences.createUpdateUserData(this, userModel);
    }


    public void setUserSettings(UserSettingsModel userSettingsModel) {
        Preferences preferences = Preferences.getInstance();
        preferences.create_update_user_settings(this, userSettingsModel);
    }

    public UserSettingsModel getUserSettings() {
        Preferences preferences = Preferences.getInstance();
        return preferences.getUserSettings(this);
    }

    public void clearRoomId() {
        Preferences preferences = Preferences.getInstance();
        preferences.clearRoomId(this);
    }

    public void setRoomId(ChatUserModel order_id) {
        Preferences preferences = Preferences.getInstance();
        preferences.create_update_room(this, order_id);
    }



    public void clearUserData() {
        Preferences preferences = Preferences.getInstance();
        preferences.clearUserData(this);
    }



    public View setUpToolbar(ToolbarBinding binding, String title, int background, int arrowTitleColor, int arrowBg, boolean action) {
        binding.setLang(getLang());
        binding.setTitle(title);
        binding.llBack.setColorFilter(ContextCompat.getColor(this, arrowTitleColor));
        binding.tvTitle.setTextColor(ContextCompat.getColor(this, arrowTitleColor));
        binding.tvTitle.setTextColor(ContextCompat.getColor(this, arrowTitleColor));
        binding.toolbar.setBackgroundResource(background);
        if (action) {
            binding.llBack.setOnClickListener(v -> finish());

        }
        return binding.llBack;
    }


}