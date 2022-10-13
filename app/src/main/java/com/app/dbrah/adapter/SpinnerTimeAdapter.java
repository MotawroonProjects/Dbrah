package com.app.dbrah.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.databinding.DataBindingUtil;

import com.app.dbrah.R;
import com.app.dbrah.databinding.CountrySpinnerRowBinding;
import com.app.dbrah.databinding.TimeSpinnerRowBinding;
import com.app.dbrah.model.CountryCodeModel;
import com.app.dbrah.model.TimeModel;

import java.util.List;

public class SpinnerTimeAdapter extends BaseAdapter {
    private List<TimeModel> list;
    private Context context;
    private LayoutInflater inflater;

    public SpinnerTimeAdapter(Context context) {
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
        @SuppressLint("ViewHolder") TimeSpinnerRowBinding binding = DataBindingUtil.inflate(inflater, R.layout.time_spinner_row, parent, false);
        binding.setModel(list.get(position));
        return binding.getRoot();
    }

    public void updateList(List<TimeModel> list) {
        if (list != null) {
            this.list = list;

        } else {
            this.list.clear();
        }
        notifyDataSetChanged();
    }
}
