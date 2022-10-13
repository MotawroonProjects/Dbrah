package com.app.dbrah.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.app.dbrah.R;
import com.app.dbrah.databinding.CategoryRowBinding;
import com.app.dbrah.model.CategoryDataModel;
import com.app.dbrah.model.CategoryModel;
import com.app.dbrah.uis.activity_home.market_module.FragmentMarket;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CategoryModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    private String lang;

    public CategoryAdapter(Context context, Fragment fragment, String lang) {
        this.context = context;
        this.fragment = fragment;
        inflater = LayoutInflater.from(context);
        this.lang = lang;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CategoryRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.category_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setModel(list.get(position));
        myHolder.binding.setLang(lang);
        if (lang.equals("ar")) {
            myHolder.binding.img.setTranslationX(-10);
        } else {
            myHolder.binding.img.setTranslationX(10);

        }
        loadImage(list.get(position), myHolder.binding);
        myHolder.itemView.setOnClickListener(view -> {
            if (fragment instanceof FragmentMarket) {
                FragmentMarket fragmentMarket = (FragmentMarket) fragment;
                fragmentMarket.showCategoryDetails(list.get(holder.getAdapterPosition()), myHolder.getAdapterPosition());
            }
        });
    }

    private void loadImage(CategoryModel categoryModel, CategoryRowBinding binding) {
        ImageView view = binding.img;
        view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (categoryModel.getImage() != null) {
                    RequestOptions options = new RequestOptions().override(view.getWidth(), view.getHeight());
                    Glide.with(view.getContext())
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .load(categoryModel.getImage())
                            .centerCrop()
                            .apply(options)
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    view.setImageBitmap(resource);
                                    getColorsBg(binding, resource);

                                }
                            });
                }
            }
        });
    }

    private void getColorsBg(CategoryRowBinding binding, Bitmap resource) {
        Palette.from(resource).maximumColorCount(64).generate(palette -> {
            if (palette != null) {
                Palette.Swatch swatch = palette.getDominantSwatch();
                if (swatch != null) {
                    GradientDrawable gd = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{swatch.getRgb(), Color.WHITE,});
                    gd.setGradientType(GradientDrawable.RADIAL_GRADIENT);
                    gd.setGradientRadius(140.0f);
                    binding.flBg.setBackground(gd);
                }
            }

        });
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

    public void updateList(List<CategoryModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }

}
