package com.apps.dbrah.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.dbrah.R;

import com.apps.dbrah.databinding.RecentRowBinding;
import com.apps.dbrah.model.ProductModel;
import com.apps.dbrah.model.RecentProductDataModel;
import com.apps.dbrah.uis.activity_home.market_module.FragmentMarket;

import java.util.List;

public class RecentProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ProductModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    private Handler handler = new Handler();
    private Runnable runnable;

    public RecentProductAdapter(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
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

        Log.e("amount", productModel.getAmount() + "");

        myHolder.binding.setModel(productModel);
        myHolder.binding.motion.setTransitionDuration(500);
        myHolder.binding.motion.setTransition(R.id.start, R.id.end);

        myHolder.binding.tvCartAmount.setOnClickListener(v -> {
            myHolder.binding.motion.transitionToEnd();

            runnable = () -> myHolder.binding.motion.transitionToStart();
            handler.postDelayed(runnable, 3000);


        });

        myHolder.binding.imgCart.setOnClickListener(v -> {
            myHolder.binding.motion.transitionToEnd();

            runnable = () -> myHolder.binding.motion.transitionToStart();
            handler.postDelayed(runnable, 5000);


        });

        myHolder.binding.imageIncrease.setOnClickListener(v -> {
            ProductModel model = list.get(myHolder.getAdapterPosition());
            int amount = model.getAmount() + 1;
            model.setAmount(amount);
            myHolder.binding.setModel(model);
            list.set(myHolder.getAdapterPosition(), model);
            handler.removeCallbacks(runnable);
            runnable = null;

            runnable = () -> myHolder.binding.motion.transitionToStart();
            handler.postDelayed(runnable, 5000);


        });

        myHolder.binding.imageDecrease.setOnClickListener(v -> {
            handler.removeCallbacks(runnable);
            runnable = null;

            runnable = () -> myHolder.binding.motion.transitionToStart();
            handler.postDelayed(runnable, 5000);


            ProductModel model = list.get(myHolder.getAdapterPosition());
            int amount = model.getAmount() - 1;

            if (amount > 0) {
                model.setAmount(amount);


            } else {
                model.setAmount(0);

            }

            myHolder.binding.setModel(model);
            list.set(myHolder.getAdapterPosition(), model);


        });


        myHolder.itemView.setOnClickListener(view -> {
            if (fragment instanceof FragmentMarket) {
                FragmentMarket fragmentMarket = (FragmentMarket) fragment;
                fragmentMarket.showProductDetials(list.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
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
}
