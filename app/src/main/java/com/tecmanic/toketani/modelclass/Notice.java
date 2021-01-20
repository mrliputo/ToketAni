package com.tecmanic.toketani.modelclass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Notice implements Serializable {

    @SerializedName("content_id")
    public String contentId;
    @SerializedName("content_name")
    public String contentName;
    @SerializedName("content_date")
    public String contentDate;

    public Notice() {
    }


    public String getContentId() {
        return contentId;
    }

    public void setContentId(String contentId) {
        this.contentId = contentId;
    }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getContentDate() {
        return contentDate;
    }

    public void setContentDate(String contentDate) {
        this.contentDate = contentDate;
    }
}
