package com.apps.dbrah.uis.activity_home.setting_module;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.apps.dbrah.R;
import com.apps.dbrah.databinding.FragmentMyListBinding;
import com.apps.dbrah.databinding.FragmentSettingBinding;
import com.apps.dbrah.mvvm.GeneralMvvm;
import com.apps.dbrah.uis.activity_about_app.AboutAppActivity;
import com.apps.dbrah.uis.activity_base.BaseFragment;
import com.apps.dbrah.uis.activity_contact_us.ContactUsActivity;
import com.apps.dbrah.uis.activity_home.HomeActivity;


public class FragmentSetting extends BaseFragment {
    private HomeActivity activity;
    private FragmentSettingBinding binding;
    private GeneralMvvm generalMvvm;


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
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        View view = activity.setUpToolbar(binding.toolbar, getString(R.string.settings), R.color.white, R.color.black, R.drawable.small_rounded_grey4, false);
        view.setOnClickListener(v -> {
            generalMvvm.onHomeBackNavigate().setValue(true);

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
            navigateToAboutAppActivity("1");
        });

        binding.llPrivacy.setOnClickListener(v -> {
            navigateToAboutAppActivity("2");
        });

        binding.llRateApp.setOnClickListener(v -> {
            rateApp();
        });
        binding.llShareApp.setOnClickListener(v -> {
            share();
        });
    }

    private void share() {
        String app_url = "https://play.google.com/store/apps/details?id="+activity.getPackageName();
        String content = getString(R.string.app_name)+"\n"+app_url;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_TITLE,getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT,content);
        startActivity(Intent.createChooser(intent,"Share"));

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

    private void navigateToAboutAppActivity(String type) {
        String url = "";
        if (type.equals("1")){

        }else {

        }
        Intent intent = new Intent(activity, AboutAppActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("url",url);
        startActivity(intent);
    }


}