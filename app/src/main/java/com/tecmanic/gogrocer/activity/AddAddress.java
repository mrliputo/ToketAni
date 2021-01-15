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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.adapters.SearchAdapter;
import com.tecmanic.gogrocer.config.BaseURL;
import com.tecmanic.gogrocer.constans.RecyclerTouchListener;
import com.tecmanic.gogrocer.modelclass.SearchModel;
import com.tecmanic.gogrocer.util.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tecmanic.gogrocer.config.BaseURL.ADD_ADDRESS;
import static com.tecmanic.gogrocer.config.BaseURL.CITY_LIST_URL;
import static com.tecmanic.gogrocer.config.BaseURL.EDIT_ADDRESS;
import static com.tecmanic.gogrocer.config.BaseURL.SOCIETY_LIST_URL;

public class AddAddress extends AppCompatActivity {
    SessionManagement sessionManagement;
    LinearLayout back;
    Button save;
    Button editBtn;
    EditText pinCode;
    EditText houseNo;
    EditText area;
    EditText city;
    EditText state;
    EditText landmaark;
    EditText name;
    EditText mobNo;
    EditText alterMob;
    TextInputLayout tpinCode;
    TextInputLayout thouseNo;
    TextInputLayout tArea;
    TextInputLayout tcity;
    TextInputLayout tstate;
    TextInputLayout tlandmaark;
    TextInputLayout tname;
    TextInputLayout tmobNo;
    TextInputLayout talterMob;
    CardView currentLoc;
    String userId;
    RecyclerView recyclerViewCity;
    RecyclerView recyclerViewSociety;
    String cityId;
    String cityName;
    String socetyId;
    String socetyName;
    String landmaarkkkk;
    String updtae;
    String addressId;
    String receiverName;
    String receiverPhone;
    String houseNoN;
    String landmark;
    String stateSt;
    String pincodeSt;
    SearchAdapter cityAdapter;
    SearchAdapter societyAdapter;
    List<SearchModel> citylist = new ArrayList<>();
    List<SearchModel> societylist = new ArrayList<>();
    private String socityKey = "society_name";
    private String statusKey = "status";
    private String messageKey = "message";
    private String cityNameKey = "city_name";
    private String receiverNameKey = "receiver_name";
    private String receiverPhoneKey = "receiver_phone";
    private String houseNoKey = "house_no";
    private String landMarkKey = "landmark";
    private String stateKey = "state";
    private String pinCodeKey = "pincode";
    private String socitKey = "society";
    private LinearLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        sessionManagement = new SessionManagement(AddAddress.this);
        cityName = getIntent().getStringExtra(cityNameKey);
        receiverName = getIntent().getStringExtra(receiverNameKey);
        receiverPhone = getIntent().getStringExtra(receiverPhoneKey);
        houseNoN = getIntent().getStringExtra(houseNoKey);
        landmark = getIntent().getStringExtra(landMarkKey);
        stateSt = getIntent().getStringExtra(stateKey);
        pincodeSt = getIntent().getStringExtra(pinCodeKey);
        socetyName = getIntent().getStringExtra(socitKey);
        progressBar = findViewById(R.id.progress_bar);
        back = findViewById(R.id.back);
        save = findViewById(R.id.SaveBtn);
        editBtn = findViewById(R.id.EditBtn);
        currentLoc = findViewById(R.id.currentLoc);

        recyclerViewCity = findViewById(R.id.recyclerCity);
        recyclerViewSociety = findViewById(R.id.recyclerSociety);

