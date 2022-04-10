package com.apps.dbrah.uis.activity_home.products_module;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.dbrah.R;
import com.apps.dbrah.adapter.MainProductCategoryAdapter;
import com.apps.dbrah.adapter.SubProductCategoryAdapter;
import com.apps.dbrah.databinding.FragmentProductsBinding;
import com.apps.dbrah.mvvm.GeneralMvvm;
import com.apps.dbrah.uis.activity_base.BaseFragment;
import com.apps.dbrah.uis.activity_home.HomeActivity;
import com.apps.dbrah.uis.activity_home.complete_order_module.FragmentCompleteOrder;


public class FragmentProducts extends BaseFragment {
    private HomeActivity activity;
    private GeneralMvvm generalMvvm;
    private FragmentProductsBinding binding;
    private MainProductCategoryAdapter mainProductCategoryAdapter;
    private SubProductCategoryAdapter subProductCategoryAdapter;

    public static FragmentProducts newInstance() {
        return new FragmentProducts();
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_products, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        mainProductCategoryAdapter=new MainProductCategoryAdapter(activity,this);
        subProductCategoryAdapter=new SubProductCategoryAdapter(activity,this);
        binding.recViewMain.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL,false));
        binding.recViewSub.setLayoutManager(new LinearLayoutManager(activity,RecyclerView.HORIZONTAL,false));
        binding.recViewMain.setAdapter(mainProductCategoryAdapter);
        binding.recViewSub.setAdapter(subProductCategoryAdapter);
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        View view = activity.setUpToolbar(binding.toolbar, getString(R.string.delivery_addresses), R.color.white, R.color.black, R.drawable.small_rounded_grey4, false);
        view.setOnClickListener(v -> {
            generalMvvm.onHomeBackNavigate().setValue(true);
        });
    }
}