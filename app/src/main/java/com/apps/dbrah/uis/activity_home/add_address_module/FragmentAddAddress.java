package com.apps.dbrah.uis.activity_home.add_address_module;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apps.dbrah.R;
import com.apps.dbrah.adapter.SpinnerTimeAdapter;
import com.apps.dbrah.databinding.FragmentAddAddressBinding;
import com.apps.dbrah.model.AddAddressModel;
import com.apps.dbrah.model.AddressModel;
import com.apps.dbrah.model.TimeModel;
import com.apps.dbrah.mvvm.FragmentAddAddressMvvm;
import com.apps.dbrah.mvvm.GeneralMvvm;
import com.apps.dbrah.uis.activity_base.BaseFragment;
import com.apps.dbrah.uis.activity_home.HomeActivity;

import java.util.List;


public class FragmentAddAddress extends BaseFragment {
    private HomeActivity activity;
    private FragmentAddAddressBinding binding;
    private GeneralMvvm generalMvvm;
    private AddressModel addressModel;
    private AddAddressModel model;
    private SpinnerTimeAdapter spinnerTimeAdapter;
    private FragmentAddAddressMvvm fragmentAddAddressMvvm;
    private View view;

    public static FragmentAddAddress newInstance() {
        return new FragmentAddAddress();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_address, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        model = new AddAddressModel();

        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);

        fragmentAddAddressMvvm = ViewModelProviders.of(activity).get(FragmentAddAddressMvvm.class);

        view = activity.setUpToolbar(binding.toolbar, getString(R.string.add_address), R.color.white, R.color.black, R.drawable.small_rounded_grey4, false);

        fragmentAddAddressMvvm.getOnDataSuccess().observe(this, new Observer<List<TimeModel>>() {
            @Override
            public void onChanged(List<TimeModel> timeModels) {

                if (spinnerTimeAdapter != null) {
                    spinnerTimeAdapter.updateList(timeModels);

                }


            }
        });

        generalMvvm.getOnAddressSelectedForUpdate().observe(activity, addressModel -> {
            this.addressModel = addressModel;
            model.setTitle(addressModel.getTitle());
            model.setAdmin_name(addressModel.getAdmin_name());
            model.setPhone(addressModel.getPhone());
            model.setTime_id(addressModel.getTime_id());
            model.setAddress(addressModel.getAddress());
            model.setLat(Double.parseDouble(addressModel.getLatitude()));
            model.setLng(Double.parseDouble(addressModel.getLongitude()));
            binding.setModel(model);
            view = activity.setUpToolbar(binding.toolbar, getString(R.string.upd_address), R.color.white, R.color.black, R.drawable.small_rounded_grey4, false);
            binding.btnAdd.setText(getString(R.string.update));

        });

        view.setOnClickListener(v -> {
            generalMvvm.onHomeBackNavigate().setValue(true);

        });

        binding.setModel(model);

        spinnerTimeAdapter = new SpinnerTimeAdapter(activity);

        binding.spinner.setAdapter(spinnerTimeAdapter);

        fragmentAddAddressMvvm.getTime(activity);

        binding.btnAdd.setOnClickListener(v -> {
            if (getUserModel() != null) {
                generalMvvm.onHomeBackNavigate().setValue(true);
                model = new AddAddressModel();
                binding.setModel(model);
            }

        });

    }


}