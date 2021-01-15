package com.tecmanic.gogrocer.util;

import android.content.Context;
import android.content.Intent;

import com.tecmanic.gogrocer.activity.MainActivity;

import java.util.HashMap;
import java.util.Map;

import static com.tecmanic.gogrocer.config.BaseURL.APP_OTP_STATUS;
import static com.tecmanic.gogrocer.config.BaseURL.CART_ID_FINAL;
import static com.tecmanic.gogrocer.config.BaseURL.COUNTRY_CODE;
import static com.tecmanic.gogrocer.config.BaseURL.IS_LOGIN;
import static com.tecmanic.gogrocer.config.BaseURL.KEY_EMAIL;
import static com.tecmanic.gogrocer.config.BaseURL.KEY_HOUSE;
import static com.tecmanic.gogrocer.config.BaseURL.KEY_ID;
import static com.tecmanic.gogrocer.config.BaseURL.KEY_IMAGE;
import static com.tecmanic.gogrocer.config.BaseURL.KEY_MOBILE;
import static com.tecmanic.gogrocer.config.BaseURL.KEY_NAME;
import static com.tecmanic.gogrocer.config.BaseURL.KEY_PASSWORD;
import static com.tecmanic.gogrocer.config.BaseURL.KEY_PAYMENT_METHOD;
import static com.tecmanic.gogrocer.config.BaseURL.KEY_PINCODE;
import static com.tecmanic.gogrocer.config.BaseURL.KEY_REWARDS_POINTS;
import static com.tecmanic.gogrocer.config.BaseURL.KEY_SOCITY_ID;
import static com.tecmanic.gogrocer.config.BaseURL.KEY_SOCITY_NAME;
import static com.tecmanic.gogrocer.config.BaseURL.KEY_WALLET_AMMOUNT;
import static com.tecmanic.gogrocer.config.BaseURL.MAP_SELECTION;
import static com.tecmanic.gogrocer.config.BaseURL.PAYMENT_PAYPAL;
import static com.tecmanic.gogrocer.config.BaseURL.PAYMENT_PAYSTACK;
import static com.tecmanic.gogrocer.config.BaseURL.PAYMENT_RAZORPZY;
import static com.tecmanic.gogrocer.config.BaseURL.TOTAL_AMOUNT;
import static com.tecmanic.gogrocer.config.BaseURL.USER_CITY;
import static com.tecmanic.gogrocer.config.BaseURL.USER_CURRENCY;
import static com.tecmanic.gogrocer.config.BaseURL.USER_CURRENCY_CNTRY;
import static com.tecmanic.gogrocer.config.BaseURL.USER_EMAIL_SERVICE;
import static com.tecmanic.gogrocer.config.BaseURL.USER_INAPP_SERVICE;
import static com.tecmanic.gogrocer.config.BaseURL.USER_LANG;
import static com.tecmanic.gogrocer.config.BaseURL.USER_LAT;
import static com.tecmanic.gogrocer.config.BaseURL.USER_OTP;
import static com.tecmanic.gogrocer.config.BaseURL.USER_SKIP;
import static com.tecmanic.gogrocer.config.BaseURL.USER_SMS_SERVICE;
import static com.tecmanic.gogrocer.config.BaseURL.USER_STATUS;
import static com.tecmanic.gogrocer.config.BaseURL.USER_STOREID;

/**
 * Created by Rajesh Dabhi on 28/6/2017.
 */

public class SessionManagement {
    SharedPreferenceUtil pref;
    Context context;

    public SessionManagement(Context context) {
        this.context = context;
        pref = new SharedPreferenceUtil(context);
    }

    public void createLoginSession(String id, String email, String name, String mobile, String password) {
        pref.setBoolean(IS_LOGIN, true);
        pref.setString(KEY_ID, id);
        pref.setString(KEY_EMAIL, email);
        pref.setString(KEY_NAME, name);
        pref.setString(KEY_MOBILE, mobile);
        pref.setString(KEY_PASSWORD, password);
        pref.setBoolean(USER_SKIP, false);
        pref.save();
    }

    public void createLoginSession(String id, String email, String name
            , String mobile, String password, boolean isLogin,boolean skip) {


        pref.setBoolean(IS_LOGIN, isLogin);
        pref.setString(KEY_ID, id);
        pref.setString(KEY_EMAIL, email);
        pref.setString(KEY_NAME, name);
        pref.setString(KEY_MOBILE, mobile);
        pref.setString(KEY_PASSWORD, password);
        pref.setBoolean(USER_SKIP, skip);
        pref.save();
    }

    public void createLoginSession(String id, String email, String name, String mobile, String password, boolean skip) {
        pref.setBoolean(IS_LOGIN, false);
        pref.setString(KEY_ID, id);
        pref.setString(KEY_EMAIL, email);
        pref.setString(KEY_NAME, name);
        pref.setString(KEY_MOBILE, mobile);
        pref.setString(KEY_PASSWORD, password);
        pref.setBoolean(USER_SKIP, skip);
        pref.save();
    }

