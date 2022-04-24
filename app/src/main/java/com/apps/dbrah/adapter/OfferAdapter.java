package com.apps.dbrah.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.dbrah.R;
import com.apps.dbrah.databinding.OfferDetialsRowBinding;
import com.apps.dbrah.databinding.OfferRowBinding;
import com.apps.dbrah.model.OrderModel;
import com.apps.dbrah.uis.activity_current_order_details.CurrentOrderDetailsActivity;

import java.util.List;

public class OfferAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<OrderModel.Offers> list;
    private Context context;
    private LayoutInflater inflater;
    private String lang;

    public OfferAdapter(Context context,String lang) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.lang = lang;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OfferRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.offer_row, parent, false);
        return new MyHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyHolder myHolder = (MyHolder) holder;
        myHolder.binding.setLang(lang);
        myHolder.binding.setModel(list.get(position));
myHolder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        if(context instanceof CurrentOrderDetailsActivity){
            CurrentOrderDetailsActivity activity=(CurrentOrderDetailsActivity) context;
            activity.show(list.get(holder.getAdapterPosition()));
        }
    }
});
    }

    @Override
    public int getItemCount() {
        return list!=null?list.size():0;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {
        public OfferRowBinding binding;

        public MyHolder(OfferRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }

    public void updateList(List<OrderModel.Offers> list) {
        this.list = list;
        notifyDataSetChanged();
    }
}
