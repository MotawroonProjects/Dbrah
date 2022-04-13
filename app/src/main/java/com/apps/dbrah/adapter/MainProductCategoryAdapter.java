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
    private MyHolder oldHolder;

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

        if (oldHolder==null){
            if (currentPos == position){
                oldHolder = myHolder;
            }
        }


        myHolder.itemView.setOnClickListener(v -> {

            CategoryModel category = list.get(myHolder.getAdapterPosition());

            if (!category.isSelected()) {

                if (oldHolder !=null) {

                    CategoryModel oldCategory = list.get(currentPos);
                    oldCategory.setSelected(false);
                    list.set(currentPos, oldCategory);
                    oldHolder.binding.setModel(oldCategory);


                }
                category.setSelected(true);
                list.set(myHolder.getAdapterPosition(), category);
                myHolder.binding.setModel(category);


                currentPos = myHolder.getAdapterPosition();
                oldHolder = myHolder;
                if (fragment instanceof FragmentProducts) {
                    FragmentProducts fragmentProducts = (FragmentProducts) fragment;
                    fragmentProducts.getSubCat(list.get(myHolder.getAdapterPosition()));
                }

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

    public void setSelectedPos(int pos){
        currentPos = pos;
    }


}
