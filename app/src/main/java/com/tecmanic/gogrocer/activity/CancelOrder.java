package com.tecmanic.gogrocer.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.adapters.ComplainAdapter;
import com.tecmanic.gogrocer.config.BaseURL;
import com.tecmanic.gogrocer.constans.RecyclerTouchListener;
import com.tecmanic.gogrocer.modelclass.ComplainModel;
import com.tecmanic.gogrocer.modelclass.NewPendingOrderModel;
import com.tecmanic.gogrocer.util.AppController;
import com.tecmanic.gogrocer.util.CustomVolleyJsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CancelOrder extends AppCompatActivity {
    RecyclerView rcComplain;
    List<ComplainModel> complainModels = new ArrayList<>();
    String reason;
    String cartId;
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
        setContentView(R.layout.activity_cancel__order);
        progressBar = findViewById(R.id.progress_bar);
        cartId = Objects.requireNonNull(getIntent().getExtras()).getString("cart_id");
        rcComplain = findViewById(R.id.rc_complain);
        LinearLayoutManager gridLayoutManagercat1 = new LinearLayoutManager(CancelOrder.this, LinearLayoutManager.VERTICAL, false);
        rcComplain.setLayoutManager(gridLayoutManagercat1);

        rcComplain.addOnItemTouchListener(new
                RecyclerTouchListener(getApplicationContext(), rcComplain, new RecyclerTouchListener.OnItemClickListener() {
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

        complainQues();
    }

    private void complainQues() {
        complainModels.clear();
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
                BaseURL.DELETE_ORDER_URL, null, response -> {
            try {
                String status = response.getString("status");
                String message = response.getString("message");
                if (status.equalsIgnoreCase("1")) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<ComplainModel>>() {
                    }.getType();
                    List<ComplainModel> movieListt = gson.fromJson(response.getString("data"), listType);
                    complainModels.addAll(movieListt);
//                    JSONArray jsonArray = response.getJSONArray("data");
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        complainModels.clear();
//                        reason = jsonObject.getString("reason");
//                        ComplainModel complainModel = new ComplainModel();
//                        complainModel.setReason(reason);
//                        complainModels.add(complainModel);
//                    }

                    ComplainAdapter complainAdapter = new ComplainAdapter(complainModels);
                    rcComplain.setAdapter(complainAdapter);
                    complainAdapter.notifyDataSetChanged();

                } else {

                    Toast.makeText(CancelOrder.this, "" + message, Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> VolleyLog.d("TAG", "Error: " + error.getMessage()));

        // Adding request to request queue
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
        AppController.getInstance().getRequestQueue().getCache().clear();
        AppController.getInstance().addToRequestQueue(jsonObjReq);


    }

    private void deleteorder() {
        Map<String, String> params = new HashMap<>();
        params.put("cart_id", cartId);
        params.put("reason", reason);

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.DELETE_ORDER, params, response -> {
            try {
                String status = response.getString("status");
                String message = response.getString("message");
                if (status.contains("1")) {
                    showCancelDialog();
                    Toast.makeText(CancelOrder.this, "" + message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                show();
            }

        }, error -> {
            show();
            VolleyLog.d("", "Error: " + error.getMessage());
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


    private void showCancelDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CancelOrder.this);
        alertDialog.setCancelable(true);
        alertDialog.setMessage("You product has been cancel!");
        alertDialog.setPositiveButton("Ok", (dialogInterface, i) -> {
            dialogInterface.dismiss();
            Intent intent = new Intent();
            setResult(12, intent);
            onBackPressed();
        });

        alertDialog.show();
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
