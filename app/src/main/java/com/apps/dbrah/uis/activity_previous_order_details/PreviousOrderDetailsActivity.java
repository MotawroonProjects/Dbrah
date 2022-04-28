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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.apps.dbrah.R;
import com.apps.dbrah.databinding.ActivityPreviousOrderDetailsBinding;
import com.apps.dbrah.databinding.BottomSheetRateDialogBinding;
import com.apps.dbrah.model.ChatUserModel;
import com.apps.dbrah.model.NotiFire;
import com.apps.dbrah.model.OrderModel;
import com.apps.dbrah.mvvm.ActivityOrderDetailsMvvm;
import com.apps.dbrah.share.Common;
import com.apps.dbrah.uis.activity_base.BaseActivity;
import com.apps.dbrah.uis.activity_chat.ChatActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class PreviousOrderDetailsActivity extends BaseActivity {
    private ActivityPreviousOrderDetailsBinding binding;
    private ActivityOrderDetailsMvvm mvvm;
    private OrderModel orderModel;
    private String order_id;
    private ActivityResultLauncher<Intent> launcher;
    private int req;
    private boolean isOrderStatusChanged = false;

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
        binding.toolbar.llBack.setOnClickListener(view -> onBackPressed());
        mvvm.getIsLoading().observe(this, isLoading -> {
            binding.swipeRefresh.setRefreshing(isLoading);
            if (isLoading) {
                binding.scrollView.setVisibility(View.GONE);
            }
        });

        mvvm.getOnRateSuccess().observe(this, success -> {
            if (success) {
                orderModel.setProvider_rated("true");
                binding.setModel(orderModel);
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

        if (getUserModel() != null) {
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this);
            }
        }

        binding.addRate.setOnClickListener(v -> {
            openSheet(orderModel);
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOrderStatusChanged(NotiFire model) {
        if (!model.getOrder_status().isEmpty()) {
            String status = model.getOrder_status();
            Log.e("status",status+"");
            orderModel.setStatus(status);
            binding.setModel(orderModel);
            isOrderStatusChanged = true;
        }
    }



    public void openSheet(OrderModel model) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        BottomSheetRateDialogBinding dialogBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.bottom_sheet_rate_dialog, null, false);
        dialog.setContentView(dialogBinding.getRoot());
        dialog.setCanceledOnTouchOutside(false);
        dialogBinding.setModel(model);

        dialogBinding.btnAddRate.setOnClickListener(v -> {
            float rate = dialogBinding.rateBar.getRating();
            String comment = dialogBinding.edtComment.getText().toString();
            dialogBinding.edtComment.setError(null);
            Common.CloseKeyBoard(this, dialogBinding.edtComment);
            mvvm.rateOrder(getUserModel(), model, rate, comment, this);
            dialog.dismiss();


        });
        dialog.show();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isOrderStatusChanged) {
            Intent intent = getIntent();
            intent.putExtra("order_status", orderModel.getStatus());
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