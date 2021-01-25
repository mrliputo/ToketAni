package com.tecmanic.toketani.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
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
import com.tecmanic.toketani.modelclass.PaymentVia;
import com.tecmanic.toketani.network.ApiInterface;
import com.tecmanic.toketani.util.NetworkConnection;
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

import static com.tecmanic.toketani.config.BaseURL.RECHAREGEWALLET;

public class RechargeWallet extends AppCompatActivity implements PaymentResultListener {
    private static final int PAYPAL_REQUEST_CODE = 123;
    EditText walletAmmount;
    RelativeLayout rechargeButton;
    String ammount;
    private SessionManagement sessionManagement;
    private ImageView dropdown;
    private LinearLayout payLay;
    private LinearLayout paymentOpt;
    private LinearLayout razorPay;
    private LinearLayout paypalLay;
    private LinearLayout paystackLay;
    private CheckBox usePaystack;
    private TextView paypaystackTxt;
    private CheckBox usePaypal;
    private CheckBox useRazorpay;
    private TextView razTxt;
    private TextView paypalTxt;
    private boolean paypal = false;
    private boolean razorpay1 = false;
    private boolean paystack = false;
    private final String successKey = "success";
    private final String toatCommonKey = "Wallet Recharge Successful";
    private final String failedKey = "failed";
    private LinearLayout progressBar;

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
        setContentView(R.layout.activity_recharge_wallet);
        Toolbar toolbar = findViewById(R.id.toolbar);
        dropdown = findViewById(R.id.dropdown);
        payLay = findViewById(R.id.pay_lay);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setOnClickListener(view -> Log.i("TAG", "not work"));
        paystackLay = findViewById(R.id.paystack_lay);
        usePaystack = findViewById(R.id.use_paystack);
        paypaystackTxt = findViewById(R.id.paypaystack_txt);
        paymentOpt = findViewById(R.id.payment_opt);
        razorPay = findViewById(R.id.razor_pay);
        paypalLay = findViewById(R.id.paypal_lay);
        useRazorpay = findViewById(R.id.use_razorpay);
        usePaypal = findViewById(R.id.use_paypal);
        razTxt = findViewById(R.id.raz_txt);
        paypalTxt = findViewById(R.id.paypal_txt);
        walletAmmount = findViewById(R.id.et_wallet_ammount);
        rechargeButton = findViewById(R.id.recharge_button);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Recharge Wallet");
        sessionManagement = new SessionManagement(RechargeWallet.this);
        final String email = sessionManagement.getUserDetails().get(BaseURL.KEY_EMAIL);
        final String mobile = sessionManagement.getUserDetails().get(BaseURL.KEY_MOBILE);
        final String name = sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
        checkUserPayNotify();

        enableDisable();

