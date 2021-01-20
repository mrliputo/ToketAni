package com.tecmanic.toketani.modelclass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NewCartModel implements Serializable {

    @SerializedName("store_id")
    private String storeId;
    @SerializedName("stock")
    private String stock;
    @SerializedName("varient_id")
    private String varientId;
    @SerializedName("product_id")
    private String productId;
    @SerializedName("product_name")
    private String productName;
    @SerializedName("product_image")
    private String productImage;
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
    @SerializedName("count")
    private String count;
    @SerializedName("valid_to")
    private String validTo;
    @SerializedName("valid_from")
    private String validFrom;
    @SerializedName("timediff")
    private String timediff;
    @SerializedName("hoursmin")
    private String hoursmin;

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

    public String getVarientId() {
        return varientId;
    }

    public void setVarientId(String varientId) {
        this.varientId = varientId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        if (price==null || price.equalsIgnoreCase("null")){
            return price = "0";
        }
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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getValidTo() {
        return validTo;
    }

    public void setValidTo(String validTo) {
        this.validTo = validTo;
    }

    public String getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(String validFrom) {
        this.validFrom = validFrom;
    }

    public String getTimediff() {
        return timediff;
    }

    public void setTimediff(String timediff) {
        this.timediff = timediff;
    }

    public String getHoursmin() {
        return hoursmin;
    }

    public void setHoursmin(String hoursmin) {
        this.hoursmin = hoursmin;
    }
}
