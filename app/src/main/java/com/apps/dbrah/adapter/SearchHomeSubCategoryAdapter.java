package com.apps.dbrah.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.dbrah.R;
import com.apps.dbrah.databinding.SearchHomeSubCategoryRowBinding;
import com.apps.dbrah.model.CategoryModel;
import com.apps.dbrah.uis.activity_home.search_module.FragmentSearch;

import java.util.List;

public class SearchHomeSubCategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CategoryModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    private String lang;
    private MyHolder oldHolder;

    public SearchHomeSubCategoryAdapter(Context context, Fragment fragment, String lang) {
        this.context = context;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
        this.lang = lang;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SearchHomeSubCategoryRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.search_home_sub_category_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.setLang(lang);

        myHolder.itemView.setOnClickListener(v -> {
            CategoryModel categoryModel = list.get(myHolder.getAdapterPosition());

            if (!categoryModel.isSelected()){
                if (oldHolder != null) {
                    CategoryModel model = list.get(oldHolder.getAdapterPosition());
                    model.setSelected(false);
                    oldHolder.binding.setModel(model);
                }

                categoryModel.setSelected(true);
                myHolder.binding.setModel(categoryModel);
                oldHolder = myHolder;
            }

            if (fragment instanceof FragmentSearch){
                FragmentSearch fragmentSearch = (FragmentSearch) fragment;
                fragmentSearch.setSubCategory(categoryModel);
            }


        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public SearchHomeSubCategoryRowBinding binding;

        public MyHolder(SearchHomeSubCategoryRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<CategoryModel> list) {
        if (oldHolder!=null){
            oldHolder = null;
        }
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
