package com.tecmanic.toketani.modelclass;

import com.squareup.moshi.Json;

public class CountryCodeDataModel {
    @Json(name = "code_id")
    private String codeId;

    @Json(name = "country_code")
    private String countryCode;

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
