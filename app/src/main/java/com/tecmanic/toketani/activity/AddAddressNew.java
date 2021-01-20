package com.tecmanic.toketani.activity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;
import com.tecmanic.toketani.R;
import com.tecmanic.toketani.config.BaseURL;
import com.tecmanic.toketani.modelclass.SearchModel;
import com.tecmanic.toketani.util.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.tecmanic.toketani.config.BaseURL.ADD_ADDRESS;
import static com.tecmanic.toketani.config.BaseURL.SOCIETY_LIST_URL;

public class AddAddressNew extends AppCompatActivity {
    private static final String TAG = AddAddressNew.class.getName();
    SessionManagement sessionManagement;
    LinearLayout back;

    EditText pinCode;
    EditText houseNo;
    EditText state;
    EditText landmaark;
    EditText name;
    EditText mobNo;
    EditText alterMob;
    TextInputLayout tpinCode;
    TextInputLayout thouseNo;
    TextInputLayout tstate;
    TextInputLayout tlandmaark;
    TextInputLayout tname;
    TextInputLayout tmobNo;
    TextInputLayout talterMob;
    CardView currentLoc;
    String userId;
    RecyclerView recyclerViewCity;
    RecyclerView recyclerViewSociety;
    String cityName = "";
    String socetyName = "";
    String landmaarkkkk;
    String updtae;
    String addressId;
    List<SearchModel> societylist = new ArrayList<>();
    ArrayList<String> vehicletypemessagebrandList = new ArrayList<>();
    TextView cityTxt;
    private boolean inSelectVal = false;
    private String statused = "status";
    private String cityNameRef = "city_name";
    private LinearLayout progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address2);
        progressBar = findViewById(R.id.progress_bar);
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
        sessionManagement = new SessionManagement(AddAddressNew.this);

        back = findViewById(R.id.back);
        Button save = findViewById(R.id.SaveBtn);
        updtae = getIntent().getStringExtra("update");
        addressId = getIntent().getStringExtra("addId");
        back.setOnClickListener(v -> {
            inSelectVal = false;
            onBackPressed();
        });
        userId = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        currentLoc = findViewById(R.id.currentLoc);
        recyclerViewCity = findViewById(R.id.recyclerCity);
        cityTxt = findViewById(R.id.city_txt);
        recyclerViewSociety = findViewById(R.id.recyclerSociety);
        AppCompatSpinner areaSpinner = findViewById(R.id.area_spinner);
        tpinCode = findViewById(R.id.input_layout_pinCode);
        thouseNo = findViewById(R.id.input_layout_HOuseNo);
        tstate = findViewById(R.id.input_layout_state);
        tlandmaark = findViewById(R.id.input_layout_landmark);
        tname = findViewById(R.id.input_layout_NAme);
        tmobNo = findViewById(R.id.input_layout_mobNo);
        talterMob = findViewById(R.id.input_layout_AltermobileNO);
        pinCode = findViewById(R.id.input_pinCode);
        houseNo = (EditText) findViewById(R.id.input_HouseNO);
        state = (EditText) findViewById(R.id.input_state);
        landmaark = (EditText) findViewById(R.id.input_landmark_2);
        name = (EditText) findViewById(R.id.input_NAme);
        mobNo = (EditText) findViewById(R.id.input_mobNO);
        alterMob = (EditText) findViewById(R.id.input_AltermobileNO);
        getAddress(areaSpinner);
        save.setOnClickListener(v -> {

            if (pinCode.getText().toString().trim().equals("")) {
                Toast.makeText(getApplicationContext(), "Enter Pincode", Toast.LENGTH_SHORT).show();
            } else if (houseNo.getText().toString().trim().equals("")) {
                Toast.makeText(getApplicationContext(), "Enter House No., Building Name", Toast.LENGTH_SHORT).show();
            } else if (cityTxt.getText().toString().trim().equals("")) {
                Toast.makeText(getApplicationContext(), "Enter City", Toast.LENGTH_SHORT).show();
            } else if (state.getText().toString().trim().equals("")) {
                Toast.makeText(getApplicationContext(), "Enter State", Toast.LENGTH_SHORT).show();
            } else if (name.getText().toString().trim().equals("")) {
                Toast.makeText(getApplicationContext(), "Enter your Name", Toast.LENGTH_SHORT).show();
            } else if (mobNo.getText().toString().trim().equals("")) {
                Toast.makeText(getApplicationContext(), "Enter Mobile No.", Toast.LENGTH_SHORT).show();
            } else {
                landmaarkkkk = landmaark.getText().toString();
                if (!landmaarkkkk.equalsIgnoreCase("")) {
                    saveAddress(cityName, socetyName, landmaarkkkk);
                } else {
                    saveAddress(cityName, socetyName, "NA");
                }
            }

        });

    }

    private void saveAddress(String cityName, String soctyName, String landmaarkkkkk) {
        show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ADD_ADDRESS, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString(statused);
                String msg = jsonObject.getString("message");
                if (status.equalsIgnoreCase("1")) {
                    Toast.makeText(getApplicationContext(), msg + "", Toast.LENGTH_SHORT).show();
                    inSelectVal = true;
                    onBackPressed();
                } else {
                    Toast.makeText(getApplicationContext(), msg + "", Toast.LENGTH_SHORT).show();
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
                param.put("user_id", sessionManagement.getUserDetails().get(BaseURL.KEY_ID));
                param.put("receiver_name", name.getText().toString());
                param.put("receiver_phone", mobNo.getText().toString());
                param.put(cityNameRef, cityName);
                param.put("society_name", (soctyName != null && !soctyName.equalsIgnoreCase("")) ? soctyName : cityName);
                param.put("house_no", houseNo.getText().toString());
                param.put("landmark", landmaarkkkkk);
                param.put("state", state.getText().toString());
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
        RequestQueue requestQueue = Volley.newRequestQueue(AddAddressNew.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    private void societyURl(String cityId, AppCompatSpinner areaSpinner) {
        vehicletypemessagebrandList.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SOCIETY_LIST_URL, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                String msg = jsonObject.getString("message");
                if (status.equals("1")) {
                    societylist.clear();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String societyId1 = jsonObject1.getString("society_id");
                        String societyName1 = jsonObject1.getString("society_name");
                        recyclerViewSociety.setVisibility(View.VISIBLE);
                        SearchModel ss = new SearchModel(societyId1, societyName1);
                        societylist.add(ss);
                        vehicletypemessagebrandList.add(societyName1);
                    }

                    socetyName = vehicletypemessagebrandList.get(0);

                    runOnUiThread(() -> {
                        ArrayAdapter adapter = new ArrayAdapter<>(AddAddressNew.this, android.R.layout.simple_spinner_item, vehicletypemessagebrandList);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        areaSpinner.setAdapter(adapter);
                        areaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                socetyName = areaSpinner.getSelectedItem().toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {
                                Log.i(TAG, "d");
                            }
                        });
                    });


                } else {
                    socetyName = "";
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }
                show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            show();
        }, Throwable::printStackTrace) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put(cityNameRef, cityId);
                Log.d(TAG, cityId);
                return param;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(AddAddressNew.this);
        requestQueue.getCache().clear();
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
        requestQueue.add(stringRequest);

    }

    private void getAddress(AppCompatSpinner areaSpinner) {
        show();
        new Thread(() -> {
            Geocoder geocoder;
            List<Address> addresses = null;
            geocoder = new Geocoder(AddAddressNew.this, Locale.getDefault());
            DecimalFormat dFormat = new DecimalFormat("#.######");
            double lat = Double.parseDouble(sessionManagement.getLatPref());
            double lang = Double.parseDouble(sessionManagement.getLangPref());
            double latitude = Double.parseDouble(dFormat.format(lat));
            double longitude = Double.parseDouble(dFormat.format(lang));

            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (!addresses.isEmpty()){
                    Address returnedAddress = addresses.get(0);
                    StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
                    for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                        strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                    }
                    String city1 = addresses.get(0).getLocality();
                    String states = addresses.get(0).getAdminArea();
                    societyURl(city1,areaSpinner);
                    runOnUiThread(() -> {
                        cityName = city1;
                        cityTxt.setText(city1);
                        if (states != null && !states.equalsIgnoreCase("")) {
                            state.setText(states);
                        } else {
                            state.setText(city1);
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                runOnUiThread(this::show);
            }
        }).start();

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("select_address", inSelectVal);
        setResult(21, intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }
}
