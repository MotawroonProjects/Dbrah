package com.apps.dbrah.uis.activity_home.market_module;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.dbrah.R;

import com.apps.dbrah.adapter.CategoryAdapter;
import com.apps.dbrah.adapter.RecentProductAdapter;
import com.apps.dbrah.adapter.SliderAdapter;
import com.apps.dbrah.databinding.FragmentMarketBinding;
import com.apps.dbrah.model.CategoryDataModel;
import com.apps.dbrah.model.ProductModel;
import com.apps.dbrah.model.RecentProductDataModel;
import com.apps.dbrah.model.SliderDataModel;
import com.apps.dbrah.mvvm.FragmentMarketMvvm;
import com.apps.dbrah.mvvm.GeneralMvvm;
import com.apps.dbrah.uis.activity_base.BaseFragment;
import com.apps.dbrah.uis.activity_home.HomeActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class FragmentMarket extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private HomeActivity activity;
    private FragmentMarketBinding binding;
    private FragmentMarketMvvm fragmentMarketMvvm;
    private SliderAdapter sliderAdapter;
    private List<SliderDataModel.SliderModel> sliderModelList;
    private Timer timer;
    private CategoryAdapter categoryAdapter;
    private RecentProductAdapter recentProductAdapter;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    public static FragmentMarket newInstance() {
        return new FragmentMarket();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_market, container, false);
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
        binding.setLang(getLang());
        sliderModelList = new ArrayList<>();
        fragmentMarketMvvm = ViewModelProviders.of(this).get(FragmentMarketMvvm.class);
        fragmentMarketMvvm.getIsLoadingLiveData().observe(activity, isLoading -> {
            if (isLoading) {
                binding.progBarSlider.setVisibility(View.VISIBLE);
            }
        });
        fragmentMarketMvvm.getSliderDataModelMutableLiveData().observe(activity, sliderDataModel -> {
            if (sliderDataModel.getData() != null) {
                binding.progBarSlider.setVisibility(View.GONE);
                sliderModelList.clear();
                sliderModelList.addAll(sliderDataModel.getData());
                sliderAdapter.notifyDataSetChanged();
                timer = new Timer();
                timer.scheduleAtFixedRate(new MyTask(), 3000, 3000);

            }
        });

        categoryAdapter = new CategoryAdapter(activity, this, getLang());
        binding.recViewCategory.setLayoutManager(new GridLayoutManager(activity, 2));
        binding.recViewCategory.setAdapter(categoryAdapter);
        fragmentMarketMvvm.getCategoryModelLiveData().observe(activity, categoryModels -> {
            if (categoryModels.size() > 0) {
                binding.tvNoCategories.setVisibility(View.GONE);
                categoryAdapter.updateList(categoryModels);
            } else {
                binding.tvNoCategories.setVisibility(View.VISIBLE);
            }
        });
        recentProductAdapter = new RecentProductAdapter(activity, this);
        binding.recViewMostRecentProducts.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        binding.recViewMostRecentProducts.setAdapter(recentProductAdapter);
        fragmentMarketMvvm.getRecentModelLiveData().observe(activity, recentProductModels -> {
            if (recentProductModels.size() > 0) {
            Log.e("asda",recentProductModels.size()+"_");
            if (recentProductModels.size()>0){
                binding.tvNoMostRecentProduct.setVisibility(View.GONE);
                recentProductAdapter.updateList(recentProductModels);
            } else {
            }else {
                binding.tvNoMostRecentProduct.setVisibility(View.VISIBLE);
            }
            if (recentProductAdapter!=null){
                recentProductAdapter.updateList(recentProductModels);

            }
        });

        sliderAdapter = new SliderAdapter(sliderModelList, activity);
        binding.pager.setAdapter(sliderAdapter);
        binding.pager.setClipToPadding(false);
        binding.pager.setPadding(20, 0, 20, 0);

        fragmentMarketMvvm.getSlider();
        fragmentMarketMvvm.getCategory();
        fragmentMarketMvvm.getRecentProduct();
    }

    public void showProductDetials(ProductModel productModel) {
        generalMvvm.getProduct_id().setValue(productModel.getId());
        generalMvvm.onHomeNavigate().setValue(6);

    }

    public void showCategoryDetials(CategoryDataModel.CategoryModel categoryModel) {
        generalMvvm.getCat_id().setValue(categoryModel.getId());
        generalMvvm.onHomeNavigate().setValue(7);
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