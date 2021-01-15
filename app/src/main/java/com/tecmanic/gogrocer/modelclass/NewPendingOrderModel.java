package com.tecmanic.gogrocer.modelclass;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class NewPendingOrderModel implements Serializable {

    @SerializedName("order_status")
    private String orderStatus;
    @SerializedName("delivery_date")
    private String deliveryDate;
    @SerializedName("time_slot")
    private String timeSlot;
    @SerializedName("payment_method")
    private String paymentMethod;
    @SerializedName("payment_status")
    private String paymentStatus;
    @SerializedName("paid_by_wallet")
    private String paidByWallet;
    @SerializedName("cart_id")
    private String cartId;
    @SerializedName("price")
    private String price;
    @SerializedName("del_charge")
    private String delCharge;
    @SerializedName("remaining_amount")
    private String remainingAmount;
    @SerializedName("coupon_discount")
    private String couponDiscount;
    @SerializedName("dboy_name")
    private String dboyName;
    @SerializedName("dboy_phone")
    private String dboyPhone;
    @SerializedName("data")
    private List<NewPendingDataModel> data;

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(String timeSlot) {
        this.timeSlot = timeSlot;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        if (paymentStatus == null) {
            this.paymentStatus = "";
        } else {
            this.paymentStatus = paymentStatus;
        }

    }

    public String getPaidByWallet() {
        return paidByWallet;
    }

    public void setPaidByWallet(String paidByWallet) {
        this.paidByWallet = paidByWallet;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDelCharge() {
        return delCharge;
    }

    public void setDelCharge(String delCharge) {
        this.delCharge = delCharge;
    }

    public List<NewPendingDataModel> getData() {
        return data;
    }

    public void setData(List<NewPendingDataModel> data) {
        this.data = data;
    }

    public String getRemainingAmount() {
        return remainingAmount;
    }

    public void setRemainingAmount(String remainingAmount) {
        this.remainingAmount = remainingAmount;
    }

    public String getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(String couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public String getDboyName() {
        if (dboyName == null) {
            return "";
        }
        return dboyName;
    }

    public void setDboyName(String dboyName) {
        this.dboyName = dboyName;
    }

    public String getDboyPhone() {
        if (dboyPhone == null) {
            return "";
        }
        return dboyPhone;
    }

    public void setDboyPhone(String dboyPhone) {
        this.dboyPhone = dboyPhone;
    }

    @Override
    public String toString() {
        return "NewPendingOrderModel{" +
                "orderStatus='" + orderStatus + '\'' +
                ", deliveryDate='" + deliveryDate + '\'' +
                ", timeSlot='" + timeSlot + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", paymentStatus='" + paymentStatus + '\'' +
                ", paidByWallet='" + paidByWallet + '\'' +
                ", cartId='" + cartId + '\'' +
                ", price='" + price + '\'' +
                ", delCharge='" + delCharge + '\'' +
                ", remainingAmount='" + remainingAmount + '\'' +
                ", couponDiscount='" + couponDiscount + '\'' +
                ", dboyName='" + dboyName + '\'' +
                ", dboyPhone='" + dboyPhone + '\'' +
                ", data=" + data +
                '}';
    }
}
