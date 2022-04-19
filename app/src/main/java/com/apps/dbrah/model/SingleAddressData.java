package com.apps.dbrah.model;

import java.io.Serializable;

public class SingleAddressData extends StatusResponse implements Serializable {
    private AddressModel data;

    public AddressModel getData() {
        return data;
    }
}
