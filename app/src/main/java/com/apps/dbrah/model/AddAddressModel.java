package com.apps.dbrah.model;

import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.apps.dbrah.BR;


public class AddAddressModel extends BaseObservable {
    private String phone;
    private String name;
    private String email;
    private String vat;
    private boolean valid;

    public void isDataValid() {
        if (!name.trim().isEmpty() &&
                Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() &&
                !vat.isEmpty()

        ) {
            setValid(true);
        } else {
            setValid(false);


        }
    }

    public AddAddressModel() {
        phone = "";
        name = "";
        email = "";
        vat = "";

    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
        isDataValid();
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
        isDataValid();
    }

    @Bindable
    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
        notifyPropertyChanged(BR.vat);
        isDataValid();
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