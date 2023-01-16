package com.app.dbrah.uis.activity_contact_us;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.app.dbrah.R;
import com.app.dbrah.adapter.OrderAutoAdapter;
import com.app.dbrah.adapter.SpinnerOrderAdapter;
import com.app.dbrah.databinding.ActivityContactUsBinding;
import com.app.dbrah.model.ContactUsModel;
import com.app.dbrah.model.UserModel;
import com.app.dbrah.mvvm.ActivityContactUsMvvm;
import com.app.dbrah.preferences.Preferences;
import com.app.dbrah.uis.activity_base.BaseActivity;

public class ContactUsActivity extends BaseActivity {
    private ActivityContactUsBinding binding;
    private ContactUsModel contactUsModel;
    private ActivityContactUsMvvm contactusActivityMvvm;
    private UserModel userModel;
    private Preferences preferences;
    private SpinnerOrderAdapter adapter;
    private OrderAutoAdapter productsAdapter;
    private String query;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);
        initView();

    }

    private void initView() {
        productsAdapter = new OrderAutoAdapter(this, R.layout.product_auto_row);
        binding.edtSearch.setAdapter(productsAdapter);

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        contactusActivityMvvm = ViewModelProviders.of(this).get(ActivityContactUsMvvm.class);
        setUpToolbar(binding.toolbar, getString(R.string.contact_us), R.color.white, R.color.black, R.drawable.small_rounded_grey4, true);
        binding.setModel(userModel);
        contactUsModel = new ContactUsModel();
        if (userModel != null) {
            contactUsModel.setName(userModel.getData().getName());
            if(userModel.getData().getEmail()!=null) {
                contactUsModel.setEmail(userModel.getData().getEmail());
            }
        }
        contactusActivityMvvm.getOnDataSuccess().observe(this, list -> {

            if (productsAdapter != null) {
                productsAdapter.updateList(list);
            }
        });

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                query = binding.edtSearch.getText().toString();
                if (query.isEmpty()) {
                    query = "";
                }
                contactusActivityMvvm.search(query,getUserModel());
               // presenter.getproducts(userModel, stock, query);

            }
        });


        binding.setContactModel(contactUsModel);

        adapter = new SpinnerOrderAdapter(this);
//        binding.spinner.setAdapter(adapter);
//        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                contactUsModel.setOrder_id(((OrderModel)parent.getSelectedItem()).getId());
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        binding.btnSend.setOnClickListener(view -> {
            if (contactUsModel.isDataValid(this)) {
                contactusActivityMvvm.contactUs(this, contactUsModel);
            }
        });
        contactusActivityMvvm.send.observe(this, aBoolean -> {
            if (aBoolean) {
                Toast.makeText(ContactUsActivity.this, getResources().getString(R.string.suc), Toast.LENGTH_LONG).show();
                finish();
            }
        });
        binding.toolbar.llBack.setOnClickListener(view -> finish());
    }


    public void setorder(String id) {
        contactUsModel.setOrder_id(id);
        binding.setContactModel(contactUsModel);
    }
}