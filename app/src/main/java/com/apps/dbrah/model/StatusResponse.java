package com.apps.dbrah.model;

import java.io.Serializable;

public class StatusResponse implements Serializable {
    protected int status;
    protected Object msg;

    public int getStatus() {
        return status;
    }

    public Object getMessage() {
        return msg;
    }
}
