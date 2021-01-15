package com.tecmanic.gogrocer.modelclass.reordermodel;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NewReorderModel {

    @SerializedName("status")
    private String status;
    @SerializedName("message")
    private String message;
    @SerializedName("total_mrp")
    private String total_mrp;
    @SerializedName("total_price")
    private String total_price;
    @SerializedName("data")
    private ArrayList<NewReorderSubModel> data;

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

    public String getTotal_mrp() {
        return total_mrp;
    }

    public void setTotal_mrp(String total_mrp) {
        this.total_mrp = total_mrp;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public ArrayList<NewReorderSubModel> getData() {
        return data;
    }

    public void setData(ArrayList<NewReorderSubModel> data) {
        this.data = data;
    }
}
