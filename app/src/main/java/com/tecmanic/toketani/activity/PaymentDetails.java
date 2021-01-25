package com.tecmanic.toketani.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.razorpay.Checkout;
import com.razorpay.Order;
import com.razorpay.PaymentResultListener;
import com.razorpay.RazorpayClient;
import com.tecmanic.toketani.R;
import com.tecmanic.toketani.config.BaseURL;
import com.tecmanic.toketani.config.SharedPref;
import com.tecmanic.toketani.modelclass.PaymentVia;
import com.tecmanic.toketani.network.ApiInterface;
import com.tecmanic.toketani.util.AppController;
import com.tecmanic.toketani.util.CustomVolleyJsonRequest;
import com.tecmanic.toketani.util.DatabaseHandler;
import com.tecmanic.toketani.util.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class PaymentDetails extends AppCompatActivity implements PaymentResultListener {

    public static final int PAYPAL_REQUEST_CODE = 123;
    LinearLayout llwallet;
    LinearLayout llpromocode;
    LinearLayout llcards;
    LinearLayout llcod;
    String lat;
    String lng;
    String getwallet;
    LinearLayout promoCodeLayout;
    RelativeLayout applyCouponCode;
    SharedPreferences sharedPreferences12;
    SharedPreferences.Editor editor12;
    String code;
    String cartId2;
    TextView twallet;
    TextView tcod;
    TextView tcards;
    TextView tpromocode;
    LinearLayout backcart;
    int status = 0;
    String paymentMethod;
    Button confirm;
    String walletAmount = "00";
    TextView resetText;
    String walletStatus = "no";
    TextView myWalletAmmount;
    TextView usedWalletAmmount;
    TextView usedCouponAmmount;
    TextView orderAmmount;
    CheckBox rbStore;
    CheckBox rbCod;
    CheckBox rbCard;
    CheckBox checkBoxWallet;
    CheckBox checkBoxCoupon;
    CheckBox useRazorpay;
    CheckBox usePaypal;
    EditText etCoupon;
    String totalAmount;
    TextView linea;
    TextView lineb;
    RadioGroup radioGroup;
    private TextView coupuntxt;
    private DatabaseHandler dbCart;
    private SessionManagement sessionManagement;
    private String getuserId = "";
    private String remaingprice = "";
    private String payableAmt = "";
    private double walletbalnce = 0;
    private String couponAmount = "";
    private ImageView dropdown;
    private TextView couponApplyT;
    private boolean paypal = false;
    private boolean razorpay = false;
    private boolean paystack = false;
    private LinearLayout razorPay1;
    private LinearLayout paypalLay;
    private LinearLayout paystackLay;
    private String orderAmtKey = "order_amt";
    private String successKey = "success";
    private String getUserIdKey = "getuser_id";
    private String cartIdKey = "cart_id";
    private String paymentMethodKey = "payment_method";
    private String walletStatusKey = "wallet_status";
    private String totalAmountKey = "total_amount";
    private String activityKey = "activity";
    private String nonWalletKey = "non_wallet";
    private String walletKey = "wallet";
    private String statusKey = "status";
    private LinearLayout progressBar;
    private TextView loadingText;
    private String processingText = "Payment is in processing...";
    private boolean checkWalletS = false;


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    private void checkUserPayNotify() {
//        processingText = "Loading please wait..";
        show();
        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);

        Call<PaymentVia> checkOtpStatus = apiInterface.getPaymentVia();

        checkOtpStatus.enqueue(new Callback<PaymentVia>() {
            @Override
            public void onResponse(@NonNull Call<PaymentVia> call, @NonNull retrofit2.Response<PaymentVia> response) {
                if (response.isSuccessful() && response.body() != null) {
                    PaymentVia modelUser = response.body();
                    if (modelUser.getStatus().equalsIgnoreCase("1")) {
                        sessionManagement.setPaymentMethodOpt(modelUser.getData().getRazorpay(), modelUser.getData().getPaypal(), modelUser.getData().getPaystack());
                        enableDiable();
                    }
                }
                show();
                loadingText.setText(processingText);
//                processingText = "Payment is in processing...";
            }

            @Override
            public void onFailure(@NonNull Call<PaymentVia> call, @NonNull Throwable t) {
                show();
                loadingText.setText(processingText);
//                processingText = "Payment is in processing...";
            }
        });

    }

    private void enableDiable() {
        if (sessionManagement.getPayPal().equalsIgnoreCase("1") && sessionManagement.getRazorPay().equalsIgnoreCase("1") && sessionManagement.getPayStack().equalsIgnoreCase("1")) {
            paypal = true;
            razorpay = true;
            paystack = true;
            dropdown.setVisibility(View.VISIBLE);
            rbCard.setVisibility(View.GONE);
        } else if (sessionManagement.getPayPal().equalsIgnoreCase("1") && sessionManagement.getRazorPay().equalsIgnoreCase("1") && sessionManagement.getPayStack().equalsIgnoreCase("0")) {
            paypal = true;
            razorpay = true;
            paystack = false;
            paystackLay.setVisibility(View.GONE);
            dropdown.setVisibility(View.VISIBLE);
            rbCard.setVisibility(View.GONE);
        } else if (sessionManagement.getPayPal().equalsIgnoreCase("1") && sessionManagement.getRazorPay().equalsIgnoreCase("0") && sessionManagement.getPayStack().equalsIgnoreCase("1")) {
            paypal = true;
            paystack = true;
            razorpay = false;
            razorPay1.setVisibility(View.GONE);
            dropdown.setVisibility(View.VISIBLE);
            rbCard.setVisibility(View.GONE);
        } else if (sessionManagement.getPayPal().equalsIgnoreCase("0") && sessionManagement.getRazorPay().equalsIgnoreCase("1") && sessionManagement.getPayStack().equalsIgnoreCase("1")) {
            paypal = false;
            razorpay = true;
            paystack = true;
            paypalLay.setVisibility(View.GONE);
            dropdown.setVisibility(View.VISIBLE);
            rbCard.setVisibility(View.GONE);
        } else {
            childVisible();
        }
    }

    private void childVisible() {
        if (sessionManagement.getPayPal().equalsIgnoreCase("1") && sessionManagement.getRazorPay().equalsIgnoreCase("0") && sessionManagement.getPayStack().equalsIgnoreCase("0")) {
            paypal = true;
            paystack = false;
            razorpay = false;
        }
        if (sessionManagement.getRazorPay().equalsIgnoreCase("1") && sessionManagement.getPayPal().equalsIgnoreCase("0") && sessionManagement.getPayStack().equalsIgnoreCase("0")) {
            paypal = false;
            paystack = false;
            razorpay = true;
        }
        if (sessionManagement.getPayStack().equalsIgnoreCase("1") && sessionManagement.getPayPal().equalsIgnoreCase("0") && sessionManagement.getRazorPay().equalsIgnoreCase("0")) {
            paypal = false;
            razorpay = false;
            paystack = true;
        }
        dropdown.setVisibility(View.GONE);
        rbCard.setVisibility(View.VISIBLE);
    }

    private void show() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_payment);
        sessionManagement = new SessionManagement(this);
        getwallet = SharedPref.getString(getApplicationContext(), BaseURL.KEY_WALLET_AMMOUNT);
        dbCart = new DatabaseHandler(this);
        getuserId = sessionManagement.userId();
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setOnClickListener(view -> Log.i("TAG", "not work"));
        loadingText = findViewById(R.id.loading_text);
