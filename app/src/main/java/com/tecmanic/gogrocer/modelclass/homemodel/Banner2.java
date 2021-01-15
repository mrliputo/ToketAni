package com.tecmanic.gogrocer.modelclass.homemodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Banner2 {

    @SerializedName("sec_banner_id")
    @Expose
    private String sec_banner_id;
    @SerializedName("banner_name")
    @Expose
    private String banner_name;
    @SerializedName("banner_image")
    @Expose
    private String banner_image;

    public String getBanner_id() {
        return sec_banner_id;
    }

    public void setBanner_id(String sec_banner_id) {
        this.sec_banner_id = sec_banner_id;
    }

    public String getBanner_name() {
        return banner_name;
    }

    public void setBanner_name(String banner_name) {
        this.banner_name = banner_name;
    }

    public String getBanner_image() {
        return banner_image;
    }

    public void setBanner_image(String banner_image) {
        this.banner_image = banner_image;
    }
}
