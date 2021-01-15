package com.tecmanic.gogrocer.modelclass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class NewCategoryDataModel implements Serializable {

    @SerializedName("product_id")
    private String productId;
    @SerializedName("cat_id")
    private String catId;
    @SerializedName("product_name")
    private String productName;
    @SerializedName("product_image")
    private String productImage;
    @SerializedName("varient_image")
    private String varientImage;
    @SerializedName("description")
    private String description;
    @SerializedName("price")
    private String price;
    @SerializedName("mrp")
    private String mrp;
    @SerializedName("unit")
    private String unit;
    @SerializedName("quantity")
    private String quantity;
    @SerializedName("store_id")
    private String storeId;
    @SerializedName("stock")
    private String stock;
    @SerializedName("varient_id")
    private String varientId;
    @SerializedName("p_id")
    private String pId;
    @SerializedName("varients")
    private List<NewCategoryVarientList> varients;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
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

    public List<NewCategoryVarientList> getVarients() {
        return varients;
    }

    public void setVarients(List<NewCategoryVarientList> varients) {
        this.varients = varients;
    }

    public String getVarientImage() {
        return varientImage;
    }

    public void setVarientImage(String varientImage) {
        this.varientImage = varientImage;
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

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }
}
