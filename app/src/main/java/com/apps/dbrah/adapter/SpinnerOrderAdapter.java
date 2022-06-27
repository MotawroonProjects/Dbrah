package com.apps.dbrah.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.apps.dbrah.R;
import com.apps.dbrah.databinding.SpinnerRowBinding;
import com.apps.dbrah.databinding.TimeSpinnerRowBinding;
import com.apps.dbrah.model.OrderModel;
import com.apps.dbrah.model.TimeModel;

import java.util.List;

public class SpinnerOrderAdapter extends BaseAdapter {
    private List<OrderModel> list;
    private Context context;
    private LayoutInflater inflater;

    public SpinnerOrderAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return list != null ? list.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint("ViewHolder") SpinnerRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.spinner_row, parent, false);
        binding.setTitle(list.get(position).getId());
        return binding.getRoot();
    }

    public void updateList(List<OrderModel> list) {
        if (list != null) {
            this.list = list;

        } else {
            this.list.clear();
        }
        notifyDataSetChanged();
    }
}
