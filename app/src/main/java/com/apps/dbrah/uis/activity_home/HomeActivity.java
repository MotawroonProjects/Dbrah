package com.apps.dbrah.uis.activity_home;

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


import com.apps.dbrah.adapter.MyPagerAdapter;
import com.apps.dbrah.mvvm.GeneralMvvm;
import com.apps.dbrah.uis.FragmentBaseNavigation;
import com.apps.dbrah.uis.activity_base.BaseActivity;

import com.apps.dbrah.R;

import com.apps.dbrah.databinding.ActivityHomeBinding;
import com.apps.dbrah.language.Language;
import com.apps.dbrah.uis.activity_home.address_module.FragmentMyAddresses;
import com.apps.dbrah.uis.activity_home.market_module.FragmentHome;
import com.apps.dbrah.uis.activity_home.my_list_module.FragmentMyList;
import com.apps.dbrah.uis.activity_home.notification_module.FragmentNotification;
import com.apps.dbrah.uis.activity_home.product_detials_module.FragmentProductDetials;
import com.apps.dbrah.uis.activity_home.search_module.FragmentSearch;
import com.apps.dbrah.uis.activity_home.setting_module.FragmentSetting;
import com.apps.dbrah.uis.add_address_module.FragmentAddAddress;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import io.paperdb.Paper;

public class HomeActivity extends BaseActivity {
    private GeneralMvvm generalMvvm;
    private ActivityHomeBinding binding;
    private MyPagerAdapter adapter;
    private List<Fragment> fragments;
    private Stack<Integer> stack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();


    }


    private void initView() {
        generalMvvm = ViewModelProviders.of(this).get(GeneralMvvm.class);
        setUpPager();
        generalMvvm.onHomeNavigate().observe(this, this::updateStack);
        generalMvvm.onHomeBackNavigate().observe(this, value -> {
            onBackPressed();
        });

       /* homeActivityMvvm = ViewModelProviders.of(this).get(HomeActivityMvvm.class);


        homeActivityMvvm.firebase.observe(this, token -> {
            if (getUserModel() != null) {
                UserModel userModel = getUserModel();
                userModel.getData().setFirebase_token(token);
                setUserModel(userModel);
            }
        });


        if (getUserModel() != null) {
            homeActivityMvvm.updateFirebase(this, getUserModel());
        }*/
    }

    private void setUpPager() {
        stack = new Stack<>();
        fragments = new ArrayList<>();
        fragments.add(FragmentHome.newInstance());
        fragments.add(FragmentNotification.newInstance());
        fragments.add(FragmentSearch.newInstance());
        fragments.add(FragmentMyList.newInstance());
        fragments.add(FragmentAddAddress.newInstance());
        fragments.add(FragmentSetting.newInstance());
        fragments.add(FragmentProductDetials.newInstance());

        adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments, null);
        binding.pager.setAdapter(adapter);
        binding.pager.setOffscreenPageLimit(fragments.size());

        stack.push(0);


    }

    private void updateStack(int pagePos) {
        stack.push(pagePos);
        binding.pager.setCurrentItem(pagePos);

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


    public void updateFirebase() {
        if (getUserModel() != null) {
            // homeActivityMvvm.updateFirebase(this, getUserModel());
        }
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

}
