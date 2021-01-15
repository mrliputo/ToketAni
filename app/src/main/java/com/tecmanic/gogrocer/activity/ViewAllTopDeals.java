package com.tecmanic.gogrocer.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.adapters.ViewAllAdapter;
import com.tecmanic.gogrocer.modelclass.NewCartModel;
import com.tecmanic.gogrocer.util.DatabaseHandler;
import com.tecmanic.gogrocer.util.SessionManagement;
import com.tecmanic.gogrocer.util.ViewNotifier;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.tecmanic.gogrocer.config.BaseURL.HOME_DEAL;
import static com.tecmanic.gogrocer.config.BaseURL.HOME_RECENT;
import static com.tecmanic.gogrocer.config.BaseURL.HOME_TOP_SELLING;
import static com.tecmanic.gogrocer.config.BaseURL.WHATSNEW;

public class ViewAllTopDeals extends AppCompatActivity {
    ViewAllAdapter topSellingAdapter;
    DatabaseHandler dbcart;
    LinearLayout bottomLayTotal;
    TextView totalCount;
    TextView totalPrice;
    private ShimmerRecyclerView rvTopSelling;
    private List<NewCartModel> topSellList = new ArrayList<>();
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
        setContentView(R.layout.activity_view_all__top_deals);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setOnClickListener(view -> Log.i("TAG","not work"));
        String actionName = (String) Objects.requireNonNull(getIntent().getExtras()).get("action_name");
        rvTopSelling = findViewById(R.id.recyclerTopSelling);
        ImageView backBtn = findViewById(R.id.back_btn);
        sessionManagement = new SessionManagement(ViewAllTopDeals.this);
        dbcart = new DatabaseHandler(ViewAllTopDeals.this);
        bottomLayTotal = findViewById(R.id.bottom_lay_total);
        totalPrice = findViewById(R.id.total_price);
        totalCount = findViewById(R.id.total_count);
        TextView continueTocart = findViewById(R.id.continue_tocart);

        if (dbcart.getCartCount() > 0) {
            bottomLayTotal.setVisibility(View.VISIBLE);
            totalPrice.setText(sessionManagement.getCurrency() + " " + dbcart.getTotalAmount());
            totalCount.setText("Total Items (" + dbcart.getCartCount() + ")");
        } else {
            bottomLayTotal.setVisibility(View.GONE);
        }

        backBtn.setOnClickListener(v -> {
            invalue = false;
            onBackPressed();
        });

        if (isOnline()) {
            show();
            if (actionName != null) {
                if (actionName.equalsIgnoreCase("Recent_Details_Fragment")) {
                    topSellingUrl(HOME_RECENT);
                } else if (actionName.equalsIgnoreCase("Whats_New_Fragment")) {
                    topSellingUrl(WHATSNEW);
                } else if (actionName.equalsIgnoreCase("Deals_Fragment")) {
                    topSellingUrl(HOME_DEAL);
                } else if (actionName.equalsIgnoreCase("Top_Deals_Fragment")) {
                    topSellingUrl(HOME_TOP_SELLING);
                }
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

    private void topSellingUrl(String url) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, response -> {
            Log.d("HomeTopSelling", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                if (status.equals("1")) {
                    topSellList.clear();
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<NewCartModel>>() {
                    }.getType();
                    List<NewCartModel> listorl = gson.fromJson(jsonObject.getString("data"), listType);
                    topSellList.addAll(listorl);
                    topSellingAdapter = new ViewAllAdapter(topSellList, getApplicationContext(), new ViewNotifier() {
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
                            Intent intent = new Intent(ViewAllTopDeals.this, ProductDetails.class);
                            intent.putExtra("sId", topSellList.get(position).getProductId());
                            intent.putExtra("sVariant_id", topSellList.get(position).getVarientId());
                            intent.putExtra("sName", topSellList.get(position).getProductName());
                            intent.putExtra("descrip", topSellList.get(position).getDescription());
                            intent.putExtra("price", topSellList.get(position).getPrice());
                            intent.putExtra("mrp", topSellList.get(position).getMrp());
                            intent.putExtra("unit", topSellList.get(position).getUnit());
                            intent.putExtra("qty", topSellList.get(position).getQuantity());
                            intent.putExtra("stock", topSellList.get(position).getStock());
                            intent.putExtra("image", topSellList.get(position).getProductImage());
                            startActivityForResult(intent,21);
                        }
                    });
                    rvTopSelling.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rvTopSelling.setAdapter(topSellingAdapter);
                    topSellingAdapter.notifyDataSetChanged();
                } else {
                    JSONObject resultObj = jsonObject.getJSONObject("results");
                    String msg = resultObj.getString("message");
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
                Map<String, String> params = new HashMap<>();
                params.put("lat", sessionManagement.getLatPref());
                params.put("lng", sessionManagement.getLangPref());
                params.put("city", sessionManagement.getLocationCity());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(ViewAllTopDeals.this);
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
            if (topSellingAdapter!=null){
                topSellingAdapter.notifyDataSetChanged();
            }
        }
    }
}
