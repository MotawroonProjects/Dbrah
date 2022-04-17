package com.apps.dbrah.uis.activity_previous_order_details;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.apps.dbrah.R;
import com.apps.dbrah.databinding.ActivityPreviousOrderDetailsBinding;
import com.apps.dbrah.uis.activity_base.BaseActivity;

public class PreviousOrderDetailsActivity extends BaseActivity {
    private ActivityPreviousOrderDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_previous_order_details);
        initView();
    }

    private void initView() {
        setUpToolbar(binding.toolbar, getString(R.string.order_details), R.color.white, R.color.black, R.drawable.small_rounded_grey4, false);
        binding.toolbar.llBack.setOnClickListener(view -> finish());
    }
}