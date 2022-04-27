package com.apps.dbrah.uis.activity_previous_order_details;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.apps.dbrah.R;
import com.apps.dbrah.databinding.ActivityPreviousOrderDetailsBinding;
import com.apps.dbrah.model.ChatUserModel;
import com.apps.dbrah.model.OrderModel;
import com.apps.dbrah.mvvm.ActivityOrderDetailsMvvm;
import com.apps.dbrah.uis.activity_base.BaseActivity;
import com.apps.dbrah.uis.activity_chat.ChatActivity;

public class PreviousOrderDetailsActivity extends BaseActivity {
    private ActivityPreviousOrderDetailsBinding binding;
    private ActivityOrderDetailsMvvm mvvm;
    private OrderModel orderModel;
    private String order_id;
    private ActivityResultLauncher<Intent> launcher;
    private int req;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_previous_order_details);
        getDataFromIntent();
        initView();
    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        order_id = intent.getStringExtra("data");

    }

    private void initView() {
        binding.setLang(getLang());
        mvvm = ViewModelProviders.of(this).get(ActivityOrderDetailsMvvm.class);
        setUpToolbar(binding.toolbar, getString(R.string.order_details), R.color.white, R.color.black, R.drawable.small_rounded_grey4, false);
        binding.toolbar.llBack.setOnClickListener(view -> finish());
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

            }
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == RESULT_OK) {
                orderModel.setStatus("delivered");
                binding.setModel(orderModel);
            }
        });
        binding.imageCall.setOnClickListener(v -> {
            if (orderModel.getAccepted_offer() != null) {
                String phone = orderModel.getAccepted_offer().getProvider().getPhone_code() + orderModel.getAccepted_offer().getProvider().getPhone();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                startActivity(intent);
            } else {
                Toast.makeText(this, "Un Available", Toast.LENGTH_SHORT).show();
            }


        });

        binding.imageChat.setOnClickListener(v -> {
            if (orderModel.getAccepted_offer() != null) {
                req = 1;
                ChatUserModel model = new ChatUserModel(orderModel.getAccepted_offer().getProvider().getId(), orderModel.getAccepted_offer().getProvider().getFake_name(), orderModel.getAccepted_offer().getProvider().getPhone_code() + orderModel.getAccepted_offer().getProvider().getPhone(), orderModel.getAccepted_offer().getProvider().getImage(), order_id);
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra("data", model);
                launcher.launch(intent);

            }


        });

        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        mvvm.getOrderDetails(order_id);
        binding.swipeRefresh.setOnRefreshListener(() -> mvvm.getOrderDetails(order_id));

    }
}