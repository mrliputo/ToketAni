package com.tecmanic.gogrocer.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tecmanic.gogrocer.Categorygridquantity;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.adapters.AdapterPopup;
import com.tecmanic.gogrocer.adapters.CategoryGridAdapter;
import com.tecmanic.gogrocer.config.BaseURL;
import com.tecmanic.gogrocer.modelclass.NewCategoryDataModel;
import com.tecmanic.gogrocer.modelclass.NewCategoryVarientList;
import com.tecmanic.gogrocer.util.AppController;
import com.tecmanic.gogrocer.util.CustomVolleyJsonRequest;
import com.tecmanic.gogrocer.util.DatabaseHandler;
import com.tecmanic.gogrocer.util.SessionManagement;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryPage extends AppCompatActivity {

    RecyclerView recyclerProduct;
    CategoryGridAdapter adapter;
    List<NewCategoryDataModel> newCategoryDataModel = new ArrayList<>();
    String catId;
    String image;
    String title;
    private BottomSheetBehavior<View> behavior;
    private List<NewCategoryVarientList> varientProducts = new ArrayList<>();
    private LinearLayout bottomLayTotal;
    private TextView totalCount;
    private TextView totalPrice;
    private SessionManagement sessionManagement;
    private DatabaseHandler dbcart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_page);
        sessionManagement = new SessionManagement(CategoryPage.this);
        recyclerProduct = findViewById(R.id.recycler_product);
        bottomLayTotal = findViewById(R.id.bottom_lay_total);
        totalPrice = findViewById(R.id.total_price);
        totalCount = findViewById(R.id.total_count);
        TextView continueTocart = findViewById(R.id.continue_tocart);
        LinearLayout bottomSheet = findViewById(R.id.bottom_sheet);
        LinearLayout back = findViewById(R.id.back);
        behavior = BottomSheetBehavior.from(bottomSheet);
        catId = getIntent().getStringExtra("cat_id");
        image = getIntent().getStringExtra("image");
        title = getIntent().getStringExtra("title");
        dbcart = new DatabaseHandler(CategoryPage.this);
        back.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("open", false);
            setResult(24, intent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask();
            } else {
                finish();
            }
        });
        Categorygridquantity categorygridquantity = new Categorygridquantity() {
            @Override
            public void onClick(View view, int position, String ccId, String id) {
                varientProducts.clear();
                TextView txt = findViewById(R.id.txt);
                txt.setText(id);
                LinearLayout cancl = findViewById(R.id.cancl);
                cancl.setOnClickListener(v -> behavior.setState(BottomSheetBehavior.STATE_COLLAPSED));
                RecyclerView recylerPopup = findViewById(R.id.recyclerVarient);
                recylerPopup.setLayoutManager(new LinearLayoutManager(CategoryPage.this));
                List<NewCategoryVarientList> newCatMod = new ArrayList<>();
                newCatMod.addAll(newCategoryDataModel.get(position).getVarients());
//                int indexd = newCatMod.indexOf(new NewCategoryVarientList(newCategoryDataModel.get(position).getVarientId()));
                newCatMod.remove(new NewCategoryVarientList(newCategoryDataModel.get(position).getVarientId()));
                if (newCatMod!=null && newCatMod.size()>0){

//                List<NewCategoryVarientList> newCatMod1 = new ArrayList<>();
                    varientProducts.addAll(newCatMod);
                    AdapterPopup selectCityAdapter = new AdapterPopup(CategoryPage.this, varientProducts, id, position1 -> {
                        if (varientProducts.get(position1).getVarientId().equalsIgnoreCase(newCategoryDataModel.get(position).getVarientId())) {
                            adapter.notifyItemChanged(position);
                        }
                    });
                    recylerPopup.setAdapter(selectCityAdapter);
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }else {
                    Toast.makeText(CategoryPage.this, "No variant available for this product..", Toast.LENGTH_SHORT).show();
                }
                
            }

            @Override
            public void onCartItemAddOrMinus() {
                if (dbcart.getCartCount() > 0) {
                    bottomLayTotal.setVisibility(View.VISIBLE);
                    totalPrice.setText(sessionManagement.getCurrency() + " " + dbcart.getTotalAmount());
                    totalCount.setText("Total Items " + dbcart.getCartCount());
                } else {
                    bottomLayTotal.setVisibility(View.GONE);
                }
            }

            @Override
            public void onProductDetials(int position) {
                Intent intent = new Intent(CategoryPage.this, ProductDetails.class);
                intent.putExtra("sId", newCategoryDataModel.get(position).getProductId());
                intent.putExtra("sName", newCategoryDataModel.get(position).getProductName());
                intent.putExtra("descrip", newCategoryDataModel.get(position).getDescription());
                intent.putExtra("price", newCategoryDataModel.get(position).getPrice());
                intent.putExtra("mrp", newCategoryDataModel.get(position).getMrp());
                intent.putExtra("unit", newCategoryDataModel.get(position).getUnit());
                intent.putExtra("qty", newCategoryDataModel.get(position).getQuantity());
                intent.putExtra("stock", newCategoryDataModel.get(position).getStock());
                intent.putExtra("image", newCategoryDataModel.get(position).getVarientImage());
                intent.putExtra("sVariant_id", newCategoryDataModel.get(position).getVarientId());
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivityForResult(intent,21);
            }
        };


        recyclerProduct.setLayoutManager(new GridLayoutManager(this, 1));
        adapter = new CategoryGridAdapter(newCategoryDataModel, CategoryPage.this, categorygridquantity);
        recyclerProduct.setAdapter(adapter);

        continueTocart.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("open", true);
            setResult(24, intent);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask();
            } else {
                finish();
            }
        });
        if (dbcart.getCartCount() > 0) {
            bottomLayTotal.setVisibility(View.VISIBLE);
            totalPrice.setText(sessionManagement.getCurrency() + " " + dbcart.getTotalAmount());
            totalCount.setText("Total Items (" + dbcart.getCartCount() + ")");
        } else {
            bottomLayTotal.setVisibility(View.GONE);
        }

        product(catId);


    }

    private void product(String catIds) {
        newCategoryDataModel.clear();
        Map<String, String> params = new HashMap<>();
        params.put("cat_id", catIds);
        params.put("lat", sessionManagement.getLatPref());
        params.put("lng", sessionManagement.getLangPref());
        params.put("city", sessionManagement.getLocationCity());

        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.CAT_PRODUCT, params, response -> {
            try {
                String status = response.getString("status");
                if (status.contains("1")) {
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<NewCategoryDataModel>>() {
                    }.getType();
                    List<NewCategoryDataModel> listorl = gson.fromJson(response.getString("data"), listType);
                    newCategoryDataModel.addAll(listorl);
                    adapter.notifyDataSetChanged();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            error.printStackTrace();
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
                error.printStackTrace();
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 21 && adapter!=null){
            adapter.notifyDataSetChanged();
        }
    }
}
