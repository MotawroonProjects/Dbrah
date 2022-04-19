package com.apps.dbrah.model;

import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.apps.dbrah.BR;


public class AddAddressModel extends BaseObservable {
    private String title;
    private String admin_name;
    private String phone_code;
    private String phone;
    private String time_id;
    private String address;
    private double lat;
    private double lng;
    private boolean valid;

    public AddAddressModel() {
        title = "";
        admin_name = "";
        phone_code = "+966";
        phone = "";
        time_id = "";
        address = "";
        lat = 0;
        lng = 0;
    }

    public void isDataValid() {
        if (!title.trim().isEmpty() &&
                !admin_name.trim().isEmpty() &&
                !phone.trim().isEmpty() &&
                !time_id.isEmpty() &&
                !address.trim().isEmpty()
        ) {
            setValid(true);
        } else {
            setValid(false);
        }
    }

    @Bindable
    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
        isDataValid();
    }

    @Bindable
    public String getAdmin_name() {
        return admin_name;
    }

    public void setAdmin_name(String admin_name) {
        this.admin_name = admin_name;
        notifyPropertyChanged(BR.admin_name);
        isDataValid();
    }

    @Bindable
    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
        notifyPropertyChanged(BR.phone_code);
    }

    @Bindable
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        notifyPropertyChanged(BR.phone);
        isDataValid();
    }

    @Bindable
    public String getTime_id() {
        return time_id;
    }

    public void setTime_id(String time_id) {
        this.time_id = time_id;
        isDataValid();

    }

    @Bindable
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
        notifyPropertyChanged(BR.address);
        isDataValid();
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    @Bindable
    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
        notifyPropertyChanged(BR.valid);
    }
}