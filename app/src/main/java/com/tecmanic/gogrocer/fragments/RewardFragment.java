package com.tecmanic.gogrocer.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.config.BaseURL;
import com.tecmanic.gogrocer.util.ConnectivityReceiver;
import com.tecmanic.gogrocer.util.GifImageView;
import com.tecmanic.gogrocer.util.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.tecmanic.gogrocer.config.BaseURL.MYPROFILE;
import static com.tecmanic.gogrocer.config.BaseURL.REDEEM_REWARDS;


/**
 * Created by Rajesh Dabhi on 29/6/2017.
 */

public class RewardFragment extends Fragment {
    TextView reedeemPoints;
    TextView rewardsPoints;
    private SessionManagement sessionManagement;
    private LinearLayout progressBar;

    public RewardFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_reward_points, container, false);
        requireActivity().setTitle("Reward");
        sessionManagement = new SessionManagement(getActivity());
        String getrewards = sessionManagement.getUserDetails().get(BaseURL.KEY_REWARDS_POINTS);
        rewardsPoints = (TextView) view.findViewById(R.id.reward_points);
        GifImageView gifImageView = (GifImageView) view.findViewById(R.id.gif_image);
        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setOnClickListener(views -> Log.i("TAG","not work"));
        gifImageView.setGifImageResource(R.drawable.pay);
        if (getrewards != null && !getrewards.equalsIgnoreCase("")) {
            rewardsPoints.setText(getrewards);
        } else {
            rewardsPoints.setText("0");
        }
        reedeemPoints = view.findViewById(R.id.reedme_point);
        reedeemPoints.setEnabled(false);
        reedeemPoints.setAlpha(0.5f);
        reedeemPoints.setOnClickListener(v -> {
            redeemPoints();
            gifImageView.setVisibility(View.VISIBLE);
            final View myview = gifImageView;

            view.postDelayed(() -> myview.setVisibility(View.GONE), 5000);
        });

        if (ConnectivityReceiver.isConnected()) {
            getRewards();
        }
        return view;

    }


    private void show() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
    }


    private void redeemPoints() {
        show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REDEEM_REWARDS, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                String message = jsonObject.getString("message");
                if (status.equals("1")) {
                    rewardsPoints.setText("0");
                    Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                show();
            }

        }, error -> {
            show();
            Toast.makeText(getContext(), "Try after sometime", Toast.LENGTH_SHORT).show();
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", sessionManagement.getUserDetails().get(BaseURL.KEY_ID));
                return params;
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
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }


    private void getRewards() {
        show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, MYPROFILE, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                if (status.equals("1")) {
                    JSONObject jsonArray = jsonObject.getJSONObject("data");

                    String rewards = jsonArray.getString("rewards");

                    rewardsPoints.setText(rewards);
                    if (rewards.equals("0")) {
                        reedeemPoints.setEnabled(false);
                        reedeemPoints.setAlpha(0.8f);
                    } else {
                        reedeemPoints.setEnabled(true);
                        reedeemPoints.setAlpha(1f);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                show();
            }

        }, error -> show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", sessionManagement.getUserDetails().get(BaseURL.KEY_ID));
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
                error.printStackTrace();
            }
        });
        requestQueue.add(stringRequest);
    }


}