package com.tecmanic.gogrocer.modelclass;

import java.io.Serializable;

public class Popular_model implements Serializable {

    private String cat_id;
    private String title;
    private String description;
    private String image;
    private String Count;

    public Popular_model(String cat_id, String title, String image) {
        this.cat_id=cat_id;
        this.title=title;
        this.image=image;
    }
    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
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
        return Count;
    }

    public void setCount(String count) {
        Count = count;
    }
}

