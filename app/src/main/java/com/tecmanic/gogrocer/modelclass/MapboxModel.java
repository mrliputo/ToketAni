package com.tecmanic.gogrocer.modelclass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MapboxModel implements Serializable {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private MapboxDataModel data;

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

    public MapboxDataModel getData() {
        return data;
    }

    public void setData(MapboxDataModel data) {
        this.data = data;
    }
}
