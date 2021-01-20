package com.tecmanic.toketani.modelclass;

import com.squareup.moshi.Json;

public class PaymentVia {

    @Json(name = "status")
    private String status;
    @Json(name = "message")
    private String message;
    @Json(name = "data")
    private PaymentViaList data;

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

    public PaymentViaList getData() {
        return data;
    }

    public void setData(PaymentViaList data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "PaymentVia{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