    public void setLocationPref(String lat, String lang) {
        pref.setString(USER_LAT, lat);
        pref.setString(USER_LANG, lang);
    }

    public String getLatPref() {
        return pref.getString(USER_LAT, "");
    }

    public String getLangPref() {
        return pref.getString(USER_LANG, "");
    }

    public String getLocationCity() {
        return pref.getString(USER_CITY, "");
    }

    public void setLocationCity(String city) {
        pref.setString(USER_CITY, city);
    }


    public Map<String, String> getUserDetails() {
        Map<String, String> user = new HashMap<>();
        user.put(KEY_ID, pref.getString(KEY_ID, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_MOBILE, pref.getString(KEY_MOBILE, null));
        user.put(KEY_IMAGE, pref.getString(KEY_IMAGE, null));
        user.put(KEY_WALLET_AMMOUNT, pref.getString(KEY_WALLET_AMMOUNT, null));
        user.put(KEY_REWARDS_POINTS, pref.getString(KEY_REWARDS_POINTS, null));
        user.put(KEY_PAYMENT_METHOD, pref.getString(KEY_PAYMENT_METHOD, ""));
        user.put(TOTAL_AMOUNT, pref.getString(TOTAL_AMOUNT, null));
        user.put(KEY_PINCODE, pref.getString(KEY_PINCODE, null));
        user.put(KEY_SOCITY_ID, pref.getString(KEY_SOCITY_ID, null));
        user.put(KEY_SOCITY_NAME, pref.getString(KEY_SOCITY_NAME, null));
        user.put(KEY_HOUSE, pref.getString(KEY_HOUSE, null));
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));
        return user;
    }

    public void logoutSession() {
        pref.clearAll();
        Intent logout = new Intent(context, MainActivity.class);
        logout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        logout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(logout);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void setLogin(boolean value) {
        pref.setBoolean(IS_LOGIN, value);
    }

    public boolean isSkip() {
        return pref.getBoolean(USER_SKIP, false);
    }


    public String userBlockStatus() {
        return pref.getString(USER_STATUS, "2");
    }

    public void setUserBlockStatus(String value) {
        pref.setString(USER_STATUS, value);
    }

    public void setEmailServer(String value) {
        pref.setString(USER_EMAIL_SERVICE, value);
    }

    public void setUserSMSService(String value) {
        pref.setString(USER_SMS_SERVICE, value);
    }

    public void setUserInAppService(String value) {
        pref.setString(USER_INAPP_SERVICE, value);
    }

    public String getEmailService() {
        return pref.getString(USER_EMAIL_SERVICE, "0");
    }

    public String getSMSService() {
        return pref.getString(USER_SMS_SERVICE, "0");
    }

    public String getINAPPService() {
        return pref.getString(USER_INAPP_SERVICE, "0");
    }

    public String userOtp() {
        return pref.getString(USER_OTP, "0");
    }

    public void setOtp(String value) {
        pref.setString(USER_OTP, value);
    }

    public String getMapSelection() {
        return pref.getString(MAP_SELECTION, "0");
    }

    public void setMapSelection(String value) {
        pref.setString(MAP_SELECTION, value);
    }

    public String userId() {
        return pref.getString(KEY_ID, "");
    }

    public void setCartID(String value) {
        pref.setString(CART_ID_FINAL, value);
    }

    public String getCartId() {
        return pref.getString(CART_ID_FINAL, "");
    }

    public String getCurrency() {
        return pref.getString(USER_CURRENCY, "");
    }

    public String getCurrencyCountry() {
        return pref.getString(USER_CURRENCY_CNTRY, "");
    }

    public void setCurrency(String name, String currency) {
        pref.setString(USER_CURRENCY, currency);
        pref.setString(USER_CURRENCY_CNTRY, name);
    }

    public String getOtpSatus() {
        return pref.getString(APP_OTP_STATUS, "1");
    }

    public void setOtpStatus(String value) {
        pref.setString(APP_OTP_STATUS, value);
    }

    public String getCountryCode() {
        return pref.getString(COUNTRY_CODE, "");
    }

    public void setCountryCode(String value) {
        pref.setString(COUNTRY_CODE, value);
    }

    public String getStoreId() {
        return pref.getString(USER_STOREID, "");
    }

    public void setStoreId(String storeId) {
        pref.setString(USER_STOREID, storeId);
    }

    public void setPaymentMethodOpt(String razorpay, String paypal, String paystack) {
        pref.setString(PAYMENT_RAZORPZY, razorpay);
        pref.setString(PAYMENT_PAYPAL, paypal);
        pref.setString(PAYMENT_PAYSTACK, paystack);
    }

    public String getPayPal() {
        return pref.getString(PAYMENT_PAYPAL, "0");
    }

    public String getRazorPay() {
        return pref.getString(PAYMENT_RAZORPZY, "0");
    }

    public String getPayStack() {
        return pref.getString(PAYMENT_PAYSTACK, "0");
    }
}
