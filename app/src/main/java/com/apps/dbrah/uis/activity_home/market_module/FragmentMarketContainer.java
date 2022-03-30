package com.apps.dbrah.uis.activity_home.market_module;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.apps.dbrah.databinding.FragmentMarketBinding;
import com.apps.dbrah.databinding.FragmentMarketContainerBinding;
import com.apps.dbrah.mvvm.GeneralMvvm;
import com.apps.dbrah.uis.activity_base.BaseFragment;
import com.apps.dbrah.uis.activity_home.HomeActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class FragmentMarketContainer extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private HomeActivity activity;
    private FragmentMarketContainerBinding binding;
    private List<Fragment> fragments;
    private MyPagerAdapter adapter;
    private Stack<Integer> stack;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    public static FragmentMarketContainer newInstance() {
        return new FragmentMarketContainer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_market_container, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        stack = new Stack<>();
        stack.push(0);
        fragments = new ArrayList<>();
        fragments.add(FragmentMarket.newInstance());
        fragments.add(FragmentSearch.newInstance());
        generalMvvm.onMarketFragmentBackNavigate().observe(activity, bBoolean -> {
            if (bBoolean) {
                onBackPress();
            }
        });

        generalMvvm.onMarketFragmentForwardNavigate().observe(activity, pagePos -> {
            if (pagePos != binding.pager.getCurrentItem()) {
                binding.pager.setCurrentItem(pagePos);
                stack.push(pagePos);
            }

        });

        adapter = new MyPagerAdapter(getChildFragmentManager(), fragments, null);
        binding.pager.setAdapter(adapter);
    }

    public boolean onBackPress() {
        if (stack.size() > 1) {
            stack.pop();
            binding.pager.setCurrentItem(stack.peek());
            return true;
        } else {
            generalMvvm.getActionMarketBottomNavSearchShow().setValue(true);
            return false;
        }
    }


}