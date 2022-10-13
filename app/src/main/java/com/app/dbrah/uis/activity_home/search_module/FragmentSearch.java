package com.app.dbrah.uis.activity_home.search_module;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.dbrah.R;
import com.app.dbrah.adapter.SearchHomeProductAdapter;
import com.app.dbrah.adapter.SearchHomeSubCategoryAdapter;
import com.app.dbrah.databinding.FragmentSearchBinding;
import com.app.dbrah.model.CategoryModel;
import com.app.dbrah.model.ProductAmount;
import com.app.dbrah.model.ProductModel;
import com.app.dbrah.mvvm.FragmentSearchMvvm;
import com.app.dbrah.mvvm.GeneralMvvm;
import com.app.dbrah.uis.activity_base.BaseFragment;
import com.app.dbrah.uis.activity_home.HomeActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


public class FragmentSearch extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private HomeActivity activity;
    private FragmentSearchBinding binding;
    private FragmentSearchMvvm mvvm;
    private SearchHomeSubCategoryAdapter subCategoryAdapter;
    private SearchHomeProductAdapter productAdapter;
    private CompositeDisposable disposable = new CompositeDisposable();


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

    @SuppressLint("CheckResult")
    private void initView() {
        binding.setLang(getLang());
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        binding.arrow.setOnClickListener(v -> {
            generalMvvm.onHomeBackNavigate().setValue(true);
        });

        mvvm = ViewModelProviders.of(activity).get(FragmentSearchMvvm.class);
        mvvm.getIsLoading().observe(activity, isLoading -> {
            if (isLoading) {
                binding.loaderSubCategory.startShimmer();
                binding.loaderSubCategory.setVisibility(View.VISIBLE);
                binding.loaderProduct.startShimmer();
                binding.loaderProduct.setVisibility(View.VISIBLE);
            } else {
                binding.loaderSubCategory.stopShimmer();
                binding.loaderSubCategory.setVisibility(View.GONE);

                binding.loaderProduct.stopShimmer();
                binding.loaderProduct.setVisibility(View.GONE);
            }
        });
        mvvm.getIsLoadingProducts().observe(activity, isLoading -> {
            binding.swipeRefresh.setRefreshing(isLoading);
        });
        mvvm.getOnDataSuccess().observe(activity, data -> {
            if (subCategoryAdapter != null) {

                subCategoryAdapter.updateList(data.getCategories());
            }

            if (data.getProducts().size() > 0) {
                binding.tvNoData.setVisibility(View.GONE);
            } else {
                binding.tvNoData.setVisibility(View.VISIBLE);

            }
            if (productAdapter != null) {
                productAdapter.updateList(data.getProducts());
            }
        });
        mvvm.getOnProductsDataSuccess().observe(activity, list -> {

            if (list.size() > 0) {
                binding.tvNoData.setVisibility(View.GONE);
            } else {
                binding.tvNoData.setVisibility(View.VISIBLE);

            }
            if (productAdapter != null) {
                productAdapter.updateList(list);
            }
        });

        subCategoryAdapter = new SearchHomeSubCategoryAdapter(activity, this, getLang());
        binding.recViewSubCategory.setLayoutManager(new GridLayoutManager(activity, 1, LinearLayoutManager.HORIZONTAL, false));
        binding.recViewSubCategory.setAdapter(subCategoryAdapter);

        productAdapter = new SearchHomeProductAdapter(activity, this, getLang());
        binding.recView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        binding.recView.setAdapter(productAdapter);


        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            binding.edtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    emitter.onNext(editable.toString());
                }
            });

        }).debounce(2, TimeUnit.SECONDS)
                .distinctUntilChanged()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(query -> {
                    if (subCategoryAdapter != null) {
                        subCategoryAdapter.updateList(null);
                    }

                    if (productAdapter != null) {
                        productAdapter.updateList(null);
                    }
                    mvvm.search(query,getUserModel());
                });


        binding.swipeRefresh.setOnRefreshListener(() -> {
            if (mvvm.getSubCategoryId().getValue() != null) {
                mvvm.getProductBySubCategory(getUserModel());
            } else {
                mvvm.search(mvvm.getQuery().getValue(),getUserModel());
                binding.swipeRefresh.setRefreshing(false);
            }
        });
        mvvm.search(null,getUserModel());

    }


    public void showProductDetails(ProductModel productModel) {
        ProductAmount productAmount = new ProductAmount(productModel.getId(), productModel.getAmount());
        generalMvvm.getProductAmount().setValue(productAmount);
        generalMvvm.onHomeNavigate().setValue(6);
    }


    public void setSubCategory(CategoryModel subCategoryModel) {
        mvvm.getSubCategoryId().setValue(subCategoryModel.getId());
        mvvm.getProductBySubCategory(getUserModel());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.clear();
    }


}