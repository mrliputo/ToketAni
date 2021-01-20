package com.tecmanic.toketani.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tecmanic.toketani.R;
import com.tecmanic.toketani.config.BaseURL;
import com.tecmanic.toketani.modelclass.CountryCodeModel;
import com.tecmanic.toketani.network.ApiInterface;
import com.tecmanic.toketani.util.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static com.tecmanic.toketani.config.BaseURL.SIGN_UP_OTP;

public class OtpVerification extends AppCompatActivity {

    Button verify;
    TextView txtMobile;
    TextView countryCode;
    LinearLayout llEdit;
    EditText etOtp;
    String mobileNO;
    private SessionManagement sessionManagement;
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
        setContentView(R.layout.activity_otp_verification);
        progressBar = findViewById(R.id.progress_bar);
        countryCode = findViewById(R.id.country_code);
        show();
        sessionManagement = new SessionManagement(OtpVerification.this);
        init();


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
                            countryCode.setText("+" + model.getData().getCountryCode());

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

    private void init() {
        txtMobile = findViewById(R.id.txt_mobile);
        etOtp = findViewById(R.id.et_otp);

        verify = findViewById(R.id.btnVerify);
        llEdit = findViewById(R.id.ll_edit);

        mobileNO = getIntent().getStringExtra("MobNo");
        txtMobile.setText(mobileNO);

        verify.setOnClickListener(v -> {
            if (etOtp.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "OTP required!", Toast.LENGTH_SHORT).show();
            } else if (!isOnline()) {
                Toast.makeText(getApplicationContext(), "Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
            } else {
                show();
                otpUrl();
            }
        });
        llEdit.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
            finish();
        });

        getCountryCode();

    }

    private void otpUrl() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SIGN_UP_OTP, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("1")) {
                    String msg = jsonObject.getString("message");
                    sessionManagement.setLogin(true);
                    Toast.makeText(getApplicationContext(), msg + "", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    sessionManagement.setLogin(false);
                    String msg = jsonObject.getString("message");
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                show();
            }

        }, error -> show()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("user_phone", mobileNO);
                param.put("otp", etOtp.getText().toString());
                return param;
            }
        };

        stringRequest.setRetryPolicy(new RetryPolicy() {
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
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Network nw = connectivityManager.getActiveNetwork();
            if (nw == null) return false;
            NetworkCapabilities actNw = connectivityManager.getNetworkCapabilities(nw);
            return actNw != null && (actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) || actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH));
        } else {
            return true;
        }
    }
}
