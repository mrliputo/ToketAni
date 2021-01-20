package com.tecmanic.toketani.modelclass;

import com.squareup.moshi.Json;

public class FirebaseStatusModel {
    @Json(name = "status")
    private String status;
    @Json(name = "message")
    private String message;
    @Json(name = "data")
    private FirebaseDataModel data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public FirebaseDataModel getData() {
        return data;
    }

    public void setData(FirebaseDataModel data) {
        this.data = data;
    }
}
