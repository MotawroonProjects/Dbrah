package com.apps.dbrah.uis.activity_home.cart_module;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.apps.dbrah.R;
import com.apps.dbrah.adapter.MainCartAdapter;
import com.apps.dbrah.databinding.FragmentCartBinding;
import com.apps.dbrah.model.ProductModel;
import com.apps.dbrah.model.cart_models.ManageCartModel;
import com.apps.dbrah.mvvm.GeneralMvvm;
import com.apps.dbrah.uis.activity_base.BaseFragment;
import com.apps.dbrah.uis.activity_home.HomeActivity;
import com.apps.dbrah.uis.activity_home.profile_module.FragmentProfile;

import java.util.ArrayList;


public class FragmentCart extends BaseFragment {
    private HomeActivity activity;
    private FragmentCartBinding binding;
    private ManageCartModel manageCartModel;
    private MainCartAdapter adapter;
    private GeneralMvvm generalMvvm;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    public static FragmentCart newInstance() {
        return new FragmentCart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        manageCartModel = ManageCartModel.newInstance();
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        generalMvvm.getOnCartRefreshed().observe(activity, isRefreshed -> {
            refreshCart();
        });

        binding.recViewLayout.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        adapter = new MainCartAdapter(activity, this, getLang());
        binding.recViewLayout.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewLayout.recView.setAdapter(adapter);
        binding.recViewLayout.tvNoData.setText(R.string.empty_cart);
        refreshCart();

        binding.recViewLayout.swipeRefresh.setOnRefreshListener(this::refreshCart);

    }

    private void refreshCart() {
        if (manageCartModel.getCartList(activity).size() > 0) {
            binding.recViewLayout.tvNoData.setVisibility(View.GONE);
        } else {
            binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);

        }
        adapter.updateList(manageCartModel.getCartList(activity));

        binding.recViewLayout.swipeRefresh.setRefreshing(false);
    }

    public void addProductToCart(ProductModel productModel) {
        manageCartModel.add(productModel, activity);
    }

    public void removeProductFromCart(ProductModel productModel) {
        manageCartModel.delete(productModel, activity);
        generalMvvm.getOnCartRefreshed().setValue(true);

    }


}