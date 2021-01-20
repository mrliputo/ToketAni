package com.tecmanic.toketani.modelclass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MapboxDataModel implements Serializable {
    @SerializedName("map_id")
    private String mapId;
    @SerializedName("mapbox_api")
    private String mapboxApi;

    public String getId() {
        return mapId;
    }

    public void setId(String mapId) {
        this.mapId = mapId;
    }

    public String getMapApiKey() {
        return mapboxApi;
    }

    public void setMapApiKey(String mapApiKey) {
        this.mapboxApi = mapApiKey;
    }
}
