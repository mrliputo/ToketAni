package com.tecmanic.gogrocer.modelclass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Rajesh Dabhi on 24/6/2017.
 */

public class CategoryModels implements Serializable {

    @SerializedName("cat_id")
    private String catId;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("image")
    private String image;
    @SerializedName("count")
    private String count;

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
