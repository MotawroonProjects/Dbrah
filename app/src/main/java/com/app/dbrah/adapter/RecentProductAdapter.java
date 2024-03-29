package com.app.dbrah.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.dbrah.R;

import com.app.dbrah.databinding.RecentRowBinding;
import com.app.dbrah.model.ProductModel;
import com.app.dbrah.uis.activity_home.market_module.FragmentMarket;

import java.util.List;

public class RecentProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ProductModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    private CountDownTimer countDownTimer;
    private String lang;


    public RecentProductAdapter(Context context, Fragment fragment,String lang) {
        this.context = context;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
        this.lang = lang;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecentRowBinding rowBinding = DataBindingUtil.inflate(inflater, R.layout.recent_row, parent, false);
        return new MyHolder(rowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        ProductModel productModel = list.get(position);
        myHolder.binding.setLang(lang);
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
            if (fragment instanceof FragmentMarket) {
                FragmentMarket fragmentMarket = (FragmentMarket) fragment;
                fragmentMarket.addProductToCart(model,"recent");
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

            if (fragment instanceof FragmentMarket) {
                FragmentMarket fragmentMarket = (FragmentMarket) fragment;
                fragmentMarket.addProductToCart(model,"recent");
            }

        });

        myHolder.binding.imageDelete.setOnClickListener(v -> {
            ProductModel model = list.get(myHolder.getAdapterPosition());
            model.setAmount(0);

            myHolder.binding.setModel(model);
            list.set(myHolder.getAdapterPosition(), model);
            startTimer(myHolder);
            if (fragment instanceof FragmentMarket) {
                FragmentMarket fragmentMarket = (FragmentMarket) fragment;
                fragmentMarket.removeProductFromCart(model,"recent");
            }

        });

        myHolder.itemView.setOnClickListener(view -> {
            if (fragment instanceof FragmentMarket) {
                FragmentMarket fragmentMarket = (FragmentMarket) fragment;
                fragmentMarket.showProductDetails(list.get(holder.getAdapterPosition()));
            }
        });

        myHolder.binding.imageTag.setOnClickListener(v -> {
            ProductModel model = list.get(myHolder.getAdapterPosition());
            if (model.getIs_list().equals("true")) {
                model.setIs_list("false");
            } else {
                model.setIs_list("true");

            }

            if (fragment instanceof FragmentMarket) {
                FragmentMarket fragmentMarket = (FragmentMarket) fragment;
                fragmentMarket.favUnFav(list.get(holder.getAdapterPosition()),myHolder.getAdapterPosition());
            }




        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public void updateItem(ProductModel productModel, int productPos) {
        this.list.set(productPos,productModel);
       // notifyItemChanged(productPos);
        notifyDataSetChanged();
    }


    public static class MyHolder extends RecyclerView.ViewHolder {
        public RecentRowBinding binding;

        public MyHolder(RecentRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<ProductModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    private void startTimer(MyHolder myHolder) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(4000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                Log.e("time",millisUntilFinished+"__");
            }

            @Override
            public void onFinish() {
                Log.e("time2","finished"+"__");

                myHolder.binding.motion.transitionToStart();
            }
        };

        countDownTimer.start();
    }

}
