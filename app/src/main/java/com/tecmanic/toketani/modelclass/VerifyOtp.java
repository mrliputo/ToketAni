package com.tecmanic.toketani.modelclass;

import com.squareup.moshi.Json;

public class VerifyOtp {
    @Json(name = "status")
    private String status;
    @Json(name = "message")
    private String message;

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

    @Override
    public String toString() {
        return "VerifyOtp{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
