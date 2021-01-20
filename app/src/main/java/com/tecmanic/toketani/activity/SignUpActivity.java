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
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.tecmanic.toketani.R;
import com.tecmanic.toketani.config.BaseURL;
import com.tecmanic.toketani.modelclass.FirebaseStatusModel;
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

import static com.tecmanic.toketani.config.BaseURL.SIGN_UP;

public class SignUpActivity extends AppCompatActivity {

    EditText etName;
    EditText etPhone;
    EditText etEmail;
    EditText etPAss;
    Button btnSignUP;
    TextView btnLogin;
    String emailPattern;
    String token;
    LinearLayout skip;
    LinearLayout flagView;
    TextView countryC;
    private SessionManagement sessionManagement;
    private String countryCode = "";
    private LinearLayout progressBar;
    private boolean fireBaseOtpOn = false;
    private String userNameKey = "user_name";
    private String userIdKey = "user_id";
    private String userEmailKey = "user_email";
    private String userNumberkey = "user_phone";
    private String userPaswordKey = "user_password";

    private void show() {
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
        setContentView(R.layout.activity_sign_up);
        progressBar = findViewById(R.id.progress_bar);
        show();
        sessionManagement = new SessionManagement(SignUpActivity.this);
        FirebaseApp.initializeApp(this);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        token = task.getResult().getToken();
                    } else {
                        token = "";
                    }

                });
        emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        etName = findViewById(R.id.etName);
        flagView = findViewById(R.id.flag_view);
        countryC = findViewById(R.id.country_c);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etPAss = findViewById(R.id.etPass);
        skip = findViewById(R.id.skip);
        btnSignUP = findViewById(R.id.btnSignUP);
        btnLogin = findViewById(R.id.btn_Login);
        init();
        getFirebaseOtpStatus();

    }

    private void init() {

        skip.setOnClickListener(v -> {
            sessionManagement.createLoginSession("", "", "", "", "", true);
            finish();
        });

        flagView.setOnClickListener(view -> startActivityForResult(new Intent(SignUpActivity.this, FlagActivity.class), 15));

        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finishAffinity();
        });

        btnSignUP.setOnClickListener(v -> {
            if (etName.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Full name required!", Toast.LENGTH_SHORT).show();
            } else if (etEmail.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Email id required!", Toast.LENGTH_SHORT).show();
            } else if (!etEmail.getText().toString().trim().matches(emailPattern)) {
                Toast.makeText(getApplicationContext(), "Valid Email id required!", Toast.LENGTH_SHORT).show();
            } else if (etPhone.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Mobile Number required!", Toast.LENGTH_SHORT).show();
            } else if (etPhone.getText().toString().trim().length() < 9) {
                Toast.makeText(getApplicationContext(), "Valid Mobile Number required!", Toast.LENGTH_SHORT).show();
            } else if (etPAss.getText().toString().trim().equalsIgnoreCase("")) {
                Toast.makeText(getApplicationContext(), "Password required!", Toast.LENGTH_SHORT).show();
            } else if (!isOnline()) {
                Toast.makeText(getApplicationContext(), "Please check your Internet Connection!", Toast.LENGTH_SHORT).show();
            } else {
                show();
                signUpUrl();

            }
        });

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

    private void signUpUrl() {

        if (token != null && !token.equalsIgnoreCase("")) {
            System.out.println("disini hit service");
            hitService();
        } else {
            System.out.println("disini hit tidak service");
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult() != null) {
                            token = task.getResult().getToken();
                            signUpUrl();
                        } else {
                            token = "";
                        }
                    });
        }


    }

    private void hitService() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SIGN_UP, response -> {
            try {
                System.out.println("disinii : "+ response);
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                String msg = jsonObject.getString("message");

                if (status.equalsIgnoreCase("1")) {
                    JSONObject resultObj = jsonObject.getJSONObject("data");

                    String userName1 = resultObj.getString(userNameKey);
                    String id = resultObj.getString(userIdKey);
                    String userEmail1 = resultObj.getString(userEmailKey);
                    String userPhone1 = resultObj.getString(userNumberkey);
                    String password = resultObj.getString(userPaswordKey);
                    String otpValue = resultObj.getString("otp_value");
                    String block = resultObj.getString("block");
                    sessionManagement.createLoginSession(id, userEmail1, userName1, userPhone1, password, false, false);
                    sessionManagement.setOtp(otpValue);
                    sessionManagement.setOtpStatus("1");
                    sessionManagement.setUserBlockStatus(block);

                    if (fireBaseOtpOn) {
                        Intent intent = new Intent(getApplicationContext(), FireOtpPageAuthentication.class);
                        intent.putExtra("MobNo", etPhone.getText().toString());
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), OtpVerification.class);
                        intent.putExtra("MobNo", etPhone.getText().toString());
                        startActivity(intent);
                        finish();
                    }

                } else if (status.equalsIgnoreCase("2")) {
                    JSONObject resultObj = jsonObject.getJSONObject("data");
                    String userName = resultObj.getString(userNameKey);
                    String id = resultObj.getString(userIdKey);
                    String userEmail = resultObj.getString(userEmailKey);
                    String userPhone = resultObj.getString(userNumberkey);
                    String password = resultObj.getString(userPaswordKey);
                    String block = resultObj.getString("block");

                    if (fireBaseOtpOn) {
                        sessionManagement.createLoginSession(id, userEmail, userName, userPhone, password, false, false);
                        sessionManagement.setUserBlockStatus(block);
                        sessionManagement.setOtpStatus("0");
                        Intent intent = new Intent(getApplicationContext(), FireOtpPageAuthentication.class);
                        intent.putExtra("MobNo", etPhone.getText().toString());
                        startActivity(intent);
                        finish();
                    } else {
                        sessionManagement.createLoginSession(id, userEmail, userName, userPhone, password);
                        sessionManagement.setUserBlockStatus(block);
                        sessionManagement.setOtpStatus("0");
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }

                } else {
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
                String cc = countryCode.replace("+", "");
                param.put(userNameKey, etName.getText().toString());
                param.put(userNumberkey, cc + "" + etPhone.getText().toString());
                param.put(userEmailKey, etEmail.getText().toString());
                param.put(userPaswordKey, etPAss.getText().toString());
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
