package com.tecmanic.toketani.modelclass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SearchModel implements Serializable {
    @SerializedName("id")
    String id;
    @SerializedName("p_name")
    String pNAme;

    public SearchModel(String productId, String productName) {
        this.id = productId;
        this.pNAme = productName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getpNAme() {
        return pNAme;
    }

    public void setpNAme(String pNAme) {
        this.pNAme = pNAme;
    }
}
