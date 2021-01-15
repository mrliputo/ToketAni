package com.tecmanic.gogrocer.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.activity.LoginActivity;
import com.tecmanic.gogrocer.activity.RechargeWallet;
import com.tecmanic.gogrocer.config.BaseURL;
import com.tecmanic.gogrocer.config.SharedPref;
import com.tecmanic.gogrocer.util.ConnectivityReceiver;
import com.tecmanic.gogrocer.util.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;


/**
 * Created by Rajesh Dabhi on 29/6/2017.
 */

public class WalletFragment extends Fragment {

    TextView walletAmmount;
    Context context;
    LinearLayout progressBar;
    private SessionManagement sessionManagement;

    public WalletFragment() {

    }

    private void show() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_wallet_ammount, container, false);
        requireActivity().setTitle("Wallet");
        sessionManagement = new SessionManagement(container.getContext());
        context = container.getContext();
        walletAmmount = view.findViewById(R.id.wallet_ammount);
        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setOnClickListener(views -> Log.i("TAG","not work"));
        TextView rechargeWallet = view.findViewById(R.id.recharge_wallet);
        rechargeWallet.setOnClickListener(v -> {
            if (sessionManagement.isLoggedIn()) {
                if (sessionManagement.userBlockStatus().equalsIgnoreCase("2")) {
                    Intent intent = new Intent(v.getContext(), RechargeWallet.class);
                    startActivityForResult(intent, 5);
                } else {
                    showBloackDialog();
                }

            } else {
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        if (ConnectivityReceiver.isConnected()) {
            getRefresrh();
        } else {
            walletAmmount.setText(sessionManagement.getCurrency() + "0");
        }

        return view;

    }

    private void showBloackDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setCancelable(true);
        alertDialog.setMessage("You are blocked from backend.\n Please Contact with customer care!");
        alertDialog.setPositiveButton("Ok", (dialogInterface, i) -> dialogInterface.dismiss());
        alertDialog.show();
    }

    private void getRefresrh() {
        show();
        String userId1 = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        RequestQueue rq = Volley.newRequestQueue(requireActivity());
        StringRequest strReq = new StringRequest(Request.Method.POST, BaseURL.WALLET_REFRESH + userId1,
                response -> {
                    try {
                        JSONObject jObj = new JSONObject(response);
                        String status = jObj.getString("status");
                        if (status.equals("1")) {
                            String walletAmount1 = jObj.getString("data");
                            walletAmmount.setText(sessionManagement.getCurrency() + "" + walletAmount1);
                            SharedPref.putString(context, BaseURL.KEY_WALLET_AMMOUNT, walletAmount1);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        show();
                    }
                }, error -> show()) {
        };
        strReq.setRetryPolicy(new RetryPolicy() {
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
        rq.add(strReq);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 5 && data != null && data.getExtras() != null && Objects.requireNonNull(data.getStringExtra("recharge")).equalsIgnoreCase("success")) {
            getRefresrh();
        }
    }
}