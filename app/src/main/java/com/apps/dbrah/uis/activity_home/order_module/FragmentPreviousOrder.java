package com.apps.dbrah.uis.activity_home.order_module;

import android.content.Context;
import android.content.Intent;
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

import com.apps.dbrah.R;
import com.apps.dbrah.adapter.OrderAdapter;
import com.apps.dbrah.databinding.FragmentCurrentOrderBinding;
import com.apps.dbrah.model.OrderModel;
import com.apps.dbrah.mvvm.FragmentCurrentOrderMvvm;
import com.apps.dbrah.mvvm.GeneralMvvm;
import com.apps.dbrah.uis.activity_base.BaseFragment;
import com.apps.dbrah.uis.activity_home.HomeActivity;
import com.apps.dbrah.uis.activity_previous_order_details.PreviousOrderDetailsActivity;

import java.util.ArrayList;
import java.util.List;


public class FragmentPreviousOrder extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private FragmentCurrentOrderMvvm mvvm;
    private HomeActivity activity;
    private FragmentCurrentOrderBinding binding;
    private OrderAdapter adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    public static FragmentPreviousOrder newInstance() {
        return new FragmentPreviousOrder();
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

        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        mvvm = ViewModelProviders.of(this).get(FragmentCurrentOrderMvvm.class);
        adapter = new OrderAdapter(activity, this, getLang());
        binding.recViewLayout.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewLayout.recView.setAdapter(adapter);
        binding.recViewLayout.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.recViewLayout.tvNoData.setText(R.string.no_orders);

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


        binding.recViewLayout.swipeRefresh.setOnRefreshListener(() -> mvvm.getOrders(getUserModel(), "old"));
        mvvm.getOrders(getUserModel(), "old");
    }


    public void navigateToDetails(OrderModel orderModel) {
        Intent intent = new Intent(activity, PreviousOrderDetailsActivity.class);
        startActivity(intent);
    }
}