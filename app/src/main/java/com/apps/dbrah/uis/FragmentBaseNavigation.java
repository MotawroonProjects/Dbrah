package com.apps.dbrah.uis;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.apps.dbrah.R;
import com.apps.dbrah.mvvm.GeneralMvvm;
import com.apps.dbrah.uis.activity_base.BaseFragment;
import com.apps.dbrah.uis.activity_home.HomeActivity;

public class FragmentBaseNavigation extends BaseFragment {
    private int toolBarId;
    private int layoutResourceId;
    private int navHostId;
    private int defaultValue = -1;
    private View view;
    private NavController navController;
    private AppCompatActivity activity;
    private GeneralMvvm generalMvvm;


    public static FragmentBaseNavigation newInstance(int toolBarId, int layoutResourceId, int navHostId) {
        Bundle bundle = new Bundle();
        bundle.putInt("toolBarId", toolBarId);
        bundle.putInt("layoutResourceId", layoutResourceId);
        bundle.putInt("navHostId", navHostId);

        FragmentBaseNavigation fragment = new FragmentBaseNavigation();
        fragment.setArguments(bundle);
        return fragment;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            toolBarId = bundle.getInt("toolBarId", defaultValue);
            layoutResourceId = bundle.getInt("layoutResourceId", defaultValue);
            navHostId = bundle.getInt("navHostId", defaultValue);

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (layoutResourceId != defaultValue) {
            view = inflater.inflate(layoutResourceId, container, false);

        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        if (toolBarId != defaultValue && layoutResourceId != defaultValue && navHostId != defaultValue) {
            if (activity instanceof HomeActivity) {
                HomeActivity homeActivity = (HomeActivity) activity;
                generalMvvm = ViewModelProviders.of(homeActivity).get(GeneralMvvm.class);


            }
            navController = Navigation.findNavController(activity, navHostId);
           /* Toolbar toolbar = view.findViewById(toolBarId);
            NavigationUI.setupWithNavController(toolbar, navController);
            TextView title = view.findViewById(R.id.tvTitle);
            FrameLayout flNotification = view.findViewById(R.id.flNotification);

            flNotification.setOnClickListener(v -> {
                if (generalMvvm != null) {
                    generalMvvm.onNotificationNavigate().setValue(1);
                }
            });*/

            /*navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
                int id = navDestination.getId();
                if (id == R.id.order) {
                    title.setText(R.string.orders);

                } else if (id == R.id.cart) {
                    title.setText(R.string.cart);

                } else if (id == R.id.profile) {
                    title.setText(R.string.profile);

                }
            });*/
        }
    }

    public boolean onBackPress() {
        return navController.navigateUp();
    }
}
