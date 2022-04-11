package com.apps.dbrah.adapter;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.dbrah.R;
import com.apps.dbrah.databinding.RecentRowBinding;
import com.apps.dbrah.databinding.SearchHomeProductRowBinding;
import com.apps.dbrah.model.ProductModel;
import com.apps.dbrah.uis.activity_home.market_module.FragmentMarket;
import com.apps.dbrah.uis.activity_home.search_module.FragmentSearch;

import java.util.List;

public class SearchHomeProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ProductModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    private String lang;

    public SearchHomeProductAdapter(Context context, Fragment fragment, String lang) {
        this.context = context;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
        this.lang = lang;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SearchHomeProductRowBinding rowBinding = DataBindingUtil.inflate(inflater, R.layout.search_home_product_row, parent, false);
        return new MyHolder(rowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        ProductModel productModel = list.get(position);
        myHolder.binding.setLang(lang);
        myHolder.binding.setModel(productModel);


        myHolder.itemView.setOnClickListener(view -> {
            if (fragment instanceof FragmentSearch) {
                FragmentSearch fragmentSearch = (FragmentSearch) fragment;
                fragmentSearch.showProductDetails(list.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


    public static class MyHolder extends RecyclerView.ViewHolder {
        public SearchHomeProductRowBinding binding;

        public MyHolder(SearchHomeProductRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<ProductModel> list) {
        if (list==null){
            if (this.list!=null){
                this.list.clear();
            }
        }else {
            this.list = list;
        }

        notifyDataSetChanged();
    }
}
