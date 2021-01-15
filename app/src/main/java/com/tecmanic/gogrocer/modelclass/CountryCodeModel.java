package com.tecmanic.gogrocer.modelclass;

import com.squareup.moshi.Json;

public class CountryCodeModel {
    @Json(name = "status")
    private String status;
    @Json(name = "message")
    private String message;
    @Json(name = "data")
    private CountryCodeDataModel data;

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

    public CountryCodeDataModel getData() {
        return data;
    }

    public void setData(CountryCodeDataModel data) {
        this.data = data;
    }
}
