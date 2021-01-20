package com.tecmanic.toketani.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.android.material.snackbar.Snackbar;
import com.tecmanic.toketani.R;
import com.tecmanic.toketani.adapters.ImageAdapterData;
import com.tecmanic.toketani.adapters.MyCalnderAdapter;
import com.tecmanic.toketani.adapters.TimingAdapter;
import com.tecmanic.toketani.config.BaseURL;
import com.tecmanic.toketani.modelclass.DeliveryModel;
import com.tecmanic.toketani.modelclass.MyCalendarData;
import com.tecmanic.toketani.modelclass.MyCalendarModel;
import com.tecmanic.toketani.modelclass.TimingModel;
import com.tecmanic.toketani.util.AppController;
import com.tecmanic.toketani.util.CustomVolleyJsonRequest;
import com.tecmanic.toketani.util.DatabaseHandler;
import com.tecmanic.toketani.util.ForClicktimings;
import com.tecmanic.toketani.util.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.android.material.snackbar.Snackbar.LENGTH_LONG;
import static com.tecmanic.toketani.config.BaseURL.CALENDER_URL;
import static com.tecmanic.toketani.config.BaseURL.MIN_MAX_ORDER;
import static com.tecmanic.toketani.config.BaseURL.ORDER_CONTINUE;
import static com.tecmanic.toketani.config.BaseURL.SHOW_ADDRESS;

