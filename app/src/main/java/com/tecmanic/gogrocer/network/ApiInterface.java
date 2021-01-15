package com.tecmanic.gogrocer.network;

import com.tecmanic.gogrocer.modelclass.CountryCodeModel;
import com.tecmanic.gogrocer.modelclass.FirebaseStatusModel;
import com.tecmanic.gogrocer.modelclass.ForgotEmailModel;
import com.tecmanic.gogrocer.modelclass.MapSelectionModel;
import com.tecmanic.gogrocer.modelclass.NotificationBannerStatus;
import com.tecmanic.gogrocer.modelclass.NotifyModelUser;
import com.tecmanic.gogrocer.modelclass.PaymentVia;
import com.tecmanic.gogrocer.modelclass.ReOrderModel;
import com.tecmanic.gogrocer.modelclass.VerifyOtp;
import com.tecmanic.gogrocer.modelclass.homemodel.MainHomeModel;
import com.tecmanic.gogrocer.modelclass.reordermodel.NewReorderModel;

import org.json.JSONArray;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @POST("forgot_password")
    @FormUrlEncoded
    Call<ForgotEmailModel> getEmailOtp(@Field("user_email") String userEmail,@Field("user_phone") String userPhone);

    @POST("verify_via_firebase")
    @FormUrlEncoded
    Call<VerifyOtp> getVerifyOtpStatus(@Field("status") String status , @Field("user_phone") String userPhone);

    @GET("checkotponoff")
    Call<ForgotEmailModel> getOtpOnOffStatus();

    @GET("pymnt_via")
    Call<PaymentVia> getPaymentVia();

    @POST("notifyby")
    @FormUrlEncoded
    Call<NotifyModelUser> getNotifyUser(@Field("user_id") String userId);

    @GET("mapby")
    Call<MapSelectionModel> getMapSelectionStatus();

    @GET("firebase")
    Call<FirebaseStatusModel> getFirebaseOtpStatus();

    @GET("countrycode")
    Call<CountryCodeModel> getCountryCode();

    @GET("app_notice")
    Call<NotificationBannerStatus> getNotificationBannerStatus();

    @POST("firebase_otp_ver")
    @FormUrlEncoded
    Call<VerifyOtp> getOtpVerifiyStatus(@Field("status") String status , @Field("user_phone") String userPhone);

    @POST("checknum")
    @FormUrlEncoded
    Call<VerifyOtp> checkNumIsRegisterOrNot(@Field("user_phone") String userPhone);

    @POST("homepage")
    @FormUrlEncoded
    Call<MainHomeModel> getMainHomeModel(@Field("lat") String lat , @Field("lng") String lng);

    @POST("checkstock")
    @FormUrlEncoded
    Call<NewReorderModel> getReorderModel(@Field("cart_id") String cart_id);

}
