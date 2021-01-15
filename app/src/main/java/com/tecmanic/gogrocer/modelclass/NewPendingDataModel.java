package com.tecmanic.gogrocer.modelclass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class NewPendingDataModel implements Serializable {
    @SerializedName("store_order_id")
    String storeOrderId;
    @SerializedName("store_approval")
    String storeApproval;
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
    @SerializedName("order_date")
    String orderDate;
    @SerializedName("price")
    String price;
    @SerializedName("total_mrp")
    String totalMrp;
    @SerializedName("varient_id")
    String varientId;
    private String p_id;
    private String stock;

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
        if (description == null) {
            return "";
        }
        return description;
    }

    public void setDescription(String description) {
        if (description == null) {
            this.description = "";
        } else {
            this.description = description;
        }
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStoreOrderId() {
        return storeOrderId;
    }

    public void setStoreOrderId(String storeOrderId) {
        this.storeOrderId = storeOrderId;
    }

    public String getStoreApproval() {
        return storeApproval;
    }

    public void setStoreApproval(String storeApproval) {
        this.storeApproval = storeApproval;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getTotalMrp() {
        return totalMrp;
    }

    public void setTotalMrp(String totalMrp) {
        this.totalMrp = totalMrp;
    }

    public String getVarientId() {
        return varientId;
    }

    public void setVarientId(String varientId) {
        this.varientId = varientId;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
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
        NewPendingDataModel that = (NewPendingDataModel) o;
        return Objects.equals(varientId, that.varientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(varientId);
    }

    @Override
    public String toString() {
        return "NewPendingDataModel{" +
                "storeOrderId='" + storeOrderId + '\'' +
                ", storeApproval='" + storeApproval + '\'' +
                ", productName='" + productName + '\'' +
                ", varientImage='" + varientImage + '\'' +
                ", qty='" + qty + '\'' +
                ", description='" + description + '\'' +
                ", unit='" + unit + '\'' +
                ", quantity='" + quantity + '\'' +
                ", orderCartId='" + orderCartId + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", price='" + price + '\'' +
                ", totalMrp='" + totalMrp + '\'' +
                ", varientId='" + varientId + '\'' +
                '}';
    }
}
