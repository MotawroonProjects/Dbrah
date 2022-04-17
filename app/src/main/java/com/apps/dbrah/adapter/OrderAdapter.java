package com.apps.dbrah.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.dbrah.R;
import com.apps.dbrah.databinding.OrderRowBinding;
import com.apps.dbrah.uis.activity_home.order_module.FragmentCurrentOrder;
import com.apps.dbrah.uis.activity_home.order_module.FragmentPreviousOrder;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;

    public OrderAdapter(List<Object> list, Context context, Fragment fragment) {
        this.list = list;
        this.context = context;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.order_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;

        myHolder.binding.llDetails.setOnClickListener(view -> {
            if (fragment instanceof FragmentCurrentOrder) {
                FragmentCurrentOrder fragmentCurrentOrder = (FragmentCurrentOrder) fragment;
                fragmentCurrentOrder.navigateToDetails();
            } else if (fragment instanceof FragmentPreviousOrder) {
                FragmentPreviousOrder fragmentPreviousOrder = (FragmentPreviousOrder) fragment;
                fragmentPreviousOrder.navigateToDetails();
            }
        });
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public OrderRowBinding binding;

        public MyHolder(OrderRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<Object> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
