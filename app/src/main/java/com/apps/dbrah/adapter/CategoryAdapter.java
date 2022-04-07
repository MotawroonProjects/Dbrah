package com.apps.dbrah.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.dbrah.R;
import com.apps.dbrah.databinding.CategoryRowBinding;
import com.apps.dbrah.model.CategoryDataModel;

import java.util.List;
import java.util.Random;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<CategoryDataModel.CategoryModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;

    public CategoryAdapter( Context context, Fragment fragment) {
        this.context = context;
        this.fragment=fragment;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryRowBinding binding= DataBindingUtil.inflate(inflater, R.layout.category_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder=(MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
//        myHolder.itemView.setBackgroundColor(getRandomColorCode());
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


    public static class MyHolder extends RecyclerView.ViewHolder {
        public CategoryRowBinding binding;

        public MyHolder(CategoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<CategoryDataModel.CategoryModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }
    public int getRandomColorCode(){

        Random random = new Random();

        return Color.argb(255, random.nextInt(256), random.nextInt(256),     random.nextInt(256));

    }
}