        tpinCode = findViewById(R.id.input_layout_pinCode);
        thouseNo = findViewById(R.id.input_layout_HOuseNo);
        tArea = findViewById(R.id.input_layout_area);
        tcity = findViewById(R.id.input_layout_CIty);
        tstate = findViewById(R.id.input_layout_state);
        tlandmaark = findViewById(R.id.input_layout_landmark);
        tname = findViewById(R.id.input_layout_NAme);
        tmobNo = findViewById(R.id.input_layout_mobNo);
        talterMob = findViewById(R.id.input_layout_AltermobileNO);
        pinCode = findViewById(R.id.input_pinCode1);
        houseNo = findViewById(R.id.input_HouseNO1);
        area = findViewById(R.id.input_area);
        city = findViewById(R.id.input_city);
        state = findViewById(R.id.input_state1);
        landmaark = findViewById(R.id.input_landmark);
        name = findViewById(R.id.input_NAme1);
        mobNo = findViewById(R.id.input_mobNO1);
        alterMob = findViewById(R.id.input_AltermobileNO1);
        init();
    }

    private void show() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    private void init() {
        userId = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        updtae = getIntent().getStringExtra("update");
        addressId = getIntent().getStringExtra("addId");
        save.setVisibility(View.GONE);
        editBtn.setVisibility(View.VISIBLE);
        back.setOnClickListener(v -> finish());

        editBtn.setOnClickListener(v -> {

            if (isOnline()) {
                landmaarkkkk = landmaark.getText().toString();
                if (!landmaarkkkk.equalsIgnoreCase("")) {
                    editAdressURl(addressId, cityName, landmaarkkkk);
                } else {
                    editAdressURl(addressId, cityName, "NA");
                }
            }
        });
        pinCode.setText(pincodeSt);
        houseNo.setText(houseNoN);
        city.setText(cityName);
        state.setText(stateSt);
        area.setText(socetyName);
        landmaark.setText(landmark);
        name.setText(receiverName);
        mobNo.setText(receiverPhone);


        area.setOnClickListener(v -> {
            show();
            societyURl(cityName);

        });

        city.setOnClickListener(v -> {
            show();
            cityUrl();
        });
        recyclerViewCity.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerViewCity, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                cityId = citylist.get(position).getId();
                cityName = citylist.get(position).getpNAme();
                city.setText(cityName);
                recyclerViewCity.setVisibility(View.GONE);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        recyclerViewSociety.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerViewSociety, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                socetyId = societylist.get(position).getId();
                socetyName = societylist.get(position).getpNAme();
                area.setText(socetyName);
                recyclerViewSociety.setVisibility(View.GONE);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        save.setOnClickListener(v -> {
            if (pinCode.getText().toString().trim().equals("")) {
                Toast.makeText(getApplicationContext(), "Enter Pincode", Toast.LENGTH_SHORT).show();
            }else if (houseNo.getText().toString().trim().equals("")) {
                Toast.makeText(getApplicationContext(), "Enter House No., Building Name", Toast.LENGTH_SHORT).show();
            } else if (area.getText().toString().trim().equals("")) {
                Toast.makeText(getApplicationContext(), "Enter Area, Colony", Toast.LENGTH_SHORT).show();
            } else {
                continueSaveAdd();
            }

        });
    }

    private void continueSaveAdd(){
        if (city.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Enter City", Toast.LENGTH_SHORT).show();
        } else if (state.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Enter State", Toast.LENGTH_SHORT).show();
        } else if (name.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Enter your Name", Toast.LENGTH_SHORT).show();
        } else if (mobNo.getText().toString().trim().equals("")) {
            Toast.makeText(getApplicationContext(), "Enter Mobile No.", Toast.LENGTH_SHORT).show();
        } else if (!isOnline()) {
            Toast.makeText(getApplicationContext(), "Check your Internet Connection!", Toast.LENGTH_SHORT).show();
        } else {
            landmaarkkkk = landmaark.getText().toString();
            saveAddress(cityName, socetyName);
        }
    }
    private void editAdressURl(String addressId, String cityName, String landmaarkkkk) {
        show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, EDIT_ADDRESS, response -> {
            Log.d("editttadrss", response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString(statusKey);
                if (status.equalsIgnoreCase("1")) {
                    finish();
                } else {
                    JSONObject resultObj = jsonObject.getJSONObject("results");
                    String msg = resultObj.getString("msg");
                    Toast.makeText(getApplicationContext(), msg + "", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                show();
            }
        }, error -> {
            show();
            error.printStackTrace();
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();

                param.put("address_id", addressId);
                param.put("user_id", sessionManagement.getUserDetails().get(BaseURL.KEY_ID));
                param.put(receiverNameKey, name.getText().toString());
                param.put(receiverPhoneKey, mobNo.getText().toString());
                param.put(cityNameKey, cityName);
                param.put(socityKey, socetyName);
                param.put(houseNoKey, houseNo.getText().toString());
                param.put(landMarkKey, landmaarkkkk);
                param.put(stateKey, state.getText().toString());
                param.put("pin", pinCode.getText().toString());
                param.put("lat", sessionManagement.getLatPref());
                param.put("lng", sessionManagement.getLangPref());
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
        RequestQueue requestQueue = Volley.newRequestQueue(AddAddress.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }


    private void societyURl(String cityId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SOCIETY_LIST_URL, response -> {
            Log.d("socirrty", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString(statusKey);
                String msg = jsonObject.getString(messageKey);
                if (status.equals("1")) {
                    societylist.clear();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String societyId = jsonObject1.getString("society_id");
                        String societyName = jsonObject1.getString(socityKey);
                        recyclerViewSociety.setVisibility(View.VISIBLE);
                        SearchModel ss = new SearchModel(societyId, societyName);
                        societylist.add(ss);
                    }
                    societyAdapter = new SearchAdapter(societylist);
                    recyclerViewSociety.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerViewSociety.setAdapter(societyAdapter);
                    societyAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                show();
            }
        }, error -> {
            show();
            error.printStackTrace();
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put(cityNameKey, cityId);
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
        RequestQueue requestQueue = Volley.newRequestQueue(AddAddress.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    private void cityUrl() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, CITY_LIST_URL, response -> {
            Log.d("cityyyyyyyy", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString(statusKey);
                String msg = jsonObject.getString(messageKey);
                if (status.equals("1")) {
                    citylist.clear();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String cityId1 = jsonObject1.getString("city_id");
                        String cityName1 = jsonObject1.getString(cityNameKey);

                        recyclerViewCity.setVisibility(View.VISIBLE);
                        SearchModel cs = new SearchModel(cityId1, cityName1);
                        citylist.add(cs);
                    }
                    cityAdapter = new SearchAdapter(citylist);
                    recyclerViewCity.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerViewCity.setAdapter(cityAdapter);
                    cityAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                show();
            }
        }, error -> {
            show();
            error.printStackTrace();
        });
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
        RequestQueue requestQueue = Volley.newRequestQueue(AddAddress.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }


    private void saveAddress(String cityName, String soctyName) {
        show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_ADDRESS, response -> {
            Log.d("addadrss", response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString(statusKey);
                String msg = jsonObject.getString(messageKey);
                if (status.equalsIgnoreCase("1")) {


                    Toast.makeText(getApplicationContext(), msg + "", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), SelectAddress.class);
                    startActivity(intent);
                    finish();

                } else {

                    Toast.makeText(getApplicationContext(), msg + "", Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                show();
            }
        }, error -> {
            show();
            error.printStackTrace();
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("user_id", sessionManagement.getUserDetails().get(BaseURL.KEY_ID));
                param.put(receiverNameKey, name.getText().toString());
                param.put(receiverPhoneKey, mobNo.getText().toString());
                param.put(cityNameKey, cityName);
                param.put(socityKey, soctyName);
                param.put(houseNoKey, houseNo.getText().toString());
                param.put(landMarkKey, "NA");
                param.put(stateKey, state.getText().toString());
                param.put("pin", pinCode.getText().toString());
                param.put("lat", sessionManagement.getLatPref());
                param.put("lng", sessionManagement.getLangPref());
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
        RequestQueue requestQueue = Volley.newRequestQueue(AddAddress.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }


    private Boolean isOnline() {
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
