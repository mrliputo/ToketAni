package com.tecmanic.gogrocer.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.config.BaseURL;
import com.tecmanic.gogrocer.util.ConnectivityReceiver;

import org.json.JSONException;
import org.json.JSONObject;


public class TermsAndConditionFragment extends Fragment {

    public TermsAndConditionFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terms_condition, container, false);
        TextView tvInfo = (TextView) view.findViewById(R.id.tv_info);
        requireActivity().setTitle("Terms & Conditions");

        if (ConnectivityReceiver.isConnected()) {
            makeGetInfoRequest(tvInfo);
        }

        return view;
    }

    private void makeGetInfoRequest(TextView tvInfo) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, BaseURL.TERMS_URL, null, response -> {
            try {
                String status = response.getString("status");
                if (status.contains("1")) {
                    JSONObject jsonObject = response.getJSONObject("data");
                    String description = jsonObject.getString("description");
                    tvInfo.setText(HtmlCompat.fromHtml(description, 0));
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }, Throwable::printStackTrace);
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
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.getCache().clear();
        requestQueue.add(jsonObjReq);
    }

}

