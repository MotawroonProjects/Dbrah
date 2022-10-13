package com.app.dbrah.uis.activity_home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;


import com.app.dbrah.adapter.MyPagerAdapter;
import com.app.dbrah.model.NotiFire;
import com.app.dbrah.model.ProductAmount;
import com.app.dbrah.model.UserModel;
import com.app.dbrah.mvvm.ActivityHomeMvvm;
import com.app.dbrah.mvvm.GeneralMvvm;
import com.app.dbrah.uis.FragmentBaseNavigation;
import com.app.dbrah.uis.activity_base.BaseActivity;

import com.app.dbrah.R;

import com.app.dbrah.databinding.ActivityHomeBinding;
import com.app.dbrah.language.Language;
import com.app.dbrah.uis.activity_home.add_address_module.FragmentAddAddress;
import com.app.dbrah.uis.activity_home.address_module.FragmentMyAddresses;
import com.app.dbrah.uis.activity_home.market_module.FragmentHome;
import com.app.dbrah.uis.activity_home.my_list_module.FragmentMyList;
import com.app.dbrah.uis.activity_home.notification_module.FragmentNotification;
import com.app.dbrah.uis.activity_home.product_detials_module.FragmentProductDetials;
import com.app.dbrah.uis.activity_home.products_module.FragmentProducts;
import com.app.dbrah.uis.activity_home.search_module.FragmentSearch;
import com.app.dbrah.uis.activity_home.setting_module.FragmentSetting;
import com.google.android.material.navigation.NavigationBarView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import io.paperdb.Paper;

public class HomeActivity extends BaseActivity {
    private ActivityHomeMvvm mvvm;
    private GeneralMvvm generalMvvm;
    private ActivityHomeBinding binding;
    private MyPagerAdapter adapter;
    private List<Fragment> fragments;
    private Stack<Integer> stack;
    private String product_id = null;
    private String order_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        getDataFromIntent();
        initView();


    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        product_id = intent.getStringExtra("product_id");
        order_id = intent.getStringExtra("order_id");

    }


    private void initView() {
        generalMvvm = ViewModelProviders.of(this).get(GeneralMvvm.class);
        setUpPager();
        generalMvvm.onHomeNavigate().observe(this, this::updateStack);
        generalMvvm.onHomeBackNavigate().observe(this, value -> {
            onBackPressed();
        });
        mvvm = ViewModelProviders.of(this).get(ActivityHomeMvvm.class);


        mvvm.onTokenSuccess().observe(this, userModel -> {
            if (getUserModel() != null) {
                setUserModel(userModel);
            }
        });

        generalMvvm.getOnUserLoggedIn().observe(this, isLoggedIn -> {
            if (isLoggedIn) {
                if (getUserModel() != null) {
                    mvvm.updateToken(getUserModel());
                }
            }
        });


        if (getUserModel() != null) {
            mvvm.updateToken(getUserModel());
            if (!EventBus.getDefault().isRegistered(this)){
                EventBus.getDefault().register(this);
            }
        }




    }


    private void setUpPager() {
        stack = new Stack<>();
        fragments = new ArrayList<>();

        fragments.add(FragmentHome.newInstance());//0
        fragments.add(FragmentNotification.newInstance());//1
        fragments.add(FragmentSearch.newInstance());//2
        fragments.add(FragmentMyList.newInstance());//3
        fragments.add(FragmentAddAddress.newInstance());//4
        fragments.add(FragmentSetting.newInstance());//5
        fragments.add(FragmentProductDetials.newInstance());//6
        //fragments.add(FragmentProducts.newInstance());//7
        fragments.add(FragmentMyAddresses.newInstance());//8

        adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments, null);
        binding.pager.setAdapter(adapter);
        binding.pager.setOffscreenPageLimit(fragments.size());

        stack.push(0);

        if (product_id != null && !product_id.isEmpty()) {
            updateStack(6);
            ProductAmount productAmount = new ProductAmount(product_id, 0);
            generalMvvm.getProductAmount().setValue(productAmount);
        }

        if (order_id != null && !order_id.isEmpty()) {
            updateStack(1);
        }


    }

    private void updateStack(int pagePos) {
        Log.e("pos",pagePos+"_");
        if (pagePos!=9){
            stack.push(pagePos);
            binding.pager.setCurrentItem(pagePos);
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onOrderStatusChanged(NotiFire model) {
        if (!model.getOrder_status().isEmpty()) {
            String status = model.getOrder_status();
            if (status.equals("offered")) {
                generalMvvm.getOnCurrentOrderRefreshed().setValue(true);
            } else if (status.equals("preparing")) {
                generalMvvm.getOnCurrentOrderRefreshed().setValue(true);

            } else if (status.equals("on_way")) {
                generalMvvm.getOnCurrentOrderRefreshed().setValue(true);

            } else if (status.equals("delivered")) {
                generalMvvm.getOnCurrentOrderRefreshed().setValue(true);
                generalMvvm.getOnPreviousOrderRefreshed().setValue(true);
                generalMvvm.onOrderNavigate().setValue(1);

            }
        }
    }


    public void refreshActivity(String lang) {
        Paper.book().write("lang", lang);
        Language.setNewLocale(this, lang);
        new Handler()
                .postDelayed(() -> {

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }, 500);


    }


    @Override
    public void onBackPressed() {
        if (stack.size() > 1) {
            stack.pop();
            binding.pager.setCurrentItem(stack.peek());
        } else {
            FragmentHome fragmentHome = (FragmentHome) adapter.getItem(0);
            if (!fragmentHome.onBackPress()) {
                super.onBackPressed();
            }

        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }
}
