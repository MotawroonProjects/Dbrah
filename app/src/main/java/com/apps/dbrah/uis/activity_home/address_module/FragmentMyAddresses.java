package com.apps.dbrah.uis.activity_home.address_module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.apps.dbrah.R;
import com.apps.dbrah.adapter.AddressAdapter;
import com.apps.dbrah.databinding.FragmentMyAddressesBinding;
import com.apps.dbrah.databinding.FragmentMyListBinding;
import com.apps.dbrah.mvvm.GeneralMvvm;
import com.apps.dbrah.uis.activity_base.BaseFragment;
import com.apps.dbrah.uis.activity_home.HomeActivity;

import java.util.ArrayList;
import java.util.List;


public class FragmentMyAddresses extends BaseFragment {
    private HomeActivity activity;
    private FragmentMyAddressesBinding binding;
    private GeneralMvvm generalMvvm;
    private AddressAdapter addressAdapter;
    private List<Object> addressList;

    public static FragmentMyAddresses newInstance() {
        return new FragmentMyAddresses();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_addresses, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        addressList = new ArrayList<>();
        addressAdapter = new AddressAdapter(addressList, activity);
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        View view = activity.setUpToolbar(binding.toolbar, getString(R.string.delivery_addresses), R.color.white, R.color.black, R.drawable.small_rounded_grey4, false);
        view.setOnClickListener(v -> {
            generalMvvm.onHomeBackNavigate().setValue(true);

        });
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(addressAdapter);


    }


}