package com.apps.dbrah.uis.activity_home.order_module;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.apps.dbrah.R;
import com.apps.dbrah.adapter.MyPagerAdapter;
import com.apps.dbrah.databinding.FragmentOrderBinding;
import com.apps.dbrah.mvvm.GeneralMvvm;
import com.apps.dbrah.uis.activity_base.BaseFragment;
import com.apps.dbrah.uis.activity_home.HomeActivity;
import com.apps.dbrah.uis.activity_home.market_module.FragmentMarket;

import java.util.ArrayList;
import java.util.List;


public class FragmentOrder extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private HomeActivity activity;
    private FragmentOrderBinding binding;
    private MyPagerAdapter adapter;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    public static FragmentOrder newInstance(){
        return new FragmentOrder();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_order, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        binding.setNotificationCount("0");
        binding.flNotification.setOnClickListener(v -> generalMvvm.onNotificationNavigate().setValue(1));
        setUpPager();
    }

    private void setUpPager() {
        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        fragments.add(FragmentCurrentOrder.newInstance());
        fragments.add(FragmentPreviousOrder.newInstance());
        titles.add(getString(R.string.current));
        titles.add(getString(R.string.prev));
        adapter = new MyPagerAdapter(getChildFragmentManager(),fragments,titles);
        binding.pager.setAdapter(adapter);
        binding.pager.setOffscreenPageLimit(fragments.size());
        binding.tab.setupWithViewPager(binding.pager);

        for (int index =0;index<binding.tab.getTabCount();index++){

        }

    }


}