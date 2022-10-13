package com.app.dbrah.model;

import android.content.Context;
import android.util.Log;
import android.util.Patterns;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.app.dbrah.BR;
import com.app.dbrah.R;


public class SignUpModel extends BaseObservable {
    private String image_uri;
    private String image_path;
    private String phone_code;
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

    public SignUpModel() {
        image_uri = "";
        image_path = "";
        phone_code = "";
        phone = "";
        name = "";
        email = "";
        vat = "";

    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;
    }

    public String getPhone_code() {
        return phone_code;
    }

    public void setPhone_code(String phone_code) {
        this.phone_code = phone_code;
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

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}