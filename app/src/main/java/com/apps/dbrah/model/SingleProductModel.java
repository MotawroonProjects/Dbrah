package com.apps.dbrah.model;

import java.io.Serializable;

public class SingleProductModel extends StatusResponse implements Serializable {
    private ProductModel data;

    public ProductModel getData() {
        return data;
    }
}
