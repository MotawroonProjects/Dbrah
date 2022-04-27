package com.apps.dbrah.uis.activity_home.notification_module;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.apps.dbrah.R;
import com.apps.dbrah.databinding.FragmentNotificationBinding;
import com.apps.dbrah.databinding.FragmentOrderBinding;
import com.apps.dbrah.mvvm.GeneralMvvm;
import com.apps.dbrah.uis.activity_base.BaseFragment;
import com.apps.dbrah.uis.activity_home.HomeActivity;
import com.apps.dbrah.uis.activity_home.market_module.FragmentHome;


public class FragmentNotification extends BaseFragment {
    private HomeActivity activity;
    private FragmentNotificationBinding binding;
    private GeneralMvvm generalMvvm;


    public static FragmentNotification newInstance() {
        return new FragmentNotification();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        View view = activity.setUpToolbar(binding.toolbar, getString(R.string.notifications), R.color.white, R.color.black, R.drawable.small_rounded_grey4, false);
        view.setOnClickListener(v -> {
            generalMvvm.onHomeBackNavigate().setValue(true);

        });
        generalMvvm.getOnUserLoggedIn().observe(this, loggedIn -> {
            if (loggedIn) {
                if (getUserModel() != null) {
                    //mvvm.getAddresses(getUserModel().getData().getId());
                }

            }
        });

        generalMvvm.getOnLoggedOutSuccess().observe(this, loggedOut -> {
            if (loggedOut) {
                //mvvm.getAddresses(null);

            }
        });

    }


}