package com.tecmanic.toketani.fragments;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tecmanic.toketani.R;
import com.tecmanic.toketani.modelclass.SupportInfoModel;
import com.tecmanic.toketani.util.AppController;
import com.tecmanic.toketani.util.ConnectivityReceiver;

import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ContactUsFragment extends Fragment {

    private static final String TAG = ContactUsFragment.class.getSimpleName();

    private TextView tvInfo;


    public ContactUsFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
        tvInfo = view.findViewById(R.id.tv_info);
        String geturl = getArguments().getString("url");
        getActivity().setTitle("Contact Us");
        if (ConnectivityReceiver.isConnected()) {
            makeGetInfoRequest(geturl);
        }
        ImageView ivCall = view.findViewById(R.id.iv_call);
        ImageView ivWhatspp = view.findViewById(R.id.iv_whatsapp);
        ivCall.setOnClickListener(v -> {
            if (isPermissionGranted()) {
                callAction("9889887711");
            }
        });
        ivWhatspp.setOnClickListener(v -> {
            String smsNumber = "9889887711";
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(smsNumber) + "@s.whatsapp.net");//phone number without "+" prefix
            startActivity(sendIntent);

        });

        return view;
    }

    public boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG", "Permission is granted");
                return true;
            } else {

                Log.v("TAG", "Permission is revoked");
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG", "Permission is granted");
            return true;
        }

    }

    public void callAction(String number) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + number));

        startActivity(callIntent);
    }

    /**
     * Method to make json object request where json response starts wtih
     */
    private void makeGetInfoRequest(String url) {
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null, response -> {
            try {
                boolean status = response.getBoolean("responce");
                if (status) {
                    List<SupportInfoModel> supportInfoModels = new ArrayList<>();
                    Gson gson = new Gson();
                    Type listType = new TypeToken<List<SupportInfoModel>>() {
                    }.getType();
                    supportInfoModels = gson.fromJson(response.getString("data"), listType);

                    String desc = supportInfoModels.get(0).getPgDescri();

                    tvInfo.setText(HtmlCompat.fromHtml(desc, 0));

                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(),
                        "Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }, error -> VolleyLog.d(TAG, "Error: " + error.getMessage()));

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

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }

}

