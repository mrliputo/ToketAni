package com.tecmanic.gogrocer.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.adapters.ComplainAdapter;
import com.tecmanic.gogrocer.adapters.MyOrderDetailAdapter;
import com.tecmanic.gogrocer.config.BaseURL;
import com.tecmanic.gogrocer.constans.RecyclerTouchListener;
import com.tecmanic.gogrocer.modelclass.ComplainModel;
import com.tecmanic.gogrocer.modelclass.NewPendingDataModel;
import com.tecmanic.gogrocer.util.AppController;
import com.tecmanic.gogrocer.util.ConnectivityReceiver;
import com.tecmanic.gogrocer.util.CustomVolleyJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Myorderdetails extends AppCompatActivity {
    SharedPreferences preferences;
    List<ComplainModel> complainModels = new ArrayList<>();
    private RelativeLayout btnCancle;
    private RecyclerView rvDetailOrder;
    private String saleId;
    private List<NewPendingDataModel> myOrderDetailModelList = new ArrayList<>();
    private LinearLayout cancelLay;
    private String reason;
    private boolean isCancel = false;
    private String status = "";
    private String statusKey = "status";
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
        setContentView(R.layout.activity_myorderdetails);
        btnCancle = findViewById(R.id.btn_order_detail_cancle);
        progressBar = findViewById(R.id.progress_bar);
        rvDetailOrder = findViewById(R.id.rv_order_detail);
        RecyclerView rvCancelOrder = findViewById(R.id.rv_cancel_order);
        ImageView backTool = findViewById(R.id.back_tool);
        cancelLay = findViewById(R.id.cancel_lay);
        rvDetailOrder.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        rvCancelOrder.setLayoutManager(new LinearLayoutManager(Myorderdetails.this, RecyclerView.VERTICAL, false));
        ComplainAdapter complainAdapter = new ComplainAdapter(complainModels);
        rvCancelOrder.setAdapter(complainAdapter);
        backTool.setOnClickListener(v -> onBackPressed());
        cancelLay.setVisibility(View.GONE);
        saleId = getIntent().getStringExtra("sale_id");
        status = getIntent().getStringExtra(statusKey);
        myOrderDetailModelList.clear();
        myOrderDetailModelList = (List<NewPendingDataModel>) getIntent().getSerializableExtra("data");
        if (status.equals("Completed") || status.equals("Out_For_Delivery") || status.equals("Cancelled")) {
            btnCancle.setVisibility(View.GONE);
        } else {
            btnCancle.setVisibility(View.VISIBLE);
        }
        preferences = getSharedPreferences("lan", MODE_PRIVATE);

        rvCancelOrder.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), rvCancelOrder, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                try {
                    show();
                    reason = complainModels.get(position).getReason();
                    deleteorder();
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));


        myOrderDetailModelList = (List<NewPendingDataModel>) getIntent().getSerializableExtra("data");
        MyOrderDetailAdapter adapter = new MyOrderDetailAdapter(myOrderDetailModelList);
        rvDetailOrder.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        btnCancle.setOnClickListener(view -> showDeleteDialog(complainAdapter));

    }

    private void showDeleteDialog(ComplainAdapter complainAdapter) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Myorderdetails.this);
        alertDialog.setMessage(getResources().getString(R.string.cancle_order_note));
        alertDialog.setNegativeButton(getResources().getString(R.string.no), (dialogInterface, i) -> dialogInterface.dismiss());
        alertDialog.setPositiveButton(getResources().getString(R.string.yes), (dialogInterface, i) -> {

            if (ConnectivityReceiver.isConnected()) {
                show();
                complainQues(complainAdapter);
            }

            dialogInterface.dismiss();
        });

        alertDialog.show();
    }

    private void complainQues(ComplainAdapter complainAdapter) {

        complainModels.clear();
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
                BaseURL.DELETE_ORDER_URL, null, response -> {
            try {
                String status1 = response.getString(statusKey);
                String message = response.getString("message");
                if (status1.equalsIgnoreCase("1")) {
//                    JSONArray jsonArray = response.getJSONArray("data");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        complainModels.clear();
//                        reason = jsonObject.getString("reason");
//                        ComplainModel complainModel = new ComplainModel();
//                        complainModel.setReason(reason);
//                        complainModels.add(complainModel);
//                    }
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<ComplainModel>>() {
                    }.getType();
                    List<ComplainModel> movieListt = gson.fromJson(response.getString("data"), listType);
                    complainModels.addAll(movieListt);
                    complainAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(Myorderdetails.this, "" + message, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (complainModels.isEmpty()) {
                    cancelLay.setVisibility(View.GONE);
                    rvDetailOrder.setVisibility(View.VISIBLE);
                    btnCancle.setVisibility(View.VISIBLE);
                } else {
                    cancelLay.setVisibility(View.VISIBLE);
                    rvDetailOrder.setVisibility(View.GONE);
                    btnCancle.setVisibility(View.GONE);
                }
                show();
            }
        }, error -> {
            if (complainModels.isEmpty()) {
                cancelLay.setVisibility(View.GONE);
                rvDetailOrder.setVisibility(View.VISIBLE);
                btnCancle.setVisibility(View.VISIBLE);
            } else {
                cancelLay.setVisibility(View.VISIBLE);
                rvDetailOrder.setVisibility(View.GONE);
                btnCancle.setVisibility(View.GONE);
            }
            show();
        });

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
        AppController.getInstance().getRequestQueue().add(jsonObjReq);
    }

    private void deleteorder() {
        Map<String, String> params = new HashMap<>();
        params.put("cart_id", saleId);
        params.put("reason", reason);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.DELETE_ORDER, params, response -> {
            try {
                String status2 = response.getString(statusKey);
                String message = response.getString("message");
                if (status2.contains("1")) {
                    showCancelDialog();
                    Toast.makeText(Myorderdetails.this, "" + message, Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                show();
            }
        }, error -> show());

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

    private void showCancelDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Myorderdetails.this);
        alertDialog.setCancelable(true);
        alertDialog.setMessage("You product has been cancel!");
        alertDialog.setPositiveButton("Ok", (dialogInterface, i) -> {
            dialogInterface.dismiss();
            Intent intent = new Intent();
            setResult(13, intent);
            isCancel = true;
            onBackPressed();
        });

        alertDialog.show();
    }

    private void doneFinish() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {

        if (isCancel) {
            doneFinish();
        } else {
            if (cancelLay.getVisibility() == View.VISIBLE) {
                cancelLay.setVisibility(View.GONE);
                rvDetailOrder.setVisibility(View.VISIBLE);
                if (status.equals("Completed") || status.equals("Out_For_Delivery") || status.equals("Cancelled")) {
                    btnCancle.setVisibility(View.GONE);
                } else {
                    btnCancle.setVisibility(View.VISIBLE);
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAndRemoveTask();
                } else {
                    finish();
                }
            }
        }


    }
}
