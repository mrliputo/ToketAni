package com.tecmanic.toketani.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.tecmanic.toketani.R;
import com.tecmanic.toketani.config.BaseURL;
import com.tecmanic.toketani.modelclass.CountryCodeModel;
import com.tecmanic.toketani.modelclass.VerifyOtp;
import com.tecmanic.toketani.network.ApiInterface;
import com.tecmanic.toketani.util.AppController;
import com.tecmanic.toketani.util.CustomVolleyJsonRequest;
import com.tecmanic.toketani.util.SessionManagement;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class ForgetOtpVerify extends AppCompatActivity {
    public static final String TAG = "Otp";
    CardView submit;
    EditText edtotp;
    TextView number;
    String getuserphone;
    private String userPhoneKey = "user_phone";
    private LinearLayout progressBar;
    private FirebaseAuth firebaseAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks changedCallbacks;
    private String mVerificationId = "";
    private SessionManagement sessionManagement;
    private boolean getFirebasestatus = false;

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
        FirebaseApp.initializeApp(ForgetOtpVerify.this);
        firebaseAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.activity_forget_otp_verify);
        sessionManagement = new SessionManagement(ForgetOtpVerify.this);
        progressBar = findViewById(R.id.progress_bar);
        edtotp = findViewById(R.id.et_otp);
        number = findViewById(R.id.txnm);
        submit = findViewById(R.id.cvLogin);
        getuserphone = getIntent().getStringExtra(userPhoneKey);
        String userFirebaseKey = "firebase";
       getFirebasestatus = getIntent().getBooleanExtra(userFirebaseKey, false);

        if (getFirebasestatus) {
            setCallback();
        }
        number.setText(getuserphone);
        submit.setOnClickListener(v -> {
            if (getFirebasestatus) {
                if (edtotp.getText().toString().length() == 0) {
                    Toast.makeText(ForgetOtpVerify.this, "Enter Otp", Toast.LENGTH_SHORT).show();
                } else {
                    show();
                    onCodeSents(edtotp.getText().toString().trim());
                }
            } else {
                if (edtotp.getText().toString().length() == 0) {
                    Toast.makeText(ForgetOtpVerify.this, "Enter Otp", Toast.LENGTH_SHORT).show();
                } else {
                    show();
                    setotpverify();
                }
            }

        });

        getCountryCode();
    }

    private void setCallback() {
        changedCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                String code = phoneAuthCredential.getSmsCode();
                if (code != null) {
                    progressBar.setVisibility(View.VISIBLE);
                    edtotp.setText(code);
                    onCodeSents(code);
                }
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(ForgetOtpVerify.this, "your verification failed!", Toast.LENGTH_SHORT).show();
                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                }
                e.printStackTrace();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                if (progressBar.getVisibility() == View.VISIBLE){
                    progressBar.setVisibility(View.GONE);
                }
                mVerificationId = s;
            }
        };
    }

    private void getCountryCode() {
        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);

        Call<CountryCodeModel> checkOtpStatus = apiInterface.getCountryCode();
        checkOtpStatus.enqueue(new Callback<CountryCodeModel>() {
            @Override
            public void onResponse(@NonNull Call<CountryCodeModel> call, @NonNull retrofit2.Response<CountryCodeModel> response) {
                if (response.isSuccessful()) {
                    CountryCodeModel model = response.body();
                    if (model != null) {
                        if (model.getStatus().equalsIgnoreCase("1")) {
                            sessionManagement.setCountryCode(model.getData().getCountryCode());
                            if (getFirebasestatus){
                                PhoneAuthProvider.getInstance(firebaseAuth).verifyPhoneNumber(
                                        "+" + sessionManagement.getCountryCode() + getuserphone,
                                        60,
                                        TimeUnit.SECONDS,
                                        ForgetOtpVerify.this,
                                        changedCallbacks);
                                firebaseAuth.setLanguageCode(Locale.getDefault().getLanguage());
                            }
                        } else {
                            sessionManagement.setCountryCode("");
                        }
                    }

                }
                show();
            }

            @Override
            public void onFailure(@NonNull Call<CountryCodeModel> call, @NonNull Throwable t) {
                t.printStackTrace();
                show();
            }
        });

    }

    private void onCodeSents(String code) {
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(phoneAuthCredential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(this, task -> {
            try {
                if (task.isSuccessful()) {
                    updateStatusLogin("success", getuserphone);
                } else {
                    updateStatusLogin("failed", getuserphone);
                    Toast.makeText(ForgetOtpVerify.this, "Verification failed!", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                FirebaseAuth.getInstance().signOut();
                e.printStackTrace();
            }
        });
    }


    private void updateStatusLogin(String status, String userNumber) {
        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);

        Call<VerifyOtp> checkOtpStatus = apiInterface.getOtpVerifiyStatus(status, userNumber);
        checkOtpStatus.enqueue(new Callback<VerifyOtp>() {
            @Override
            public void onResponse(@NonNull Call<VerifyOtp> call, @NonNull retrofit2.Response<VerifyOtp> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getStatus().equalsIgnoreCase("1") && status.equalsIgnoreCase("success")) {
                    Intent intent = new Intent(ForgetOtpVerify.this, NewPassword.class);
                    intent.putExtra(userPhoneKey, getuserphone);
                    startActivity(intent);
                }
                FirebaseAuth.getInstance().signOut();
                show();
            }

            @Override
            public void onFailure(@NonNull Call<VerifyOtp> call, @NonNull Throwable t) {
                FirebaseAuth.getInstance().signOut();
                show();
                t.printStackTrace();
            }
        });

    }

    public void setotpverify() {
        Map<String, String> params = new HashMap<>();
        params.put(userPhoneKey, getuserphone);
        params.put("otp", edtotp.getText().toString());
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.VERIFY_OTP, params, response -> {
            try {
                String status = response.getString("status");
                String message = response.getString("message");
                if (status.contains("1")) {
                    Intent intent = new Intent(ForgetOtpVerify.this, NewPassword.class);
                    intent.putExtra(userPhoneKey, getuserphone);
                    startActivity(intent);
                } else {
                    Toast.makeText(ForgetOtpVerify.this, message, Toast.LENGTH_SHORT).show();
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