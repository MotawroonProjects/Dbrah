package com.apps.dbrah.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.dbrah.R;
import com.apps.dbrah.databinding.MainCategoryRowBinding;
import com.apps.dbrah.model.CategoryDataModel;
import com.apps.dbrah.model.CategoryModel;
import com.apps.dbrah.uis.activity_home.products_module.FragmentProducts;

import java.util.List;

public class MainProductCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CategoryModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    private String lang;
    private String cat_id;
    private int currentPos = -1;
    private int oldPos = currentPos;
    private RecyclerView.ViewHolder oldHolder;

    public MainProductCategoryAdapter(Context context, Fragment fragment, String lang) {
        this.context = context;
        this.fragment = fragment;
        this.lang = lang;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MainCategoryRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.main_category_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.setLang(lang);
        if (oldHolder == null) {
            if (list.get(position).isSelected()) {
                Log.e("sslsll", String.valueOf(position));
                oldHolder = myHolder;
                currentPos = position;
                oldPos = currentPos;
            }

        } else {
            if (list.get(position).isSelected()) {
                oldHolder = myHolder;
                currentPos = position;
                oldPos = currentPos;
            }
        }
        myHolder.itemView.setOnClickListener(v -> {
            if (oldHolder != null) {

                CategoryModel oldCategory = list.get(oldPos);
                oldCategory.setSelected(false);
                list.set(oldPos, oldCategory);
                MainProductCategoryAdapter.MyHolder oHolder = (MainProductCategoryAdapter.MyHolder) oldHolder;
                oHolder.binding.setModel(oldCategory);


            }
            currentPos = myHolder.getAdapterPosition();
            CategoryModel category = list.get(currentPos);
            category.setSelected(true);
            list.set(currentPos, category);

            myHolder.binding.setModel(category);

            oldHolder = myHolder;
            oldPos = currentPos;
            if (fragment instanceof FragmentProducts) {
                FragmentProducts fragmentProducts = (FragmentProducts) fragment;
                fragmentProducts.getsubcat(list.get(currentPos));
            }

        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


    public static class MyHolder extends RecyclerView.ViewHolder {
        public MainCategoryRowBinding binding;

        public MyHolder(MainCategoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<CategoryModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
