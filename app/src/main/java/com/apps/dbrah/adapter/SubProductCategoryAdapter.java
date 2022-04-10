package com.apps.dbrah.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.dbrah.R;
import com.apps.dbrah.databinding.SubCategoryRowBinding;
import com.apps.dbrah.model.CategoryDataModel;

import java.util.List;

public class SubProductCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CategoryDataModel.CategoryModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    private String lang;
    private int currentPos = 0;
    private int oldPos = currentPos;
    private RecyclerView.ViewHolder oldHolder;

    public SubProductCategoryAdapter(Context context, Fragment fragment, String lang) {
        this.context = context;
        this.fragment = fragment;
        this.lang = lang;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SubCategoryRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.sub_category_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        if (oldHolder == null) {
            oldHolder = myHolder;
        }
        myHolder.binding.setLang(lang);
        myHolder.binding.setModel(list.get(position));
        myHolder.itemView.setOnClickListener(v -> {
            if (oldHolder != null) {

               CategoryDataModel.CategoryModel oldCategory = list.get(oldPos);
                oldCategory.setSelected(false);
                list.set(oldPos, oldCategory);
                MyHolder oHolder = (MyHolder) oldHolder;
                oHolder.binding.setModel(oldCategory);


            }
            currentPos = myHolder.getAdapterPosition();
            CategoryDataModel.CategoryModel category = list.get(currentPos);
            category.setSelected(true);
            list.set(currentPos, category);

            myHolder.binding.setModel(category);

            oldHolder = myHolder;
            oldPos = currentPos;



        });

    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }


    public static class MyHolder extends RecyclerView.ViewHolder {
        public SubCategoryRowBinding binding;

        public MyHolder(SubCategoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<CategoryDataModel.CategoryModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
