package com.tecmanic.gogrocer.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.activity.CategoryPage;
import com.tecmanic.gogrocer.adapters.HomeCategoryAdapter;
import com.tecmanic.gogrocer.modelclass.HomeCate;
import com.tecmanic.gogrocer.util.CustomVolleyJsonRequest;
import com.tecmanic.gogrocer.util.FragmentClickListner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tecmanic.gogrocer.config.BaseURL.CATEGORIES;

public class CategoryFragment extends Fragment {

    RecyclerView recyclerView;
    HomeCategoryAdapter cateAdapter;
    private List<HomeCate> cateList = new ArrayList<>();
    private FragmentClickListner fragmentClickListner;
    private LinearLayout progressBar;

    public CategoryFragment(FragmentClickListner fragmentClickListner) {
        this.fragmentClickListner = fragmentClickListner;
    }

    private void show() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        recyclerView = view.findViewById(R.id.recyclerCAte);
        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setOnClickListener(views -> Log.i("TAG","not work"));
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        getActivity().setTitle(getResources().getString(R.string.Category));
        if (isOnline(container.getContext())) {
            categoryUrl();
        }
        return view;
    }

    private void categoryUrl() {
        show();
        cateList.clear();
        Map<String, String> params = new HashMap<>();
        params.put("parent", "");
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.GET,
                CATEGORIES, params, response -> {
            try {
                if (response != null && response.length() > 0) {
                    String status = response.getString("status");
                    if (status.equals("1")) {
                        JSONArray array = response.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject object = array.getJSONObject(i);
                            HomeCate model = new HomeCate();
                            model.setDetail(object.getString("description"));
                            model.setId(object.getString("cat_id"));
                            model.setImages(object.getString("image"));
                            model.setName(object.getString("title"));
                            model.setSubArray(object.getJSONArray("subcategory"));
                            cateList.add(model);
                        }
                        cateAdapter = new HomeCategoryAdapter(cateList, getContext(), catIds -> {
                            Intent intent = new Intent(requireActivity(), CategoryPage.class);
                            intent.putExtra("cat_id", catIds);
                            startActivityForResult(intent, 24);
                        });
                        recyclerView.setAdapter(cateAdapter);
                        cateAdapter.notifyDataSetChanged();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                show();
            }
        }, error -> show());

        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
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

            }
        });
        requestQueue.add(jsonObjReq);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 24 && data != null && data.getBooleanExtra("open", false) && fragmentClickListner != null) {
            fragmentClickListner.onFragmentClick(data.getBooleanExtra("open", false));
        }
    }

    private boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
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
