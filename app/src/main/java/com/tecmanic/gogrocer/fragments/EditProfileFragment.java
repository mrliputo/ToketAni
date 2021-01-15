package com.tecmanic.gogrocer.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.activity.MainActivity;
import com.tecmanic.gogrocer.config.BaseURL;
import com.tecmanic.gogrocer.modelclass.ForgotEmailModel;
import com.tecmanic.gogrocer.modelclass.NotifyModelUser;
import com.tecmanic.gogrocer.network.ApiInterface;
import com.tecmanic.gogrocer.util.AppController;
import com.tecmanic.gogrocer.util.CustomVolleyJsonRequest;
import com.tecmanic.gogrocer.util.SessionManagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

import static com.tecmanic.gogrocer.config.BaseURL.EDIT_PROFILE_URL;
import static com.tecmanic.gogrocer.config.BaseURL.UPDATENOTIFYBY;


public class EditProfileFragment extends Fragment implements View.OnClickListener {

    String getphone;
    String getid;
    String userId1;
    String getname;
    String getemail;
    String getpassword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    TextView updateProfile;
    private EditText etPhone;
    private EditText etName;
    private EditText etEmail;
    private SessionManagement sessionManagement;
    private LinearLayout smsLay;
    private String userIdKey = "user_id";
    private String smsdata = "";
    private LinearLayout progressBar;

