package com.app.dbrah.model;

import java.io.Serializable;

public class CountryCodeModel implements Serializable {
    private int flag;
    private String name;
    private String code;
    private String country_code;

    public CountryCodeModel(int flag, String name, String code, String country_code) {
        this.flag = flag;
        this.name = name;
        this.code = code;
        this.country_code = country_code;
    }

    public int getFlag() {
        return flag;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public String getCountry_code() {
        return country_code;
    }
}
