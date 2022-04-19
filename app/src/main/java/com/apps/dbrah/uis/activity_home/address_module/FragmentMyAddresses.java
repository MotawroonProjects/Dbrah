package com.apps.dbrah.uis.activity_home.address_module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.apps.dbrah.model.AddressModel;
import com.apps.dbrah.mvvm.FragmentMyAddressesMvvm;
import com.apps.dbrah.mvvm.GeneralMvvm;
import com.apps.dbrah.uis.activity_base.BaseFragment;
import com.apps.dbrah.uis.activity_home.HomeActivity;

import java.util.ArrayList;
import java.util.List;


public class FragmentMyAddresses extends BaseFragment {
    private HomeActivity activity;
    private FragmentMyAddressesMvvm mvvm;
    private FragmentMyAddressesBinding binding;
    private GeneralMvvm generalMvvm;
    private AddressAdapter addressAdapter;
    private int selectedAddressPos = -1;
    private String action = "";

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
        mvvm = ViewModelProviders.of(this).get(FragmentMyAddressesMvvm.class);

        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        View view = activity.setUpToolbar(binding.toolbar, getString(R.string.delivery_addresses), R.color.white, R.color.black, R.drawable.small_rounded_grey4, false);
        view.setOnClickListener(v -> {
            generalMvvm.onHomeBackNavigate().setValue(true);

        });

        generalMvvm.getOnAddressAdded().observe(activity, addressModel -> {
            if (mvvm.getOnDataSuccess().getValue() != null) {
                mvvm.getOnDataSuccess().getValue().add(0, addressModel);
                if (addressAdapter != null) {
                    addressAdapter.notifyItemInserted(0);
                }
            }
        });
        generalMvvm.getOnAddressUpdated().observe(activity, addressModel -> {
            if (mvvm.getOnDataSuccess().getValue() != null) {
                if (selectedAddressPos != -1) {
                    mvvm.getOnDataSuccess().getValue().set(selectedAddressPos, addressModel);
                    if (addressAdapter != null) {
                        addressAdapter.notifyItemChanged(selectedAddressPos);
                        selectedAddressPos = -1;
                    }
                }
            }
        });

        mvvm.getIsLoading().observe(activity, isLoading -> {
            binding.swipeRefresh.setRefreshing(isLoading);
        });
        mvvm.getOnDataSuccess().observe(activity, list -> {
            if (addressAdapter != null) {
                addressAdapter.updateList(list);
            }
        });
        binding.llNewAddress.setOnClickListener(v -> {
            generalMvvm.getAddAddressFragmentAction().setValue("add");
            generalMvvm.onHomeNavigate().setValue(4);
        });


        addressAdapter = new AddressAdapter(activity, this);
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(addressAdapter);
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        binding.swipeRefresh.setOnRefreshListener(() -> mvvm.getAddresses(getUserModel().getData().getId()));
        mvvm.getAddresses(getUserModel().getData().getId());

    }


    public void setItemAddress(AddressModel addressModel, int adapterPosition) {
        if (generalMvvm.getMyAddressFragmentAction().getValue() != null && generalMvvm.getMyAddressFragmentAction().getValue().equals("forOrder")) {
            generalMvvm.getOnAddressSelectedForOrder().setValue(addressModel);
            generalMvvm.onHomeBackNavigate().setValue(true);
            generalMvvm.getMyAddressFragmentAction().setValue("");
        }
    }

    public void delete(AddressModel addressModel, int adapterPosition) {

    }
}