package com.apps.dbrah.uis.activity_home.market_module;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.apps.dbrah.R;
import com.apps.dbrah.adapter.MyPagerAdapter;
import com.apps.dbrah.databinding.CartCountBinding;
import com.apps.dbrah.databinding.FragmentHomeBinding;
import com.apps.dbrah.model.cart_models.ManageCartModel;
import com.apps.dbrah.mvvm.GeneralMvvm;
import com.apps.dbrah.uis.activity_base.BaseFragment;
import com.apps.dbrah.uis.activity_home.HomeActivity;
import com.apps.dbrah.uis.activity_home.cart_module.FragmentCart;
import com.apps.dbrah.uis.activity_home.order_module.FragmentOrder;
import com.apps.dbrah.uis.activity_home.products_module.FragmentProducts;
import com.apps.dbrah.uis.activity_home.profile_module.FragmentProfile;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;


public class FragmentHome extends BaseFragment implements ViewPager.OnPageChangeListener, NavigationBarView.OnItemSelectedListener {
    private GeneralMvvm generalMvvm;
    private HomeActivity activity;
    private FragmentHomeBinding binding;
    private Stack<Integer> stack;
    private Map<Integer, Integer> map;
    private List<Fragment> fragments;
    private MyPagerAdapter adapter;
    private CartCountBinding countBinding;
    private ManageCartModel manageCartModel;
    public static FragmentHome newInstance() {
        return new FragmentHome();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();

    }

    private void initView() {
        manageCartModel = ManageCartModel.newInstance();
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        binding.setNotificationCount("0");
        binding.setModel(getUserModel());
        binding.llSearch.setOnClickListener(v -> generalMvvm.onHomeNavigate().setValue(2));
        binding.flNotification.setOnClickListener(v -> generalMvvm.onHomeNavigate().setValue(1));
        generalMvvm.getActionFragmentHomeNavigator().observe(activity, this::setItemPos);
        generalMvvm.getOnUserLoggedIn().observe(activity, isLoggedIn -> {
            binding.setModel(getUserModel());

        });

        generalMvvm.getOnLoggedOutSuccess().observe(activity, isLoggedOut -> {
            binding.setModel(getUserModel());

        });
        generalMvvm.getOnCartRefreshed().observe(activity,isRefreshed->{
            countBinding.setCounter(manageCartModel.getItemsCount(activity));

        });

        binding.flSetting.setOnClickListener(v -> {
            generalMvvm.onHomeNavigate().setValue(5);
        });


        countBinding = DataBindingUtil.inflate(LayoutInflater.from(activity),R.layout.cart_count,null,false);
        countBinding.setCounter(manageCartModel.getItemsCount(activity));
        BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) binding.bottomNavigationView.getChildAt(0);
        BottomNavigationItemView bottomNavigationItemView = (BottomNavigationItemView) bottomNavigationMenuView.getChildAt(2);
        bottomNavigationItemView.addView(countBinding.getRoot());
        setUpPager();

    }

    private void setUpPager() {
        fragments = new ArrayList<>();
        stack = new Stack<>();
        map = new HashMap<>();
        if (stack.isEmpty()) {
            stack.push(0);

        }


        map.put(0, R.id.market);
        map.put(1, R.id.order);
        map.put(2, R.id.cart);
        map.put(3, R.id.profile);
        map.put(4, R.id.products);

        fragments.add(FragmentMarket.newInstance());
        fragments.add(FragmentOrder.newInstance());
        fragments.add(FragmentCart.newInstance());
        fragments.add(FragmentProfile.newInstance());
        fragments.add(FragmentProducts.newInstance());

        adapter = new MyPagerAdapter(getChildFragmentManager(), fragments, null);
        binding.pager.setAdapter(adapter);
        binding.pager.setOffscreenPageLimit(fragments.size());

        binding.pager.addOnPageChangeListener(this);
        binding.bottomNavigationView.setOnItemSelectedListener(this);

        binding.bottomNavigationView.getMenu().getItem(4).setVisible(false);

        generalMvvm.onHomeNavigate().observe(activity,pos->{
            if (pos==9){
                setItemPos(4);
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int itemId = map.get(position);
        if (itemId != binding.bottomNavigationView.getSelectedItemId()) {
            binding.bottomNavigationView.setSelectedItemId(itemId);
        }
        if (position == 2) {
            binding.llSearch.setVisibility(View.INVISIBLE);
        } else {
            binding.llSearch.setVisibility(View.VISIBLE);

        }


        if (getUserModel()==null){
            binding.flSetting.setVisibility(View.VISIBLE);
        }else {
            binding.flSetting.setVisibility(View.GONE);

        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemPos = getItemPos(item.getItemId());
        if (itemPos != binding.pager.getCurrentItem()) {
            setItemPos(itemPos);
        }
        return true;
    }


    private void setItemPos(int pos) {

        binding.pager.setCurrentItem(pos);
        stack.push(pos);
    }

    private int getItemPos(int item_id) {
        for (int pos : map.keySet()) {
            if (map.get(pos) == item_id) {
                return pos;
            }
        }
        return 0;
    }

    public boolean onBackPress() {
        if (stack.size() > 1) {
            stack.pop();
            binding.pager.setCurrentItem(stack.peek());
            return true;
        } else {
            return false;
        }

    }


}