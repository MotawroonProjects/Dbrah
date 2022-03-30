package com.apps.dbrah.uis.activity_home.market_module;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.apps.dbrah.R;
import com.apps.dbrah.databinding.FragmentOrderBinding;
import com.apps.dbrah.databinding.FragmentSearchBinding;
import com.apps.dbrah.mvvm.GeneralMvvm;
import com.apps.dbrah.uis.activity_base.BaseFragment;
import com.apps.dbrah.uis.activity_home.HomeActivity;


public class FragmentSearch extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private HomeActivity activity;
    private FragmentSearchBinding binding;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    public static FragmentSearch newInstance() {
        return new FragmentSearch();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        binding.setLang(getLang());
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        binding.arrow.setOnClickListener(v -> {
            generalMvvm.getActionMarketBottomNavSearchShow().setValue(true);
            generalMvvm.onMarketFragmentBackNavigate().setValue(true);
        });
    }


}