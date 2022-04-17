package com.apps.dbrah.model;

import java.io.Serializable;
import java.util.List;

public class AddressesDataModel extends StatusResponse implements Serializable {
    private List<AddressModel> data;

    public List<AddressModel> getData() {
        return data;
    }
}
