package com.app.dbrah.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.dbrah.R;
import com.app.dbrah.databinding.FilterProductRowBinding;
import com.app.dbrah.databinding.RecentRowBinding;
import com.app.dbrah.model.ProductModel;
import com.app.dbrah.uis.activity_home.market_module.FragmentMarket;
import com.app.dbrah.uis.activity_home.products_module.FragmentProducts;

import java.util.List;

public class FilterProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ProductModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    private CountDownTimer countDownTimer;
    private String lang;


    public FilterProductAdapter(Context context, Fragment fragment,String lang) {
        this.context = context;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
        this.lang = lang;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FilterProductRowBinding rowBinding = DataBindingUtil.inflate(inflater, R.layout.filter_product_row, parent, false);
        return new MyHolder(rowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setLang(lang);
        ProductModel productModel = list.get(position);

        myHolder.binding.setModel(productModel);
        myHolder.binding.motion.setTransitionDuration(500);
        myHolder.binding.motion.setTransition(R.id.start, R.id.end);
        myHolder.binding.tvCartAmount.setOnClickListener(v -> {
            myHolder.binding.motion.transitionToEnd();
            startTimer(myHolder);

        });

        myHolder.binding.imgCart.setOnClickListener(v -> {
            myHolder.binding.motion.transitionToEnd();
            startTimer(myHolder);
        });

        myHolder.binding.imageIncrease.setOnClickListener(v -> {
            ProductModel model = list.get(myHolder.getAdapterPosition());
            int amount = model.getAmount() + 1;
            model.setAmount(amount);
            myHolder.binding.setModel(model);
            list.set(myHolder.getAdapterPosition(), model);
            startTimer(myHolder);

            if (fragment instanceof FragmentProducts) {
                FragmentProducts fragmentProducts = (FragmentProducts) fragment;
                fragmentProducts.addProductToCart(model);
            }


        });

        myHolder.binding.imageDecrease.setOnClickListener(v -> {

            ProductModel model = list.get(myHolder.getAdapterPosition());
            int amount = model.getAmount() - 1;

            if (amount > 1) {
                model.setAmount(amount);

            } else {
                model.setAmount(1);


            }


            myHolder.binding.setModel(model);
            list.set(myHolder.getAdapterPosition(), model);

            startTimer(myHolder);
            if (fragment instanceof FragmentProducts) {
                FragmentProducts fragmentProducts = (FragmentProducts) fragment;
                fragmentProducts.addProductToCart(model);
            }

        });

        myHolder.binding.imageDelete.setOnClickListener(v -> {
            ProductModel model = list.get(myHolder.getAdapterPosition());
            model.setAmount(0);

            myHolder.binding.setModel(model);
            list.set(myHolder.getAdapterPosition(), model);
            startTimer(myHolder);

            if (fragment instanceof FragmentProducts) {
                FragmentProducts fragmentProducts = (FragmentProducts) fragment;
                fragmentProducts.removeProductFromCart(model);
            }


        });


        myHolder.itemView.setOnClickListener(view -> {
            if (fragment instanceof FragmentMarket) {
                FragmentMarket fragmentMarket = (FragmentMarket) fragment;
                fragmentMarket.showProductDetails(list.get(holder.getAdapterPosition()));
            } else if (fragment instanceof FragmentProducts) {
                FragmentProducts fragmentProducts = (FragmentProducts) fragment;
                fragmentProducts.showProductDetails(list.get(holder.getAdapterPosition()));
            }
        });

        myHolder.binding.imageTag.setOnClickListener(v -> {
            ProductModel model = list.get(myHolder.getAdapterPosition());
            if (model.getIs_list().equals("true")) {
                model.setIs_list("false");
            } else {
                model.setIs_list("true");

            }

            if (fragment instanceof FragmentProducts) {
                FragmentProducts fragmentProducts = (FragmentProducts) fragment;
                fragmentProducts.favUnFav(list.get(holder.getAdapterPosition()), myHolder.getAdapterPosition());
            }


        });
    }

    private void startTimer(MyHolder myHolder) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(4000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                myHolder.binding.motion.transitionToStart();
            }
        };

        countDownTimer.start();
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        public FilterProductRowBinding binding;

        public MyHolder(FilterProductRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<ProductModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