        toolbar.setNavigationOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("recharge", "fail");
            setResult(5, intent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask();
            } else {
                finish();
            }
        });

        dropdown.setOnClickListener(view -> {
            if (paymentOpt.getVisibility() == View.VISIBLE) {
                paymentOpt.setVisibility(View.GONE);
                dropdown.setBackground(ContextCompat.getDrawable(RechargeWallet.this, R.drawable.ic_arrow_righ_new));
            } else {
                paymentOpt.setVisibility(View.VISIBLE);
                dropdown.setBackground(ContextCompat.getDrawable(RechargeWallet.this, R.drawable.ic_down_new));
            }
        });

        razorPay.setOnClickListener(view -> razorPayClick());

        paypalLay.setOnClickListener(view -> payClick());

        paystackLay.setOnClickListener(view -> payStackClick());


        rechargeButton.setOnClickListener(v -> rechargeClick(name, email, mobile));
    }

    private void rechargeClick(String name, String email, String mobile) {
        ammount = walletAmmount.getText().toString();
        if (paypal && razorpay1 && paystack) {
            if (useRazorpay.isChecked()) {
                show();
                startPayment(name, ammount, email, mobile);
            } else if (usePaypal.isChecked()) {
                show();
                startPaypal(ammount, email, mobile);
            } else if (usePaystack.isChecked()) {
                Intent intent = new Intent(RechargeWallet.this, PaystackPayment.class);
                intent.putExtra("activity", "wallet");
                intent.putExtra("total_amount", ammount);
                startActivityForResult(intent, 25);
            } else {
                Toast.makeText(RechargeWallet.this, "Please select payment mode!", Toast.LENGTH_SHORT).show();
                if (paymentOpt.getVisibility() != View.VISIBLE) {
                    paymentOpt.setVisibility(View.VISIBLE);
                    dropdown.setBackground(ContextCompat.getDrawable(RechargeWallet.this, R.drawable.ic_down_new));
                }
            }
        } else {
            if (usePaypal.isChecked() || paypal) {
                show();
                startPaypal(ammount, email, mobile);
            } else if (useRazorpay.isChecked() || razorpay1) {
                show();
                startPayment(name, ammount, email, mobile);
            } else if (usePaystack.isChecked() || paystack) {
                Intent intent = new Intent(RechargeWallet.this, PaystackPayment.class);
                intent.putExtra("activity", "wallet");
                intent.putExtra("total_amount", ammount);
                startActivityForResult(intent, 25);
            }
        }
    }

    private void payStackClick() {
        if (usePaystack.isChecked()) {
            useRazorpay.setChecked(false);
            usePaypal.setChecked(false);
            usePaystack.setChecked(false);
            razorPay.setBackgroundResource(R.drawable.border_rounded1);
            razTxt.setTextColor(ContextCompat.getColor(RechargeWallet.this, R.color.black));
            paypalLay.setBackgroundResource(R.drawable.border_rounded1);
            paypalTxt.setTextColor(ContextCompat.getColor(RechargeWallet.this, R.color.black));
            paystackLay.setBackgroundResource(R.drawable.border_rounded1);
            paypaystackTxt.setTextColor(ContextCompat.getColor(RechargeWallet.this, R.color.black));
        } else {
            useRazorpay.setChecked(false);
            usePaypal.setChecked(false);
            usePaystack.setChecked(true);
            razorPay.setBackgroundResource(R.drawable.border_rounded1);
            razTxt.setTextColor(ContextCompat.getColor(RechargeWallet.this, R.color.black));
            paypalLay.setBackgroundResource(R.drawable.border_rounded1);
            paypalTxt.setTextColor(ContextCompat.getColor(RechargeWallet.this, R.color.black));
            paystackLay.setBackgroundResource(R.drawable.gradientbg);
            paypaystackTxt.setTextColor(ContextCompat.getColor(RechargeWallet.this, R.color.white));
        }
    }

    private void payClick() {
        if (usePaypal.isChecked()) {
            useRazorpay.setChecked(false);
            usePaypal.setChecked(false);
            usePaystack.setChecked(false);
            razorPay.setBackgroundResource(R.drawable.border_rounded1);
            razTxt.setTextColor(ContextCompat.getColor(RechargeWallet.this, R.color.black));
            paypalLay.setBackgroundResource(R.drawable.border_rounded1);
            paypalTxt.setTextColor(ContextCompat.getColor(RechargeWallet.this, R.color.black));
            paystackLay.setBackgroundResource(R.drawable.border_rounded1);
            paypaystackTxt.setTextColor(ContextCompat.getColor(RechargeWallet.this, R.color.black));
        } else {
            useRazorpay.setChecked(false);
            usePaystack.setChecked(false);
            usePaypal.setChecked(true);
            razorPay.setBackgroundResource(R.drawable.border_rounded1);
            razTxt.setTextColor(ContextCompat.getColor(RechargeWallet.this, R.color.black));
            paystackLay.setBackgroundResource(R.drawable.border_rounded1);
            paypaystackTxt.setTextColor(ContextCompat.getColor(RechargeWallet.this, R.color.black));
            paypalLay.setBackgroundResource(R.drawable.gradientbg);
            paypalTxt.setTextColor(ContextCompat.getColor(RechargeWallet.this, R.color.white));
        }
    }

    private void razorPayClick() {
        if (useRazorpay.isChecked()) {
            useRazorpay.setChecked(false);
            usePaypal.setChecked(false);
            usePaystack.setChecked(false);
            razorPay.setBackgroundResource(R.drawable.border_rounded1);
            razTxt.setTextColor(ContextCompat.getColor(RechargeWallet.this, R.color.black));
            paypalLay.setBackgroundResource(R.drawable.border_rounded1);
            paypalTxt.setTextColor(ContextCompat.getColor(RechargeWallet.this, R.color.black));
            paystackLay.setBackgroundResource(R.drawable.border_rounded1);
            paypaystackTxt.setTextColor(ContextCompat.getColor(RechargeWallet.this, R.color.black));
        } else {
            useRazorpay.setChecked(true);
            usePaystack.setChecked(false);
            usePaypal.setChecked(false);
            razorPay.setBackgroundResource(R.drawable.gradientbg);
            razTxt.setTextColor(ContextCompat.getColor(RechargeWallet.this, R.color.white));
            paystackLay.setBackgroundResource(R.drawable.border_rounded1);
            paypaystackTxt.setTextColor(ContextCompat.getColor(RechargeWallet.this, R.color.black));
            paypalLay.setBackgroundResource(R.drawable.border_rounded1);
            paypalTxt.setTextColor(ContextCompat.getColor(RechargeWallet.this, R.color.black));
        }
    }

    private void enableDisable() {

        int count = 0;

        if (sessionManagement.getPayPal().equalsIgnoreCase("1")) {
            count = 1;
            paypal = true;
            payLay.setVisibility(View.VISIBLE);
        } else {
            paypal = false;
            payLay.setVisibility(View.GONE);
        }

        if (sessionManagement.getRazorPay().equalsIgnoreCase("1")) {
            count += count;
            razorpay1 = true;
            razorPay.setVisibility(View.VISIBLE);
        } else {
            razorpay1 = false;
            razorPay.setVisibility(View.GONE);
        }

        if (sessionManagement.getPayStack().equalsIgnoreCase("1")) {
            count += count;
            paystack = true;
            paystackLay.setVisibility(View.VISIBLE);
        } else {
            paystack = false;
            paystackLay.setVisibility(View.GONE);
        }


        if (count == 0) {
            dropdown.setVisibility(View.GONE);
        } else {
            dropdown.setVisibility(View.VISIBLE);
        }


//        if (sessionManagement.getPayPal().equalsIgnoreCase("1") && sessionManagement.getRazorPay().equalsIgnoreCase("1") && sessionManagement.getPayStack().equalsIgnoreCase("1")) {
//
//            razorpay1 = true;
//            paystack = true;
//            payLay.setVisibility(View.VISIBLE);
//        } else if (sessionManagement.getPayPal().equalsIgnoreCase("1") && sessionManagement.getRazorPay().equalsIgnoreCase("1") && sessionManagement.getPayStack().equalsIgnoreCase("0")) {
//            paypal = true;
//            razorpay1 = true;
//            paystack = false;
//            paystackLay.setVisibility(View.GONE);
//            dropdown.setVisibility(View.VISIBLE);
//        } else if (sessionManagement.getPayPal().equalsIgnoreCase("1") && sessionManagement.getRazorPay().equalsIgnoreCase("0") && sessionManagement.getPayStack().equalsIgnoreCase("1")) {
//            paypal = true;
//            paystack = true;
//            razorpay1 = false;
//            razorPay.setVisibility(View.GONE);
//            dropdown.setVisibility(View.VISIBLE);
//        } else if (sessionManagement.getPayPal().equalsIgnoreCase("0") && sessionManagement.getRazorPay().equalsIgnoreCase("1") && sessionManagement.getPayStack().equalsIgnoreCase("1")) {
//            paypal = false;
//            razorpay1 = true;
//            paystack = true;
//            paypalLay.setVisibility(View.GONE);
//            dropdown.setVisibility(View.VISIBLE);
//        } else {
//            enableDisable1();
//        }
    }

