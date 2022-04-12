package com.apps.dbrah.uis.activity_home.products_module;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.dbrah.R;
import com.apps.dbrah.adapter.MainProductCategoryAdapter;
import com.apps.dbrah.adapter.RecentProductAdapter;
import com.apps.dbrah.adapter.SubProductCategoryAdapter;
import com.apps.dbrah.databinding.FragmentProductsBinding;
import com.apps.dbrah.model.CategoryModel;
import com.apps.dbrah.mvvm.FragmentProductsMvvm;
import com.apps.dbrah.mvvm.GeneralMvvm;
import com.apps.dbrah.uis.activity_base.BaseFragment;
import com.apps.dbrah.uis.activity_home.HomeActivity;

import java.util.ArrayList;


public class FragmentProducts extends BaseFragment {
    private HomeActivity activity;
    private GeneralMvvm generalMvvm;
    private FragmentProductsMvvm mvvm;
    private FragmentProductsBinding binding;
    private MainProductCategoryAdapter mainProductCategoryAdapter;
    private SubProductCategoryAdapter subProductCategoryAdapter;
    private RecentProductAdapter recentProductAdapter;
    private String query = "";

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
        mvvm = ViewModelProviders.of(activity).get(FragmentProductsMvvm.class);

        View view = activity.setUpToolbar(binding.toolbar, getString(R.string.products), R.color.white, R.color.black, R.drawable.small_rounded_grey4, false);
        view.setOnClickListener(v -> {
            generalMvvm.onHomeBackNavigate().setValue(true);
        });

        generalMvvm.getCategoryList().observe(activity, list -> {
            mvvm.getOnCategoryDataSuccess().setValue(list);
            if (mainProductCategoryAdapter != null) {
                mainProductCategoryAdapter.updateList(list);
            }
        });

        generalMvvm.getCategory_pos().observe(activity, pos -> {

            if (mainProductCategoryAdapter != null && mvvm.getOnCategoryDataSuccess().getValue() != null && mvvm.getCategoryPos().getValue() != null && mvvm.getCategoryPos().getValue() != -1) {
                mvvm.getOnCategoryDataSuccess().getValue().get(mvvm.getCategoryPos().getValue()).setSelected(false);
                mainProductCategoryAdapter.notifyItemChanged(mvvm.getCategoryPos().getValue());

            }
            if (mainProductCategoryAdapter != null && mvvm.getOnCategoryDataSuccess().getValue() != null) {
                mvvm.setCategoryId(mvvm.getOnCategoryDataSuccess().getValue().get(pos).getId());
                mvvm.getOnCategoryDataSuccess().getValue().get(pos).setSelected(true);
                mainProductCategoryAdapter.notifyItemChanged(pos);
            }

            mvvm.getCategoryPos().setValue(pos);
        });

        mvvm.getIsLoading().observe(activity, isLoading -> {
            if (isLoading) {
            }
        });

        mvvm.getOnSubCategoryDataSuccess().observe(activity,list->{
            if (subProductCategoryAdapter!=null){
                subProductCategoryAdapter.updateList(list);
            }
        });

        mainProductCategoryAdapter = new MainProductCategoryAdapter(activity, this, getLang());
        binding.recViewMain.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        binding.recViewMain.setAdapter(mainProductCategoryAdapter);

        subProductCategoryAdapter = new SubProductCategoryAdapter(activity, this, getLang());
        binding.recViewSub.setAdapter(subProductCategoryAdapter);
        binding.recViewSub.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));


        recentProductAdapter = new RecentProductAdapter(activity, this);
        binding.recViewProducts.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        binding.recViewProducts.setAdapter(recentProductAdapter);


    }


    public void getSubCat(CategoryModel categoryModel) {
        mvvm.setCategoryId(categoryModel.getId());
    }

    public void showProducts(CategoryModel categoryModel) {
        Log.e("dd",categoryModel.getId()+"");
        mvvm.getSubCategoryId().setValue(categoryModel.getId());
        mvvm.searchProduct(binding.edtSearch.getText().toString());
    }
}