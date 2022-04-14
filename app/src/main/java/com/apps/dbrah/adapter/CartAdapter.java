package com.apps.dbrah.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.dbrah.R;
import com.apps.dbrah.databinding.CartRowBinding;
import com.apps.dbrah.databinding.MainCartRowBinding;
import com.apps.dbrah.model.ProductModel;
import com.apps.dbrah.uis.activity_home.cart_module.FragmentCart;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<ProductModel> list;
    private Context context;
    private LayoutInflater inflater;
    private Fragment fragment;
    private String lang;
    public CartAdapter(Context context,Fragment fragment,String lang) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.fragment = fragment;
        this.lang = lang;

    }




    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartRowBinding binding= DataBindingUtil.inflate(inflater, R.layout.cart_row,parent,false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder=(MyHolder) holder;
        myHolder.binding.setLang(lang);
        myHolder.binding.setModel(list.get(position));

        myHolder.binding.imageIncrease.setOnClickListener(v -> {
            ProductModel productModel = list.get(myHolder.getAdapterPosition());
            int amount = productModel.getAmount() +1;
            productModel.setAmount(amount);
            list.set(myHolder.getAdapterPosition(),productModel);
            if (fragment instanceof FragmentCart){
                FragmentCart fragmentCart = (FragmentCart) fragment;
                fragmentCart.addProductToCart(productModel);
            }
            myHolder.binding.setModel(productModel);

        });

        myHolder.binding.imageDecrease.setOnClickListener(v -> {
            ProductModel productModel = list.get(myHolder.getAdapterPosition());
            if (productModel.getAmount()>1){

                int amount = productModel.getAmount() -1;
                productModel.setAmount(amount);
                list.set(myHolder.getAdapterPosition(),productModel);
                if (fragment instanceof FragmentCart){
                    FragmentCart fragmentCart = (FragmentCart) fragment;
                    fragmentCart.addProductToCart(productModel);
                }

            }
            myHolder.binding.setModel(productModel);


        });

        myHolder.binding.imgDelete.setOnClickListener(v -> {
            ProductModel productModel = list.get(myHolder.getAdapterPosition());
            if (fragment instanceof FragmentCart){
                FragmentCart fragmentCart = (FragmentCart) fragment;
                fragmentCart.removeProductFromCart(productModel);
            }


        });
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public CartRowBinding binding;

        public MyHolder(CartRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
    public void updateList(List<ProductModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
