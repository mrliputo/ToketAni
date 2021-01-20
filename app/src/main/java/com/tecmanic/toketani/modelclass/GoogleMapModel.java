package com.tecmanic.toketani.modelclass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GoogleMapModel implements Serializable {
    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private GoogleMapDataModel data;

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

    public GoogleMapDataModel getData() {
        return data;
    }

    public void setData(GoogleMapDataModel data) {
        this.data = data;
    }
}
