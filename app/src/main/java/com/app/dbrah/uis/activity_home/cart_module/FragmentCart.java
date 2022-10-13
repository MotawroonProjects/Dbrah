package com.app.dbrah.uis.activity_home.cart_module;

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

import com.app.dbrah.R;
import com.app.dbrah.adapter.MainCartAdapter;
import com.app.dbrah.databinding.FragmentCartBinding;
import com.app.dbrah.model.AddressModel;
import com.app.dbrah.model.ProductModel;
import com.app.dbrah.model.cart_models.CartModel;
import com.app.dbrah.model.cart_models.CartSingleModel;
import com.app.dbrah.model.cart_models.ManageCartModel;
import com.app.dbrah.mvvm.FragmentCartMvvm;
import com.app.dbrah.mvvm.GeneralMvvm;
import com.app.dbrah.uis.activity_base.BaseFragment;
import com.app.dbrah.uis.activity_home.HomeActivity;
import com.app.dbrah.uis.activity_home.profile_module.FragmentProfile;

import java.util.ArrayList;


public class FragmentCart extends BaseFragment {
    private HomeActivity activity;
    private FragmentCartMvvm mvvm;
    private FragmentCartBinding binding;
    private ManageCartModel manageCartModel;
    private MainCartAdapter adapter;
    private GeneralMvvm generalMvvm;
    private AddressModel selectedAddress;

    private int selectedOrderPos = -1;

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
        mvvm = ViewModelProviders.of(activity).get(FragmentCartMvvm.class);
        binding.setLang(getLang());
        manageCartModel = ManageCartModel.newInstance();
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        generalMvvm.getOnCartRefreshed().observe(activity, isRefreshed -> {
            refreshCart();
        });

        generalMvvm.getOnAddressSelectedForOrder().observe(activity, addressModel -> {
            this.selectedAddress = addressModel;
        });

        mvvm.getOnAllOrderSentSuccess().observe(activity, cartModel -> {
            generalMvvm.onOrderNavigate().setValue(0);
            generalMvvm.getActionFragmentHomeNavigator().setValue(1);

            manageCartModel.clear(activity);
            binding.flOrderAll.setVisibility(View.GONE);
            generalMvvm.getOnCartRefreshed().setValue(true);
            generalMvvm.getOnCurrentOrderRefreshed().setValue(true);
            for (CartModel.CartObject object : cartModel.getCartList()){
                for (ProductModel productModel :object.getProducts()){
                    productModel.setAmount(0);
                    generalMvvm.getOnCartItemUpdated().setValue(productModel);

                }
            }

        });

        
        mvvm.getOnSingleOrderSentSuccess().observe(activity, cartSingleModel -> {
            generalMvvm.onOrderNavigate().setValue(0);
            generalMvvm.getActionFragmentHomeNavigator().setValue(1);

            generalMvvm.getOnCurrentOrderRefreshed().setValue(true);

            if (selectedOrderPos != -1) {
                manageCartModel.deleteMainCategory(selectedOrderPos, activity);
                generalMvvm.getOnCartRefreshed().setValue(true);
                for (CartModel.CartObject object : cartSingleModel.getCartList()){
                    for (ProductModel productModel :object.getProducts()){
                        productModel.setAmount(0);
                        generalMvvm.getOnCartItemUpdated().setValue(productModel);

                    }
                }
                this.selectedOrderPos = -1;

            }
        });

        adapter = new MainCartAdapter(activity, this, getLang());
        binding.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recView.setAdapter(adapter);
        binding.tvNoData.setText(R.string.empty_cart);
        binding.flOrderAll.setOnClickListener(v -> {
            if (getUserModel() != null) {
                manageCartModel.addUser(getUserModel().getData().getId(), activity);

                if (selectedAddress != null) {
                    manageCartModel.addAddress(selectedAddress.getId(), activity);
                    if (manageCartModel.getCartModel()!=null){
                        mvvm.sendAllOrder(activity,manageCartModel.getCartModel());

                    }
                }
                else {
                    generalMvvm.getMyAddressFragmentAction().setValue("forOrder");
                    generalMvvm.onHomeNavigate().setValue(7);

                }
            } else {
                generalMvvm.getActionFragmentHomeNavigator().setValue(8);


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
            this.selectedAddress = null;
            manageCartModel.clear(activity);


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

    public void sendSingleOrder(CartModel.CartObject cartObject, int adapterPosition) {
        selectedOrderPos = adapterPosition;
        CartSingleModel cartSingleModel = new CartSingleModel();
        cartSingleModel.addItem(cartObject);
        if (getUserModel() != null) {
            cartSingleModel.setUser_id(getUserModel().getData().getId());
            if (selectedAddress != null) {
                cartSingleModel.setAddress_id(selectedAddress.getId());
                 mvvm.sendSingleOrder(activity, cartSingleModel);
            } else {
                generalMvvm.getMyAddressFragmentAction().setValue("forOrder");
                generalMvvm.onHomeNavigate().setValue(7);
            }

        } else {
            generalMvvm.getActionFragmentHomeNavigator().setValue(3);

        }


    }


}