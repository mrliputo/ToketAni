package com.tecmanic.gogrocer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.config.BaseURL;
import com.tecmanic.gogrocer.modelclass.FirebaseStatusModel;
import com.tecmanic.gogrocer.modelclass.ForgotEmailModel;
import com.tecmanic.gogrocer.modelclass.VerifyOtp;
import com.tecmanic.gogrocer.network.ApiInterface;
import com.tecmanic.gogrocer.util.AppController;
import com.tecmanic.gogrocer.util.CustomVolleyJsonRequest;
import com.tecmanic.gogrocer.util.SessionManagement;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;


public class ForgotPassOtp extends AppCompatActivity {
    public static final String TAG = "Login";
    EditText etReqMobile;
    CardView cvverify;
    CardView cvEmail;
    EditText etMail;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private SessionManagement sessionManagement;
    private String toastStringkey = "Please try again!";
    private LinearLayout progressBar;
    private boolean fireBaseOtpOn = false;

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
        setContentView(R.layout.activity_forgot_pass_otp);
        etReqMobile = findViewById(R.id.et_req_mobile);
        cvverify = findViewById(R.id.cvverify);
        cvEmail = findViewById(R.id.cv_email);
        etMail = findViewById(R.id.et_mail);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setOnClickListener(view -> Log.i("TAG","not work"));
        sessionManagement = new SessionManagement(this);
        checkOtpStatus();
        if (sessionManagement.getOtpSatus().equalsIgnoreCase("0")) {
            cvEmail.setVisibility(View.VISIBLE);
            cvverify.setVisibility(View.VISIBLE);
        } else {
            cvEmail.setVisibility(View.GONE);
            cvverify.setVisibility(View.VISIBLE);
        }

