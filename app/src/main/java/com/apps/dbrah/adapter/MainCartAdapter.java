package com.apps.dbrah.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.apps.dbrah.R;
import com.apps.dbrah.databinding.MainCartRowBinding;
import com.apps.dbrah.model.cart_models.CartModel;

import java.util.ArrayList;
import java.util.List;

public class MainCartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CartModel.CartObject> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    private String lang;

    public MainCartAdapter(Context context,Fragment fragment,String lang) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
        this.lang = lang;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MainCartRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.main_cart_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        CartModel.CartObject cartObject = list.get(position);
        myHolder.binding.setLang(lang);
        myHolder.binding.setModel(cartObject);
        myHolder.binding.recView.setLayoutManager(new LinearLayoutManager(context));
        CartAdapter adapter = new CartAdapter(context,fragment,lang);
        myHolder.binding.recView.setAdapter(adapter);
        adapter.updateList(cartObject.getProducts());
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public MainCartRowBinding binding;

        public MyHolder(MainCartRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<CartModel.CartObject> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
