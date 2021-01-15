package com.tecmanic.gogrocer.modelclass;

import com.google.gson.annotations.SerializedName;

public class MyOrderDetailModel {

    @SerializedName("product_name")
    String productName;
    @SerializedName("varient_image")
    String varientImage;
    @SerializedName("qty")
    String qty;
    @SerializedName("description")
    String description;
    @SerializedName("unit")
    String unit;
    @SerializedName("quantity")
    String quantity;
    @SerializedName("order_cart_id")
    String orderCartId;
    @SerializedName("price")
    String price;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getVarientImage() {
        return varientImage;
    }

    public void setVarientImage(String varientImage) {
        this.varientImage = varientImage;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getOrderCartId() {
        return orderCartId;
    }

    public void setOrderCartId(String orderCartId) {
        this.orderCartId = orderCartId;
    }
}
