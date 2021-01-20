package com.tecmanic.toketani.modelclass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NotifyModelUser implements Serializable {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("data")
    private NotifyUserDatapart data;

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

    public NotifyUserDatapart getData() {
        return data;
    }

    public void setData(NotifyUserDatapart data) {
        this.data = data;
    }
}
