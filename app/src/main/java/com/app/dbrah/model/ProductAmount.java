package com.app.dbrah.model;

import java.io.Serializable;

public class ProductAmount implements Serializable {
    private String product_d;
    private int amount;

    public ProductAmount(String product_d, int amount) {
        this.product_d = product_d;
        this.amount = amount;
    }

    public String getProduct_d() {
        return product_d;
    }

    public void setProduct_d(String product_d) {
        this.product_d = product_d;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
