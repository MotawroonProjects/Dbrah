package com.app.dbrah.model;

import android.content.Context;
import android.util.Log;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;

import com.app.dbrah.BR;
import com.app.dbrah.R;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.io.Serializable;

public class LoginModel extends BaseObservable implements Serializable {
    private String phone_code;
    private String code;
    private String phone;
    private boolean valid;
    PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
    public LoginModel() {
        phone_code = "+966";
        phone = "";
        code="SA";
        valid = false;
    }

    public void isDataValid() {

        Phonenumber.PhoneNumber swissNumberProto;
        boolean isValid=false;

        try {
             swissNumberProto = phoneUtil.parse("0"+phone, code);
            isValid = phoneUtil.isValidNumber(swissNumberProto);
        } catch (NumberParseException e) {
            Log.e("NumberParseException" , e.toString());
        }
        if (!phone.isEmpty()&&isValid) {
            setValid(true);
        } else {
            setValid(false);
        }
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
    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
        notifyPropertyChanged(BR.valid);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
        isDataValid();
    }
}