package com.tecmanic.gogrocer.modelclass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SubProductList implements Serializable {

    @SerializedName("varient_id")
    String varientId;
    @SerializedName("product_id")
    String productId;
    @SerializedName("quantity")
    String quantity;
    @SerializedName("unit")
    String unit;
    @SerializedName("mrp")
    String mrp;
    @SerializedName("price")
    String price;
    @SerializedName("description")
    String description;
    @SerializedName("varient_image")
    String varientImage;
    @SerializedName("stock")
    String stock;

    public SubProductList(String varientId, String productId, String quantity, String unit, String mrp, String price, String description, String varientImage, String stock) {
        this.varientId = varientId;
        this.productId = productId;
        this.quantity = quantity;
        this.unit = unit;
        this.mrp = mrp;
        this.price = price;
        this.description = description;
        this.varientImage = varientImage;
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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVarientImage() {
        return varientImage;
    }

    public void setVarientImage(String varientImage) {
        this.varientImage = varientImage;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }
}
