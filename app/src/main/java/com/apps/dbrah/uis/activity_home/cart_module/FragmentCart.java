package com.apps.dbrah.uis.activity_home.cart_module;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
        binding.setLang(getLang());
        manageCartModel = ManageCartModel.newInstance();
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        generalMvvm.getOnCartRefreshed().observe(activity, isRefreshed -> {
            refreshCart();
        });

        adapter = new MainCartAdapter(activity, this, getLang());
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(adapter);
        binding.tvNoData.setText(R.string.empty_cart);
        binding.flOrderAll.setOnClickListener(v -> {
            if (getUserModel() != null) {
                generalMvvm.onHomeNavigate().setValue(9);
            } else {
                generalMvvm.getActionFragmentHomeNavigator().setValue(3);
            }
        });

        refreshCart();


    }

    private void refreshCart() {
        if (manageCartModel.getCartList(activity).size() > 0) {
            if (manageCartModel.getCartList(activity).size() > 1) {
                binding.flOrderAll.setVisibility(View.VISIBLE);
            } else {
                binding.flOrderAll.setVisibility(View.GONE);

            }
            binding.tvNoData.setVisibility(View.GONE);
        } else {
            binding.tvNoData.setVisibility(View.VISIBLE);

        }
        adapter.updateList(manageCartModel.getCartList(activity));

    }

    public void addProductToCart(ProductModel productModel) {
        generalMvvm.getOnCartItemUpdated().setValue(productModel);
        manageCartModel.add(productModel, activity);
    }

    public void removeProductFromCart(ProductModel productModel) {
        productModel.setAmount(0);
        generalMvvm.getOnCartItemUpdated().setValue(productModel);
        manageCartModel.delete(productModel, activity);
        generalMvvm.getOnCartRefreshed().setValue(true);

    }


}