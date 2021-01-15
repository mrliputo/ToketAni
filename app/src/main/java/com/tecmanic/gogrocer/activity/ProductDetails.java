package com.tecmanic.gogrocer.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.adapters.AdapterPopup;
import com.tecmanic.gogrocer.config.BaseURL;
import com.tecmanic.gogrocer.modelclass.NewCategoryVarientList;
import com.tecmanic.gogrocer.util.DatabaseHandler;
import com.tecmanic.gogrocer.util.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tecmanic.gogrocer.config.BaseURL.PRODUCT_VARIENT;

public class ProductDetails extends AppCompatActivity {
    int minteger = 0;
    LinearLayout back;
    LinearLayout btnAdd;
    LinearLayout llAddQuan;
    LinearLayout llDetails;
    LinearLayout outofsIn;
    LinearLayout outofs;
    ImageView pImage;
    TextView prodName;
    TextView prodDesc;
    TextView prodPrice;
    TextView prodMrp;
    TextView nodata;
    TextView txtQuan;
    TextView minus;
    TextView plus;
    TextView txtUnit;
    TextView txtQty;
    String varientName;
    String discription12;
    String price12;
    String mrp12;
    String unit12;
    String qty;
    String varientimage;
    String productId;
    String varientId;
    String stock = "0";
    private DatabaseHandler dbcart;
    private SharedPreferences preferences;
    private List<NewCategoryVarientList> varientProducts = new ArrayList<>();
    private SessionManagement sessionManagement;
    private AdapterPopup selectCityAdapter;
    private String varientIdKey = "varient_id";
    private DecimalFormat dFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        sessionManagement = new SessionManagement(ProductDetails.this);
        dbcart = new DatabaseHandler(ProductDetails.this);
        preferences = getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
        init();

    }

    private void init() {
        dFormat = new DecimalFormat("#.##");
        productId = getIntent().getStringExtra("sId");
        varientName = getIntent().getStringExtra("sName");
        discription12 = getIntent().getStringExtra("descrip");
        price12 = getIntent().getStringExtra("price");
        mrp12 = getIntent().getStringExtra("mrp");
        unit12 = getIntent().getStringExtra("unit");
        qty = getIntent().getStringExtra("qty");
        varientimage = getIntent().getStringExtra("image");
        varientId = getIntent().getStringExtra("sVariant_id");
        stock = getIntent().getStringExtra("stock");

        llDetails = findViewById(R.id.ll3);
        back = findViewById(R.id.back);
        pImage = findViewById(R.id.pImage);
        nodata = findViewById(R.id.nodata);
        prodName = findViewById(R.id.txt_pName);
        prodDesc = findViewById(R.id.txt_pInfo);
        prodPrice = findViewById(R.id.txt_pPrice);
        prodMrp = findViewById(R.id.txt_pMrp);
        btnAdd = findViewById(R.id.btn_Add);
        llAddQuan = findViewById(R.id.ll_addQuan);
        outofsIn = findViewById(R.id.outofs_in);
        outofs = findViewById(R.id.outofs);
        txtQuan = findViewById(R.id.txtQuan);
        minus = findViewById(R.id.minus);
        plus = findViewById(R.id.plus);
        txtUnit = findViewById(R.id.txt_unit);
        txtQty = findViewById(R.id.txt_qty);
        RecyclerView recyclerUnit = findViewById(R.id.recyclerUnit);

        recyclerUnit.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        recyclerUnit.setHasFixedSize(true);

        back.setOnClickListener(v -> finish());
        prodDesc.setText(discription12);

        if (Integer.parseInt(stock) > 0) {
            outofs.setVisibility(View.GONE);
            outofsIn.setVisibility(View.VISIBLE);
        } else {
            outofsIn.setVisibility(View.GONE);
            outofs.setVisibility(View.VISIBLE);
        }

        selectCityAdapter = new AdapterPopup(ProductDetails.this, varientProducts, varientName, position -> {
            if (varientId.equalsIgnoreCase(varientProducts.get(position).getVarientId())) {
                int qtyd = Integer.parseInt(dbcart.getInCartItemQtys(varientId));
                if (qtyd > 0) {
                    btnAdd.setVisibility(View.GONE);
                    llAddQuan.setVisibility(View.VISIBLE);
                    txtQuan.setText("" + qtyd);
                    double priced = Double.parseDouble(price12);
                    double mrpd = Double.parseDouble(mrp12);
                    prodPrice.setText("" + dFormat.format((priced * qtyd)));
                    prodMrp.setText("" + dFormat.format((mrpd * qtyd)));
                } else {
                    btnAdd.setVisibility(View.VISIBLE);
                    llAddQuan.setVisibility(View.GONE);
                    prodPrice.setText(price12);
                    prodMrp.setText(mrp12);
                    txtQuan.setText("" + 0);
                }
            }
        });
        recyclerUnit.setAdapter(selectCityAdapter);

        varientProduct(productId,varientId);

        setUpItemQty();


        btnAdd.setOnClickListener(v -> {
            btnAdd.setVisibility(View.GONE);
            llAddQuan.setVisibility(View.VISIBLE);
            txtQuan.setText("1");
            updateMultiply();

        });

        plus.setOnClickListener(v -> {
            if (Integer.parseInt(txtQuan.getText().toString()) < Integer.parseInt(stock)) {
                int qtyde = Integer.parseInt(dbcart.getInCartItemQtys(varientId));
                qtyde += 1;
                txtQuan.setText("" + qtyde);
                updateMultiply();
            }

        });

        minus.setOnClickListener(v -> {
            int qtyde = Integer.parseInt(dbcart.getInCartItemQtys(varientId));
            qtyde -= 1;
            if (qtyde == 0) {
                llAddQuan.setVisibility(View.GONE);
                btnAdd.setVisibility(View.VISIBLE);
            } else {
                llAddQuan.setVisibility(View.VISIBLE);
                btnAdd.setVisibility(View.GONE);
            }
            txtQuan.setText("" + qtyde);
            updateMultiply();
        });

        if (isOnline()) {
            prodName.setText(varientName);
            prodPrice.setText(sessionManagement.getCurrency() + "" + price12);
            prodMrp.setPaintFlags(prodMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            prodMrp.setText(mrp12);
            txtUnit.setText(unit12);
            txtQty.setText(qty);
            Picasso.with(this).load(BaseURL.IMG_URL + varientimage).networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE).into(pImage);
        }
    }

    private void setUpItemQty() {
        int qtyd = Integer.parseInt(dbcart.getInCartItemQtys(varientId));
        if (qtyd > 0) {
            btnAdd.setVisibility(View.GONE);
            llAddQuan.setVisibility(View.VISIBLE);
            txtQuan.setText("" + qtyd);
            double priced = Double.parseDouble(price12);
            double mrpd = Double.parseDouble(mrp12);
            prodPrice.setText("" + (priced * qtyd));
            prodMrp.setText("" + (mrpd * qtyd));
        } else {
            btnAdd.setVisibility(View.VISIBLE);
            llAddQuan.setVisibility(View.GONE);
            prodPrice.setText(price12);
            prodMrp.setText(mrp12);
            txtQuan.setText("" + 0);
        }
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


    private void updateMultiply() {
        HashMap<String, String> map = new HashMap<>();
        map.put("product_id", productId);
        map.put("product_name", varientName);
        map.put(varientIdKey, varientId);
        map.put("title", varientName);
        map.put("price", price12);
        map.put("deal_price", "");
        map.put("product_image", varientimage);
        map.put("unit_value", qty);
        map.put("unit", unit12);
        map.put("increament", "0");
        map.put("rewards", "0");
        map.put("stock", "0");
        map.put("product_description", discription12);
        if (!txtQuan.getText().toString().equalsIgnoreCase("0")) {
            dbcart.setCart(map, Integer.parseInt(txtQuan.getText().toString()));
        } else {
            dbcart.removeItemFromCart(map.get(varientIdKey));
        }
        try {
            int items = Integer.parseInt(dbcart.getInCartItemQtys(map.get(varientIdKey)));
            double price = Double.parseDouble(price12);
            double mrp = Double.parseDouble(mrp12);
            if (items == 0) {
                prodPrice.setText(sessionManagement.getCurrency() + "" + dFormat.format(price));
                prodMrp.setText(sessionManagement.getCurrency() + "" + dFormat.format(mrp));
            } else {
                prodPrice.setText(sessionManagement.getCurrency() + "" + dFormat.format(price * items));
                prodMrp.setText(sessionManagement.getCurrency() + "" + dFormat.format(mrp * items));
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                preferences.edit().putInt("cardqnty", dbcart.getCartCount()).apply();
            }
            selectCityAdapter.notifyDataSetChanged();
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private void varientProduct(String pId, String varientId) {
        varientProducts.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, PRODUCT_VARIENT, response -> {
            try {
                varientProducts.clear();

                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                if (status.equals("1")) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<NewCategoryVarientList>>() {
                    }.getType();
                    List<NewCategoryVarientList> listorl = gson.fromJson(jsonObject.getString("data"), listType);
                    if (listorl!=null && listorl.size()>0){
                        List<NewCategoryVarientList> newCatMod = new ArrayList<>();
                        newCatMod.addAll(listorl);
                        newCatMod.remove(new NewCategoryVarientList(varientId));
                        varientProducts.addAll(newCatMod);
                    }else {
                        varientProducts.clear();
                    }

//                    varientProducts.addAll(listorl);
                    selectCityAdapter.notifyDataSetChanged();
                } else {
                    varientProducts.clear();
                    selectCityAdapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> selectCityAdapter.notifyDataSetChanged()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("product_id", pId);
                params.put("lat", sessionManagement.getLatPref());
                params.put("lng", sessionManagement.getLangPref());
                params.put("city", sessionManagement.getLocationCity());
                return params;
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

}
