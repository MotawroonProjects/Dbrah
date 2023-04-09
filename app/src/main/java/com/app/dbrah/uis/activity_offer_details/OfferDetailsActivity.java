package com.app.dbrah.uis.activity_offer_details;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.app.dbrah.R;
import com.app.dbrah.adapter.OfferDetialsAdapter;
import com.app.dbrah.databinding.ActivityOfferDetialsBinding;
import com.app.dbrah.model.OrderModel;
import com.app.dbrah.mvvm.ActivityOfferDetailsMvvm;
import com.app.dbrah.uis.activity_base.BaseActivity;
import com.app.dbrah.uis.activity_payment.PaypalwebviewActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OfferDetailsActivity extends BaseActivity {
    private ActivityOfferDetialsBinding binding;
    private OfferDetialsAdapter offerDetialsAdapter;
    private List<OrderModel.OfferDetail> offerList;
    private ActivityOfferDetailsMvvm activityOfferDetailsMvvm;
    private String offer_id;
    private ActivityResultLauncher<Intent> launcher;
    private OrderModel.Offers offers;
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
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if(result.getResultCode()==RESULT_OK){
                activityOfferDetailsMvvm.getOnOrderStatusSuccess().setValue("accepted");
            }
        });
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
                    OfferDetailsActivity.this.offers=offers;
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
activityOfferDetailsMvvm.getPaymenturl().observe(this, new Observer<String>() {
    @Override
    public void onChanged(String s) {
        Intent intent = new Intent(OfferDetailsActivity.this, PaypalwebviewActivity.class);
        intent.putExtra("url",s);

        launcher.launch(intent);
    }
});
        setUpToolbar(binding.toolbar, getString(R.string.offer_detials), R.color.white, R.color.black, R.drawable.small_rounded_grey4, true);
        offerList = new ArrayList<>();
        offerDetialsAdapter = new OfferDetialsAdapter(offerList, this, getLang());
        binding.recViewOffer.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewOffer.setAdapter(offerDetialsAdapter);
        binding.llReject.setOnClickListener(view -> activityOfferDetailsMvvm.acceptRefuseOffer(offer_id, "rejected",null, this));
        binding.llAccept.setOnClickListener(view -> activityOfferDetailsMvvm.acceptRefuseOffer(offer_id, "accepted",offers.getTotal_price()+"", this));
        activityOfferDetailsMvvm.getOfferDetails(offer_id);
    }
}