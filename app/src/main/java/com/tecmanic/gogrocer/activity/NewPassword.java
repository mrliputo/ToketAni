package com.tecmanic.gogrocer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.android.volley.Request;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.util.AppController;
import com.tecmanic.gogrocer.util.CustomVolleyJsonRequest;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import static com.tecmanic.gogrocer.config.BaseURL.CHANGE_PASS;

public class NewPassword extends AppCompatActivity {

    public static final String TAG = "Otp";
    CardView cvsubmit;
    EditText etReqNewpassword;
    String getuserphone;

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
        setContentView(R.layout.activity_new_password);
        etReqNewpassword = findViewById(R.id.et_req_newpassword);
        cvsubmit = findViewById(R.id.cvsubmit);
        progressBar = findViewById(R.id.progress_bar);
        getuserphone = getIntent().getStringExtra("user_phone");
        cvsubmit.setOnClickListener(v -> {

            if (etReqNewpassword.getText().toString().length() == 0) {
                Toast.makeText(NewPassword.this, "Enter New Password", Toast.LENGTH_SHORT).show();
            } else {
                show();
                setotpverify();
            }
        });

    }

    public void setotpverify() {
        Map<String, String> params = new HashMap<String, String>();
        params.put("user_phone", getuserphone);
        params.put("user_password", etReqNewpassword.getText().toString());
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                CHANGE_PASS, params, response -> {
            try {
                String status = response.getString("status");
                String message = response.getString("message");
                if (status.contains("1")) {
                    Intent intent = new Intent(NewPassword.this, LoginActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(NewPassword.this, message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                show();
            }
        }, error -> show());
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
}
