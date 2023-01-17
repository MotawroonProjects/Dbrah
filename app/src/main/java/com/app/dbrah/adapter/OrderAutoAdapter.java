package com.app.dbrah.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;


import com.app.dbrah.R;
import com.app.dbrah.model.OrderModel;
import com.app.dbrah.model.OrderModel;
import com.app.dbrah.uis.activity_contact_us.ContactUsActivity;

import java.util.ArrayList;
import java.util.List;

public class OrderAutoAdapter extends BaseAdapter implements Filterable {
    private final Context mContext;
    private List<OrderModel> OrderModelList;
    private final int mLayoutResourceId;

    public OrderAutoAdapter(Context context, int resource) {
        this.mContext = context;
        this.mLayoutResourceId = resource;
    }

    public int getCount() {
        Log.e("F;", OrderModelList.size() + "");

        return OrderModelList != null ? OrderModelList.size() : 0;
    }

    public OrderModel getItem(int position) {
        return OrderModelList.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Log.e("F;eeeee", OrderModelList.size() + "");
        try {
            if (convertView == null) {
                LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
                convertView = inflater.inflate(mLayoutResourceId, parent, false);
            }
            Log.e("F;sssflfl", OrderModelList.size() + "");

            OrderModel OrderModel = getItem(position);
            TextView name = (TextView) convertView.findViewById(R.id.tvTitle);
            name.setText(OrderModel.getId());
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mContext instanceof ContactUsActivity) {
                        ContactUsActivity contactUsActivity = (ContactUsActivity) mContext;
                        contactUsActivity.setorder(OrderModelList.get(position).getId());
                    }
                }
            });
        } catch (Exception e) {
            Log.e("D;d;d;;", e.toString());
        }
        return convertView;
    }

    public void updateList(List<OrderModel> list) {
        this.OrderModelList = list;
        Log.e("F;flfl", OrderModelList.size() + "");
        notifyDataSetChanged();
       // notifyDataSetChanged();

    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            public String convertResultToString(Object resultValue) {
                return ((OrderModel) resultValue).getId();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                List<OrderModel> departmentsSuggestion = new ArrayList<>();
                if (constraint != null) {
                    for (OrderModel department : OrderModelList) {
                        if (department.getId().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            departmentsSuggestion.add(department);
                        }
                    }
                    filterResults.values = departmentsSuggestion;
                    filterResults.count = departmentsSuggestion.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                OrderModelList.clear();
                if (results != null && results.count > 0) {
                    // avoids unchecked cast warning when using mDepartments.addAll((ArrayList<Department>) results.values);
                    for (Object object : (List<?>) results.values) {
                        if (object instanceof OrderModel) {
                            OrderModelList.add((OrderModel) object);
                        }
                    }
                    notifyDataSetChanged();
                } else if (constraint == null) {
                    // no filter, add entire original list back in
                   // mDepartments.addAll(mDepartmentsAll);
                    notifyDataSetInvalidated();
                }
            }
        };
    }
}