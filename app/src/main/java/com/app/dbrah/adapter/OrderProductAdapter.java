package com.app.dbrah.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.dbrah.R;
import com.app.dbrah.databinding.OrderProductRowBinding;
import com.app.dbrah.databinding.SearchHomeProductRowBinding;
import com.app.dbrah.model.OrderModel;
import com.app.dbrah.model.ProductModel;
import com.app.dbrah.uis.activity_home.search_module.FragmentSearch;

import java.util.List;

public class OrderProductAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<OrderModel.Details> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    private String lang;

    public OrderProductAdapter(Context context, Fragment fragment, String lang) {
        this.context = context;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
        this.lang = lang;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderProductRowBinding rowBinding = DataBindingUtil.inflate(inflater, R.layout.order_product_row, parent, false);
        return new MyHolder(rowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
   MyHolder myHolder = (MyHolder) holder;
        ProductModel productModel = list.get(position).getProduct();
        myHolder.binding.setLang(lang);
        myHolder.binding.setModel(productModel);



    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


    public static class MyHolder extends RecyclerView.ViewHolder {
        public OrderProductRowBinding binding;

        public MyHolder(OrderProductRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<OrderModel.Details> list) {
        if (list == null) {
            if (this.list != null) {
                this.list.clear();
            }
        } else {
            this.list = list;
        }

        notifyDataSetChanged();
    }
}
