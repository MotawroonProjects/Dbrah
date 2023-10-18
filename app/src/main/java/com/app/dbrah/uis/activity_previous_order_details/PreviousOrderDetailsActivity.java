package com.app.dbrah.uis.activity_previous_order_details;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.app.dbrah.R;
import com.app.dbrah.adapter.OrderProductAdapter;
import com.app.dbrah.databinding.ActivityPreviousOrderDetailsBinding;
import com.app.dbrah.databinding.BottomSheetRateDialogBinding;
import com.app.dbrah.model.ChatUserModel;
import com.app.dbrah.model.NotiFire;
import com.app.dbrah.model.OrderModel;
import com.app.dbrah.mvvm.ActivityOrderDetailsMvvm;
import com.app.dbrah.share.Common;
import com.app.dbrah.uis.activity_base.BaseActivity;
import com.app.dbrah.uis.activity_chat.ChatActivity;
import com.app.dbrah.uis.activity_contact_us.ContactUsActivity;
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
    private boolean  isOrderStatusChanged = false;
    private Intent intent;
    private static final int REQUEST_PHONE_CALL = 1;
    private String rate;
    private OrderProductAdapter orderProductAdapter;

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
        rate = intent.getStringExtra("rate");

    }

    private void initView() {
        orderProductAdapter = new OrderProductAdapter(this,null,getLang());
        binding.recViewProduct.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewProduct.setAdapter(orderProductAdapter);
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
        binding.llCancelOrder.setOnClickListener(v -> {
            Intent intent = new Intent(this, ContactUsActivity.class);
            startActivity(intent);
        });
        mvvm.getOnRateSuccess().observe(this, success -> {
            if (success) {
                orderModel.setProvider_rated("true");
                binding.setModel(orderModel);
                if(orderProductAdapter!=null){
                    orderProductAdapter.updateList(orderModel.getDetails());
                }
            }
        });
        mvvm.getOnDataSuccess().observe(this, model -> {
            if (model != null) {
                binding.scrollView.setVisibility(View.VISIBLE);
                orderModel = model;
                binding.setModel(orderModel);
                Log.e("D;dldlld",orderModel.getStatus());
                if(rate!=null){
                    openSheet(orderModel);
                }
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
                 intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(PreviousOrderDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(PreviousOrderDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                    } else {
                        startActivity(intent);
                    }
                } else {
                    startActivity(intent);
                }
            } else {
                Toast.makeText(this, "Un Available", Toast.LENGTH_SHORT).show();
            }


        });

        binding.imageChat.setOnClickListener(v -> {
            if (orderModel.getAccepted_offer() != null) {
                req = 1;
                ChatUserModel model = new ChatUserModel(orderModel.getAccepted_offer().getProvider().getId(), getUserModel().getData().getId(), "", orderModel.getAccepted_offer().getProvider().getName(), orderModel.getAccepted_offer().getProvider().getPhone(), orderModel.getAccepted_offer().getProvider().getImage(), order_id);
                Intent intent = new Intent(this, ChatActivity.class);
                intent.putExtra("data", model);
                launcher.launch(intent);

            }


        });
                binding.imCallReprentative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderModel.getOrder_representative().getRepresentative() != null) {
                    intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", orderModel.getOrder_representative().getRepresentative().getPhone_code() + orderModel.getOrder_representative().getRepresentative().getPhone(), null));
                }
                if (intent != null) {
                    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(PreviousOrderDetailsActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(PreviousOrderDetailsActivity.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                        } else {
                            startActivity(intent);
                        }
                    } else {
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(PreviousOrderDetailsActivity.this, getResources().getString(R.string.not_available), Toast.LENGTH_SHORT).show();

                    // Common.CreateAlertDialog(QuestionsActivity.this, getResources().getString(R.string.phone_not_found));
                }

            }
        });

            binding.imChatRepresent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                req = 1;
                ChatUserModel model = new ChatUserModel("", getUserModel().getData().getId(), orderModel.getOrder_representative().getRepresentative().getId(), orderModel.getOrder_representative().getRepresentative().getName(), orderModel.getOrder_representative().getRepresentative().getPhone(), orderModel.getOrder_representative().getRepresentative().getImage(), order_id);
                Intent intent = new Intent(PreviousOrderDetailsActivity.this, ChatActivity.class);
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