    public EditProfileFragment() {

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
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        setHasOptionsMenu(true);
        requireActivity().setTitle("Edit Profile");
        checkOtpStatus();
        sessionManagement = new SessionManagement(getActivity());
        userId1 = sessionManagement.userId();

        new Thread(this::checkUserNotify).start();

        etPhone = view.findViewById(R.id.et_pro_phone);
        smsLay = view.findViewById(R.id.sms_lay);

        RadioButton emailYes = view.findViewById(R.id.email_yes);
        RadioButton emailNo = view.findViewById(R.id.email_no);
        RadioButton smsYes = view.findViewById(R.id.sms_yes);
        RadioButton smsNo = view.findViewById(R.id.sms_no);
        RadioButton inappYes = view.findViewById(R.id.inapp_yes);
        RadioButton inappNo = view.findViewById(R.id.inapp_no);
        etName = view.findViewById(R.id.et_pro_name);
        etEmail = view.findViewById(R.id.et_pro_email);
        RelativeLayout btnUpdate = view.findViewById(R.id.btn_pro_edit);
        SwitchCompat emailToggle = view.findViewById(R.id.email_toggle);
        SwitchCompat smsToggle = view.findViewById(R.id.sms_toggle);
        SwitchCompat inappToggle = view.findViewById(R.id.inapp_toggle);
        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setOnClickListener(views -> Log.i("TAG","not work"));
        getemail = sessionManagement.getUserDetails().get(BaseURL.KEY_EMAIL);
        getpassword = sessionManagement.getUserDetails().get(BaseURL.KEY_PASSWORD);
        getname = sessionManagement.getUserDetails().get(BaseURL.KEY_NAME);
        getphone = sessionManagement.getUserDetails().get(BaseURL.KEY_MOBILE);
        getid = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        etName.setText(getname);
        etPhone.setText(getphone);
        AtomicReference<String> emaildata = new AtomicReference<>("");
        AtomicReference<String> inappdata = new AtomicReference<>("");
        updateProfile = view.findViewById(R.id.update_profile);

        if (sessionManagement.getOtpSatus().equalsIgnoreCase("1")) {
            smsLay.setVisibility(View.VISIBLE);
        } else {
            smsLay.setVisibility(View.GONE);
        }

        emailYes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                emailNo.setChecked(false);
                sessionManagement.setEmailServer("1");
                emaildata.set("1");
            }
        });

        emailNo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                emaildata.set("0");
                emailYes.setChecked(false);
                sessionManagement.setEmailServer("0");
            }
        });

        smsYes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                smsdata = "1";
                smsNo.setChecked(false);
                sessionManagement.setUserSMSService("1");
            }
        });

        smsNo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                smsdata = "0";
                smsYes.setChecked(false);
                sessionManagement.setEmailServer("0");
            }
        });

        inappYes.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                inappNo.setChecked(false);
                inappdata.set("1");
                sessionManagement.setUserInAppService("1");
            }
        });

        inappNo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                inappdata.set("0");
                inappYes.setChecked(false);
                sessionManagement.setUserInAppService("0");
            }
        });


        setView(emailYes, emailNo, emaildata, emailToggle, smsYes, smsNo, smsToggle, inappYes, inappNo, inappToggle, inappdata);

        emailToggle.setOnCheckedChangeListener((buttonView, isChecked) -> emailToggleClick(isChecked,emaildata));

        smsToggle.setOnCheckedChangeListener((buttonView, isChecked) -> smsToggleClick(isChecked));

        inappToggle.setOnCheckedChangeListener((buttonView, isChecked) -> inappToggleClick(isChecked,inappdata));

        updateProfile.setOnClickListener(v -> {
            show();
            userId1 = sessionManagement.userId();
            notif(userId1, emaildata.get(), inappdata.get(), smsdata);
        });

        if (!TextUtils.isEmpty(getemail)) {
            etEmail.setText(getemail);
        }
        btnUpdate.setOnClickListener(this);

        return view;
    }

    private void emailToggleClick(boolean isChecked, AtomicReference<String> emaildata) {
        if (isChecked) {
            sessionManagement.setEmailServer("1");
            emaildata.set("1");
        } else {
            sessionManagement.setEmailServer("0");
            emaildata.set("0");
        }
    }

    private void inappToggleClick(boolean isChecked, AtomicReference<String> inappdata) {
        if (isChecked) {
            inappdata.set("1");
            sessionManagement.setUserInAppService("1");
        } else {
            inappdata.set("0");
            sessionManagement.setUserInAppService("0");
        }
    }

    private void smsToggleClick(boolean isChecked) {
        if (isChecked) {
            smsdata = "1";
            sessionManagement.setUserSMSService("1");
        } else {
            smsdata = "0";
            sessionManagement.setUserSMSService("0");
        }
    }

    private void setView(RadioButton emailYes, RadioButton emailNo, AtomicReference<String> emaildata, SwitchCompat emailToggle, RadioButton smsYes, RadioButton smsNo, SwitchCompat smsToggle, RadioButton inappYes, RadioButton inappNo, SwitchCompat inappToggle, AtomicReference<String> inappdata) {
        if (sessionManagement.getEmailService().equalsIgnoreCase("1")) {
            emailYes.setChecked(true);
            emailNo.setChecked(false);
            emailToggle.setChecked(true);
            emaildata.set("1");
        } else {
            emailYes.setChecked(false);
            emailNo.setChecked(true);
            emailToggle.setChecked(false);
            emaildata.set("0");
        }

        if (sessionManagement.getSMSService().equalsIgnoreCase("1")) {
            smsYes.setChecked(true);
            smsNo.setChecked(false);
            smsToggle.setChecked(true);
            smsdata = "1";
        } else {
            smsYes.setChecked(false);
            smsNo.setChecked(true);
            smsToggle.setChecked(false);
            smsdata = "1";
        }

        if (sessionManagement.getINAPPService().equalsIgnoreCase("1")) {
            inappYes.setChecked(true);
            inappNo.setChecked(false);
            inappToggle.setChecked(true);
            inappdata.set("1");
        } else {
            inappNo.setChecked(true);
            inappYes.setChecked(false);
            inappToggle.setChecked(false);
            inappdata.set("1");
        }
    }

    private void notif(String userId, String email1, String app, String sms) {
        Map<String, String> params = new HashMap<>();
        params.put(userIdKey, userId);
        params.put("sms", sms);
        params.put("app", app);
        params.put("email", email1);

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, UPDATENOTIFYBY, params
                , response -> {
            try {
                String message = response.getString("message");
                Toast.makeText(getContext(), "" + message, Toast.LENGTH_SHORT).show();
                show();
            } catch (JSONException e) {
                show();
                e.printStackTrace();
            }
        }, error -> show());
        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
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
        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void checkOtpStatus() {

        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);

        Call<ForgotEmailModel> checkOtpStatus = apiInterface.getOtpOnOffStatus();
        checkOtpStatus.enqueue(new Callback<ForgotEmailModel>() {
            @Override
            public void onResponse(@NonNull Call<ForgotEmailModel> call, @NonNull retrofit2.Response<ForgotEmailModel> response) {
                if (response.isSuccessful()) {
                    ForgotEmailModel model = response.body();
                    if (model != null) {
                        if (model.getStatus().equalsIgnoreCase("0")) {
                            sessionManagement.setOtpStatus("0");
                            smsLay.setVisibility(View.GONE);
                            smsdata = "0";
                            sessionManagement.setUserSMSService("0");
                        } else {
                            sessionManagement.setOtpStatus("1");
                            sessionManagement.setUserSMSService("1");
                            smsdata = "1";
                            smsLay.setVisibility(View.VISIBLE);
                        }
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ForgotEmailModel> call, @NonNull Throwable t) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_pro_edit) {

            if (etEmail.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), "enter email address", Toast.LENGTH_SHORT).show();
            } else {
                if (etEmail.getText().toString().trim().matches(emailPattern)) {
                    getphone = etPhone.getText().toString();
                    getname = etName.getText().toString();
                    getemail = etEmail.getText().toString();
                    show();
                    updateprofile();

                } else {
                    Toast.makeText(getActivity(), "Invalid email address", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void updateprofile() {
        Map<String, String> params = new HashMap<>();
        params.put("user_name", getname);
        params.put(userIdKey, getid);
        params.put("user_phone", getphone);
        params.put("user_email", getemail);
        params.put("user_password", getpassword);

        CustomVolleyJsonRequest jsonObjectRequest = new CustomVolleyJsonRequest(Request.Method.POST, EDIT_PROFILE_URL, params, response -> {
            try {
                String status = response.getString("status");
                String message = response.getString("message");
                if (status.contains("1")) {
                    JSONObject obj = response.getJSONObject("data");
                    String userId = obj.getString(userIdKey);
                    String userFullname = obj.getString("user_name");
                    String userEmail = obj.getString("user_email");
                    String userPhone = obj.getString("user_phone");
                    String password = obj.getString("user_password");
                    SharedPreferences.Editor editor = requireContext().getSharedPreferences(BaseURL.MY_PREPRENCE, Context.MODE_PRIVATE).edit();
                    editor.putString(BaseURL.KEY_MOBILE, userPhone);
                    editor.putString(BaseURL.KEY_PASSWORD, password);
                    editor.apply();
                    sessionManagement.createLoginSession(userId, userEmail, userFullname, userPhone, password);
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    EditProfileFragment fm = new EditProfileFragment();
                    FragmentManager fragmentManager = getParentFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.contentPanel, fm).addToBackStack(null).commit();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                show();
            }
        }, error -> show());

        AppController.getInstance().addToRequestQueue(jsonObjectRequest);
    }

    private void checkUserNotify() {
        Retrofit emailOtp = new Retrofit.Builder()
                .baseUrl(BaseURL.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

        ApiInterface apiInterface = emailOtp.create(ApiInterface.class);

        Call<NotifyModelUser> checkOtpStatus = apiInterface.getNotifyUser(userId1);

        checkOtpStatus.enqueue(new Callback<NotifyModelUser>() {
            @Override
            public void onResponse(@NonNull Call<NotifyModelUser> call, @NonNull retrofit2.Response<NotifyModelUser> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NotifyModelUser modelUser = response.body();
                    if (modelUser.getStatus().equalsIgnoreCase("1")) {
                        sessionManagement.setEmailServer(modelUser.getData().getEmail());
                        sessionManagement.setUserSMSService(modelUser.getData().getSms());
                        sessionManagement.setUserInAppService(modelUser.getData().getApp());
                    } else {
                        sessionManagement.setEmailServer("0");
                        sessionManagement.setUserSMSService("0");
                        sessionManagement.setUserInAppService("0");
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<NotifyModelUser> call, @NonNull Throwable t) {

            }
        });

    }

}
