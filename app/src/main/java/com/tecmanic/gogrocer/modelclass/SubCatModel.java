package com.tecmanic.gogrocer.modelclass;

import org.json.JSONArray;

public class SubCatModel {
    String name;
    String id;
    String detail;
    String images;
    JSONArray subArray = new JSONArray();

    public JSONArray getSubArray() {
        return subArray;
    }

    public void setSubArray(JSONArray subArray) {
        this.subArray = subArray;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }
}
