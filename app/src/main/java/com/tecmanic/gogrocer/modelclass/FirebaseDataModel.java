package com.tecmanic.gogrocer.modelclass;

import com.squareup.moshi.Json;

public class FirebaseDataModel {
    @Json(name = "f_id")
    private String fId;
    @Json(name = "status")
    private String status;

    public String getfId() {
        return fId;
    }

    public void setfId(String fId) {
        this.fId = fId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
