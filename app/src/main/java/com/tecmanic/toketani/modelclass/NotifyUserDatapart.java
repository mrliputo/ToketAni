package com.tecmanic.toketani.modelclass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NotifyUserDatapart implements Serializable {

    @SerializedName("noti_id")
    private String notiId;
    @SerializedName("user_id")
    private String userId;
    @SerializedName("sms")
    private String sms;
    @SerializedName("app")
    private String app;
    @SerializedName("email")
    private String email;


    public String getNotiId() {
        return notiId;
    }

    public void setNotiId(String notiId) {
        this.notiId = notiId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSms() {
        return sms;
    }

    public void setSms(String sms) {
        this.sms = sms;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
