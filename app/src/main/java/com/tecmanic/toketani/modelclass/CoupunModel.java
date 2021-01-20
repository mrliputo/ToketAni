package com.tecmanic.toketani.modelclass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CoupunModel implements Serializable {


    @SerializedName("coupon_id")
    private String couponId;
    @SerializedName("coupon_name")
    private String couponName;
    @SerializedName("coupon_code")
    private String couponCode;
    @SerializedName("coupon_description")
    private String couponDescription;
    @SerializedName("start_date")
    private String startDate;
    @SerializedName("end_date")
    private String endDate;
    @SerializedName("cart_value")
    private String cartValue;
    @SerializedName("amount")
    private String amount;
    @SerializedName("type")
    private String type;
    @SerializedName("uses_restriction")
    private String usesRestriction;

    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCouponDescription() {
        return couponDescription;
    }

    public void setCouponDescription(String couponDescription) {
        this.couponDescription = couponDescription;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCartValue() {
        return cartValue;
    }

    public void setCartValue(String cartValue) {
        this.cartValue = cartValue;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsesRestriction() {
        return usesRestriction;
    }

    public void setUsesRestriction(String usesRestriction) {
        this.usesRestriction = usesRestriction;
    }
}
