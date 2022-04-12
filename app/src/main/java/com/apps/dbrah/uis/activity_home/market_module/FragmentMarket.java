package com.apps.dbrah.uis.activity_home.market_module;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.dbrah.R;

import com.apps.dbrah.adapter.CategoryAdapter;
import com.apps.dbrah.adapter.MostSaleProductAdapter;
import com.apps.dbrah.adapter.RecentProductAdapter;
import com.apps.dbrah.adapter.SliderAdapter;
import com.apps.dbrah.databinding.FragmentMarketBinding;
import com.apps.dbrah.model.CategoryDataModel;
import com.apps.dbrah.model.CategoryModel;
import com.apps.dbrah.model.ProductModel;
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
    private FragmentMarketMvvm mvvm;
    private SliderAdapter sliderAdapter;
    private Timer timer;
    private CategoryAdapter categoryAdapter;
    private RecentProductAdapter recentProductAdapter;
    private MostSaleProductAdapter mostSaleProductAdapter;


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
        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        mvvm = ViewModelProviders.of(this).get(FragmentMarketMvvm.class);
        mvvm.getIsLoadingSlider().observe(activity, isLoading -> {
            if (!isLoading) {
                binding.loaderSlider.stopShimmer();
                binding.loaderSlider.setVisibility(View.GONE);
            }
        });
        mvvm.getIsLoadingCategory().observe(activity, isLoading -> {
            if (!isLoading) {
                binding.loaderCategory.stopShimmer();
                binding.loaderCategory.setVisibility(View.GONE);
            }
        });
        mvvm.getIsLoadingRecentProduct().observe(activity, isLoading -> {
            if (!isLoading) {
                binding.loaderRecent.stopShimmer();
                binding.loaderRecent.setVisibility(View.GONE);
            }
        });

        mvvm.getIsLoadingMostSaleProduct().observe(activity, isLoading -> {
            if (!isLoading) {
                binding.loaderMostSales.stopShimmer();
                binding.loaderMostSales.setVisibility(View.GONE);
            }
        });

        mvvm.getOnSliderDataSuccess().observe(activity, list -> {
            binding.swipeRefresh.setRefreshing(false);
            if (sliderAdapter != null) {
                sliderAdapter.updateList(list);
            }
            if (list.size() > 0) {
                binding.flSlider.setVisibility(View.VISIBLE);
                if (list.size() > 1) {
                    timer = new Timer();
                    timer.scheduleAtFixedRate(new MyTask(), 3000, 3000);
                }
            } else {
                binding.flSlider.setVisibility(View.GONE);
            }
        });
        mvvm.getOnCategoryDataSuccess().observe(activity, list -> {
            binding.swipeRefresh.setRefreshing(false);

            if (list.size() > 0) {
                binding.tvNoCategories.setVisibility(View.GONE);
            } else {
                binding.tvNoCategories.setVisibility(View.VISIBLE);
            }

            if (categoryAdapter != null) {
                categoryAdapter.updateList(list);

            }
        });
        mvvm.getOnRecentProductDataModel().observe(activity, list -> {
            binding.swipeRefresh.setRefreshing(false);
            if (list.size() > 0) {
                binding.tvNoMostRecentProduct.setVisibility(View.GONE);
            } else {
                binding.tvNoMostRecentProduct.setVisibility(View.VISIBLE);
            }
            if (recentProductAdapter != null) {
                recentProductAdapter.updateList(list);

            }

        });
        mvvm.getOnMostProductDataModel().observe(activity, list -> {
            binding.swipeRefresh.setRefreshing(false);
            if (list.size() > 0) {
                binding.tvNoMostSaleProduct.setVisibility(View.GONE);
            } else {
                binding.tvNoMostSaleProduct.setVisibility(View.VISIBLE);
            }
            if (mostSaleProductAdapter != null) {
                mostSaleProductAdapter.updateList(list);

            }

        });

        setUpSliderData();

        categoryAdapter = new CategoryAdapter(activity, this, getLang());
        binding.recViewCategory.setLayoutManager(new GridLayoutManager(activity, 2));
        binding.recViewCategory.setAdapter(categoryAdapter);


        recentProductAdapter = new RecentProductAdapter(activity, this);
        binding.recViewMostRecentProducts.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        binding.recViewMostRecentProducts.setAdapter(recentProductAdapter);

        mostSaleProductAdapter = new MostSaleProductAdapter(activity, this);
        binding.recViewMostSaleProducts.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        binding.recViewMostSaleProducts.setAdapter(mostSaleProductAdapter);


        binding.swipeRefresh.setOnRefreshListener(this::getData);

        getData();
    }

    private void getData() {
        mvvm.getSlider();
        mvvm.getCategory();
        mvvm.getMostSaleProduct();
        mvvm.getRecentProduct();

    }

    private void setUpSliderData() {
        sliderAdapter = new SliderAdapter(activity);
        binding.pager.setAdapter(sliderAdapter);
        binding.pager.setClipToPadding(false);
        binding.pager.setPadding(20, 0, 20, 0);
    }

    public void showProductDetails(ProductModel productModel) {
        generalMvvm.getProduct_id().setValue(productModel.getId());
        generalMvvm.onHomeNavigate().setValue(6);

    }

    public void showCategoryDetails(CategoryModel categoryModel) {
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