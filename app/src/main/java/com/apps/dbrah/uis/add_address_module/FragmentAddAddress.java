package com.apps.dbrah.uis.add_address_module;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.dbrah.R;
import com.apps.dbrah.adapter.SpinnerTimeAdapter;
import com.apps.dbrah.databinding.FragmentAddAddressBinding;
import com.apps.dbrah.model.TimeModel;
import com.apps.dbrah.mvvm.FragmentAddAddressMvvm;
import com.apps.dbrah.mvvm.GeneralMvvm;
import com.apps.dbrah.uis.activity_base.BaseFragment;
import com.apps.dbrah.uis.activity_home.HomeActivity;
import com.apps.dbrah.uis.activity_home.address_module.FragmentMyAddresses;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.util.List;


public class FragmentAddAddress extends BaseFragment implements OnMapReadyCallback {
    private HomeActivity activity;
    private FragmentAddAddressBinding binding;
    private GeneralMvvm generalMvvm;
    private SpinnerTimeAdapter spinnerTimeAdapter;
    private FragmentAddAddressMvvm fragmentAddAddressMvvm;


    public static FragmentAddAddress newInstance() {
        return new FragmentAddAddress();
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
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

        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        fragmentAddAddressMvvm = ViewModelProviders.of(activity).get(FragmentAddAddressMvvm.class);
        View view = activity.setUpToolbar(binding.toolbar, getString(R.string.add_address), R.color.white, R.color.black, R.drawable.small_rounded_grey4, false);
        view.setOnClickListener(v -> {
            generalMvvm.onHomeBackNavigate().setValue(true);

        });
        fragmentAddAddressMvvm.getOnDataSuccess().observe(this, new Observer<List<TimeModel>>() {
            @Override
            public void onChanged(List<TimeModel> timeModels) {
                if (timeModels.size() > 0) {
                    timeModels.add(0, new TimeModel(getString(R.string.ch_time)));
                    spinnerTimeAdapter.updateList(timeModels);
                }
            }
        });
        spinnerTimeAdapter = new SpinnerTimeAdapter(activity);
        binding.spinner.setAdapter(spinnerTimeAdapter);
        fragmentAddAddressMvvm.getTime();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}