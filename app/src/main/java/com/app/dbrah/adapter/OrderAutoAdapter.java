package com.app.dbrah.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.app.dbrah.R;
import com.app.dbrah.model.OrderModel;
import com.app.dbrah.model.OrderModel;
import com.app.dbrah.uis.activity_contact_us.ContactUsActivity;

import java.util.List;

public class OrderAutoAdapter extends ArrayAdapter<OrderModel> {
    private final Context mContext;
    private  List<OrderModel> OrderModelList;
    private final int mLayoutResourceId;

    public OrderAutoAdapter(Context context, int resource) {
        super(context, resource);
        this.mContext = context;
        this.mLayoutResourceId = resource;
    }

    public int getCount() {

        return OrderModelList!=null?OrderModelList.size():0;
    }

    public OrderModel getItem(int position) {
        return OrderModelList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(mLayoutResourceId, parent, false);
            }
            OrderModel OrderModel = getItem(position);
            TextView name = (TextView) convertView.findViewById(R.id.tvTitle);
            name.setText(OrderModel.getId());
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mContext instanceof ContactUsActivity){
ContactUsActivity contactUsActivity=(ContactUsActivity) mContext;
contactUsActivity.setorder(OrderModelList.get(position).getId());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    public void updateList(List<OrderModel> list) {
        this.OrderModelList=list;
        notifyDataSetChanged();
    }
}