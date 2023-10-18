package com.app.dbrah.uis.activity_current_order_details;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.dbrah.R;
import com.app.dbrah.adapter.OfferAdapter;
import com.app.dbrah.adapter.OrderProductAdapter;
import com.app.dbrah.databinding.ActivityCurrentOrderDetailsBinding;
import com.app.dbrah.model.NotiFire;
import com.app.dbrah.model.OrderModel;
import com.app.dbrah.mvvm.ActivityOrderDetailsMvvm;
import com.app.dbrah.uis.activity_base.BaseActivity;
import com.app.dbrah.uis.activity_offer_details.OfferDetailsActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CurrentOrderDetailsActivity extends BaseActivity {
    private ActivityOrderDetailsMvvm mvvm;
    private ActivityCurrentOrderDetailsBinding binding;
    private OfferAdapter adapter;
    private OrderProductAdapter orderProductAdapter;
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

    }

    private void initView() {

        binding.tvCancel.setPaintFlags(binding.tvCancel.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                update = true;
                if (result.getData() != null && result.getData().getStringExtra("data") != null) {
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
        mvvm = ViewModelProviders.of(this).get(ActivityOrderDetailsMvvm.class);
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
                if(orderProductAdapter!=null){
                    orderProductAdapter.updateList(orderModel.getDetails());
                }
            }
        });

        mvvm.getOrderDetails(order_id);

        mvvm.getOnOrderCanceled().observe(this,isCanceled->{
            if (isCanceled){
                setResult(RESULT_OK);
                finish();
            }
        });

        adapter = new OfferAdapter(this, getLang());
        binding.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recView.setAdapter(adapter);
        orderProductAdapter = new OrderProductAdapter(this,null,getLang());
        binding.recViewProduct.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewProduct.setAdapter(orderProductAdapter);
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        binding.swipeRefresh.setOnRefreshListener(() -> mvvm.getOrderDetails(order_id));

        binding.llCancelOrder.setOnClickListener(v -> {
            mvvm.acceptCancelOrder(order_id, this);
        });
        binding.tvCancel.setOnClickListener(view -> mvvm.refuseAllOffer(order_id,  this));

        if (getUserModel() != null) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }
    }

    public void show(OrderModel.Offers offers) {
        Intent intent = new Intent(this, OfferDetailsActivity.class);
        intent.putExtra("offer_id", offers.getId());
        launcher.launch(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOrderStatusChanged(NotiFire model) {
        if (!model.getOrder_status().isEmpty()) {
            String status = model.getOrder_status();
            Log.e("status",status+"");
            mvvm.getOrderDetails(order_id);
        }
    }


    @Override
    public void onBackPressed() {
        if (update) {
            setResult(RESULT_OK);
        }
        finish();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}