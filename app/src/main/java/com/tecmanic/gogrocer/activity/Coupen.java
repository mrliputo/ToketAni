package com.tecmanic.gogrocer.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.adapters.CoupunListingAdapter;
import com.tecmanic.gogrocer.config.BaseURL;
import com.tecmanic.gogrocer.modelclass.CoupunModel;
import com.tecmanic.gogrocer.util.AppController;
import com.tecmanic.gogrocer.util.CustomVolleyJsonRequest;
import com.tecmanic.gogrocer.util.SessionManagement;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Coupen extends AppCompatActivity {

    List<CoupunModel> coupunModelList = new ArrayList<>();
    RecyclerView coupenRecycler;
    String cartId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupen);

        SessionManagement sessionManagement = new SessionManagement(Coupen.this);
        cartId = sessionManagement.getCartId();
        coupenRecycler = findViewById(R.id.recycleer_coupen);
        coupenRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        CoupunListingAdapter complainAdapter = new CoupunListingAdapter(coupunModelList, couponCode -> {
            Intent backResult = new Intent();
            backResult.putExtra("code", couponCode);
            setResult(2, backResult);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask();
            } else {
                finish();
            }
        });
        coupenRecycler.setAdapter(complainAdapter);
        couponCode(complainAdapter);
    }

    private void couponCode(CoupunListingAdapter complainAdapter) {
        coupunModelList.clear();
        Map<String, String> params = new HashMap<>();
        params.put("cart_id", cartId);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.COUPON_CODE, params, response -> {
            try {

                String status = response.getString("status");
                String message = "";
                if (response.has("message")) {
                    message = response.getString("message");
                }

                if (status.equalsIgnoreCase("1")) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<CoupunModel>>() {
                    }.getType();

                    coupunModelList = gson.fromJson(response.getString("data"), listType);
                    complainAdapter.setList(coupunModelList);
                    complainAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(Coupen.this, "" + message, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            VolleyLog.d("TAG", "Error: " + error.getMessage());
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                Toast.makeText(Coupen.this, "check your internet connection!", Toast.LENGTH_SHORT).show();
            }
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

    @Override
    public void onBackPressed() {
        Intent backResult = new Intent();
        backResult.putExtra("code", "");
        setResult(2, backResult);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }
}
