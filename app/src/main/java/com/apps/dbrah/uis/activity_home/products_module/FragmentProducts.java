package com.apps.dbrah.uis.activity_home.products_module;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.dbrah.R;
import com.apps.dbrah.adapter.MainProductCategoryAdapter;
import com.apps.dbrah.adapter.RecentProductAdapter;
import com.apps.dbrah.adapter.SubProductCategoryAdapter;
import com.apps.dbrah.databinding.FragmentProductsBinding;
import com.apps.dbrah.model.CategoryDataModel;
import com.apps.dbrah.model.ProductModel;
import com.apps.dbrah.mvvm.FragmentProductsMvvm;
import com.apps.dbrah.mvvm.GeneralMvvm;
import com.apps.dbrah.uis.activity_base.BaseFragment;
import com.apps.dbrah.uis.activity_home.HomeActivity;
import com.apps.dbrah.uis.activity_home.complete_order_module.FragmentCompleteOrder;

import java.util.ArrayList;
import java.util.List;


public class FragmentProducts extends BaseFragment {
    private HomeActivity activity;
    private GeneralMvvm generalMvvm;
    private FragmentProductsMvvm fragmentProductsMvvm;
    private FragmentProductsBinding binding;
    private MainProductCategoryAdapter mainProductCategoryAdapter;
    private SubProductCategoryAdapter subProductCategoryAdapter;
    private RecentProductAdapter recentProductAdapter;
    private String cat_id;
    private String query;
    private String sub_cat_id;

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
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        fragmentProductsMvvm = ViewModelProviders.of(activity).get(FragmentProductsMvvm.class);
        generalMvvm.getCat_id().observe(activity, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                if (s != null) {

                    cat_id = s;
                    fragmentProductsMvvm.getCategory();

                }
            }
        });
        fragmentProductsMvvm.getIsLoadingLiveData().observe(activity, isLoading -> {
            if (isLoading) {
            }
        });
        mainProductCategoryAdapter = new MainProductCategoryAdapter(activity, this, getLang());
        subProductCategoryAdapter = new SubProductCategoryAdapter(activity, this, getLang());
        binding.recViewMain.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        binding.recViewSub.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        binding.recViewMain.setAdapter(mainProductCategoryAdapter);
        binding.recViewSub.setAdapter(subProductCategoryAdapter);
        recentProductAdapter = new RecentProductAdapter(activity, this);
        binding.recViewProducts.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        binding.recViewProducts.setAdapter(recentProductAdapter);
        View view = activity.setUpToolbar(binding.toolbar, getString(R.string.products), R.color.white, R.color.black, R.drawable.small_rounded_grey4, false);
        view.setOnClickListener(v -> {
            generalMvvm.onHomeBackNavigate().setValue(true);
        });
        fragmentProductsMvvm.getCategoryModelLiveData().observe(activity, this::updateMainCategoryData);
        fragmentProductsMvvm.getSubCategoryModelLiveData().observe(activity, this::updateSubCategoryData);
        fragmentProductsMvvm.getListMutableLiveData().observe(activity, productModels -> updateProductData(productModels));
    binding.edtSearch.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            query=binding.edtSearch.getText().toString();
fragmentProductsMvvm.searchProduct(cat_id,sub_cat_id,query);
        }
    });

    }

    private void updateProductData(List<ProductModel> productModels) {
        if(productModels.size()>0){
            recentProductAdapter.updateList(productModels);
        }
        else{
            recentProductAdapter.updateList(new ArrayList<>());

        }
    }

    private void updateSubCategoryData(List<CategoryDataModel.CategoryModel> categoryModels) {
        if (categoryModels.size() > 0) {
            CategoryDataModel.CategoryModel categoryModel = categoryModels.get(0);
            categoryModel.setSelected(true);
            categoryModels.set(0, categoryModel);
            subProductCategoryAdapter.updateList(categoryModels);
            sub_cat_id=categoryModel.getId();
            fragmentProductsMvvm.searchProduct(cat_id, categoryModel.getId(), query);
        } else {
            subProductCategoryAdapter.updateList(new ArrayList<>());
            recentProductAdapter.updateList(new ArrayList<>());

        }
    }

    private void updateMainCategoryData(List<CategoryDataModel.CategoryModel> categoryModels) {
        if (categoryModels.size() > 0) {
            for (int i = 0; i < categoryModels.size(); i++) {
                ///   Log.e("fllfll",cat_id+"_"+categoryModels.get(i).getId());
                if (categoryModels.get(i).getId().equals(cat_id)) {
                    CategoryDataModel.CategoryModel categoryModel = categoryModels.get(i);
                    categoryModel.setSelected(true);
                    categoryModels.set(i, categoryModel);
                    break;
                }
            }
            mainProductCategoryAdapter.updateList(categoryModels);
            fragmentProductsMvvm.getSubCategory(cat_id);
        } else {
        }
    }

    public void getsubcat(CategoryDataModel.CategoryModel categoryModel) {
        cat_id = categoryModel.getId();
        fragmentProductsMvvm.getSubCategory(cat_id);
    }

    public void showProducts(CategoryDataModel.CategoryModel categoryModel) {
        sub_cat_id=categoryModel.getId();
        fragmentProductsMvvm.searchProduct(cat_id, categoryModel.getId(), query);

    }
}