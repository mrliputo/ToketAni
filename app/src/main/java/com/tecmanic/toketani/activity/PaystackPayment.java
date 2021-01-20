package com.tecmanic.toketani.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputLayout;
import com.tecmanic.toketani.R;
import com.tecmanic.toketani.config.BaseURL;
import com.tecmanic.toketani.util.AppController;
import com.tecmanic.toketani.util.CustomVolleyJsonRequest;
import com.tecmanic.toketani.util.DatabaseHandler;
import com.tecmanic.toketani.util.SessionManagement;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import co.paystack.android.Paystack;
import co.paystack.android.PaystackSdk;
import co.paystack.android.Transaction;
import co.paystack.android.model.Card;
import co.paystack.android.model.Charge;

public class PaystackPayment extends AppCompatActivity {
    SharedPreferences delivercharge;
    private SessionManagement sessionManagement;
    private String getWalletAmmount = "";
    private String getuserId = "";
    private LinearLayout progressBar;
    private TextInputLayout mCardNumber;
    private TextInputLayout mCardExpiry;
    private TextInputLayout mCardCVV;
    private String email = "";
    private String activity = "";
    private DatabaseHandler dbCart;
    private String cartId = "";
    private String paymentMethod = "";
    private String walletStatus = "";
    private String walletKey = "wallet";

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
        setContentView(R.layout.activity_paystack_payment);
        progressBar = findViewById(R.id.progress_bar);
        mCardNumber = findViewById(R.id.til_card_number);
        mCardExpiry = findViewById(R.id.til_card_expiry);
        mCardCVV = findViewById(R.id.til_card_cvv);
        Button makePayment = findViewById(R.id.btn_make_payment);
        dbCart = new DatabaseHandler(this);
        sessionManagement = new SessionManagement(PaystackPayment.this);
        delivercharge = getSharedPreferences("charges", 0);

        email = sessionManagement.getUserDetails().get(BaseURL.KEY_EMAIL);
        activity = getIntent().getStringExtra("activity");
        getWalletAmmount = getIntent().getStringExtra("total_amount");
        getuserId = getIntent().getStringExtra("getuser_id");
        cartId = getIntent().getStringExtra("cart_id");
        paymentMethod = getIntent().getStringExtra("payment_method");
        walletStatus = getIntent().getStringExtra("wallet_status");

        Objects.requireNonNull(mCardExpiry.getEditText()).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 2 && !s.toString().contains("/")) {
                    s.append("/");
                }
            }
        });

        initializePaystack();
        makePayment.setOnClickListener(view -> performCharge());
    }

    private void initializePaystack() {
        PaystackSdk.initialize(PaystackPayment.this);
        PaystackSdk.setPublicKey("pk_test_f0269be01832feda8b9cce63a261770ecd249f77");
    }

    public void performCharge() {
        int priceRs1 = (Integer.parseInt(getWalletAmmount)*100);
        String cardNumber = mCardNumber.getEditText().getText().toString();
        String cardExpiry = mCardExpiry.getEditText().getText().toString();
        String cvv = mCardCVV.getEditText().getText().toString();

        String[] cardExpiryArray = cardExpiry.split("/");
        int expiryMonth = Integer.parseInt(cardExpiryArray[0]);
        int expiryYear = Integer.parseInt(cardExpiryArray[1]);


        Card card = new Card(cardNumber, expiryMonth, expiryYear, cvv);

        Charge charge = new Charge();
        charge.setAmount(priceRs1);
        charge.setEmail(email);
        charge.setCurrency(sessionManagement.getCurrency());
        charge.setCard(card);

        PaystackSdk.chargeCard(this, charge, new Paystack.TransactionCallback() {
            @Override
            public void onSuccess(Transaction transaction) {
                parseResponse();
            }

            @Override
            public void beforeValidate(Transaction transaction) {
            }

            @Override
            public void onError(Throwable error, Transaction transaction) {
            }

        });
    }

    private void parseResponse() {
        if (activity.equalsIgnoreCase(walletKey)) {
            Intent intent = new Intent();
            intent.putExtra("result", "success");
            setResult(25, intent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask();
            } else {
                finish();
            }
        } else if (activity.equalsIgnoreCase("non_wallet")) {
            makeAddOrderRequest(getuserId, cartId, paymentMethod, walletStatus, "success");
        }

    }


    @Override
    public void onBackPressed() {
        if (activity.equalsIgnoreCase(walletKey)) {
            Intent intent = new Intent();
            intent.putExtra("result", "false");
            setResult(25, intent);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            finish();
        }

    }

    private void makeAddOrderRequest(String userid, String cartId1, String paymentMethod1, String walletStatus1, String paymentStatus1) {
        show();
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userid);
        params.put("payment_status", paymentStatus1);
        params.put("cart_id", cartId1);
        params.put("payment_method", paymentMethod1);
        params.put(walletKey, walletStatus1);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.ADD_ORDER_URL, params, response -> {
            try {
                String status = response.getString("status");
                String message = response.getString("message");
                if (status.equalsIgnoreCase("1") || status.equalsIgnoreCase("2")) {
                    sessionManagement.setCartID("");
                    dbCart.clearCart();
                    Intent intent = new Intent(getApplicationContext(), OrderSuccessful.class);
                    intent.putExtra("msg", message);
                    startActivity(intent);
                    Toast.makeText(PaystackPayment.this, "" + message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PaystackPayment.this, "" + message, Toast.LENGTH_SHORT).show();
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

}