package com.tecmanic.gogrocer.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.config.BaseURL;
import com.tecmanic.gogrocer.modelclass.DeliveryModel;
import com.tecmanic.gogrocer.util.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tecmanic.gogrocer.config.BaseURL.SELECT_ADDRESS_URL;
import static com.tecmanic.gogrocer.config.BaseURL.SHOW_ADDRESS;

public class SelectAddress extends AppCompatActivity {
    private static final int REQUEST_LOCATION_PERMISSION = 123;
    SessionManagement sessionManagement;
    LinearLayout back;
    LinearLayout addAddress;
    RecyclerView recycleraddressList;
    List<DeliveryModel> dlist;
    DeliveryAdapter deliveryAdapter;
    String userId;
    private int lastSelectedPosition = -1;
    private boolean showAdd = false;
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
        setContentView(R.layout.activity_select_address);
        dlist = new ArrayList<>();
        init();
    }

    private void init() {
        back = findViewById(R.id.back);
        addAddress = findViewById(R.id.addAdreess);
        recycleraddressList = findViewById(R.id.recycleraddressList);
        progressBar = findViewById(R.id.progress_bar);

        back.setOnClickListener(v -> {
            showAdd = false;
            onBackPressed();
        });
        sessionManagement = new SessionManagement(getApplicationContext());
        userId = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);

        addAddress.setOnClickListener(v -> {
            if (!userId.equalsIgnoreCase("")) {
                if (ContextCompat.checkSelfPermission(SelectAddress.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(SelectAddress.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    checkAndRequestPermissions(true);
                }else {
                    Intent intf = new Intent(SelectAddress.this, AddAddressNew.class);
                    startActivityForResult(intf, 21);
                }
            } else {
                Intent intf = new Intent(SelectAddress.this, LoginActivity.class);
                startActivity(intf);
                finish();
            }
        });
        showAdreesUrl();
    }

    private void checkAndRequestPermissions(boolean status) {

        int locationPermission = ContextCompat.checkSelfPermission(SelectAddress.this, Manifest.permission.ACCESS_FINE_LOCATION);
        int locationPermissionCoarse = ContextCompat.checkSelfPermission(SelectAddress.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (locationPermissionCoarse != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(SelectAddress.this);
            alertDialog.setCancelable(false);
            alertDialog.setTitle("Location permission for grocery door step delivery");
            alertDialog.setMessage("We need location for the following things and mentioned here.\n1. To show you your nearest grocery store at you location.\n2. To save your delivery address to provide your grocery at your door step or where you want.");
            alertDialog.setPositiveButton("Ok", (dialogInterface, i) -> {
                ActivityCompat.requestPermissions(SelectAddress.this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_LOCATION_PERMISSION);
                dialogInterface.dismiss();
            });
            alertDialog.setNegativeButton("dismiss", (dialogInterface, i) -> {
                dialogInterface.dismiss();
            });
            alertDialog.show();
        }
    }

    private void showAdreesUrl() {
        dlist.clear();
        show();
        if (!userId.equalsIgnoreCase("")) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, SHOW_ADDRESS, response -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equals("1")) {
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
                            dlist.add(ss);
                        }
                        deliveryAdapter = new DeliveryAdapter(dlist);
                        recycleraddressList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        recycleraddressList.setAdapter(deliveryAdapter);
                        deliveryAdapter.notifyDataSetChanged();

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
                    param.put("user_id", userId);
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
            RequestQueue requestQueue = Volley.newRequestQueue(SelectAddress.this);
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);
        } else {
            show();
        }


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("show_address", showAdd);
        setResult(2, intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAndRemoveTask();
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 21 && data != null && data.getBooleanExtra("select_address", false)) {
            showAdreesUrl();
        }
    }

    public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.MyViewHolder> {
        private List<DeliveryModel> dlist;

        public DeliveryAdapter(List<DeliveryModel> dlist) {
            this.dlist = dlist;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_deliveryaddress, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            DeliveryModel dd = dlist.get(position);
            holder.name.setText(dd.getName());
            holder.address.setText(dd.getAddress());
            holder.phone.setText(dd.getPhone());
            holder.radioButton.setChecked(lastSelectedPosition == position);

            holder.layout.setOnClickListener(v -> {
                Intent intent = new Intent(getApplicationContext(), OrderSummary.class);
                intent.putExtra("dId", dd.getId());
                intent.putExtra("dName", dd.getName());
                startActivity(intent);
            });
            holder.edit.setOnClickListener(v -> {
                Intent i = new Intent(SelectAddress.this, AddAddress.class);
                i.putExtra("update", "UPDATE");
                i.putExtra("addId", dd.getId());
                i.putExtra("city_name", dd.getCityName());
                i.putExtra("society", dd.getSociety());
                i.putExtra("receiver_name", dd.getReceiverName());
                i.putExtra("receiver_phone", dd.getReceiverPhone());
                i.putExtra("house_no", dd.getHouseNo());
                i.putExtra("landmark", dd.getLandmark());
                i.putExtra("state", dd.getState());
                i.putExtra("pincode", dd.getPincode());
                Log.d("ff", dd.getId());
                startActivity(i);
            });
            holder.radioButton.setOnClickListener(v -> {

                lastSelectedPosition = position;
                selectAddrsUrl(dlist.get(position).getId());
                notifyDataSetChanged();
            });
        }

        @Override
        public int getItemCount() {
            return dlist.size();
        }

        private void selectAddrsUrl(String id) {
            show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, SELECT_ADDRESS_URL, response -> {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String msg = jsonObject.getString("message");
                    if (status.equals("1")) {
                        showAdd = true;
                        onBackPressed();
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
                Toast.makeText(SelectAddress.this, "Server error ", Toast.LENGTH_SHORT).show();
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> param = new HashMap<>();
                    param.put("address_id", id);
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
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.getCache().clear();
            requestQueue.add(stringRequest);

        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView name;
            TextView phone;
            TextView address;
            ImageView editAddress;
            RadioButton radioButton;
            LinearLayout edit;
            LinearLayout layout;

            public MyViewHolder(View view) {
                super(view);
                radioButton = view.findViewById(R.id.radioButton);
                layout = view.findViewById(R.id.layout);
                editAddress = view.findViewById(R.id.edit_address);
                name = (TextView) view.findViewById(R.id.txt_name_myhistoryaddrss_item);
                phone = (TextView) view.findViewById(R.id.txt_mobileno_myaddrss_history);
                address = (TextView) view.findViewById(R.id.txt_address_myaddrss_history);
                edit = view.findViewById(R.id.edit);
            }
        }
    }
}
