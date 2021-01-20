package com.tecmanic.toketani.modelclass;

import com.squareup.moshi.Json;

public class PaymentViaList {

    @Json(name = "p_id")
    private String pId;
    @Json(name = "paypal")
    private String paypal;
    @Json(name = "razorpay")
    private String razorpay;
    @Json(name = "paystack")
    private String paystack;

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getPaypal() {
        return paypal;
    }

    public void setPaypal(String paypal) {
        this.paypal = paypal;
    }

    public String getRazorpay() {
        return razorpay;
    }

    public void setRazorpay(String razorpay) {
        this.razorpay = razorpay;
    }

    public String getPaystack() {
        return paystack;
    }

    public void setPaystack(String paystack) {
        this.paystack = paystack;
    }

    @Override
    public String toString() {
        return "PaymentViaList{" +
                "p_id='" + pId + '\'' +
                ", paypal='" + paypal + '\'' +
                ", razorpay='" + razorpay + '\'' +
                ", paystack='" + paystack + '\'' +
                '}';
    }
}