//    private void enableDisable1() {
//        payLay.setVisibility(View.GONE);
//        if (sessionManagement.getPayPal().equalsIgnoreCase("1") && sessionManagement.getRazorPay().equalsIgnoreCase("0") && sessionManagement.getPayStack().equalsIgnoreCase("0")) {
//            paypal = true;
//            paystack = false;
//            razorpay1 = false;
//        } else if (sessionManagement.getRazorPay().equalsIgnoreCase("1") && sessionManagement.getPayPal().equalsIgnoreCase("0") && sessionManagement.getPayStack().equalsIgnoreCase("0")) {
//            paypal = false;
//            paystack = false;
//            razorpay1 = true;
//        } else if (sessionManagement.getPayStack().equalsIgnoreCase("1") && sessionManagement.getPayPal().equalsIgnoreCase("0") && sessionManagement.getRazorPay().equalsIgnoreCase("0")) {
//            paypal = false;
//            razorpay1 = false;
//            paystack = true;
//        }
//    }

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
                    orderObject.put("receipt", "orderID" + System.currentTimeMillis());
                    orderObject.put("payment_capture", true);

                    Order orderId = razorpayC.Orders.create(orderObject);
                    if (orderId.has("id")) {
                        Log.i("OrderID", "" + orderId.get("id"));
                        JSONObject options = new JSONObject();

                        options.put("name", name);
                        options.put("description", "Wallet Recharge");
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
                    Toast.makeText(RechargeWallet.this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }).start();
    }

