package com.apps.dbrah.uis.activity_current_order_details;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.apps.dbrah.R;
import com.apps.dbrah.adapter.OfferAdapter;
import com.apps.dbrah.databinding.ActivityCurrentOrderDetailsBinding;
import com.apps.dbrah.model.OrderModel;
import com.apps.dbrah.mvvm.FragmentOrderDetailsMvvm;
import com.apps.dbrah.uis.activity_base.BaseActivity;
import com.apps.dbrah.uis.activity_offer_details.OfferDetailsActivity;

public class CurrentOrderDetailsActivity extends BaseActivity {
    private FragmentOrderDetailsMvvm mvvm;
    private ActivityCurrentOrderDetailsBinding binding;
    private OfferAdapter adapter;
    private OrderModel orderModel;
    private String order_id;
    private ActivityResultLauncher<Intent> launcher;
    private boolean update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_current_order_details);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        order_id = intent.getStringExtra("data");
        Log.e("order_id", order_id + "__");

    }

    private void initView() {
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                update = true;
                if (result.getData()!=null&&result.getData().getStringExtra("data") != null) {
                    Intent intent = getIntent();
                    intent.putExtra("data", order_id);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    mvvm.getOrderDetails(order_id);
                }
            }
        });
        binding.setLang(getLang());
        mvvm = ViewModelProviders.of(this).get(FragmentOrderDetailsMvvm.class);
        setUpToolbar(binding.toolbar, getString(R.string.order_details), R.color.white, R.color.black, R.drawable.small_rounded_grey4, true);
        mvvm.getIsLoading().observe(this, isLoading -> {
            binding.swipeRefresh.setRefreshing(isLoading);
            if (isLoading) {
                binding.scrollView.setVisibility(View.GONE);
            }
        });

        mvvm.getOnDataSuccess().observe(this, model -> {
            if (model != null) {
                binding.scrollView.setVisibility(View.VISIBLE);
                orderModel = model;
                binding.setModel(orderModel);
                if (orderModel.getOffers().size() > 0) {
                    binding.llNoOffer.setVisibility(View.GONE);
                } else {
                    binding.llNoOffer.setVisibility(View.VISIBLE);

                }

                if (adapter != null) {
                    adapter.updateList(orderModel.getOffers());
                }
            }
        });

        mvvm.getOrderDetails(order_id);

        adapter = new OfferAdapter(this, getLang());
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(adapter);

        binding.swipeRefresh.setOnRefreshListener(() -> mvvm.getOrderDetails(order_id));

    }

    public void show(OrderModel.Offers offers) {
        Intent intent = new Intent(this, OfferDetailsActivity.class);
        intent.putExtra("offer_id", offers.getId());
        launcher.launch(intent);
    }

    @Override
    public void onBackPressed() {
        if (update) {
            setResult(RESULT_OK);
        }
        finish();

    }
}