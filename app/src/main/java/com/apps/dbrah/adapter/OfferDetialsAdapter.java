package com.apps.dbrah.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.apps.dbrah.R;
import com.apps.dbrah.databinding.OfferDetialsRowBinding;
import com.apps.dbrah.model.OrderModel;

import java.util.List;

public class OfferDetialsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<OrderModel.OfferDetail> list;
    private Context context;
    private LayoutInflater inflater;
    private String lang;

    public OfferDetialsAdapter(List<OrderModel.OfferDetail> list, Context context,String lang) {
        this.list = list;
        this.context = context;
        this.lang=lang;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OfferDetialsRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.offer_detials_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setLang(lang);
        myHolder.binding.setModel(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public OfferDetialsRowBinding binding;

        public MyHolder(OfferDetialsRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<OrderModel.OfferDetail> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
