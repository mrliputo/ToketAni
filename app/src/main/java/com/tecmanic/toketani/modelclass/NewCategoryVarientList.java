package com.tecmanic.toketani.modelclass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class NewCategoryVarientList implements Serializable {

    @SerializedName("varient_id")
    private String varientId;
    @SerializedName("description")
    private String description;
    @SerializedName("price")
    private String price;
    @SerializedName("mrp")
    private String mrp;
    @SerializedName("varient_image")
    private String varientImage;
    @SerializedName("unit")
    private String unit;
    @SerializedName("quantity")
    private String quantity;
    @SerializedName("deal_price")
    private String dealPrice;
    @SerializedName("valid_from")
    private String validFrom;
    @SerializedName("valid_to")
    private String validTo;
    @SerializedName("store_id")
    private String storeId;
    @SerializedName("stock")
    private String stock;

    public NewCategoryVarientList(String varientId) {
        this.varientId = varientId;
    }

    public String getVarientId() {
        return varientId;
    }

    public void setVarientId(String varientId) {
        this.varientId = varientId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getVarientImage() {
        return varientImage;
    }

    public void setVarientImage(String varientImage) {
        this.varientImage = varientImage;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(String dealPrice) {
        this.dealPrice = dealPrice;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewCategoryVarientList that = (NewCategoryVarientList) o;
        return Objects.equals(varientId, that.varientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(varientId);
    }
}
