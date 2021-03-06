package com.apps.dbrah.uis.activity_offer_details;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.apps.dbrah.R;
import com.apps.dbrah.adapter.OfferDetialsAdapter;
import com.apps.dbrah.databinding.ActivityOfferDetialsBinding;
import com.apps.dbrah.model.OrderModel;
import com.apps.dbrah.mvvm.ActivityOfferDetailsMvvm;
import com.apps.dbrah.uis.activity_base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class OfferDetailsActivity extends BaseActivity {
    private ActivityOfferDetialsBinding binding;
    private OfferDetialsAdapter offerDetialsAdapter;
    private List<OrderModel.OfferDetail> offerList;
    private ActivityOfferDetailsMvvm activityOfferDetailsMvvm;
    private String offer_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_offer_detials);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        offer_id = intent.getStringExtra("offer_id");

    }

    private void initView() {
        activityOfferDetailsMvvm = ViewModelProviders.of(this).get(ActivityOfferDetailsMvvm.class);
        activityOfferDetailsMvvm.getIsLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    binding.lldata.setVisibility(View.GONE);
                    binding.progBar.setVisibility(View.VISIBLE);
                } else {
                    binding.progBar.setVisibility(View.GONE);
                    binding.lldata.setVisibility(View.VISIBLE);
                }
            }
        });
        activityOfferDetailsMvvm.getOnDataSuccess().observe(this, new Observer<OrderModel.Offers>() {
            @Override
            public void onChanged(OrderModel.Offers offers) {
                if (offers != null) {
                    offerDetialsAdapter.updateList(offers.getOffer_details());
                    binding.setModel(offers);
                }
            }
        });
        activityOfferDetailsMvvm.getOnOrderStatusSuccess().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {
                    if (s.equals("accepted")) {
                        Intent intent = getIntent();
                        intent.putExtra("data", "accept");
                        setResult(RESULT_OK, intent);

                    } else {
                        setResult(RESULT_OK);
                    }
                    finish();
                }
            }
        });

        setUpToolbar(binding.toolbar, getString(R.string.offer_detials), R.color.white, R.color.black, R.drawable.small_rounded_grey4, true);
        offerList = new ArrayList<>();
        offerDetialsAdapter = new OfferDetialsAdapter(offerList, this, getLang());
        binding.recViewOffer.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewOffer.setAdapter(offerDetialsAdapter);
        binding.llReject.setOnClickListener(view -> activityOfferDetailsMvvm.acceptRefuseOffer(offer_id, "rejected", this));
        binding.llAccept.setOnClickListener(view -> activityOfferDetailsMvvm.acceptRefuseOffer(offer_id, "accepted", this));
        activityOfferDetailsMvvm.getOfferDetails(offer_id);
    }
}