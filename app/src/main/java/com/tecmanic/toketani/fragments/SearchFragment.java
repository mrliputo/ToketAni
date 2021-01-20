package com.tecmanic.toketani.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tecmanic.toketani.R;
import com.tecmanic.toketani.activity.ProductDetails;
import com.tecmanic.toketani.adapters.SearchProdcutAdapter;
import com.tecmanic.toketani.constans.RecyclerTouchListener;
import com.tecmanic.toketani.modelclass.SearchProductModel;
import com.tecmanic.toketani.modelclass.SubProductList;
import com.tecmanic.toketani.util.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tecmanic.toketani.config.BaseURL.SEARCH;

public class SearchFragment extends Fragment {

    RecyclerView recyclerSearch;
    EditText txtSearch;
    ContentLoadingProgressBar progressDialog;
    SearchProdcutAdapter searchAdapter;
    List<SearchProductModel> searchlist = new ArrayList<>();
    String seaarchId;
    String seaarchName;
    private SessionManagement sessionManagement;

    public SearchFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search, container, false);
        recyclerSearch = view.findViewById(R.id.recyclerSearch);
        txtSearch = view.findViewById(R.id.txtSearch);
        sessionManagement = new SessionManagement(container.getContext());
        progressDialog = view.findViewById(R.id.progress_bar);
        progressDialog.hide();
        if (isOnline()) {
            txtSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if (txtSearch.getText().length() > 2) {
                        searchlist.clear();
                        searchUrl(txtSearch.getText().toString().trim());
                    }
                }
            });
        }

        recyclerSearch.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerSearch, new RecyclerTouchListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                seaarchId = searchlist.get(position).getId();
                seaarchName = searchlist.get(position).getpNAme();
                Intent intent = new Intent(getActivity(), ProductDetails.class);
                intent.putExtra("sId", seaarchId);
                intent.putExtra("sName", seaarchName);
                intent.putExtra("sVariant_id", searchlist.get(position).getSubprodList().getVarientId());
                intent.putExtra("descrip", searchlist.get(position).getSubprodList().getDescription());
                intent.putExtra("price", searchlist.get(position).getSubprodList().getPrice());
                intent.putExtra("mrp", searchlist.get(position).getSubprodList().getMrp());
                intent.putExtra("unit", searchlist.get(position).getSubprodList().getUnit());
                intent.putExtra("stock", searchlist.get(position).getSubprodList().getStock());
                intent.putExtra("qty", searchlist.get(position).getSubprodList().getQuantity());
                intent.putExtra("image", searchlist.get(position).getSubprodList().getVarientImage());
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
        return view;
    }

    private void searchUrl(final String name) {
        progressDialog.show();
        searchlist.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, SEARCH, response -> {
            Log.i("TAG",response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                String msg = jsonObject.getString("message");
                if (status.equals("1")) {
                    searchlist.clear();
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String productId = jsonObject1.getString("product_id");
                        String productName = jsonObject1.getString("product_name");
                        JSONArray arra1 = jsonObject1.getJSONArray("varients");
                        for (int j = 0; j < arra1.length(); j++) {
                            JSONObject jsonObject12 = arra1.getJSONObject(j);
                            String varientId = jsonObject12.getString("varient_id");
                            String quantity = jsonObject12.getString("quantity");
                            String unit = jsonObject12.getString("unit");
                            String mrp = jsonObject12.getString("mrp");
                            String price = jsonObject12.getString("price");
                            String description = jsonObject12.getString("description");
                            String stock = jsonObject12.getString("stock");
                            String varientImage = jsonObject12.getString("varient_image");

                            SearchProductModel ss = new SearchProductModel(productId, productName, new SubProductList(varientId, productId, quantity, unit, mrp, price, description, varientImage, stock));
                            searchlist.add(ss);
                        }

                    }
                    searchAdapter = new SearchProdcutAdapter(searchlist);
                    recyclerSearch.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                    recyclerSearch.setAdapter(searchAdapter);
                    searchAdapter.notifyDataSetChanged();

                } else {
                    searchAdapter = new SearchProdcutAdapter(searchlist);
                    recyclerSearch.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
                    recyclerSearch.setAdapter(searchAdapter);
                    searchAdapter.notifyDataSetChanged();

                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                progressDialog.hide();
            }

        }, error -> {
            searchAdapter = new SearchProdcutAdapter(searchlist);
            recyclerSearch.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));
            recyclerSearch.setAdapter(searchAdapter);
            searchAdapter.notifyDataSetChanged();
            progressDialog.hide();

        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("keyword", name);
                params.put("lat", sessionManagement.getLatPref());
                params.put("lng", sessionManagement.getLangPref());
                params.put("city", sessionManagement.getLocationCity());
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
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

            }
        });
        requestQueue.add(stringRequest);

    }

    private boolean isOnline() {
        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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
