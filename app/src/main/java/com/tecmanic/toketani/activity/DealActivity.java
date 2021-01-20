package com.tecmanic.toketani.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tecmanic.toketani.R;
import com.tecmanic.toketani.adapters.DealAdapter;
import com.tecmanic.toketani.modelclass.NewCartModel;
import com.tecmanic.toketani.util.DatabaseHandler;
import com.tecmanic.toketani.util.SessionManagement;
import com.tecmanic.toketani.util.ViewNotifier;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tecmanic.toketani.config.BaseURL.HOME_DEAL;

public class DealActivity extends AppCompatActivity {

    private ShimmerRecyclerView rvTopSelling;
    private List<NewCartModel> dealList = new ArrayList<>();
    private DealAdapter dealAdapter;
    private SessionManagement sessionManagement;
    private boolean invalue = false;
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
        setContentView(R.layout.activity_deal);
        sessionManagement = new SessionManagement(DealActivity.this);
        rvTopSelling = findViewById(R.id.recyclerTopSelling);
        TextView continueTocart;
        try (DatabaseHandler dbcart = new DatabaseHandler(DealActivity.this)) {
            LinearLayout bottomLayTotal = findViewById(R.id.bottom_lay_total);
            TextView totalPrice = findViewById(R.id.total_price);
            progressBar = findViewById(R.id.progress_bar);
            ImageView backBtn = findViewById(R.id.back_btn);
            TextView totalCount = findViewById(R.id.total_count);
            continueTocart = findViewById(R.id.continue_tocart);

            if (dbcart.getCartCount() > 0) {
                bottomLayTotal.setVisibility(View.VISIBLE);
                totalPrice.setText(sessionManagement.getCurrency() + " " + dbcart.getTotalAmount());
                totalCount.setText("Total Items (" + dbcart.getCartCount() + ")");
            } else {
                bottomLayTotal.setVisibility(View.GONE);
            }
            backBtn.setOnClickListener(v -> {
                invalue = false;
                dbcart.close();
                onBackPressed();
            });

            if (isOnline()) {
                show();
                dealUrl(dbcart,bottomLayTotal,totalCount,totalPrice);
            }
        }

        continueTocart.setOnClickListener(v -> {
            invalue = true;
            onBackPressed();
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

    private void dealUrl(DatabaseHandler dbcart, LinearLayout bottomLayTotal, TextView totalCount, TextView totalPrice) {
        dealList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, HOME_DEAL, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                String message = jsonObject.getString("message");
                if (status.equals("1")) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<NewCartModel>>() {
                    }.getType();
                    List<NewCartModel> listorl = gson.fromJson(jsonObject.getString("data"), listType);
                    dealList.addAll(listorl);

                    dealAdapter = new DealAdapter(DealActivity.this, dealList, new ViewNotifier() {
                        @Override
                        public void onViewNotify() {
                            if (dbcart.getCartCount() > 0) {
                                bottomLayTotal.setVisibility(View.VISIBLE);
                                totalPrice.setText(sessionManagement.getCurrency() + " " + dbcart.getTotalAmount());
                                totalCount.setText("Total Items (" + dbcart.getCartCount() + ")");
                            } else {
                                bottomLayTotal.setVisibility(View.GONE);
                            }
                        }
                        @Override
                        public void onProductDetailClick(int position) {

                            Intent intent = new Intent(DealActivity.this, ProductDetails.class);
                            intent.putExtra("sId", dealList.get(position).getProductId());
                            intent.putExtra("sName", dealList.get(position).getProductName());
                            intent.putExtra("descrip", dealList.get(position).getDescription());
                            intent.putExtra("price", dealList.get(position).getPrice());
                            intent.putExtra("mrp", dealList.get(position).getMrp());
                            intent.putExtra("unit", dealList.get(position).getUnit());
                            intent.putExtra("stock", dealList.get(position).getStock());
                            intent.putExtra("qty", dealList.get(position).getQuantity());
                            intent.putExtra("image", dealList.get(position).getProductImage());
                            intent.putExtra("sVariant_id", dealList.get(position).getVarientId());
                            startActivityForResult(intent,21);

                        }
                    });
                    rvTopSelling.setLayoutManager(new LinearLayoutManager(DealActivity.this, RecyclerView.VERTICAL, false));
                    rvTopSelling.setAdapter(dealAdapter);
                    dealAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(DealActivity.this, message, Toast.LENGTH_SHORT).show();
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
                params.put("lat", sessionManagement.getLatPref());
                params.put("lng", sessionManagement.getLangPref());
                params.put("city", sessionManagement.getLocationCity());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(DealActivity.this);
        requestQueue.getCache().clear();
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 60000;
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
        requestQueue.add(stringRequest);

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("carttogo", invalue);
        setResult(56, intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 21){
            if (dealAdapter!=null){
                dealAdapter.notifyDataSetChanged();
            }
        }
    }
}
