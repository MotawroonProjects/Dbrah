package com.apps.dbrah.uis.activity_current_order_details;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import com.apps.dbrah.R;
import com.apps.dbrah.adapter.OfferAdapter;
import com.apps.dbrah.adapter.OfferDetialsAdapter;
import com.apps.dbrah.databinding.ActivityCurrentOrderDetailsBinding;
import com.apps.dbrah.uis.activity_base.BaseActivity;
import com.apps.dbrah.uis.activity_preview.OfferDetialsActivity;

import java.util.ArrayList;
import java.util.List;

public class CurrentOrderDetailsActivity extends BaseActivity {
    private ActivityCurrentOrderDetailsBinding binding;
    private OfferAdapter offerDetialsAdapter;
    private List<Object> offerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_current_order_details);
        initView();
    }

    private void initView() {
        setUpToolbar(binding.toolbar, getString(R.string.order_details), R.color.white, R.color.black, R.drawable.small_rounded_grey4, false);
        binding.toolbar.llBack.setOnClickListener(view -> finish());
        offerList = new ArrayList<>();
        offerDetialsAdapter = new OfferAdapter(offerList, this);
        binding.recViewOffer.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewOffer.setAdapter(offerDetialsAdapter);
    }

    public void show() {
        Intent intent = new Intent(this, OfferDetialsActivity.class);
        startActivity(intent);
    }
}