//    public void startPayment(String name, String amount, String email, String phone) {
//        final Activity activity = this;
//        final Checkout co = new Checkout();
//        try {
//            JSONObject options = new JSONObject();
//            options.put("name", name);
//            options.put("description", "Wallet Charges");
//            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
//            options.put("currency", "IDR");
//            options.put("amount", Integer.parseInt(amount) * 100);
//            JSONObject preFill = new JSONObject();
//            preFill.put("email", email);
//            preFill.put("contact", phone);
//
//            options.put("prefill", preFill);
//
//            co.open(activity, options);
//
//        } catch (Exception e) {
//            Toast.makeText(RechargeWallet.this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            show();
//            e.printStackTrace();
//        }
//    }

    private void checkUserPayNotify() {
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
                show();
                if (response.isSuccessful() && response.body() != null) {
                    PaymentVia modelUser = response.body();
                    if (modelUser.getStatus().equalsIgnoreCase("1")) {
                        sessionManagement.setPaymentMethodOpt(modelUser.getData().getRazorpay(), modelUser.getData().getPaypal(), modelUser.getData().getPaystack());
                        enableDisable();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<PaymentVia> call, @NonNull Throwable t) {
                show();
            }
        });

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

    private void getPayment(String totalRs, PayPalConfiguration config) {
        PayPalPayment payment = new PayPalPayment(new BigDecimal(totalRs), "USD", "Shopping Fee", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }


    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")

    public void onPaymentSuccess(String razorpayPaymentID) {
        rechargeWallet(successKey);
        Toast.makeText(this, toatCommonKey, Toast.LENGTH_SHORT).show();
    }

    public void onPaymentError(int i, String s) {
        rechargeWallet(failedKey);
    }

    private void rechargeWallet(String success) {
        final String user_id = sessionManagement.userId();
        if (NetworkConnection.connectionChecking(this)) {
            RequestQueue rq = Volley.newRequestQueue(this);
            StringRequest postReq = new StringRequest(Request.Method.POST, RECHAREGEWALLET,
                    response -> {
                        try {
                            JSONObject object = new JSONObject(response);
                            String message = object.getString("message");
                            Toast.makeText(RechargeWallet.this, "" + message, Toast.LENGTH_LONG).show();
                            if (object.getString("status").equalsIgnoreCase("1")) {
                                Intent intent = new Intent();
                                intent.putExtra("recharge", successKey);
                                setResult(5, intent);
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    finishAndRemoveTask();
                                } else {
                                    finish();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            show();
                        }
                    }, error -> show()) {

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("user_id", user_id);
                    params.put("amount", ammount);
                    params.put("recharge_status", success);
                    return params;
                }
            };
            postReq.setRetryPolicy(new RetryPolicy() {
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
            rq.add(postReq);
        } else {
            Intent intent = new Intent(RechargeWallet.this, NetworkError.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PAYPAL_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                payConfirmation(confirm);
            } else if (resultCode == Activity.RESULT_CANCELED) {
                show();
                Log.i("paymentExample", "The user canceled.");
                Toast.makeText(this, "Wallet Recharge Failed!", Toast.LENGTH_SHORT).show();
                rechargeWallet(failedKey);
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                show();
                Toast.makeText(this, "Wallet Recharge Failed!", Toast.LENGTH_SHORT).show();
                rechargeWallet(failedKey);
            }
        } else if (requestCode == 25 && Objects.requireNonNull(Objects.requireNonNull(data).getStringExtra("result")).equalsIgnoreCase(successKey)) {
            rechargeWallet(successKey);
            Toast.makeText(this, toatCommonKey, Toast.LENGTH_SHORT).show();
        }
    }

    private void payConfirmation(PaymentConfirmation confirm) {
        if (confirm != null) {
            try {
                JSONObject jsonObject = confirm.toJSONObject();
                JSONObject responseJs = jsonObject.getJSONObject("response");
                if (responseJs.getString("state").equalsIgnoreCase("approved")) {
                    rechargeWallet(successKey);
                    Toast.makeText(this, toatCommonKey, Toast.LENGTH_SHORT).show();
                    overridePendingTransition(0, 0);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                show();
            }
        }
    }
}
