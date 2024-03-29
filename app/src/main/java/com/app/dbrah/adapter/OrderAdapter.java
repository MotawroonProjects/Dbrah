package com.app.dbrah.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.dbrah.R;
import com.app.dbrah.databinding.OrderRowBinding;
import com.app.dbrah.model.OrderModel;
import com.app.dbrah.uis.activity_home.order_module.FragmentCurrentOrder;
import com.app.dbrah.uis.activity_home.order_module.FragmentPreviousOrder;
import com.app.dbrah.uis.activity_previous_order.PreviousOrderActivity;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<OrderModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    private String lang;

    public OrderAdapter(Context context, Fragment fragment,String lang) {
        this.context = context;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
        this.lang = lang;

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
        if(context instanceof PreviousOrderActivity){
            myHolder.binding.llResend.setVisibility(View.GONE);
        }
        myHolder.binding.setLang(lang);
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.llDetails.setOnClickListener(view -> {
            if (fragment instanceof FragmentCurrentOrder) {
                FragmentCurrentOrder fragmentCurrentOrder = (FragmentCurrentOrder) fragment;
                fragmentCurrentOrder.navigateToDetails(list.get(myHolder.getAdapterPosition()));
            } else if (fragment instanceof FragmentPreviousOrder) {
                FragmentPreviousOrder fragmentPreviousOrder = (FragmentPreviousOrder) fragment;
                fragmentPreviousOrder.navigateToDetails(list.get(myHolder.getAdapterPosition()),myHolder.getAdapterPosition());
            }
        });

        myHolder.binding.llResend.setOnClickListener(view -> {
             if (fragment instanceof FragmentPreviousOrder) {
                FragmentPreviousOrder fragmentPreviousOrder = (FragmentPreviousOrder) fragment;
                fragmentPreviousOrder.reSend(list.get(myHolder.getAdapterPosition()));
            }
        });


    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public OrderRowBinding binding;

        public MyHolder(OrderRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<OrderModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
