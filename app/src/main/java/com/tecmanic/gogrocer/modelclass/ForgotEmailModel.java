package com.tecmanic.gogrocer.modelclass;

import com.squareup.moshi.Json;

public class ForgotEmailModel {

    @Json(name = "status")
    private String status;
    @Json(name = "message")
    private String message;

    public ForgotEmailModel() {
    }

    public ForgotEmailModel(String status, String message) {
        this.status = status;
        this.message = message;
    }

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
}
