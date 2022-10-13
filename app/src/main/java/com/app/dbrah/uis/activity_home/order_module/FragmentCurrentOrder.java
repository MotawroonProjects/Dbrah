package com.app.dbrah.uis.activity_home.order_module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.dbrah.R;
import com.app.dbrah.adapter.OrderAdapter;
import com.app.dbrah.databinding.FragmentCurrentOrderBinding;
import com.app.dbrah.model.OrderModel;
import com.app.dbrah.mvvm.FragmentCurrentOrderMvvm;
import com.app.dbrah.mvvm.GeneralMvvm;
import com.app.dbrah.uis.activity_base.BaseFragment;
import com.app.dbrah.uis.activity_current_order_details.CurrentOrderDetailsActivity;
import com.app.dbrah.uis.activity_home.HomeActivity;
import com.app.dbrah.uis.activity_previous_order_details.PreviousOrderDetailsActivity;

import java.util.ArrayList;


public class FragmentCurrentOrder extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private FragmentCurrentOrderMvvm mvvm;
    private HomeActivity activity;
    private FragmentCurrentOrderBinding binding;
    private OrderAdapter adapter;
    private ActivityResultLauncher<Intent> launcher;
    private int req;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    public static FragmentCurrentOrder newInstance() {
        return new FragmentCurrentOrder();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_current_order, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                mvvm.getOrders(getUserModel(), "new");

                if (result.getData() != null) {
                    if (result.getData().hasExtra("data")) {
                        if (result.getData().getStringExtra("data") != null) {
                            Intent intent = new Intent(activity, PreviousOrderDetailsActivity.class);
                            intent.putExtra("data", result.getData().getStringExtra("data"));
                            launcher.launch(intent);
                        }
                    } else if (result.getData().hasExtra("order_status")) {

                        if (result.getData().getStringExtra("order_status").equals("delivered")) {
                            generalMvvm.getOnPreviousOrderRefreshed().setValue(true);
                            generalMvvm.onOrderNavigate().setValue(1);
                        }
                    }

                }


            }
        });
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        mvvm = ViewModelProviders.of(this).get(FragmentCurrentOrderMvvm.class);
        adapter = new OrderAdapter(activity, this, getLang());
        binding.recViewLayout.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewLayout.recView.setAdapter(adapter);
        binding.recViewLayout.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.recViewLayout.tvNoData.setText(R.string.no_orders);

        generalMvvm.getOnCurrentOrderRefreshed().observe(activity, isRefreshed -> {
            if (isRefreshed) {
                mvvm.getOrders(getUserModel(), "new");
            }
        });

        generalMvvm.getOnLoggedOutSuccess().observe(activity, loggedOut -> {
            if (loggedOut) {
                mvvm.getOrders(getUserModel(), "new");

            }
        });

        generalMvvm.getOnUserLoggedIn().observe(activity, loggedIn -> {
            if (loggedIn) {
                mvvm.getOrders(getUserModel(), "new");

            }
        });

        mvvm.getIsLoading().observe(activity, isLoading -> {
            binding.recViewLayout.swipeRefresh.setRefreshing(isLoading);
        });
        mvvm.getOnDataSuccess().observe(this, list -> {
            if (list.size() > 0) {
                binding.recViewLayout.tvNoData.setVisibility(View.GONE);
            } else {
                binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);

            }
            if (adapter != null) {
                adapter.updateList(list);
            }
        });

        binding.recViewLayout.swipeRefresh.setOnRefreshListener(() -> mvvm.getOrders(getUserModel(), "new"));
        mvvm.getOrders(getUserModel(), "new");

    }


    public void navigateToDetails(OrderModel orderModel) {
        req = 1;
        Intent intent;
        if (orderModel.getStatus().equals("new") || orderModel.getStatus().equals("offered")) {
            intent = new Intent(activity, CurrentOrderDetailsActivity.class);
        } else {
            intent = new Intent(activity, PreviousOrderDetailsActivity.class);
        }
        intent.putExtra("data", orderModel.getId());
        launcher.launch(intent);
    }
}