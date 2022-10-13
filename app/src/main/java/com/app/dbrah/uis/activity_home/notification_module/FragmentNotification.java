package com.app.dbrah.uis.activity_home.notification_module;

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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.dbrah.R;
import com.app.dbrah.adapter.NotificationAdapter;
import com.app.dbrah.databinding.FragmentNotificationBinding;
import com.app.dbrah.databinding.FragmentOrderBinding;
import com.app.dbrah.model.NotificationModel;
import com.app.dbrah.mvvm.FragmentNotificationMvvm;
import com.app.dbrah.mvvm.GeneralMvvm;
import com.app.dbrah.uis.activity_base.BaseFragment;
import com.app.dbrah.uis.activity_home.HomeActivity;
import com.app.dbrah.uis.activity_home.market_module.FragmentHome;


public class FragmentNotification extends BaseFragment {
    private HomeActivity activity;
    private FragmentNotificationBinding binding;
    private FragmentNotificationMvvm mvvm;
    private GeneralMvvm generalMvvm;
    private NotificationAdapter adapter;


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
        mvvm = ViewModelProviders.of(this).get(FragmentNotificationMvvm.class);
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        View view = activity.setUpToolbar(binding.toolbar, getString(R.string.notifications), R.color.white, R.color.black, R.drawable.small_rounded_grey4, false);
        view.setOnClickListener(v -> {
            generalMvvm.onHomeBackNavigate().setValue(true);

        });
        binding.recViewLayout.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        generalMvvm.getOnUserLoggedIn().observe(this, loggedIn -> {
            if (loggedIn) {
                if (getUserModel() != null) {
                    mvvm.getNotifications(getUserModel());
                }

            }
        });

        generalMvvm.getOnLoggedOutSuccess().observe(this, loggedOut -> {
            if (loggedOut) {
                mvvm.getNotifications(null);

            }
        });

        mvvm.getIsLoading().observe(activity, isLoading -> {
            binding.recViewLayout.swipeRefresh.setRefreshing(isLoading);
        });
        mvvm.getOnDataSuccess().observe(activity, list -> {
            binding.recViewLayout.tvNoData.setText(R.string.no_notifications);
            if (list.size() > 0) {
                binding.recViewLayout.tvNoData.setVisibility(View.GONE);
            } else {
                binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);

            }

            if (adapter != null) {
                adapter.updateList(list);
            }
        });
        adapter = new NotificationAdapter(activity, this);
        binding.recViewLayout.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewLayout.recView.setAdapter(adapter);
        binding.recViewLayout.swipeRefresh.setOnRefreshListener(() -> mvvm.getNotifications(getUserModel()));

        mvvm.getNotifications(getUserModel());


    }


    public void orderDetails(NotificationModel notificationModel) {


    }
}