package com.tecmanic.toketani.modelclass;

import com.squareup.moshi.Json;

public class MapSelectionModel {

    @Json(name = "status")
    private String status;
    @Json(name = "message")
    private String message;
    @Json(name = "data")
    private MapSelectionData data;

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

    public MapSelectionData getData() {
        return data;
    }

    public void setData(MapSelectionData data) {
        this.data = data;
    }
}
