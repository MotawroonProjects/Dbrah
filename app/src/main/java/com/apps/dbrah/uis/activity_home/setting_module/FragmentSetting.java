package com.apps.dbrah.uis.activity_home.setting_module;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.apps.dbrah.R;
import com.apps.dbrah.databinding.FragmentMyListBinding;
import com.apps.dbrah.databinding.FragmentSettingBinding;
import com.apps.dbrah.model.SettingDataModel;
import com.apps.dbrah.mvvm.FragmentSettingMvvm;
import com.apps.dbrah.mvvm.GeneralMvvm;
import com.apps.dbrah.tags.Tags;
import com.apps.dbrah.uis.activity_about_app.AboutAppActivity;
import com.apps.dbrah.uis.activity_base.BaseFragment;
import com.apps.dbrah.uis.activity_contact_us.ContactUsActivity;
import com.apps.dbrah.uis.activity_home.HomeActivity;


public class FragmentSetting extends BaseFragment {
    private HomeActivity activity;
    private FragmentSettingBinding binding;
    private FragmentSettingMvvm mvvm;
    private GeneralMvvm generalMvvm;
    private SettingDataModel.Data setting;


    public static FragmentSetting newInstance() {
        return new FragmentSetting();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        binding.setLang(getLang());
        mvvm = ViewModelProviders.of(this).get(FragmentSettingMvvm.class);
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        View view = activity.setUpToolbar(binding.toolbar, getString(R.string.settings), R.color.white, R.color.black, R.drawable.small_rounded_grey4, false);
        view.setOnClickListener(v -> {
            generalMvvm.onHomeBackNavigate().setValue(true);

        });

        mvvm.getOnLoggedOutSuccess().observe(this, loggedOut -> {
            if (loggedOut) {
                clearUserModel(activity);
                generalMvvm.getOnLoggedOutSuccess().setValue(true);
                generalMvvm.onHomeBackNavigate().setValue(true);
            }
        });
        mvvm.getOnDataSuccess().observe(activity, model -> {
            setting = model;
        });

        binding.llContactUs.setOnClickListener(v -> {
            Intent intent = new Intent(activity, ContactUsActivity.class);
            startActivity(intent);
        });

        binding.tvLanguage.setOnClickListener(v -> {
            if (getLang().equals("ar")) {
                activity.refreshActivity("en");
            } else {
                activity.refreshActivity("ar");

            }
        });

        binding.llTerms.setOnClickListener(v -> {
            if (setting != null && setting.getTerms() != null) {
                String url = Tags.base_url + "webView?type=" + setting.getTerms();
                navigateToAboutAppActivity("1", url);
            } else {
                Toast.makeText(activity, R.string.inv_link, Toast.LENGTH_SHORT).show();
            }

        });

        binding.llPrivacy.setOnClickListener(v -> {
            if (setting != null && setting.getPrivacy() != null) {
                String url = Tags.base_url + "webView?type=" + setting.getPrivacy();
                navigateToAboutAppActivity("2", url);
            } else {
                Toast.makeText(activity, R.string.inv_link, Toast.LENGTH_SHORT).show();
            }
        });

        binding.llRateApp.setOnClickListener(v -> {
            rateApp();
        });
        binding.llShareApp.setOnClickListener(v -> {
            share();
        });

        binding.imageFacebook.setOnClickListener(v -> {

            if (setting != null && setting.getFacebook() != null) {
                String url = setting.getFacebook();
                createIntentForSocial(url);
            } else {
                Toast.makeText(activity, R.string.inv_link, Toast.LENGTH_SHORT).show();
            }
        });

        binding.imageTwitter.setOnClickListener(v -> {

            if (setting != null && setting.getTwitter() != null) {
                String url = setting.getTwitter();
                createIntentForSocial(url);
            } else {
                Toast.makeText(activity, R.string.inv_link, Toast.LENGTH_SHORT).show();
            }
        });

        binding.imageInstagram.setOnClickListener(v -> {

            if (setting != null && setting.getInsta() != null) {
                String url = setting.getInsta();
                createIntentForSocial(url);
            } else {
                Toast.makeText(activity, R.string.inv_link, Toast.LENGTH_SHORT).show();
            }
        });

        binding.imageSnapChat.setOnClickListener(v -> {

            if (setting != null && setting.getSnapchat() != null) {
                String url = setting.getSnapchat();
                createIntentForSocial(url);
            } else {
                Toast.makeText(activity, R.string.inv_link, Toast.LENGTH_SHORT).show();
            }
        });

        binding.llLogout.setOnClickListener(v -> {
            mvvm.logout(getUserModel(),activity);
        });

        mvvm.getSettings(activity);
    }

    private void createIntentForSocial(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void share() {
        String app_url = "https://play.google.com/store/apps/details?id=" + activity.getPackageName();
        String content = getString(R.string.app_name) + "\n" + app_url;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_TITLE, getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, content);
        startActivity(Intent.createChooser(intent, "Share"));

    }

    private void rateApp() {
        final String appPackageName = activity.getPackageName();
        try {
            Intent appStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
            appStoreIntent.setPackage("com.android.vending");

            startActivity(appStoreIntent);
        } catch (android.content.ActivityNotFoundException exception) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void navigateToAboutAppActivity(String type, String url) {

        Intent intent = new Intent(activity, AboutAppActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("url", url);
        startActivity(intent);
    }


}