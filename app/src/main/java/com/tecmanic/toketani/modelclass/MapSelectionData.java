package com.tecmanic.toketani.modelclass;

import com.squareup.moshi.Json;

public class MapSelectionData {

    @Json(name = "map_id")
    private String mapId;
    @Json(name = "mapbox")
    private String mapbox;
    @Json(name = "google_map")
    private String googleMap;

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public String getMapbox() {
        return mapbox;
    }

    public void setMapbox(String mapbox) {
        this.mapbox = mapbox;
    }

    public String getGoogleMap() {
        return googleMap;
    }

    public void setGoogleMap(String googleMap) {
        this.googleMap = googleMap;
    }
}