//        loadingText.setText(processingText);
        llwallet = findViewById(R.id.llwallet);
        couponApplyT = findViewById(R.id.coupon_apply_t);
        llpromocode = findViewById(R.id.llpromocode);
        llcod = findViewById(R.id.llcod);
        llcards = findViewById(R.id.llcards);
        paystackLay = findViewById(R.id.paystack_lay);
        CheckBox usePaystack = findViewById(R.id.use_paystack);
        TextView paypaystackTxt = findViewById(R.id.paypaystack_txt);

        dropdown = findViewById(R.id.dropdown);
        LinearLayout paymentOpt = findViewById(R.id.payment_opt);
        usePaypal = findViewById(R.id.use_paypal);
        razorPay1 = findViewById(R.id.razor_pay);
        paypalLay = findViewById(R.id.paypal_lay);
        useRazorpay = findViewById(R.id.use_razorpay);
        TextView paypalTxt = findViewById(R.id.paypal_txt);
        TextView razTxt = findViewById(R.id.raz_txt);

        backcart = findViewById(R.id.backcart);
        confirm = findViewById(R.id.confirm_order);
        cartId2 = sessionManagement.getCartId();
        linea = findViewById(R.id.line1);
        lineb = findViewById(R.id.line2);
        resetText = findViewById(R.id.reset_text);
        twallet = findViewById(R.id.walletext);
        tcod = findViewById(R.id.txtcod);
        tcards = findViewById(R.id.txtcards);
        tpromocode = findViewById(R.id.txtpromo);
        checkBoxWallet = findViewById(R.id.use_wallet);
        rbStore = findViewById(R.id.use_store_pickup);
        rbCod = findViewById(R.id.use_COD);
        rbCard = findViewById(R.id.use_card);
        checkBoxCoupon = findViewById(R.id.use_coupon);
        etCoupon = findViewById(R.id.et_coupon_code);
        promoCodeLayout = findViewById(R.id.prommocode_layout);
        applyCouponCode = findViewById(R.id.apply_coupoun_code);
        radioGroup = findViewById(R.id.radio_group);
        myWalletAmmount = findViewById(R.id.user_wallet);
        orderAmmount = findViewById(R.id.order_ammount);
        usedWalletAmmount = findViewById(R.id.used_wallet_ammount);
        usedCouponAmmount = findViewById(R.id.used_coupon_ammount);
        coupuntxt = findViewById(R.id.coupuntxt);
        coupuntxt.setText("Apply");
        checkBoxWallet.setClickable(false);
        rbCard.setClickable(false);
        rbCod.setClickable(false);
        checkBoxCoupon.setClickable(false);
        usePaypal.setClickable(false);
        useRazorpay.setClickable(false);

        checkUserPayNotify();

        resetText.setOnClickListener(v -> {
            checkBoxWallet.setClickable(false);
            rbCard.setClickable(false);
            rbCod.setClickable(false);
            checkBoxCoupon.setClickable(false);
            useRazorpay.setClickable(false);
            usePaypal.setClickable(false);
            usePaystack.setClickable(false);
            useRazorpay.setChecked(false);
            usePaypal.setChecked(false);
            usePaystack.setChecked(false);
            totalAmount = getIntent().getStringExtra(orderAmtKey);
            payableAmt = getIntent().getStringExtra(orderAmtKey);
            code = "";
            paymentMethod = "";
            walletStatus = "no";
            getwallet = SharedPref.getString(getApplicationContext(), BaseURL.KEY_WALLET_AMMOUNT);
            walletAmount = getwallet;
            walletbalnce = 0;
            myWalletAmmount.setText(sessionManagement.getCurrency() + "" + walletAmount);
            orderAmmount.setText(totalAmount + " " + sessionManagement.getCurrency());
            myWalletAmmount.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
            checkBoxWallet.setChecked(false);
            llwallet.setBackgroundResource(R.drawable.border_rounded1);
            twallet.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
            checkBoxCoupon.setChecked(false);
            llpromocode.setBackgroundResource(R.drawable.border_rounded1);
            tpromocode.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
            rbCod.setChecked(false);
            llcod.setBackgroundResource(R.drawable.border_rounded1);
            tcod.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
            rbCard.setChecked(false);
            llcards.setBackgroundResource(R.drawable.border_rounded1);
            tcards.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
            etCoupon.setText("");
            promoCodeLayout.setVisibility(View.GONE);
            couponApplyT.setVisibility(View.VISIBLE);
            promoCodeLayout.setClickable(true);
            llcards.setClickable(true);
            llcod.setClickable(true);
            llpromocode.setClickable(true);
            razorPay1.setClickable(true);
            paypalLay.setClickable(true);
            paystackLay.setClickable(true);
            enableDiable();
            paymentOpt.setVisibility(View.GONE);
            razorPay1.setBackgroundResource(R.drawable.border_rounded1);
            razTxt.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
            paypalLay.setBackgroundResource(R.drawable.border_rounded1);
            paypalTxt.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
            paystackLay.setBackgroundResource(R.drawable.border_rounded1);
            paypaystackTxt.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
        });
        totalAmount = getIntent().getStringExtra(orderAmtKey);
        payableAmt = getIntent().getStringExtra(orderAmtKey);

        orderAmmount.setText(totalAmount + " " + sessionManagement.getCurrency());

        enableDiable();

        dropdown.setOnClickListener(v -> {
            if (paymentOpt.getVisibility() == View.VISIBLE) {
                paymentOpt.setVisibility(View.GONE);
                dropdown.setBackground(ContextCompat.getDrawable(PaymentDetails.this, R.drawable.ic_arrow_righ_new));
            } else {
                paymentOpt.setVisibility(View.VISIBLE);
                dropdown.setBackground(ContextCompat.getDrawable(PaymentDetails.this, R.drawable.ic_down_new));
            }
        });

        backcart.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask();
            } else {
                finish();
            }
        });

        sharedPreferences12 = getSharedPreferences("loction", MODE_PRIVATE);
        editor12 = sharedPreferences12.edit();
        applyCouponCode.setOnClickListener(v -> {
            if (!coupuntxt.getText().toString().trim().equalsIgnoreCase("Applied")) {
                code = etCoupon.getText().toString().trim();
                apply();
            }
        });
        getRefresh();
        rewardliness();
        final String email = sessionManagement.getUserDetails().get(BaseURL.KEY_EMAIL);
        final String mobile = sessionManagement.getUserDetails().get(BaseURL.KEY_MOBILE);
        final String name = sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
        confirm.setOnClickListener(v -> {
            show();
            confirmPay(name, email, mobile, usePaystack);
        });

        llcards.setOnClickListener(v -> llCardClick());

        razorPay1.setOnClickListener(v -> razorPayClick(usePaystack, razTxt, paypalTxt, paypaystackTxt));

        paypalLay.setOnClickListener(v -> paypayClick(usePaystack, razTxt, paypalTxt, paypaystackTxt));

        paystackLay.setOnClickListener(v -> payStackClick(usePaystack, razTxt, paypalTxt, paypaystackTxt));

        llcod.setOnClickListener(v -> codeClick());

        llwallet.setOnClickListener(v -> walletClick());

        llpromocode.setOnClickListener(v -> {
            if (!checkBoxCoupon.isChecked()) {
                couponApplyT.setVisibility(View.GONE);
                checkBoxCoupon.setChecked(true);
                llpromocode.setBackgroundResource(R.drawable.gradientbg);
                tpromocode.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.white));
                Intent coupounIntent = new Intent(PaymentDetails.this, Coupen.class);
                startActivityForResult(coupounIntent, 2);
            } else {
                couponApplyT.setVisibility(View.VISIBLE);
                checkBoxCoupon.setChecked(false);
                llpromocode.setBackgroundResource(R.drawable.border_rounded1);
                tpromocode.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
                etCoupon.setText("");
                promoCodeLayout.setVisibility(View.GONE);
                promoCodeLayout.setClickable(true);
                if (code != null && !code.equalsIgnoreCase("")) {
                    apply();
                }
            }
            checkBoxWallet.setClickable(false);
            rbCard.setClickable(false);
            rbCod.setClickable(false);
            checkBoxCoupon.setClickable(false);
            useRazorpay.setClickable(false);
            usePaypal.setClickable(false);
        });

    }

    private void walletClick() {
        if (!checkBoxWallet.isChecked()) {
            checkBoxWallet.setChecked(true);
            llwallet.setBackgroundResource(R.drawable.gradientbg);
            twallet.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.white));
            myWalletAmmount.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.white));
            double amt = Double.parseDouble(totalAmount);
            double wallet = Double.parseDouble(walletAmount);
            if (wallet > 0) {
                if (amt <= wallet) {
                    walletbalnce = wallet - amt;
                    totalAmount = "0";
                    llcards.setClickable(false);
                    llcod.setClickable(false);
                    llpromocode.setClickable(false);
                    myWalletAmmount.setText(sessionManagement.getCurrency() + "" + walletbalnce);
                    orderAmmount.setText(totalAmount + " " + sessionManagement.getCurrency());
                } else {
                    walletbalnce = 0;
                    totalAmount = String.valueOf((amt - wallet));
                    myWalletAmmount.setText(sessionManagement.getCurrency() + "" + walletbalnce);
                    orderAmmount.setText(totalAmount + " " + sessionManagement.getCurrency());
                }
                walletStatus = "yes";
                paymentMethod = walletKey;
            } else {
                walletStatus = "no";
                paymentMethod = "";
                checkBoxWallet.setChecked(false);
                llwallet.setBackgroundResource(R.drawable.border_rounded1);
                twallet.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
                myWalletAmmount.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
                double wallett = Double.parseDouble(walletAmount);
                myWalletAmmount.setText(sessionManagement.getCurrency() + wallett);
                totalAmount = payableAmt;
                walletbalnce = wallet;
                orderAmmount.setText(totalAmount + " " + sessionManagement.getCurrency());
                walletStatus = "no";
                paymentMethod = "";
                llcod.setClickable(true);
                llcards.setClickable(true);
                llpromocode.setClickable(true);
                startActivityForResult(new Intent(PaymentDetails.this, RechargeWallet.class), 5);
            }

        } else {
            checkBoxWallet.setChecked(false);
            llwallet.setBackgroundResource(R.drawable.border_rounded1);
            twallet.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
            myWalletAmmount.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
            double wallet = Double.parseDouble(walletAmount);
            myWalletAmmount.setText(sessionManagement.getCurrency() + "" + wallet);

            if (checkBoxCoupon.isChecked()) {

                walletClick1(wallet);

            } else {
                totalAmount = payableAmt;
                walletbalnce = wallet;
                orderAmmount.setText(totalAmount + " " + sessionManagement.getCurrency());
                walletStatus = "no";
                paymentMethod = "";
            }
            llcod.setClickable(true);
            llcards.setClickable(true);
            llpromocode.setClickable(true);
        }

        checkBoxWallet.setClickable(false);
        rbCard.setClickable(false);
        rbCod.setClickable(false);
        checkBoxCoupon.setClickable(false);
        useRazorpay.setClickable(false);
        usePaypal.setClickable(false);
    }

    private void walletClick1(double wallet) {
        if (couponAmount != null && !couponAmount.equalsIgnoreCase("")) {
            double amountd = Double.parseDouble(payableAmt) - Double.parseDouble(couponAmount);
            totalAmount = String.valueOf(amountd);
            walletbalnce = wallet;
            orderAmmount.setText(totalAmount + " " + sessionManagement.getCurrency());
            walletStatus = "no";
            paymentMethod = "promocode";
        } else {
            totalAmount = payableAmt;
            walletbalnce = wallet;
            orderAmmount.setText(totalAmount + " " + sessionManagement.getCurrency());
            walletStatus = "no";
            paymentMethod = "";
        }
    }

    private void codeClick() {
        if (!totalAmount.equalsIgnoreCase("0") && !totalAmount.equalsIgnoreCase("0.0") && !totalAmount.equalsIgnoreCase("")) {
            if (!rbCod.isChecked()) {
                rbCod.setChecked(true);
                rbCard.setChecked(false);
                llcod.setBackgroundResource(R.drawable.gradientbg);
                tcod.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.white));
                llcards.setBackgroundResource(R.drawable.border_rounded1);
                tcards.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
                if (checkBoxWallet.isChecked()) {
                    walletStatus = "yes";
                } else {
                    walletStatus = "no";
                }
                paymentMethod = "COD";
            } else {
                rbCod.setChecked(false);
                rbCard.setChecked(false);
                llcod.setBackgroundResource(R.drawable.border_rounded1);
                tcod.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
                if (checkBoxWallet.isChecked()) {
                    walletStatus = "yes";
                    paymentMethod = walletKey;
                } else {
                    walletStatus = "no";
                    paymentMethod = "";
                }
            }
        }
        checkBoxWallet.setClickable(false);
        rbCard.setClickable(false);
        rbCod.setClickable(false);
        checkBoxCoupon.setClickable(false);
        useRazorpay.setClickable(false);
        usePaypal.setClickable(false);
    }

    private void payStackClick(CheckBox usePaystack, TextView razTxt, TextView paypalTxt, TextView paypaystackTxt) {
        if (!totalAmount.equalsIgnoreCase("0") && !totalAmount.equalsIgnoreCase("0.0") && !totalAmount.equalsIgnoreCase("")) {
            if (!usePaystack.isChecked()) {
                usePaystack.setChecked(true);
                usePaypal.setChecked(false);
                useRazorpay.setChecked(false);
                rbCod.setChecked(false);
                paystackLay.setBackgroundResource(R.drawable.gradientbg);
                paypaystackTxt.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.white));
                paypalLay.setBackgroundResource(R.drawable.border_rounded1);
                paypalTxt.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
                llcod.setBackgroundResource(R.drawable.border_rounded1);
                tcod.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
                razorPay1.setBackgroundResource(R.drawable.border_rounded1);
                razTxt.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
                if (checkBoxWallet.isChecked()) {
                    walletStatus = "yes";
                } else {
                    walletStatus = "no";
                }
                paymentMethod = "card";
            } else {
                usePaypal.setChecked(false);
                usePaystack.setChecked(false);
                paystackLay.setBackgroundResource(R.drawable.border_rounded1);
                paypaystackTxt.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
                paypalLay.setBackgroundResource(R.drawable.border_rounded1);
                paypalTxt.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
                if (checkBoxWallet.isChecked()) {
                    walletStatus = "yes";
                    paymentMethod = walletKey;
                } else {
                    paymentMethod = "";
                    walletStatus = "no";
                }
            }
        }
        checkBoxWallet.setClickable(false);
        rbCard.setClickable(false);
        rbCod.setClickable(false);
        checkBoxCoupon.setClickable(false);
        useRazorpay.setClickable(false);
        usePaypal.setClickable(false);
        usePaystack.setClickable(false);
    }

    private void paypayClick(CheckBox usePaystack, TextView razTxt, TextView paypalTxt, TextView paypaystackTxt) {
        if (!totalAmount.equalsIgnoreCase("0") && !totalAmount.equalsIgnoreCase("0.0") && !totalAmount.equalsIgnoreCase("")) {
            if (!usePaypal.isChecked()) {
                usePaypal.setChecked(true);
                useRazorpay.setChecked(false);
                usePaystack.setChecked(false);
                rbCod.setChecked(false);
                paypalLay.setBackgroundResource(R.drawable.gradientbg);
                paypalTxt.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.white));
                llcod.setBackgroundResource(R.drawable.border_rounded1);
                tcod.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
                razorPay1.setBackgroundResource(R.drawable.border_rounded1);
                razTxt.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
                paystackLay.setBackgroundResource(R.drawable.border_rounded1);
                paypaystackTxt.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
                if (checkBoxWallet.isChecked()) {
                    walletStatus = "yes";
                } else {
                    walletStatus = "no";
                }
                paymentMethod = "card";
            } else {
                usePaypal.setChecked(false);
                paypalLay.setBackgroundResource(R.drawable.border_rounded1);
                paypalTxt.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
                if (checkBoxWallet.isChecked()) {
                    walletStatus = "yes";
                    paymentMethod = walletKey;
                } else {
                    paymentMethod = "";
                    walletStatus = "no";
                }
            }
        }
        checkBoxWallet.setClickable(false);
        rbCard.setClickable(false);
        rbCod.setClickable(false);
        checkBoxCoupon.setClickable(false);
        useRazorpay.setClickable(false);
        usePaypal.setClickable(false);
        usePaystack.setClickable(false);
    }

    private void razorPayClick(CheckBox usePaystack, TextView razTxt, TextView paypalTxt, TextView paypaystackTxt) {
        if (!totalAmount.equalsIgnoreCase("0") && !totalAmount.equalsIgnoreCase("0.0") && !totalAmount.equalsIgnoreCase("")) {
            if (!useRazorpay.isChecked()) {
                useRazorpay.setChecked(true);
                usePaystack.setChecked(false);
                usePaypal.setChecked(false);
                rbCod.setChecked(false);
                razorPay1.setBackgroundResource(R.drawable.gradientbg);
                razTxt.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.white));
                paypalLay.setBackgroundResource(R.drawable.border_rounded1);
                paypalTxt.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
                llcod.setBackgroundResource(R.drawable.border_rounded1);
                tcod.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
                paystackLay.setBackgroundResource(R.drawable.border_rounded1);
                paypaystackTxt.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
                if (checkBoxWallet.isChecked()) {
                    walletStatus = "yes";
                } else {
                    walletStatus = "no";
                }
                paymentMethod = "card";
            } else {
                useRazorpay.setChecked(false);
                razorPay1.setBackgroundResource(R.drawable.border_rounded1);
                razTxt.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
                if (checkBoxWallet.isChecked()) {
                    walletStatus = "yes";
                    paymentMethod = walletKey;
                } else {
                    paymentMethod = "";
                    walletStatus = "no";
                }
            }
        }
        checkBoxWallet.setClickable(false);
        rbCard.setClickable(false);
        rbCod.setClickable(false);
        checkBoxCoupon.setClickable(false);
        useRazorpay.setClickable(false);
        usePaypal.setClickable(false);
        usePaystack.setClickable(false);
    }

    private void llCardClick() {
        if (dropdown.getVisibility() == View.GONE && !totalAmount.equalsIgnoreCase("0") && !totalAmount.equalsIgnoreCase("0.0") && !totalAmount.equalsIgnoreCase("")) {
            if (!rbCard.isChecked()) {
                rbCard.setChecked(true);
                rbCod.setChecked(false);
                llcards.setBackgroundResource(R.drawable.gradientbg);
                tcards.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.white));
                llcod.setBackgroundResource(R.drawable.border_rounded1);
                tcod.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
                if (checkBoxWallet.isChecked()) {
                    walletStatus = "yes";
                } else {
                    walletStatus = "no";
                }
                paymentMethod = "card";
            } else {
                rbCard.setChecked(false);
                llcards.setBackgroundResource(R.drawable.border_rounded1);
                tcards.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
                if (checkBoxWallet.isChecked()) {
                    walletStatus = "yes";
                    paymentMethod = walletKey;
                } else {
                    paymentMethod = "";
                    walletStatus = "no";
                }
            }
        }
        checkBoxWallet.setClickable(false);
        rbCard.setClickable(false);
        rbCod.setClickable(false);
        checkBoxCoupon.setClickable(false);
        useRazorpay.setClickable(false);
        usePaypal.setClickable(false);
    }

    private void confirmPay(String name, String email, String mobile, CheckBox usePaystack) {
        if (rbCod.isChecked() && checkBoxWallet.isChecked()) {
            walletStatus = "yes";
            paymentMethod = "COD";
            makeAddOrderRequest(/*getuserId, */cartId2, paymentMethod, walletStatus, successKey);
        } else if (rbCard.isChecked() && checkBoxWallet.isChecked()) {
            walletStatus = "yes";
            paymentMethod = "card";
            if (razorpay) {
                startPayment(name, totalAmount, email, mobile);
            } else if (paypal) {
                startPaypal(totalAmount, email, mobile);
            } else if (paystack) {
                Intent intent = new Intent(PaymentDetails.this, PaystackPayment.class);
                intent.putExtra(getUserIdKey, getuserId);
                intent.putExtra(cartIdKey, cartId2);
                intent.putExtra(paymentMethodKey, paymentMethod);
                intent.putExtra(walletStatusKey, walletStatus);
                intent.putExtra(totalAmountKey, totalAmount);
                intent.putExtra(activityKey, nonWalletKey);
                startActivity(intent);
            }

        } else if (rbCard.isChecked()) {
            walletStatus = "no";
            paymentMethod = "card";
            if (razorpay) {
                startPayment(name, totalAmount, email, mobile);
            } else if (paypal) {
                startPaypal(totalAmount, email, mobile);
            } else if (paystack) {
                Intent intent = new Intent(PaymentDetails.this, PaystackPayment.class);
                intent.putExtra(getUserIdKey, getuserId);
                intent.putExtra(cartIdKey, cartId2);
                intent.putExtra(paymentMethodKey, paymentMethod);
                intent.putExtra(walletStatusKey, walletStatus);
                intent.putExtra(totalAmountKey, totalAmount);
                intent.putExtra(activityKey, nonWalletKey);
                startActivity(intent);
            }
        } else {
            confirmPay1(name, email, mobile, usePaystack);
        }
    }

    private void confirmPay1(String name, String email, String mobile, CheckBox usePaystack) {
        if (useRazorpay.isChecked() && checkBoxWallet.isChecked()) {
            walletStatus = "yes";
            paymentMethod = "card";
            startPayment(name, totalAmount, email, mobile);
        } else if (useRazorpay.isChecked()) {
            walletStatus = "no";
            paymentMethod = "card";
            startPayment(name, totalAmount, email, mobile);
        } else if (usePaystack.isChecked() && checkBoxWallet.isChecked()) {
            walletStatus = "yes";
            paymentMethod = "card";
            Intent intent = new Intent(PaymentDetails.this, PaystackPayment.class);
            intent.putExtra(getUserIdKey, getuserId);
            intent.putExtra(cartIdKey, cartId2);
            intent.putExtra(paymentMethodKey, paymentMethod);
            intent.putExtra(walletStatusKey, walletStatus);
            intent.putExtra(totalAmountKey, totalAmount);
            intent.putExtra(activityKey, nonWalletKey);
            startActivity(intent);
        } else if (usePaystack.isChecked()) {
            walletStatus = "no";
            paymentMethod = "card";
            Intent intent = new Intent(PaymentDetails.this, PaystackPayment.class);
            intent.putExtra(getUserIdKey, getuserId);
            intent.putExtra(cartIdKey, cartId2);
            intent.putExtra(paymentMethodKey, paymentMethod);
            intent.putExtra(walletStatusKey, walletStatus);
            intent.putExtra(totalAmountKey, totalAmount);
            intent.putExtra(activityKey, nonWalletKey);
            startActivity(intent);
        } else if (usePaypal.isChecked() && checkBoxWallet.isChecked()) {
            walletStatus = "yes";
            paymentMethod = "card";
            startPaypal(totalAmount, email, mobile);
        } else if (usePaypal.isChecked()) {
            walletStatus = "no";
            paymentMethod = "card";
            startPaypal(totalAmount, email, mobile);
        } else {
            confirmPay2();
        }
    }

    private void confirmPay2() {
        if (rbCod.isChecked()) {
            walletStatus = "no";
            paymentMethod = "COD";
            makeAddOrderRequest(/*getuserId, */cartId2, paymentMethod, walletStatus, successKey);
        } else if (checkBoxWallet.isChecked()) {
            walletStatus = "yes";
            paymentMethod = walletKey;
            if (walletbalnce == 0.0 && Double.parseDouble(totalAmount) > 0.0) {
                show();
                Toast.makeText(PaymentDetails.this, "Select Card Or COD", Toast.LENGTH_SHORT).show();
            } else {
                makeAddOrderRequest(/*getuserId, */cartId2, paymentMethod, walletStatus, successKey);
            }

        } else {
            if (checkBoxCoupon.isChecked()) {
                if (Double.parseDouble(totalAmount) > 0.0) {
                    show();
                    Toast.makeText(PaymentDetails.this, "Please select payment method!", Toast.LENGTH_SHORT).show();
                } else {
                    walletStatus = "no";
                    paymentMethod = "promocode";
                    makeAddOrderRequest(/*getuserId, */cartId2, paymentMethod, walletStatus, successKey);
                }
            } else {
                show();
                Toast.makeText(PaymentDetails.this, "Please select payment method!", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void startPaypal(String totalAmount1, String email, String mobile) {
        PayPalConfiguration config = new PayPalConfiguration()
                .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .acceptCreditCards(true)
                .defaultUserPhone(mobile)
                .defaultUserEmail(email)
                .clientId(PayPalConfig.PAYPAL_CLIENT_ID);
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
        getPayment(totalAmount1, config);
    }

    private void getPayment(String totalRs1, PayPalConfiguration config) {
        PayPalPayment payment = new PayPalPayment(new BigDecimal(totalRs1), "USD", "Shopping Fee", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }


    private void rewardliness() {
        Map<String, String> params = new HashMap<>();
        params.put(cartIdKey, cartId2);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.REWARDLINES, params, response -> {
            try {
                String status = response.getString(statusKey);
                if (status.contains("1")) {
                    String line1 = response.getString("line1");
                    String line2 = response.getString("line2");
                    linea.setVisibility(View.VISIBLE);
                    lineb.setVisibility(View.VISIBLE);
                    if (line1.equalsIgnoreCase("")) {
                        linea.setVisibility(View.GONE);
                        lineb.setText(line2);
                    }
                    if (line2.equalsIgnoreCase("")) {
                        lineb.setVisibility(View.GONE);
                        linea.setText(line1);
                    }
                    linea.setText(line1);
                    lineb.setText(line2);
                } else {
                    linea.setVisibility(View.GONE);
                    lineb.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);

        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 90000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                error.printStackTrace();
            }
        });
        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().getRequestQueue().add(jsonObjReq);


    }


    private void apply() {
        show();
        Map<String, String> params = new HashMap<>();
        params.put(cartIdKey, cartId2);
        params.put("coupon_code", code);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST, BaseURL.APPLY_COUPON, params, response -> {
            try {
                String statuss = response.getString(statusKey);
                String message = response.getString("message");
                if (statuss.contains("1")) {
                    JSONObject jsonObject = response.getJSONObject("data");
                    remaingprice = jsonObject.getString("rem_price");
                    coupuntxt.setText("Applied");
                    couponAmount = jsonObject.getString("coupon_discount");
                    verfiyDetails();
                    Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();
                } else if (statuss.contains("2")) {
                    verfiyDetails1();
                } else {
                    code = "";
                    status = 1;
                    promoCodeLayout.setVisibility(View.GONE);
                    couponApplyT.setVisibility(View.VISIBLE);
                    checkBoxCoupon.setChecked(false);
                    llpromocode.setBackgroundResource(R.drawable.border_rounded1);
                    tpromocode.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
                    Toast.makeText(getApplicationContext(), "" + message, Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                show();
            }
        }, error -> show());

        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 90000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                error.printStackTrace();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void verfiyDetails1() {
        code = "";
        status = 1;
        if (checkBoxWallet.isChecked()) {
            double amt = Double.parseDouble(payableAmt);
            double wallet = Double.parseDouble(walletAmount);
            if (wallet > 0) {
                if (amt <= wallet) {
                    walletbalnce = wallet - amt;
                    totalAmount = "0";
                    rbCard.setClickable(false);
                    rbCod.setClickable(false);
                    checkBoxCoupon.setClickable(false);
                    myWalletAmmount.setText(sessionManagement.getCurrency() + "" + walletbalnce);
                    orderAmmount.setText(totalAmount + " " + sessionManagement.getCurrency());
                } else {
                    walletbalnce = 0;
                    totalAmount = String.valueOf((amt - wallet));
                    orderAmmount.setText(totalAmount + " " + sessionManagement.getCurrency());
                }
            }
        } else {
            totalAmount = payableAmt;
            orderAmmount.setText(totalAmount + " " + sessionManagement.getCurrency());
        }
    }

    private void verfiyDetails() {
        if (checkBoxWallet.isChecked()) {
            double remInt = Double.parseDouble(remaingprice);
            double wallet = Double.parseDouble(walletAmount);
            if (wallet >= remInt) {
                walletbalnce = (int) (wallet - remInt);
                totalAmount = "0";
                rbCard.setClickable(false);
                rbCod.setClickable(false);
                checkBoxCoupon.setClickable(true);
                myWalletAmmount.setText(sessionManagement.getCurrency() + walletbalnce);
                orderAmmount.setText(totalAmount + " " + sessionManagement.getCurrency());
            } else {
                if (wallet == 0.0) {
                    totalAmount = remaingprice;
                    orderAmmount.setText(totalAmount + " " + sessionManagement.getCurrency());
                } else {
                    walletbalnce = 0;
                    totalAmount = String.valueOf((remInt - wallet));
                    orderAmmount.setText(totalAmount + " " + sessionManagement.getCurrency());
                }
            }
        } else {
            totalAmount = remaingprice;
            orderAmmount.setText(totalAmount + " " + sessionManagement.getCurrency());
        }
    }

    private void makeAddOrderRequest(/*String userid, */String cartId, String paymentMethod, String walletStatus, String paymentStatus) {
        Map<String, String> params = new HashMap<>();
        /*params.put("user_id", userid);*/
        params.put("payment_status", paymentStatus);
        params.put(cartIdKey, cartId);
        params.put(paymentMethodKey, paymentMethod);
        params.put(walletKey, walletStatus);
        System.out.println("param  - >>>> : "+ params);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.ADD_ORDER_URL, params, response -> {
            try {
                String status = response.getString(statusKey);
                String message = response.getString("message");
                if (status.equalsIgnoreCase("1")) {
                    hitIntent(message);
                } else if (status.equalsIgnoreCase("2")) {
                    hitIntent(message);
                } else {
                    Toast.makeText(PaymentDetails.this, "" + message, Toast.LENGTH_SHORT).show();
                }
                show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> show());

        jsonObjReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 90000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                error.printStackTrace();
            }
        });

        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void hitIntent(String message) {
        sessionManagement.setCartID("");
        dbCart.clearCart();
        Intent intent = new Intent(getApplicationContext(), OrderSuccessful.class);
        intent.putExtra("msg", message);
        startActivity(intent);
        Toast.makeText(PaymentDetails.this, "" + message, Toast.LENGTH_SHORT).show();
    }

    public void getRefresh() {
        String userId = sessionManagement.userId();
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        StringRequest strReq = new StringRequest(Request.Method.POST, BaseURL.WALLET_REFRESH + userId,
                response -> {
                    try {
                        JSONObject jObj = new JSONObject(response);

                        String statusss = jObj.getString(statusKey);
                        if (statusss.equalsIgnoreCase("1")) {
                            walletAmount = jObj.getString("data");
                            myWalletAmmount.setText(sessionManagement.getCurrency() + "" + walletAmount);
                            SharedPref.putString(PaymentDetails.this, BaseURL.KEY_WALLET_AMMOUNT, walletAmount);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        if (checkWalletS && progressBar.getVisibility() == View.VISIBLE) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }, volleyError -> {
            if (checkWalletS && progressBar.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.GONE);
            }
        }) {
        };
        strReq.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 90000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 0;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                error.printStackTrace();
            }
        });
        rq.add(strReq);
    }

    public void startPayment(String name, String amount, String email, String phone) {
        final Activity activity = this;
        final Checkout co = new Checkout();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    RazorpayClient razorpayC = new RazorpayClient("rzp_test_j2Fez1mKPs66Vq", "J3HWEBdAP3KRrKd6AZ3JRI71");
                    JSONObject orderObject = new JSONObject();
                    orderObject.put("amount", Double.parseDouble(amount) * 100); // amount in the smallest currency unit
                    orderObject.put("currency", "IDR");
                    orderObject.put("receipt", "orderID"+String.valueOf(System.currentTimeMillis()));
                    orderObject.put("payment_capture", true);

                    Order orderId = razorpayC.Orders.create(orderObject);
                    if (orderId.has("id")) {
                        Log.i("OrderID", "" + orderId.get("id"));
                        JSONObject options = new JSONObject();

                        options.put("name", name);
                        options.put("description", "Shopping Charges");
                        options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
                        options.put("currency", "IDR");
                        options.put("amount", Double.parseDouble(amount) * 100);
                        options.put("order_id", orderId.get("id"));
                        JSONObject preFill = new JSONObject();
                        preFill.put("email", email);
                        preFill.put("contact", phone);
                        options.put("prefill", preFill);
                        co.open(activity, options);
                    }
                } catch (Exception e) {
                    Toast.makeText(PaymentDetails.this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    public void onPaymentSuccess(String s) {
        if (s != null) {
            makeAddOrderRequest(/*getuserId, */cartId2, paymentMethod, walletStatus, successKey);
        }
    }

    @Override
    public void onPaymentError(int i, String s) {
        show();
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && data != null && data.getExtras() != null) {
            code = data.getExtras().getString("code");
            if (code == null) {
                setCouponLabel();
            } else if (code.equalsIgnoreCase("")) {
                setCouponLabel();
            } else {
                checkBoxCoupon.setChecked(true);
                llpromocode.setBackgroundResource(R.drawable.gradientbg);
                tpromocode.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.white));
                etCoupon.setText(code);
                promoCodeLayout.setVisibility(View.VISIBLE);
                promoCodeLayout.setClickable(false);
                apply();
            }
        } else if (requestCode == 5 && data != null && data.getExtras() != null && Objects.requireNonNull(data.getStringExtra("recharge")).equalsIgnoreCase(successKey)) {
            checkWalletS = true;
            getRefresh();
        } else if (requestCode == PAYPAL_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            PaymentConfirmation confirmPayment = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
            paymentConfirmation(confirmPayment);
        } else if (resultCode == Activity.RESULT_CANCELED) {
            show();
        } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            show();
        } else {
            if (progressBar.getVisibility() == View.VISIBLE) {
                progressBar.setVisibility(View.GONE);
            }
        }
    }

    private void paymentConfirmation(PaymentConfirmation confirmPayment) {
        if (confirmPayment != null) {
            try {
                JSONObject jsonObject = confirmPayment.toJSONObject();
                JSONObject responseJs = jsonObject.getJSONObject("response");
                if (responseJs.getString("state").equalsIgnoreCase("approved")) {
                    makeAddOrderRequest(/*getuserId, */cartId2, paymentMethod, walletStatus, successKey);
                }
            } catch (JSONException e) {
                show();
            }
        }
    }

    private void setCouponLabel() {
        code = "";
        status = 1;
        promoCodeLayout.setVisibility(View.GONE);
        couponApplyT.setVisibility(View.VISIBLE);
        checkBoxCoupon.setChecked(false);
        llpromocode.setBackgroundResource(R.drawable.border_rounded1);
        tpromocode.setTextColor(ContextCompat.getColor(PaymentDetails.this, R.color.black));
    }
}