public class OrderSummary extends AppCompatActivity implements ForClicktimings {
    private static final String TAG = OrderSummary.class.getName();
    String cartId;
    String totalAtm;
    Button btnAddAddress;
    LinearLayout back;
    TextView btnContine;
    TextView txtDeliver;
    TextView txtTotalItems;
    TextView pPrice;
    TextView pMrp;
    TextView totalItms;
    TextView price;
    TextView deliveryCharge;
    TextView amounttotal;
    TextView txtTotalPrice;
    String dname;
    JSONArray array;
    RecyclerView recyclerItemsList;
    RecyclerView recyclerTimeSlot;
    RecyclerView calendarview2;
    TimingAdapter bAdapter1;
    String timeslot;
    String addressid;
    String userId;
    String minVAlue;
    String maxValue;
    private ArrayList<TimingModel> dateDayModelClasses1 = new ArrayList<>();
    private DatabaseHandler db;
    private SessionManagement sessionManagement;
    private String todaydatee;
    private TextView textviewNameOfdata;
    private TextView textviewMobileDelivery;
    private TextView addCheckNotice;
    private List<MyCalendarModel> calendarModelList = new ArrayList<>();
    private String statusKey = "status";
    private String messageKey = "message";
    private String userIdKey = "user_id";
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
        setContentView(R.layout.activity_order_summary);
        progressBar = findViewById(R.id.progress_bar);
        progressBar.setOnClickListener(view -> Log.i("TAG","not work"));
        sessionManagement = new SessionManagement(OrderSummary.this);
        userId = sessionManagement.userId();
        array = new JSONArray();
        prepareData();
        init();
    }

    private void prepareData() {
        for (int i = 0; i < 10; i++) {
            MyCalendarData calendarData = new MyCalendarData(i);
            MyCalendarModel calendar = new MyCalendarModel(calendarData.getWeekDay(), String.valueOf(calendarData.getDay()), String.valueOf(calendarData.getMonth()), String.valueOf(calendarData.getYear()), 0, String.valueOf(calendarData.getMonth()));
            calendarData.getNextWeekDay(1);
            Log.i(TAG, calendar.toString());
            calendarModelList.add(calendar);

        }
    }

    private void init() {
        db = new DatabaseHandler(this);
        dname = getIntent().getStringExtra("dName");
        addressid = getIntent().getStringExtra("dId");
        btnAddAddress = findViewById(R.id.btn_AddAddress);
        txtTotalItems = findViewById(R.id.txtTotalItems);
        calendarview2 = findViewById(R.id.calendarview2);
        TextView currencyIndicator = findViewById(R.id.currency_indicator);
        TextView currencyIndicator2 = findViewById(R.id.currency_indicator_2);
        TextView ruppy = findViewById(R.id.rupyy);
        btnContine = findViewById(R.id.btn_Contine);
        addCheckNotice = findViewById(R.id.add_check_notice);
        txtDeliver = findViewById(R.id.txt_deliver);
        textviewMobileDelivery = findViewById(R.id.textview_mobile_delivery);
        recyclerItemsList = findViewById(R.id.recycler_itemsList);
        textviewNameOfdata = findViewById(R.id.textview_name_ofdata);
        recyclerTimeSlot = findViewById(R.id.recyclerTimeSlot);
        pPrice = findViewById(R.id.pPrice);
        pMrp = findViewById(R.id.pMrp);
        totalItms = findViewById(R.id.totalItms);
        price = findViewById(R.id.price);
        deliveryCharge = findViewById(R.id.DeliveryCharge);
        amounttotal = findViewById(R.id.Amounttotal);
        txtTotalPrice = findViewById(R.id.txt_totalPrice);
        back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());
        btnAddAddress.setOnClickListener(v -> {
            Intent inr = new Intent(getApplicationContext(), SelectAddress.class);
            startActivityForResult(inr, 2);
        });


        calendarview2.setLayoutManager(new LinearLayoutManager(OrderSummary.this, RecyclerView.HORIZONTAL, false));
        calendarview2.setHasFixedSize(true);
        calendarview2.setAdapter(new MyCalnderAdapter(calendarModelList, position -> {
            show();
            todaydatee = calendarModelList.get(position).getYear() + "-" + calendarModelList.get(position).getMonthValue() + "-" + calendarModelList.get(position).getDate();
            makeGetAddressRequests(todaydatee);
        }));

        todaydatee = calendarModelList.get(0).getYear() + "-" + calendarModelList.get(0).getMonthValue() + "-" + calendarModelList.get(0).getDate();
        currencyIndicator.setText(sessionManagement.getCurrency());
        currencyIndicator2.setText(sessionManagement.getCurrency());
        ruppy.setText(sessionManagement.getCurrency());
        List<HashMap<String, String>> map = db.getCartAll();
        try {
            JSONArray object = new JSONArray(map);
            for (int i = 0; i < object.length(); i++) {
                JSONObject object1 = object.getJSONObject(i);
                JSONObject productArray = new JSONObject();
                productArray.put("qty", object1.getString("qty"));
                productArray.put("varient_id", object1.getString("varient_id"));
                productArray.put("product_image", object1.getString("product_image"));
                array.put(productArray);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ImageAdapterData adapters = new ImageAdapterData(OrderSummary.this, map);
        recyclerItemsList.setLayoutManager(new LinearLayoutManager(OrderSummary.this, LinearLayoutManager.HORIZONTAL, false));
        recyclerItemsList.setAdapter(adapters);
        showAdreesUrl();
        btnContine.setOnClickListener(this::helpMethod);

    }

    private void helpMethod(View v) {
        if (!txtTotalPrice.getText().toString().equalsIgnoreCase("")) {
            show();
            if (Double.parseDouble(txtTotalPrice.getText().toString()) <= Double.parseDouble(minVAlue)) {
                Snackbar.make(v, "Please order more than" + " " + minVAlue, LENGTH_LONG).show();
                show();
            } else if (Double.parseDouble(txtTotalPrice.getText().toString()) >= Double.parseDouble(maxValue)) {
                Snackbar.make(v, "Please order less than" + " " + maxValue, LENGTH_LONG).show();
                show();
            } else {
                if (timeslot != null && !timeslot.equalsIgnoreCase("")) {
                    continueUrl(timeslot);
                } else {
                    show();
                    Snackbar.make(v, "Please select time slot for placed order!", LENGTH_LONG).show();
                }
            }
        } else {
            Snackbar.make(v, "Please wait", LENGTH_LONG).show();
        }
    }

    private void showAdreesUrl() {
        show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SHOW_ADDRESS, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString(statusKey);
                String msg = jsonObject.getString(messageKey);
                if (status.equals("1")) {
                    addCheckNotice.setVisibility(View.VISIBLE);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String addressId = jsonObject1.getString("address_id");
                        String receiverName = jsonObject1.getString("receiver_name");
                        String receiverPhone = jsonObject1.getString("receiver_phone");
                        String cityyyy = jsonObject1.getString("city");
                        String society = jsonObject1.getString("society");
                        String houseNo = jsonObject1.getString("house_no");
                        String pincode = jsonObject1.getString("pincode");
                        String stateeee = jsonObject1.getString("state");
                        String landmark2 = jsonObject1.getString("landmark");
                        int selectStatus = jsonObject1.getInt("select_status");
                        DeliveryModel ss = new DeliveryModel(addressId, receiverName, receiverPhone, houseNo + ", " + society
                                + "," + cityyyy + ", " + stateeee + ", " + pincode);
                        if (selectStatus == 1) {
                            txtDeliver.setText(houseNo + ", " + society + "," + cityyyy + ", " + stateeee + ", " + pincode);
                            textviewNameOfdata.setText(receiverName);
                            textviewMobileDelivery.setText(receiverPhone);
                        }
                        ss.setCityName(cityyyy);
                        ss.setHouseNo(houseNo);
                        ss.setLandmark(landmark2);
                        ss.setPincode(pincode);
                        ss.setState(stateeee);
                        ss.setReceiverPhone(receiverPhone);
                        ss.setReceiverName(receiverName);
                        ss.setId(addressId);
                        ss.setSelectStatus(selectStatus);
                        ss.setSociety(society);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                makeGetAddressRequests(todaydatee);
            }
        }, error -> makeGetAddressRequests(todaydatee)) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put(userIdKey, userId);
                param.put("store_id", sessionManagement.getStoreId());
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(OrderSummary.this);
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

    private void minimaxAmount() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, MIN_MAX_ORDER, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString(statusKey);
                if (status.equals("1")) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    String min = jsonObject1.getString("min_value");
                    String max = jsonObject1.getString("max_value");
                    minVAlue = min;
                    maxValue = max;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                show();
            }

        }, error -> show());

        RequestQueue requestQueue = Volley.newRequestQueue(OrderSummary.this);
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

    private void continueUrl(final String timeslot) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, ORDER_CONTINUE, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString(statusKey);
                String msg = jsonObject.getString(messageKey);
                if (status.equals("1")) {
                    JSONObject object = jsonObject.getJSONObject("data");
                    cartId = object.getString("cart_id");
                    sessionManagement.setCartID(cartId);
                    Intent intent = new Intent(getApplicationContext(), PaymentDetails.class);
                    intent.putExtra("order_amt", txtTotalPrice.getText().toString());
                    startActivity(intent);
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
                param.put("time_slot", timeslot);
                param.put(userIdKey, userId);
                param.put("delivery_date", todaydatee);
                param.put("order_array", array.toString());
                param.put("store_id", sessionManagement.getStoreId());
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
        RequestQueue requestQueue = Volley.newRequestQueue(OrderSummary.this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);

    }

    private void updateData() {
        pPrice.setText("" + db.getTotalAmount());
        price.setText("" + db.getTotalAmount());
        totalAtm = db.getTotalAmount();
        txtTotalItems.setText("" + db.getCartCount());
        totalItms.setText("" + db.getCartCount() + " " + " Items");
        deliverychrge();
    }

    private void makeGetAddressRequests(String date) {
        timeslot = "";
        dateDayModelClasses1.clear();
        Map<String, String> params = new HashMap<>();
        params.put("selected_date", date);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                CALENDER_URL, params, response -> {
            try {
                String status = response.getString(statusKey);
                String msg = response.getString(messageKey);
                if (status.contains("1")) {
                    dateDayModelClasses1.clear();
                    JSONArray jsonArray = response.getJSONArray("data");
                    if (jsonArray.length() > 0) {
                        recyclerTimeSlot.setVisibility(View.VISIBLE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            String data = jsonArray.getString(i);
                            TimingModel mycreditList = new TimingModel();
                            mycreditList.setTiming(data);
                            dateDayModelClasses1.add(mycreditList);
                        }
                        timeslot = dateDayModelClasses1.get(0).getTiming();
                        bAdapter1 = new TimingAdapter(OrderSummary.this, dateDayModelClasses1, OrderSummary.this);
                        recyclerTimeSlot.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recyclerTimeSlot.setAdapter(bAdapter1);
                        bAdapter1.notifyDataSetChanged();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
                    timeslot = "";
                    bAdapter1 = new TimingAdapter(OrderSummary.this, dateDayModelClasses1, OrderSummary.this);
                    recyclerTimeSlot.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    recyclerTimeSlot.setAdapter(bAdapter1);
                    bAdapter1.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                updateData();
            }
        }, error -> {
            timeslot = "";
            bAdapter1 = new TimingAdapter(OrderSummary.this, dateDayModelClasses1, OrderSummary.this);
            recyclerTimeSlot.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerTimeSlot.setAdapter(bAdapter1);
            bAdapter1.notifyDataSetChanged();
            updateData();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<>();
                param.put("selected_date", date);
                return param;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.getCache().clear();
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
        requestQueue.add(jsonObjReq);

    }

    private void deliverychrge() {
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
                BaseURL.DELIVERY_INFO, null, response -> {
            try {
                String status = response.getString(statusKey);
                if (status.contains("1")) {
                    JSONObject jsonObject = response.getJSONObject("data");
                    String minCartValue = jsonObject.getString("min_cart_value");
                    String delCharge = jsonObject.getString("del_charge");
                    if (Double.parseDouble(pPrice.getText().toString()) >= Double.parseDouble(minCartValue)) {
                        deliveryCharge.setText("0");
                        txtTotalPrice.setText(String.valueOf(totalAtm));
                    } else {
                        deliveryCharge.setText(delCharge);
                        txtTotalPrice.setText(String.valueOf(Double.parseDouble(String.valueOf(totalAtm)) + Double.parseDouble(delCharge)));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                minimaxAmount();
            }
        }, error -> minimaxAmount());

        AppController.getInstance().getRequestQueue().getCache().clear();
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
    public void getTimeSlot(String timeslot) {
        this.timeslot = timeslot;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            showAdreesUrl();
        }
    }
}