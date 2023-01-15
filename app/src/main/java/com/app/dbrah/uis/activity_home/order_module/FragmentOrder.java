package com.app.dbrah.uis.activity_home.order_module;

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

import com.app.dbrah.R;
import com.app.dbrah.adapter.MyPagerAdapter;
import com.app.dbrah.databinding.FragmentOrderBinding;
import com.app.dbrah.mvvm.GeneralMvvm;
import com.app.dbrah.uis.activity_base.BaseFragment;
import com.app.dbrah.uis.activity_home.HomeActivity;

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

    public static FragmentOrder newInstance() {
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
        generalMvvm.onOrderNavigate().observe(activity, pos -> {
            binding.pager.setCurrentItem(pos);
        });
        setUpPager();
    }

    private void setUpPager() {
        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        fragments.add(FragmentCurrentOrder.newInstance());
        fragments.add(FragmentPreviousOrder.newInstance());
        titles.add(getString(R.string.current));
        titles.add(getString(R.string.re_order));
        adapter = new MyPagerAdapter(getChildFragmentManager(), fragments, titles);
        binding.pager.setAdapter(adapter);
        binding.pager.setOffscreenPageLimit(fragments.size());
        binding.tab.setupWithViewPager(binding.pager);


    }


}