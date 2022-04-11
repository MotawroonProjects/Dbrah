package com.apps.dbrah.uis.activity_home.search_module;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.dbrah.R;
import com.apps.dbrah.adapter.RecentProductAdapter;
import com.apps.dbrah.databinding.FragmentSearchBinding;
import com.apps.dbrah.model.ProductModel;
import com.apps.dbrah.mvvm.FragmentProductsMvvm;
import com.apps.dbrah.mvvm.FragmentSearchMvvm;
import com.apps.dbrah.mvvm.GeneralMvvm;
import com.apps.dbrah.uis.activity_base.BaseFragment;
import com.apps.dbrah.uis.activity_home.HomeActivity;

import java.util.ArrayList;
import java.util.List;


public class FragmentSearch extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private HomeActivity activity;
    private FragmentSearchBinding binding;
    private FragmentSearchMvvm fragmentSearchMvvm;
    private RecentProductAdapter recentProductAdapter;
    private String query;

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

    private void initView() {
        binding.setLang(getLang());
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        fragmentSearchMvvm = ViewModelProviders.of(activity).get(FragmentSearchMvvm.class);
        fragmentSearchMvvm.getIsLoadingLiveData().observe(activity, isLoading -> {

            binding.recViewLayout.swipeRefresh.setRefreshing(isLoading);


        });
        recentProductAdapter = new RecentProductAdapter(activity, this);
        binding.recViewLayout.recView.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false));
        binding.recViewLayout.recView.setAdapter(recentProductAdapter);
        binding.arrow.setOnClickListener(v -> {
            generalMvvm.onHomeBackNavigate().setValue(true);
        });

        fragmentSearchMvvm.getListMutableLiveData().observe(activity, productModels -> updateProductData(productModels));
        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                query = binding.edtSearch.getText().toString();
                fragmentSearchMvvm.searchProduct(query);
            }
        });
        fragmentSearchMvvm.searchProduct(query);

    }

    private void updateProductData(List<ProductModel> productModels) {
        if (productModels.size() > 0) {
            recentProductAdapter.updateList(productModels);
            binding.recViewLayout.tvNoData.setVisibility(View.GONE);

        } else {
            recentProductAdapter.updateList(new ArrayList<>());
            binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);
        }
    }


}