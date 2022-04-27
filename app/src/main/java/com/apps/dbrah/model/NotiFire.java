package com.apps.dbrah.model;

import java.io.Serializable;

public class NotiFire implements Serializable {
    private boolean newData = false;

    public NotiFire(boolean newData) {
        this.newData = newData;
    }

    public boolean isNewData() {
        return newData;
    }
}
