package com.tecmanic.toketani.modelclass.reordermodel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Objects;

public class NewReorderSubModel implements Serializable {

    @SerializedName("p_id")
    private String p_id;
    @SerializedName("varient_id")
    private String varient_id;
    @SerializedName("stock")
    private String stock;
    @SerializedName("store_id")
    private String store_id;
    @SerializedName("mrp")
    private String mrp;
    @SerializedName("price")
    private String price;
    @SerializedName("product_id")
    private String product_id;
    @SerializedName("quantity")
    private String quantity;
    @SerializedName("unit")
    private String unit;
    @SerializedName("base_mrp")
    private String base_mrp;
    @SerializedName("base_price")
    private String base_price;
    @SerializedName("description")
    private String description;
    @SerializedName("varient_image")
    private String varient_image;
    @SerializedName("cat_id")
    private String cat_id;
    @SerializedName("product_name")
    private String product_name;
    @SerializedName("product_image")
    private String product_image;
    @SerializedName("hide")
    private String hide;
    @SerializedName("seq")
    private String seq;


    public NewReorderSubModel(String p_id, String varient_id, String stock, String store_id, String mrp, String price, String product_id, String quantity, String unit, String base_mrp, String base_price, String description, String varient_image, String cat_id, String product_name, String product_image, String hide, String seq) {
        this.p_id = p_id;
        this.varient_id = varient_id;
        this.stock = stock;
        this.store_id = store_id;
        this.mrp = mrp;
        this.price = price;
        this.product_id = product_id;
        this.quantity = quantity;
        this.unit = unit;
        this.base_mrp = base_mrp;
        this.base_price = base_price;
        this.description = description;
        this.varient_image = varient_image;
        this.cat_id = cat_id;
        this.product_name = product_name;
        this.product_image = product_image;
        this.hide = hide;
        this.seq = seq;
    }

    public String getP_id() {
        return p_id;
    }

    public void setP_id(String p_id) {
        this.p_id = p_id;
    }

    public String getVarient_id() {
        return varient_id;
    }

    public void setVarient_id(String varient_id) {
        this.varient_id = varient_id;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
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

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
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

    public String getBase_mrp() {
        return base_mrp;
    }

    public void setBase_mrp(String base_mrp) {
        this.base_mrp = base_mrp;
    }

    public String getBase_price() {
        return base_price;
    }

    public void setBase_price(String base_price) {
        this.base_price = base_price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVarient_image() {
        return varient_image;
    }

    public void setVarient_image(String varient_image) {
        this.varient_image = varient_image;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getHide() {
        return hide;
    }

    public void setHide(String hide) {
        this.hide = hide;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewReorderSubModel that = (NewReorderSubModel) o;
        return Objects.equals(varient_id, that.varient_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(varient_id);
    }
}
