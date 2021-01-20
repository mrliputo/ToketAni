package com.tecmanic.toketani.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tecmanic.toketani.R;
import com.tecmanic.toketani.adapters.OrderAdapter;
import com.tecmanic.toketani.config.BaseURL;
import com.tecmanic.toketani.modelclass.ListAssignAndUnassigned;
import com.tecmanic.toketani.modelclass.NewPendingDataModel;
import com.tecmanic.toketani.modelclass.NewPendingOrderModel;
import com.tecmanic.toketani.modelclass.reordermodel.NewReorderModel;
import com.tecmanic.toketani.modelclass.reordermodel.NewReorderSubModel;
import com.tecmanic.toketani.network.ApiInterface;
import com.tecmanic.toketani.util.AppController;
import com.tecmanic.toketani.util.CustomVolleyJsonArrayRequest;
import com.tecmanic.toketani.util.SessionManagement;
import com.tecmanic.toketani.util.TodayOrderClickListner;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.tecmanic.toketani.config.BaseURL.KEY_MOBILE;
import static com.tecmanic.toketani.config.BaseURL.ORDER_DONE_URL;
import static com.tecmanic.toketani.config.BaseURL.PENDING_ORDER_URL;


public class MyOrderActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String moblie;
    private final String numberPhone = "";

    private ViewPager2 viewPager2;
    private SwipeRefreshLayout swipeTo;
    private final List<ListAssignAndUnassigned> listAssignAndUnassigneds = new ArrayList<>();
    private final List<NewPendingOrderModel> movieList = new ArrayList<>();
    private final List<NewPendingOrderModel> nextdayOrderModels = new ArrayList<>();
    private OrderAdapter adapter;
    private String userId = "";
    private final String pendingKey = "pending";
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
        setContentView(R.layout.activity_my__oreder_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("My Order");
        }
        progressBar = findViewById(R.id.progress_bar);
        toolbar.setNavigationOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask();
            } else {
                finish();
            }
        });

        sharedPreferences = getSharedPreferences(BaseURL.MY_PREPRENCE, MODE_PRIVATE);
        moblie = sharedPreferences.getString(KEY_MOBILE, null);

        SessionManagement sessionManagement = new SessionManagement(MyOrderActivity.this);
        userId = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.viewpager);

        String pendOrderKey = "Pending Orders";
        tabLayout.addTab(tabLayout.newTab().setText(pendOrderKey));
        String pastOrderKey = "Past Order";
        tabLayout.addTab(tabLayout.newTab().setText(pastOrderKey));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        swipeTo = findViewById(R.id.swipe_to);
        tabLayout.addTab(tabLayout.newTab().setText(pendOrderKey));
        tabLayout.addTab(tabLayout.newTab().setText(pastOrderKey));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        listAssignAndUnassigneds.add(new ListAssignAndUnassigned("assigned", movieList, nextdayOrderModels));
        listAssignAndUnassigneds.add(new ListAssignAndUnassigned("unassigned", movieList, nextdayOrderModels));
        adapter = new OrderAdapter(MyOrderActivity.this, listAssignAndUnassigneds, new TodayOrderClickListner() {

            @Override
            public void onCallToDeliveryBoy(String number) {
                if (isPermissionGranted()) {
                    callAction(number);
                }
            }

            @Override
            public void onClickForOrderDetails(int position, String viewType) {
                if (viewType.equalsIgnoreCase(pendingKey)) {
                    String saleId = movieList.get(position).getCartId();
                    String date = movieList.get(position).getDeliveryDate();
                    String time = movieList.get(position).getTimeSlot();
                    String total = movieList.get(position).getPrice();
                    String status = movieList.get(position).getOrderStatus();
                    String deliCharge = movieList.get(position).getDelCharge();
                    Intent intent = new Intent(MyOrderActivity.this, Myorderdetails.class);
                    intent.putExtra("sale_id", saleId);
                    intent.putExtra("pastorder", "false");
                    intent.putExtra("date", date);
                    intent.putExtra("time", time);
                    intent.putExtra("total", total);
                    intent.putExtra("status", status);
                    intent.putExtra("deli_charge", deliCharge);
                    intent.putExtra("data", new ArrayList<>(movieList.get(position).getData()));
                    startActivityForResult(intent, 13);
                } else if (viewType.equalsIgnoreCase("past")) {
                    String saleId = nextdayOrderModels.get(position).getCartId();
                    String date = nextdayOrderModels.get(position).getDeliveryDate();
                    String time = nextdayOrderModels.get(position).getTimeSlot();
                    String total = nextdayOrderModels.get(position).getPrice();
                    String status = nextdayOrderModels.get(position).getOrderStatus();
                    String deliCharge = nextdayOrderModels.get(position).getDelCharge();
                    Intent intent = new Intent(MyOrderActivity.this, Myorderdetails.class);
                    intent.putExtra("sale_id", saleId);
                    intent.putExtra("pastorder", "true");
                    intent.putExtra("date", date);
                    intent.putExtra("time", time);
                    intent.putExtra("total", total);
                    intent.putExtra("status", status);
                    intent.putExtra("deli_charge", deliCharge);
                    intent.putExtra("data", new ArrayList<>(nextdayOrderModels.get(position).getData()));
                    startActivityForResult(intent, 13);
                }
            }

            @Override
            public void onReorderClick(int position, String viewType) {
                if (viewType.equalsIgnoreCase(pendingKey)) {
                    progressBar.setVisibility(View.VISIBLE);
//                    JSONArray array = new JSONArray();
//                    for (int i = 0; i < movieList.get(position).getData().size(); i++) {
//                        Log.i("TAGARRAy", "" + i + "" + movieList.get(position).getData().get(0));
//                        JSONObject productArray = new JSONObject();
//                        try {
////                            productArray.put("qty", movieList.get(position).getData().get(i).getQty());
//                            productArray.put("varient_id", movieList.get(position).getData().get(i).getVarientId());
////                            productArray.put("price", movieList.get(position).getData().get(i).getPrice());
//                            array.put(productArray);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                    Log.i("TAGARRAy", "" + array.toString());
//                    Log.i("storeid", "" + movieList.get(position).getData().get(0).getStoreOrderId());
                    hitReorderServices(movieList.get(position).getData(),movieList.get(position).getCartId(), pendingKey);
                } else if (viewType.equalsIgnoreCase("past")) {
                    progressBar.setVisibility(View.VISIBLE);
//                    JSONArray array = new JSONArray();
//                    for (int i = 0; i < nextdayOrderModels.get(position).getData().size(); i++) {
//                        Log.i("TAGARRAy", "" + i + "" + nextdayOrderModels.get(position).getData().get(0));
//                        JSONObject productArray = new JSONObject();
//                        try {
////                            productArray.put("qty", movieList.get(position).getData().get(i).getQty());
//                            productArray.put("varient_id", nextdayOrderModels.get(position).getData().get(i).getVarientId());
////                            productArray.put("price", movieList.get(position).getData().get(i).getPrice());
//                            array.put(productArray);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                    Log.i("TAGARRAy", "" + array.toString());
//                    Log.i("storeid", "" + nextdayOrderModels.get(position).getData().get(0).getStoreOrderId());
                    hitReorderServices(movieList.get(position).getData(),movieList.get(position).getCartId(),"past");
                }
            }

            @Override
            public void onCancelClick(int position, String viewType) {
                showDeleteDialog(position);
            }
        });
        viewPager2.setAdapter(adapter);
        wrapTabIndicatorToTitle(tabLayout, 80, 80);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, (tab, position) -> {
            if (position == 0) {
                tab.setText("Pending Order");
            } else if (position == 1) {
                tab.setText("Past Order");
            }

        });
        tabLayoutMediator.attach();
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                viewPager2.setCurrentItem(position);
            }
        });
        swipeTo.setOnRefreshListener(() -> makePendingorderRequest(userId));

        makePendingorderRequest(userId);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MyOrderActivity.this, "Permission granted", Toast.LENGTH_SHORT).show();
                callAction(numberPhone);
            } else {
                Toast.makeText(MyOrderActivity.this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void callAction(String number) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + number));
            startActivity(callIntent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(MyOrderActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true;
        }
    }

    @Override
    protected void onDestroy() {
        overridePendingTransition(0, 0);
        super.onDestroy();
    }

    public void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            Intent intent = new Intent(MyOrderActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void wrapTabIndicatorToTitle(TabLayout tabLayout, int externalMargin, int internalMargin) {
        View tabStrip = tabLayout.getChildAt(0);
        if (tabStrip instanceof ViewGroup) {
            ViewGroup tabStripGroup = (ViewGroup) tabStrip;
            int childCount = ((ViewGroup) tabStrip).getChildCount();
            for (int i = 0; i < childCount; i++) {
                View tabView = tabStripGroup.getChildAt(i);
                tabView.setMinimumWidth(0);
                tabView.setPadding(0, tabView.getPaddingTop(), 0, tabView.getPaddingBottom());
                if (tabView.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tabView.getLayoutParams();
                    if (i == 0) {
                        setMargin(layoutParams, externalMargin, internalMargin);
                    } else if (i == childCount - 1) {
                        setMargin(layoutParams, internalMargin, externalMargin);
                    } else {
                        setMargin(layoutParams, internalMargin, internalMargin);
                    }
                }
            }

            tabLayout.requestLayout();
        }
    }

    private void setMargin(ViewGroup.MarginLayoutParams layoutParams, int start, int end) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            layoutParams.setMarginStart(start);
            layoutParams.setMarginEnd(end);
        } else {
            layoutParams.leftMargin = start;
            layoutParams.rightMargin = end;
        }
    }

    private void makePendingorderRequest(String userid) {
        show();
        movieList.clear();
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userid);

        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
                PENDING_ORDER_URL, params, response -> {
            if (response.length() > 0) {
                Gson gson = new Gson();
                Type listType = new TypeToken<List<NewPendingOrderModel>>() {
                }.getType();
                List<NewPendingOrderModel> movieListt = gson.fromJson(response.toString(), listType);
                movieList.addAll(movieListt);
            }
            makePastOrder(userid);
        }, error -> {
            makePastOrder(userid);
            VolleyLog.d("TAG", "Error: " + error.getMessage());
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

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

    private void makePastOrder(String userid) {
        nextdayOrderModels.clear();
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userid);
        CustomVolleyJsonArrayRequest jsonObjReq = new CustomVolleyJsonArrayRequest(Request.Method.POST,
                ORDER_DONE_URL, params, response -> {
            try {
                JSONObject object = response.getJSONObject(0);
                String data = object.getString("data");
                if (!data.equalsIgnoreCase("no orders yet")) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<NewPendingOrderModel>>() {
                    }.getType();
                    List<NewPendingOrderModel> nextdayOrderModelss = gson.fromJson(response.toString(), listType);
                    nextdayOrderModels.addAll(nextdayOrderModelss);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                swipeTo.setRefreshing(false);
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
                show();
            }
        }, error -> {
            swipeTo.setRefreshing(false);
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
            show();
        });
        RequestQueue requestQueue = Volley.newRequestQueue(MyOrderActivity.this);
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

    private void showDeleteDialog(int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MyOrderActivity.this);
        alertDialog.setMessage(getResources().getString(R.string.cancle_order_note));
        alertDialog.setNegativeButton(getResources().getString(R.string.no), (dialogInterface, i) -> dialogInterface.dismiss());
        alertDialog.setPositiveButton(getResources().getString(R.string.yes), (dialogInterface, i) -> {

            Intent intent = new Intent(MyOrderActivity.this, CancelOrder.class);
            intent.putExtra("cart_id", movieList.get(position).getCartId());
            startActivityForResult(intent, 12);
            dialogInterface.dismiss();
        });

        alertDialog.show();
    }

    private void hitReorderServices(List<NewPendingDataModel> datamodel, String cart_id, String pendingKeys) {
//        progressBar.setVisibility(View.VISIBLE);
        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);
        Call<NewReorderModel> reOrderModelCall = apiInterface.getReorderModel(cart_id);
        reOrderModelCall.enqueue(new Callback<NewReorderModel>() {
            @Override
            public void onResponse(@Nullable Call<NewReorderModel> call, @Nullable Response<NewReorderModel> response) {
                progressBar.setVisibility(View.GONE);
//                Log.i("TAGREORDER",""+response.code()+""+response.body().getStatus());
                if (response.isSuccessful() && response.code() == 200 && response.body() != null && response.body().getStatus().equalsIgnoreCase("1")) {
                    ArrayList<NewPendingDataModel> pendingModel = new ArrayList<>();
                    for (int i = 0;i<datamodel.size();i++){
//                        if (response.body().getData().contains(new NewReorderSubModel("",datamodel.get(i).getVarientId(),"","","","","","","","","","","","","","","",""))){
//
//                        }
                        int dataIndex = response.body().getData().indexOf(new NewReorderSubModel("",datamodel.get(i).getVarientId(),"","","","","","","","","","","","","","","",""));
                        if (dataIndex>0){
                            NewReorderSubModel model = response.body().getData().get(dataIndex);
                            NewPendingDataModel pModel = datamodel.get(i);
                            pModel.setP_id(model.getP_id());
                            pModel.setStock(model.getStock());
                            pModel.setPrice(model.getPrice());
                            pModel.setTotalMrp(model.getMrp());
                            pendingModel.add(pModel);
                        }

                    }

//                    ArrayList<NewPendingDataModel> newData = new ArrayList<>();
//                    pendingModel.addAll(datamodel);
//                    newData.addAll(response.body().getData());
//                    pendingModel.retainAll(newData);
                    Intent backresult = new Intent();
                    if (pendingKeys.equalsIgnoreCase("past")) {
                        backresult.putExtra("actIdentfy", "past");
                        backresult.putExtra("datalist", new ArrayList<>(pendingModel));
                        setResult(4, backresult);
                        onBackPressed();
                    } else if (pendingKeys.equalsIgnoreCase(pendingKey)) {
                        backresult.putExtra("actIdentfy", pendingKey);
                        backresult.putExtra("datalist", new ArrayList<>(pendingModel));
                        setResult(4, backresult);
                        onBackPressed();
                    }
                }else {
                    Toast.makeText(MyOrderActivity.this, "All products are Out of stock..", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(@Nullable Call<NewReorderModel> call, @Nullable Throwable t) {
                Log.i("TAGREORDER",""+t.getLocalizedMessage());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MyOrderActivity.this, "Now we sorry for this inconvenience..", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12 || requestCode == 13) {
            makePendingorderRequest(userId);
        }
    }


    @Override
    public void onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }
}

