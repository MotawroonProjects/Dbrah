package com.apps.dbrah.uis.activity_home.profile_module;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.dbrah.R;
import com.apps.dbrah.mvvm.GeneralMvvm;
import com.apps.dbrah.uis.activity_base.BaseFragment;
import com.apps.dbrah.databinding.FragmentProfileBinding;
import com.apps.dbrah.uis.activity_home.HomeActivity;
import com.apps.dbrah.uis.activity_home.order_module.FragmentOrder;
import com.apps.dbrah.uis.activity_login.LoginActivity;

import java.util.List;


public class FragmentProfile extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private HomeActivity activity;
    private FragmentProfileBinding binding;
    private ActivityResultLauncher<Intent> launcher;
    private int req = 1;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == Activity.RESULT_OK) {
                binding.setModel(getUserModel());
                binding.profileLayout.setModel(getUserModel());
            }
        });

    }

    public static FragmentProfile newInstance() {
        return new FragmentProfile();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        binding.setModel(getUserModel());
        binding.profileLayout.setLang(getLang());
        if (getUserModel() != null) {
            binding.profileLayout.setModel(getUserModel());
        }
        binding.profileNotLoggedLayout.btnLogin.setOnClickListener(v -> {
            navigateToLoginActivity();
        });

        binding.profileLayout.cardMyList.setOnClickListener(v -> {
            generalMvvm.onHomeNavigate().setValue(3);
        });

        binding.profileLayout.cardMyAddresses.setOnClickListener(v -> {
            generalMvvm.onHomeNavigate().setValue(8);
        });

        binding.profileLayout.carViewSetting.setOnClickListener(v -> {
            generalMvvm.onHomeNavigate().setValue(5);
        });

    }

    private void navigateToLoginActivity() {
        req = 1;
        Intent intent = new Intent(activity, LoginActivity.class);
        launcher.launch(intent);
    }


}