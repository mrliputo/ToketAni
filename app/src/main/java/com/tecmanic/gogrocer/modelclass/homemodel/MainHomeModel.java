package com.tecmanic.gogrocer.modelclass.homemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.moshi.Json;
import com.tecmanic.gogrocer.modelclass.NewCartModel;

import java.util.ArrayList;

public class MainHomeModel {

    @SerializedName("status")
    @Expose
    @Json(name = "status")
    private String status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("banner1")
    @Expose
    private ArrayList<Banner1> banner1;
    @SerializedName("banner2")
    @Expose
    private ArrayList<Banner2> banner2;
    @SerializedName("top_selling")
    @Expose
    private ArrayList<NewCartModel> top_selling;
    @SerializedName("recentselling")
    @Expose
    private ArrayList<NewCartModel> recentselling;
    @SerializedName("whats_new")
    @Expose
    private ArrayList<NewCartModel> whats_new;
    @SerializedName("deal_products")
    @Expose
    private ArrayList<NewCartModel> deal_products;
    @SerializedName("top_category")
    @Expose
    private ArrayList<NewTopCategory> top_category;

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

    public ArrayList<Banner1> getBanner1() {
        return banner1;
    }

    public void setBanner1(ArrayList<Banner1> banner1) {
        this.banner1 = banner1;
    }

    public ArrayList<Banner2> getBanner2() {
        return banner2;
    }

    public void setBanner2(ArrayList<Banner2> banner2) {
        this.banner2 = banner2;
    }

    public ArrayList<NewCartModel> getTop_selling() {
        return top_selling;
    }

    public void setTop_selling(ArrayList<NewCartModel> top_selling) {
        this.top_selling = top_selling;
    }

    public ArrayList<NewCartModel> getRecentselling() {
        return recentselling;
    }

    public void setRecentselling(ArrayList<NewCartModel> recentselling) {
        this.recentselling = recentselling;
    }

    public ArrayList<NewCartModel> getWhats_new() {
        return whats_new;
    }

    public void setWhats_new(ArrayList<NewCartModel> whats_new) {
        this.whats_new = whats_new;
    }

    public ArrayList<NewCartModel> getDeal_products() {
        return deal_products;
    }

    public void setDeal_products(ArrayList<NewCartModel> deal_products) {
        this.deal_products = deal_products;
    }

    public ArrayList<NewTopCategory> getTop_category() {
        return top_category;
    }

    public void setTop_category(ArrayList<NewTopCategory> top_category) {
        this.top_category = top_category;
    }

    @Override
    public String toString() {
        return "MainHomeModel{" +
                "status='" + status + '\'' +
                ", message='" + message + '\'' +
                ", banner1=" + banner1 +
                ", banner2=" + banner2 +
                ", top_selling=" + top_selling +
                ", recentselling=" + recentselling +
                ", whats_new=" + whats_new +
                ", deal_products=" + deal_products +
                ", top_category=" + top_category +
                '}';
    }
}
