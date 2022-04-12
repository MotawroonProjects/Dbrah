package com.apps.dbrah.uis.activity_home.product_detials_module;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.apps.dbrah.R;
import com.apps.dbrah.adapter.ProductSliderAdapter;
import com.apps.dbrah.databinding.FragmentProductDetialsBinding;
import com.apps.dbrah.databinding.FragmentSearchBinding;
import com.apps.dbrah.model.ProductModel;
import com.apps.dbrah.mvvm.FragmentProductDetailsMvvm;
import com.apps.dbrah.mvvm.GeneralMvvm;
import com.apps.dbrah.uis.FragmentBaseNavigation;
import com.apps.dbrah.uis.activity_base.BaseFragment;
import com.apps.dbrah.uis.activity_home.HomeActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class FragmentProductDetials extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private FragmentProductDetailsMvvm fragmentProductDetailsMvvm;
    private HomeActivity activity;
    private FragmentProductDetialsBinding binding;
    private String productId;
    private ProductSliderAdapter sliderAdapter;
    private List<ProductModel.Image> imagesList;
    private Timer timer;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    public static FragmentProductDetials newInstance() {
        return new FragmentProductDetials();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_detials, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();

    }

    private void initView() {
        imagesList = new ArrayList<>();
        binding.setLang(getLang());
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        fragmentProductDetailsMvvm = ViewModelProviders.of(activity).get(FragmentProductDetailsMvvm.class);
        generalMvvm.getProduct_id().observe(activity, product_id -> {
            if (product_id != null) {
                productId = product_id;
                fragmentProductDetailsMvvm.getSingleProduct(productId);
            }
        });
        fragmentProductDetailsMvvm.getOnDataSuccess().observe(this, new Observer<ProductModel>() {
            @Override
            public void onChanged(ProductModel productModel) {
                if (productModel != null) {
                    updateData(productModel);
                }
            }
        });
        binding.llBack.setOnClickListener(v -> {
            generalMvvm.onHomeBackNavigate().setValue(true);
        });
        sliderAdapter = new ProductSliderAdapter(imagesList, activity);
        binding.pager.setAdapter(sliderAdapter);
        binding.pager.setClipToPadding(false);
        binding.tab.setViewPager(binding.pager);

    }

    private void updateData(ProductModel productModel) {
        binding.setPrductModel(productModel);
        imagesList.clear();
        if (productModel.getImages().size() > 0) {
            imagesList.addAll(productModel.getImages());
            sliderAdapter.notifyDataSetChanged();
            timer = new Timer();
            timer.scheduleAtFixedRate(new MyTask(), 3000, 3000);
            binding.fl.setVisibility(View.VISIBLE);
            binding.image.setVisibility(View.GONE);
            binding.pager.setVisibility(View.VISIBLE);
        } else {
            binding.fl.setVisibility(View.GONE);
            binding.image.setVisibility(View.VISIBLE);
            binding.pager.setVisibility(View.GONE);
        }

    }

    public class MyTask extends TimerTask {
        @Override
        public void run() {
            activity.runOnUiThread(() -> {
                int current_page = binding.pager.getCurrentItem();
                if (current_page < sliderAdapter.getCount() - 1) {
                    binding.pager.setCurrentItem(binding.pager.getCurrentItem() + 1);
                } else {
                    binding.pager.setCurrentItem(0);

                }
            });

        }

    }

}