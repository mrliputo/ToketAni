package com.tecmanic.gogrocer.modelclass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Rajesh Dabhi on 27/6/2017.
 */

public class SupportInfoModel implements Serializable {

    @SerializedName("id")
    String id;
    @SerializedName("pg_title")
    String pgTitle;
    @SerializedName("pg_slug")
    String pgSlug;
    @SerializedName("pg_descri")
    String pgDescri;
    @SerializedName("pg_status")
    String pgStatus;
    @SerializedName("pg_foot")
    String pgFoot;
    @SerializedName("crated_date")
    String cratedDate;

    public String getId() {
        return id;
    }

    public String getPgTitle() {
        return pgTitle;
    }

    public String getPgSlug() {
        return pgSlug;
    }

    public String getPgDescri() {
        return pgDescri;
    }

    public String getPgStatus() {
        return pgStatus;
    }

    public String getPgFoot() {
        return pgFoot;
    }

    public String getCratedDate() {
        return cratedDate;
    }

}
