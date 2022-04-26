package com.apps.dbrah.uis.activity_home.product_detials_module;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.apps.dbrah.R;
import com.apps.dbrah.adapter.ProductSliderAdapter;
import com.apps.dbrah.databinding.FragmentProductDetialsBinding;
import com.apps.dbrah.model.ProductModel;
import com.apps.dbrah.model.cart_models.ManageCartModel;
import com.apps.dbrah.mvvm.FragmentProductDetailsMvvm;
import com.apps.dbrah.mvvm.GeneralMvvm;
import com.apps.dbrah.tags.Tags;
import com.apps.dbrah.uis.activity_base.BaseFragment;
import com.apps.dbrah.uis.activity_home.HomeActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class FragmentProductDetials extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private FragmentProductDetailsMvvm mvvm;
    private HomeActivity activity;
    private FragmentProductDetialsBinding binding;
    private String productId;
    private ProductSliderAdapter sliderAdapter;
    private List<ProductModel.Image> imagesList;
    private Timer timer;
    private int amount = 0;
    private CountDownTimer countDownTimer;
    private ManageCartModel manageCartModel;
    private ProductModel productModel;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    public static FragmentProductDetials newInstance() {
        return new FragmentProductDetials();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_product_detials, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();

    }

    private void initView() {
        manageCartModel = ManageCartModel.newInstance();

        imagesList = new ArrayList<>();
        binding.setLang(getLang());
        binding.setAmount(amount);
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        mvvm = ViewModelProviders.of(activity).get(FragmentProductDetailsMvvm.class);
        generalMvvm.getProductAmount().observe(activity, productAmount -> {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            binding.motion.transitionToStart();
            productId = productAmount.getProduct_d();

            if (productAmount.getProduct_d() != null && productModel != null && productAmount.getProduct_d().equals(productModel.getId())) {
                amount = productAmount.getAmount();
                binding.setAmount(amount);
                productModel.setAmount(amount);


            } else {
                amount = 0;
                binding.setAmount(amount);
                binding.setPrductModel(null);
                mvvm.getSingleProduct(productId,getUserModel());
            }
        });

        mvvm.getOnDataSuccess().observe(this, productModel -> {
            if (productModel != null) {
                FragmentProductDetials.this.productModel = productModel;
                amount = manageCartModel.getProductAmount(FragmentProductDetials.this.productModel.getId(), activity);
                binding.setAmount(amount);
                FragmentProductDetials.this.productModel.setAmount(amount);
                updateData(FragmentProductDetials.this.productModel);
            }
        });
        mvvm.getOnFavUnFavSuccess().observe(activity, model -> {
            generalMvvm.getOnProductItemUpdated().setValue(model);
        });
        generalMvvm.getOnProductItemUpdated().observe(activity, productModel -> {
            if (this.productModel != null) {
                this.productModel.setIs_list(productModel.getIs_list());
                binding.setPrductModel(this.productModel);
            }

        });
        binding.llBack.setOnClickListener(v -> {
            generalMvvm.onHomeBackNavigate().setValue(true);
        });
        sliderAdapter = new ProductSliderAdapter(imagesList, activity);
        binding.pager.setAdapter(sliderAdapter);
        binding.pager.setClipToPadding(false);
        binding.tab.setViewPager(binding.pager);


        binding.tvCartAmount.setOnClickListener(v -> {
            binding.motion.transitionToEnd();
            startTimer();


        });

        binding.imgCart.setOnClickListener(v -> {
            binding.motion.transitionToEnd();
            startTimer();
        });

        binding.imageIncrease.setOnClickListener(v -> {

            amount += 1;
            binding.setAmount(amount);
            startTimer();
            productModel.setAmount(amount);
            addProductToCart(productModel);


        });

        binding.imageDecrease.setOnClickListener(v -> {

            amount = amount - 1;

            if (amount > 1) {
                binding.setAmount(amount);


            } else {
                amount = 1;
                binding.setAmount(1);


            }
            productModel.setAmount(amount);
            addProductToCart(productModel);

            startTimer();
        });

        binding.imageDelete.setOnClickListener(v -> {
            amount = 0;
            binding.setAmount(0);
            productModel.setAmount(amount);
            removeProductFromCart(productModel);
            startTimer();

        });

        binding.imageShare.setOnClickListener(v -> {
            share();
        });

        binding.imageTag.setOnClickListener(v -> {

            if (productModel.getIs_list().equals("true")) {
                productModel.setIs_list("false");
            } else {
                productModel.setIs_list("true");

            }

            binding.setPrductModel(productModel);
            generalMvvm.getOnProductItemUpdated().setValue(productModel);
            mvvm.favUnFav(getUserModel(), productModel);
        });

    }

    private void share() {
        String shareLink = Tags.base_url + "product/details/" + productId;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, shareLink);
        startActivity(Intent.createChooser(intent, productModel.getTitle_ar()));

    }

    private void startTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(1500, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                binding.motion.transitionToStart();
            }
        };

        countDownTimer.start();
    }

    private void updateData(ProductModel productModel) {
        binding.setPrductModel(productModel);
        imagesList.clear();
        if (productModel.getImages().size() > 0) {
            imagesList.addAll(productModel.getImages());
            sliderAdapter.notifyDataSetChanged();
            timer = new Timer();
            timer.scheduleAtFixedRate(new MyTask(), 3000, 3000);
            binding.fl.setVisibility(View.VISIBLE);
            binding.image.setVisibility(View.GONE);
            binding.pager.setVisibility(View.VISIBLE);
        } else {
            binding.fl.setVisibility(View.GONE);
            binding.image.setVisibility(View.VISIBLE);
            binding.pager.setVisibility(View.GONE);
        }

    }

    public class MyTask extends TimerTask {
        @Override
        public void run() {
            activity.runOnUiThread(() -> {
                int current_page = binding.pager.getCurrentItem();
                if (current_page < sliderAdapter.getCount() - 1) {
                    binding.pager.setCurrentItem(binding.pager.getCurrentItem() + 1);
                } else {
                    binding.pager.setCurrentItem(0);

                }
            });

        }

    }

    public void addProductToCart(ProductModel productModel) {
        manageCartModel.add(productModel, activity);
        generalMvvm.getOnCartRefreshed().setValue(true);
        generalMvvm.getOnCartItemUpdated().setValue(productModel);

    }

    public void removeProductFromCart(ProductModel productModel) {
        manageCartModel.delete(productModel, activity);
        generalMvvm.getOnCartRefreshed().setValue(true);
        generalMvvm.getOnCartMyListRefreshed().setValue(true);
        productModel.setAmount(0);
        generalMvvm.getOnCartItemUpdated().setValue(productModel);
    }

}