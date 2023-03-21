package com.app.dbrah.uis.activity_previous_order;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.dbrah.R;
import com.app.dbrah.adapter.OrderAdapter;
import com.app.dbrah.adapter.OrderAutoAdapter;
import com.app.dbrah.adapter.SpinnerOrderAdapter;
import com.app.dbrah.databinding.ActivityContactUsBinding;
import com.app.dbrah.databinding.ActivityPreviousOrderDetailsBinding;
import com.app.dbrah.databinding.ActivityPreviousOrdersBinding;
import com.app.dbrah.databinding.FragmentCurrentOrderBinding;
import com.app.dbrah.model.ContactUsModel;
import com.app.dbrah.model.OrderModel;
import com.app.dbrah.model.UserModel;
import com.app.dbrah.model.cart_models.ManageCartModel;
import com.app.dbrah.mvvm.ActivityContactUsMvvm;
import com.app.dbrah.mvvm.FragmentCurrentOrderMvvm;
import com.app.dbrah.mvvm.GeneralMvvm;
import com.app.dbrah.preferences.Preferences;
import com.app.dbrah.uis.activity_base.BaseActivity;
import com.app.dbrah.uis.activity_home.HomeActivity;
import com.app.dbrah.uis.activity_previous_order_details.PreviousOrderDetailsActivity;

import java.util.ArrayList;

public class PreviousOrderActivity extends BaseActivity {
    private ActivityPreviousOrdersBinding binding;
    private GeneralMvvm generalMvvm;
    private FragmentCurrentOrderMvvm mvvm;
    private OrderAdapter adapter;
    private ManageCartModel manageCartModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_previous_orders);
        initView();

    }

    private void initView() {
        setUpToolbar(binding.toolbar, getString(R.string.prev), R.color.white, R.color.black, R.drawable.small_rounded_grey4, true);
        binding.toolbar.llBack.setOnClickListener(view -> finish());

        manageCartModel = ManageCartModel.newInstance();
        generalMvvm = ViewModelProviders.of(this).get(GeneralMvvm.class);
        mvvm = ViewModelProviders.of(this).get(FragmentCurrentOrderMvvm.class);
        adapter = new OrderAdapter(this, null, getLang());
        binding.recViewLayout.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewLayout.recView.setAdapter(adapter);
        binding.recViewLayout.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.recViewLayout.tvNoData.setText(R.string.no_orders);

        generalMvvm.getOnPreviousOrderRefreshed().observe(this, isRefreshed -> {
            if (isRefreshed) {
                mvvm.getOrders(getUserModel(), "old");
            }
        });

        generalMvvm.getOnLoggedOutSuccess().observe(this, loggedOut -> {
            if (loggedOut) {
                mvvm.getOrders(getUserModel(), "old");

            }
        });

        generalMvvm.getOnUserLoggedIn().observe(this, loggedIn -> {
            if (loggedIn) {
                mvvm.getOrders(getUserModel(), "old");

            }
        });
        mvvm.getIsLoading().observe(this, isLoading -> {
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


    public void navigateToDetails(OrderModel orderModel, int adapterPosition) {
        Intent intent = new Intent(this, PreviousOrderDetailsActivity.class);
        intent.putExtra("data", orderModel.getId());
        startActivity(intent);
    }




}