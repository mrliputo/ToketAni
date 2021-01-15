package com.tecmanic.gogrocer.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.config.BaseURL;
import com.tecmanic.gogrocer.modelclass.CountryCodeModel;
import com.tecmanic.gogrocer.modelclass.ForgotEmailModel;
import com.tecmanic.gogrocer.modelclass.MapSelectionModel;
import com.tecmanic.gogrocer.modelclass.NotifyModelUser;
import com.tecmanic.gogrocer.network.ApiInterface;
import com.tecmanic.gogrocer.util.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static com.tecmanic.gogrocer.config.BaseURL.LOGIN;

public class LoginActivity extends AppCompatActivity {

    Button signIn;
    TextView forgotPAss;
    TextView btnignUp;
    EditText etMob;
    EditText etPass;
    String token;
    LinearLayout skip;
    LinearLayout flagView;
    TextView countryC;
    private SessionManagement sessionManagement;
    private LinearLayout progressBar;

    private void show() {
//        progressBar.setVisibility(View.GONE);
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(LoginActivity.this);
        setContentView(R.layout.activity_login);
        progressBar = findViewById(R.id.progress_bar);
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
//                    if (!task.isSuccessful()) {
//                        token = "";
//                        return;
//                    }
                    if(task.isSuccessful() && task.getResult()!=null){
                        token = task.getResult().getToken();
                    }

                });
        sessionManagement = new SessionManagement(LoginActivity.this);
        new Thread(this::checkUserNotify).start();
        init();
    }

    private void init() {
        etMob = findViewById(R.id.etMob);
        etPass = findViewById(R.id.etPass);
        signIn = findViewById(R.id.btn_Login);
        flagView = findViewById(R.id.flag_view);
        countryC = findViewById(R.id.country_c);
        forgotPAss = findViewById(R.id.btn_ForgotPass);
        btnignUp = findViewById(R.id.btn_Signup);
        skip = findViewById(R.id.skip);
        checkOtpStatus();

        flagView.setOnClickListener(view -> startActivityForResult(new Intent(LoginActivity.this, FlagActivity.class), 15));

        skip.setOnClickListener(v -> {
            sessionManagement.createLoginSession("", "", "", "", "", true);
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        forgotPAss.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPassOtp.class);
            startActivity(intent);

        });
        btnignUp.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
            startActivity(intent);
            finish();
        });

        signIn.setOnClickListener(v -> {
            if (etMob.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Mobile Number required!", Toast.LENGTH_SHORT).show();
            } else if (etMob.getText().toString().trim().length() < 9) {
                Toast.makeText(getApplicationContext(), "Valid Mobile Number required!", Toast.LENGTH_SHORT).show();
            } else if (etPass.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Password required!", Toast.LENGTH_SHORT).show();
            } else if (!isOnline()) {
                Toast.makeText(getApplicationContext(), "Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
            } else {
                System.out.println("disini 139");
                show();
                loginUrl();
            }
        });
    }

    private void loginUrl() {

        if (token != null && !token.equalsIgnoreCase("")) {
            hitService();
        } else {
            FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            token = task.getResult().getToken();
                            loginUrl();
                        } else {
                            token = "";
                        }
                    });
        }

    }

    private void hitService() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN, response -> {
            try {
                System.out.println(response);
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                String msg = jsonObject.getString("message");
                if (status.equals("1")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject obj = jsonArray.getJSONObject(i);
                        String userId = obj.getString("user_id");
                        String userFullname = obj.getString("user_name");
                        String userEmail = obj.getString("user_email");
                        String userPhone = obj.getString("user_phone");
                        String password = obj.getString("user_password");
                        String block = obj.getString("block");
                        SharedPreferences.Editor editor = getSharedPreferences(BaseURL.MY_PREPRENCE, MODE_PRIVATE).edit();
                        editor.putString(BaseURL.KEY_MOBILE, userPhone);
                        editor.putString(BaseURL.KEY_PASSWORD, password);
                        editor.apply();
                        sessionManagement.createLoginSession(userId, userEmail, userFullname, userPhone, password);
                        sessionManagement.setUserBlockStatus(block);
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                        finish();

                    }
                } else {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                System.out.println("disini 201");
                show();
            }
        }, error -> show()) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("user_phone", etMob.getText().toString());
                param.put("user_password", etPass.getText().toString());
                param.put("device_id", token);
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

    private void checkOtpStatus() {
        System.out.println("disini 238");
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
                    if (model != null) {
                        if (model.getStatus().equalsIgnoreCase("0")) {
                            sessionManagement.setOtpStatus("0");
                        } else {
                            sessionManagement.setOtpStatus("1");
                        }
                    }

                }
                checkMapSelection();
            }

            @Override
            public void onFailure(@NonNull Call<ForgotEmailModel> call, @NonNull Throwable t) {
                checkMapSelection();
            }
        });

    }


    private void checkMapSelection() {
        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL_MAP)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);

        Call<MapSelectionModel> checkOtpStatus = apiInterface.getMapSelectionStatus();
        checkOtpStatus.enqueue(new Callback<MapSelectionModel>() {
            @Override
            public void onResponse(@NonNull Call<MapSelectionModel> call, @NonNull retrofit2.Response<MapSelectionModel> response) {
                if (response.isSuccessful()) {
                    MapSelectionModel model = response.body();
                    if (model != null) {
                        if (model.getData().getMapbox().equalsIgnoreCase("1")) {
                            sessionManagement.setMapSelection("mapbox");
                        } else if (model.getData().getGoogleMap().equalsIgnoreCase("1")) {
                            sessionManagement.setMapSelection("googlemap");
                        }

                    }
                }
                getCountryCode();
            }

            @Override
            public void onFailure(@NonNull Call<MapSelectionModel> call, @NonNull Throwable t) {
                getCountryCode();
            }
        });

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
                        } else {
                            sessionManagement.setCountryCode("");
                        }
                    }

                }
                System.out.println("disini 332");
                show();
            }

            @Override
            public void onFailure(@NonNull Call<CountryCodeModel> call, @NonNull Throwable t) {
                System.out.println("disini 337");
                show();
            }
        });

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

    private void checkUserNotify() {
        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);

        Call<NotifyModelUser> checkOtpStatus = apiInterface.getNotifyUser(sessionManagement.userId());

        checkOtpStatus.enqueue(new Callback<NotifyModelUser>() {
            @Override
            public void onResponse(@NonNull Call<NotifyModelUser> call, @NonNull Response<NotifyModelUser> response) {

                if (response.isSuccessful() && response.body() != null) {
                    NotifyModelUser modelUser = response.body();
                    if (modelUser.getStatus().equalsIgnoreCase("1")) {
                        sessionManagement.setEmailServer(modelUser.getData().getEmail());
                        sessionManagement.setUserSMSService(modelUser.getData().getSms());
                        sessionManagement.setUserInAppService(modelUser.getData().getApp());
                    }
                }

            }

            @Override
            public void onFailure(@NonNull Call<NotifyModelUser> call, @NonNull Throwable t) {

            }
        });

    }
}
