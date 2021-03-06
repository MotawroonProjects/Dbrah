package com.apps.dbrah.uis.activity_contact_us;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.apps.dbrah.R;
import com.apps.dbrah.adapter.SpinnerOrderAdapter;
import com.apps.dbrah.databinding.ActivityContactUsBinding;
import com.apps.dbrah.model.ContactUsModel;
import com.apps.dbrah.model.OrderModel;
import com.apps.dbrah.model.UserModel;
import com.apps.dbrah.mvvm.ActivityContactUsMvvm;
import com.apps.dbrah.preferences.Preferences;
import com.apps.dbrah.uis.activity_base.BaseActivity;

public class ContactUsActivity extends BaseActivity {
    private ActivityContactUsBinding binding;
    private ContactUsModel contactUsModel;
    private ActivityContactUsMvvm contactusActivityMvvm;
    private UserModel userModel;
    private Preferences preferences;
    private SpinnerOrderAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_contact_us);
        initView();

    }

    private void initView() {
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        contactusActivityMvvm = ViewModelProviders.of(this).get(ActivityContactUsMvvm.class);
        setUpToolbar(binding.toolbar, getString(R.string.contact_us), R.color.white, R.color.black, R.drawable.small_rounded_grey4, true);
        binding.setModel(userModel);
        contactUsModel = new ContactUsModel();
//        if (userModel != null) {
//            contactUsModel.setName(userModel.getData().getUser().getName());
//            if(userModel.getData().getUser().getEmail()!=null) {
//                contactUsModel.setEmail(userModel.getData().getUser().getEmail());
//            }
//        }



        binding.setContactModel(contactUsModel);

        adapter = new SpinnerOrderAdapter(this);
        binding.spinner.setAdapter(adapter);
        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                contactUsModel.setOrder_id(((OrderModel)parent.getSelectedItem()).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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


}