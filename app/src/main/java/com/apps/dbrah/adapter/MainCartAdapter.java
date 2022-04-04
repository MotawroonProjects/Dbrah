package com.apps.dbrah.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.apps.dbrah.R;
import com.apps.dbrah.databinding.MainCartRowBinding;

import java.util.ArrayList;
import java.util.List;

public class MainCartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> list;
    private Context context;
    private LayoutInflater inflater;
    private AppCompatActivity appCompatActivity;

    public MainCartAdapter(List<Object> list, Context context) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
        appCompatActivity = (AppCompatActivity) context;
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
        ((MyHolder) holder).binding.recView.setLayoutManager(new LinearLayoutManager(context));
        ((MyHolder) holder).binding.recView.setAdapter(new CartAdapter(new ArrayList<>(), context));
    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public MainCartRowBinding binding;

        public MyHolder(MainCartRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<Object> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
