package com.apps.dbrah.uis.activity_preview;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;


import com.apps.dbrah.R;
import com.apps.dbrah.adapter.OfferDetialsAdapter;
import com.apps.dbrah.databinding.ActivityOfferDetialsBinding;
import com.apps.dbrah.uis.activity_base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class OfferDetialsActivity extends BaseActivity {
    private ActivityOfferDetialsBinding binding;
    private OfferDetialsAdapter offerDetialsAdapter;
    private List<Object> offerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_offer_detials);
        initView();
    }

    private void initView() {
        setUpToolbar(binding.toolbar, getString(R.string.offer_detials), R.color.white, R.color.black, R.drawable.small_rounded_grey4, true);
        offerList = new ArrayList<>();
        offerDetialsAdapter = new OfferDetialsAdapter(offerList, this);
        binding.recViewOffer.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewOffer.setAdapter(offerDetialsAdapter);
    }
}