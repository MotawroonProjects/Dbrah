package com.apps.dbrah.model;

import java.io.Serializable;
import java.util.List;

public class TimeDataModel extends StatusResponse implements Serializable {
    private List<TimeModel> data;

    public List<TimeModel> getData() {
        return data;
    }
}
