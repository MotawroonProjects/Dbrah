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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.dbrah.R;

import com.apps.dbrah.adapter.CategoryAdapter;
import com.apps.dbrah.adapter.MostSaleProductAdapter;
import com.apps.dbrah.adapter.RecentProductAdapter;
import com.apps.dbrah.adapter.SliderAdapter;
import com.apps.dbrah.databinding.FragmentMarketBinding;
import com.apps.dbrah.model.CategoryModel;
import com.apps.dbrah.model.ProductAmount;
import com.apps.dbrah.model.ProductModel;
import com.apps.dbrah.model.cart_models.ManageCartModel;
import com.apps.dbrah.mvvm.FragmentMarketMvvm;
import com.apps.dbrah.mvvm.GeneralMvvm;
import com.apps.dbrah.uis.activity_base.BaseFragment;
import com.apps.dbrah.uis.activity_home.HomeActivity;

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
    private ManageCartModel manageCartModel;


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
        binding.setLang(getLang());
        manageCartModel = ManageCartModel.newInstance();
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        binding.setNotificationCount("0");
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
            generalMvvm.getCategoryList().setValue(list);

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
                binding.llMostSale.setVisibility(View.VISIBLE);
                binding.tvNoMostSaleProduct.setVisibility(View.GONE);
            } else {
                binding.llMostSale.setVisibility(View.GONE);

                binding.tvNoMostSaleProduct.setVisibility(View.VISIBLE);
            }
            if (mostSaleProductAdapter != null) {
                mostSaleProductAdapter.updateList(list);

            }

        });
        mvvm.getOnFavUnFavSuccess().observe(activity, model -> {
            int productPos = getCartItemPosMostSale(model.getId());
            if (productPos != -1) {
                if (mvvm.getOnMostProductDataModel().getValue() != null) {
                    mvvm.getOnMostProductDataModel().getValue().get(productPos).setIs_list(model.getIs_list());
                    if (mostSaleProductAdapter != null) {
                        mostSaleProductAdapter.notifyItemChanged(productPos);
                    }
                }
            }
            productPos = getCartItemPosRecent(model.getId());

            if (productPos != -1 && mvvm.getOnRecentProductDataModel().getValue() != null) {
                try {
                    mvvm.getOnRecentProductDataModel().getValue().get(productPos).setIs_list(model.getIs_list());
                    if (recentProductAdapter != null) {
                        recentProductAdapter.notifyItemChanged(productPos);
                    }
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }

            }
        });

        generalMvvm.getOnCartItemUpdated().observe(activity, productModel -> {
            int productPos = getCartItemPosMostSale(productModel.getId());
            if (productPos != -1) {
                if (mvvm.getOnMostProductDataModel().getValue() != null) {
                    mvvm.getOnMostProductDataModel().getValue().get(productPos).setAmount(productModel.getAmount());
                    if (mostSaleProductAdapter != null) {
                        mostSaleProductAdapter.notifyItemChanged(productPos);
                    }
                }
            }
            productPos = getCartItemPosRecent(productModel.getId());

            if (productPos != -1 && mvvm.getOnRecentProductDataModel().getValue() != null) {
                try {
                    mvvm.getOnRecentProductDataModel().getValue().get(productPos).setAmount(productModel.getAmount());
                    if (recentProductAdapter != null) {
                        recentProductAdapter.notifyItemChanged(productPos);
                    }
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }

            }
        });

        generalMvvm.getOnProductItemUpdated().observe(activity, productModel -> {
            int productPos = getCartItemPosMostSale(productModel.getId());
            if (productPos != -1) {
                if (mvvm.getOnMostProductDataModel().getValue() != null) {
                    mvvm.getOnMostProductDataModel().getValue().get(productPos).setIs_list(productModel.getIs_list());
                    if (mostSaleProductAdapter != null) {
                        mostSaleProductAdapter.updateItem(productModel, productPos);

                    }
                }
            }
            productPos = getCartItemPosRecent(productModel.getId());

            if (productPos != -1 && mvvm.getOnRecentProductDataModel().getValue() != null) {
                try {
                    mvvm.getOnRecentProductDataModel().getValue().get(productPos).setIs_list(productModel.getIs_list());
                    if (recentProductAdapter != null) {
                        recentProductAdapter.updateItem(productModel, productPos);
                    }
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }

            }
        });

        setUpSliderData();

        categoryAdapter = new CategoryAdapter(activity, this, getLang());
        binding.recViewCategory.setLayoutManager(new GridLayoutManager(activity, 2));
        binding.recViewCategory.setAdapter(categoryAdapter);


        recentProductAdapter = new RecentProductAdapter(activity, this, getLang());
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
        mvvm.getMostSaleProduct(activity, getUserModel());
        mvvm.getRecentProduct(activity, getUserModel());

    }

    private void setUpSliderData() {
        sliderAdapter = new SliderAdapter(activity);
        binding.pager.setAdapter(sliderAdapter);
        binding.pager.setClipToPadding(false);
        binding.pager.setPadding(20, 0, 20, 0);
    }

    public void showProductDetails(ProductModel productModel) {
        ProductAmount productAmount = new ProductAmount(productModel.getId(), productModel.getAmount());
        generalMvvm.getProductAmount().setValue(productAmount);
        generalMvvm.onHomeNavigate().setValue(6);

    }

    public void showCategoryDetails(CategoryModel categoryModel, int pos) {
        generalMvvm.getCategory_pos().setValue(pos);
        generalMvvm.onHomeNavigate().setValue(7);
    }

    public void favUnFav(ProductModel productModel, int adapterPosition) {
        generalMvvm.getOnProductItemUpdated().setValue(productModel);
        mvvm.favUnFav(getUserModel(), productModel);
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

    public void addProductToCart(ProductModel productModel, String fromList) {
        if (fromList.equals("most")) {

            int productPos = getCartItemPosRecent(productModel.getId());

            if (mvvm.getOnRecentProductDataModel().getValue() != null) {
                try {
                    mvvm.getOnRecentProductDataModel().getValue().get(productPos).setAmount(productModel.getAmount());
                    if (recentProductAdapter != null) {
                        recentProductAdapter.notifyItemChanged(productPos);
                    }
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }

            }
        } else {
            int productPos = getCartItemPosMostSale(productModel.getId());

            if (productPos != -1) {
                if (mvvm.getOnMostProductDataModel().getValue() != null) {
                    try {
                        mvvm.getOnMostProductDataModel().getValue().get(productPos).setAmount(productModel.getAmount());
                        if (mostSaleProductAdapter != null) {
                            mostSaleProductAdapter.notifyItemChanged(productPos);
                        }
                    } catch (Exception e) {
                        Log.e("error", e.getMessage());
                    }


                }
            }
        }

        manageCartModel.add(productModel, activity);
        generalMvvm.getOnCartRefreshed().setValue(true);
    }

    public void removeProductFromCart(ProductModel productModel, String fromList) {
        manageCartModel.delete(productModel, activity);
        generalMvvm.getOnCartRefreshed().setValue(true);


        if (fromList.equals("most")) {

            int productPos = getCartItemPosRecent(productModel.getId());

            try {
                if (mvvm.getOnRecentProductDataModel().getValue() != null) {
                    mvvm.getOnRecentProductDataModel().getValue().get(productPos).setAmount(productModel.getAmount());
                    if (recentProductAdapter != null) {
                        recentProductAdapter.notifyItemChanged(productPos);
                    }
                }
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }


        } else {
            int productPos = getCartItemPosMostSale(productModel.getId());

            if (productPos != -1) {

                try {
                    if (mvvm.getOnMostProductDataModel().getValue() != null) {
                        mvvm.getOnMostProductDataModel().getValue().get(productPos).setAmount(productModel.getAmount());
                        if (mostSaleProductAdapter != null) {
                            mostSaleProductAdapter.notifyItemChanged(productPos);
                        }
                    }
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                }


            }
        }

    }

    private int getCartItemPosMostSale(String product_id) {
        if (mvvm.getOnMostProductDataModel().getValue() != null) {
            for (int index = 0; index < mvvm.getOnMostProductDataModel().getValue().size(); index++) {
                if (mvvm.getOnMostProductDataModel().getValue().get(index).getId().equals(product_id)) {
                    return index;
                }
            }
        }

        return -1;
    }

    private int getCartItemPosRecent(String product_id) {
        if (mvvm.getOnRecentProductDataModel().getValue() != null) {
            for (int index = 0; index < mvvm.getOnRecentProductDataModel().getValue().size(); index++) {
                if (mvvm.getOnRecentProductDataModel().getValue().get(index).getId().equals(product_id)) {
                    return index;
                }
            }
        }

        return -1;
    }


}