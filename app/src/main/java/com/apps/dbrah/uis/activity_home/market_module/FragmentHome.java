package com.apps.dbrah.uis.activity_home.market_module;

import android.content.Context;
import android.os.Bundle;
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
import com.apps.dbrah.databinding.FragmentHomeBinding;
import com.apps.dbrah.mvvm.GeneralMvvm;
import com.apps.dbrah.uis.activity_base.BaseFragment;
import com.apps.dbrah.uis.activity_home.HomeActivity;
import com.apps.dbrah.uis.activity_home.cart_module.FragmentCart;
import com.apps.dbrah.uis.activity_home.order_module.FragmentOrder;
import com.apps.dbrah.uis.activity_home.profile_module.FragmentProfile;
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
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        binding.setNotificationCount("0");
        binding.llSearch.setOnClickListener(v -> generalMvvm.onHomeNavigate().setValue(2));
        binding.flNotification.setOnClickListener(v -> generalMvvm.onHomeNavigate().setValue(1));

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

        fragments.add(FragmentMarket.newInstance());
        fragments.add(FragmentOrder.newInstance());
        fragments.add(FragmentCart.newInstance());
        fragments.add(FragmentProfile.newInstance());
        adapter = new MyPagerAdapter(getChildFragmentManager(), fragments, null);
        binding.pager.setAdapter(adapter);
        binding.pager.setOffscreenPageLimit(fragments.size());

        binding.pager.addOnPageChangeListener(this);
        binding.bottomNavigationView.setOnItemSelectedListener(this);

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