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
import com.apps.dbrah.uis.activity_home.market_module.FragmentHome;
import com.apps.dbrah.uis.activity_home.notification_module.FragmentNotification;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import io.paperdb.Paper;

public class HomeActivity extends BaseActivity implements ViewPager.OnPageChangeListener{
    private GeneralMvvm generalMvvm;
    private ActivityHomeBinding binding;
    private MyPagerAdapter adapter;
    private List<Fragment> fragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();


    }


    private void initView() {
        generalMvvm = ViewModelProviders.of(this).get(GeneralMvvm.class);
        setUpPager();
        generalMvvm.onNotificationNavigate().observe(this, this::updatePagePos);


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
        fragments = new ArrayList<>();
        fragments.add(FragmentHome.newInstance());
        fragments.add(FragmentNotification.newInstance());

        adapter = new MyPagerAdapter(getSupportFragmentManager(),fragments, null);
        binding.pager.setAdapter(adapter);
        binding.pager.setOffscreenPageLimit(fragments.size());
        binding.pager.addOnPageChangeListener(this);




    }

    public void updatePagePos(int pos) {
        binding.pager.setCurrentItem(pos);
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

        if (binding.pager.getCurrentItem()!=0) {
            updatePagePos(0);

        } else {

            FragmentHome fragmentHome = (FragmentHome) adapter.getItem(0);
            if (!fragmentHome.onBackPress()) {
                super.onBackPressed();
            }


        }


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
