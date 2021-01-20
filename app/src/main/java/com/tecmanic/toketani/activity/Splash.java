package com.tecmanic.toketani.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tecmanic.toketani.R;
import com.tecmanic.toketani.config.BaseURL;
import com.tecmanic.toketani.util.GpsUtils;
import com.tecmanic.toketani.util.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.tecmanic.toketani.config.BaseURL.USERBLOCKAPI;

public class Splash extends AppCompatActivity {
    SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setFinishOnTouchOutside(true);
        sessionManagement = new SessionManagement(Splash.this);
//        sessionManagement.setLocationPref(String.valueOf(29.010260903193778), String.valueOf(77.06049536229332));
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(this)) {
            Toast.makeText(getApplicationContext(), "Gps already enabled", Toast.LENGTH_SHORT).show();
            redirectionScreen();
        } else {
            if (!hasGPSDevice(this)) {
                Toast.makeText(getApplicationContext(), "Gps not Supported", Toast.LENGTH_SHORT).show();
                finish();
            }
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(this)) {
                Toast.makeText(getApplicationContext(), "Gps not enabled", Toast.LENGTH_SHORT).show();
                new GpsUtils(this).turnGPSOn(isGPSEnable -> {
                    if (isGPSEnable) {
                        redirectionScreen();
                    }
                });
            }
        }
        fetchBlockStatus();
    }

    private void fetchBlockStatus() {

        if (!sessionManagement.userId().equalsIgnoreCase("")) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, USERBLOCKAPI, response -> {
                Log.d("adresssHoww", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equals("2")) {
                        sessionManagement.setUserBlockStatus("2");
                    } else {
                        sessionManagement.setUserBlockStatus("1");
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    }
                    Toast.makeText(Splash.this, "" + msg, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }, error -> {
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("user_id", sessionManagement.userId());
                    return param;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(Splash.this);
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);
        }

    }

    private void redirectionScreen() {
        ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();
        Runnable runnable = () -> {
            if (sessionManagement.isLoggedIn()) {
                Intent intent1 = new Intent(Splash.this, MainActivity.class);
                startActivity(intent1);
                finish();
            } else {
                if (sessionManagement.isSkip()) {
                    Intent intent1 = new Intent(Splash.this, MainActivity.class);
                    startActivity(intent1);
                    finish();
                } else {
                    Intent intent1 = new Intent(Splash.this, LoginActivity.class);
                    startActivity(intent1);
                    finish();
                }
            }
        };
        worker.schedule(runnable, 3, TimeUnit.SECONDS);

    }


    private boolean hasGPSDevice(Context context) {
        final LocationManager mgr = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        if (mgr == null)
            return false;
        final List<String> providers = mgr.getAllProviders();
        return providers.contains(LocationManager.GPS_PROVIDER);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BaseURL.GPS_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                redirectionScreen();
            } else {
                finish();
            }
        }
    }
}