        cvverify.setEnabled(true);
        cvverify.setOnClickListener(v -> checkNumber(etReqMobile.getText().toString().trim()));
    }

    private void checkNumber(String phoneNumber){
        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);

        Call<VerifyOtp> checkOtpStatus = apiInterface.checkNumIsRegisterOrNot(phoneNumber);
        checkOtpStatus.enqueue(new Callback<VerifyOtp>() {
            @Override
            public void onResponse(@NonNull Call<VerifyOtp> call, @NonNull retrofit2.Response<VerifyOtp> response) {
                if (response.isSuccessful()) {
                    VerifyOtp model = response.body();
                    if (model != null) {
                        boolean otpOn = model.getStatus().equalsIgnoreCase("1");
                        if (otpOn){
                            if (fireBaseOtpOn) {
                                if (etReqMobile.getText().toString().length() != 10 || etReqMobile.getText().toString().contains("+")) {
                                    etReqMobile.setError("Enter valid mobile number");
                                } else {
                                    Intent intent = new Intent(ForgotPassOtp.this, ForgetOtpVerify.class);
                                    intent.putExtra("user_phone", etReqMobile.getText().toString());
                                    intent.putExtra("firebase", true);
                                    startActivity(intent);
                                }

                            } else {
                                if (sessionManagement.getOtpSatus().equalsIgnoreCase("0")) {
                                    setHitService1();
                                } else {
                                    setHitService();
                                }
                            }
                        }else {
                            Toast.makeText(ForgotPassOtp.this, "Number not Register!..", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                cvverify.setEnabled(true);
                show();
            }

            @Override
            public void onFailure(@NonNull Call<VerifyOtp> call, @NonNull Throwable t) {
                cvverify.setEnabled(true);
                show();
                t.printStackTrace();
            }
        });
    }

    private void setHitService1() {
        if (etReqMobile.getText().toString().length() != 10 || etReqMobile.getText().toString().contains("+")) {
            etReqMobile.setError("Enter valid mobile number");
        } else {
            if (etMail.getText().toString().matches(emailPattern) && etMail.getText().toString().length() > 0) {
                show();
                makeotpRequest(etReqMobile.getText().toString(), etMail.getText().toString());
            } else {
                etReqMobile.setError("Enter valid email address!");
            }
        }
    }

    private void setHitService() {
        if (etReqMobile.getText().toString().length() != 10 || etReqMobile.getText().toString().contains("+")) {
            etReqMobile.setError("Enter valid mobile number");
        } else {
            show();
            makeRegisterRequest();

        }
    }

    private void makeRegisterRequest() {
        Map<String, String> params = new HashMap<>();
        params.put("user_phone", etReqMobile.getText().toString());
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.FORGET_PASSWORD, params, response -> {
            try {
                String status = response.getString("status");
                String message = response.getString("message");
                if (status.contains("1")) {
                    cvverify.setEnabled(false);
                    Toast.makeText(ForgotPassOtp.this, message, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ForgotPassOtp.this, ForgetOtpVerify.class);
                    intent.putExtra("user_phone", etReqMobile.getText().toString());
                    intent.putExtra("firebase", false);
                    startActivity(intent);
                } else {
                    cvverify.setEnabled(true);
                    Toast.makeText(ForgotPassOtp.this, message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                show();
            }
        }, error -> {
            show();
            cvverify.setEnabled(true);
        });

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

    private void getFirebaseOtpStatus() {
        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);

        Call<FirebaseStatusModel> checkOtpStatus = apiInterface.getFirebaseOtpStatus();
        checkOtpStatus.enqueue(new Callback<FirebaseStatusModel>() {
            @Override
            public void onResponse(@NonNull Call<FirebaseStatusModel> call, @NonNull retrofit2.Response<FirebaseStatusModel> response) {
                if (response.isSuccessful()) {
                    FirebaseStatusModel model = response.body();
                    if (model != null) {
                        fireBaseOtpOn = model.getData().getStatus().equalsIgnoreCase("1");
                        if (fireBaseOtpOn){
                            cvEmail.setVisibility(View.GONE);
                            cvverify.setVisibility(View.VISIBLE);
                        }else {
                            if (sessionManagement.getOtpSatus().equalsIgnoreCase("0")) {
                                cvEmail.setVisibility(View.VISIBLE);
                                cvverify.setVisibility(View.VISIBLE);
                            } else {
                                cvEmail.setVisibility(View.GONE);
                                cvverify.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
                show();
            }

            @Override
            public void onFailure(@NonNull Call<FirebaseStatusModel> call, @NonNull Throwable t) {
                t.printStackTrace();
                show();
            }
        });

    }

    private void makeotpRequest(String userPhone, String userEmail) {


        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);
        Call<ForgotEmailModel> forgotEmailModelCall = apiInterface.getEmailOtp(userEmail, userPhone);
        forgotEmailModelCall.enqueue(new Callback<ForgotEmailModel>() {
            @Override
            public void onResponse(@NonNull Call<ForgotEmailModel> call, @NonNull retrofit2.Response<ForgotEmailModel> response) {

                if (response.isSuccessful()) {
                    ForgotEmailModel model = response.body();
                    if (model != null) {
                        if (model.getStatus().equalsIgnoreCase("1")) {
                            Toast.makeText(ForgotPassOtp.this, "Email sent successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ForgotPassOtp.this, "" + model.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ForgotPassOtp.this, toastStringkey, Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(ForgotPassOtp.this, toastStringkey, Toast.LENGTH_SHORT).show();
                }

                show();

            }

            @Override
            public void onFailure(@NonNull Call<ForgotEmailModel> call, @NonNull Throwable t) {
                Toast.makeText(ForgotPassOtp.this, toastStringkey, Toast.LENGTH_SHORT).show();
                show();
            }
        });
    }

    private void checkOtpStatus() {
        show();
        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);

        Call<ForgotEmailModel> checkOtpStatus = apiInterface.getOtpOnOffStatus();
        checkOtpStatus.enqueue(new Callback<ForgotEmailModel>() {
            @Override
            public void onResponse(@NonNull Call<ForgotEmailModel> call, @NonNull retrofit2.Response<ForgotEmailModel> response) {
                if (response.isSuccessful()) {
                    ForgotEmailModel model = response.body();
                    if (model != null && model.getStatus().equalsIgnoreCase("0")) {
                        sessionManagement.setOtpStatus("0");
                        cvEmail.setVisibility(View.VISIBLE);
                        cvverify.setVisibility(View.VISIBLE);
                    } else {
                        sessionManagement.setOtpStatus("1");
                        cvEmail.setVisibility(View.GONE);
                        cvverify.setVisibility(View.VISIBLE);
                    }

                }
                getFirebaseOtpStatus();
            }

            @Override
            public void onFailure(@NonNull Call<ForgotEmailModel> call, @NonNull Throwable t) {
                getFirebaseOtpStatus();
            }
        });

    }
}