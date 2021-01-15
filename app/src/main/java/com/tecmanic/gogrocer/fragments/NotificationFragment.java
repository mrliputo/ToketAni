package com.tecmanic.gogrocer.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.activity.MainActivity;
import com.tecmanic.gogrocer.config.BaseURL;
import com.tecmanic.gogrocer.modelclass.Notice;
import com.tecmanic.gogrocer.util.AppController;
import com.tecmanic.gogrocer.util.CustomVolleyJsonRequest;
import com.tecmanic.gogrocer.util.SessionManagement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.tecmanic.gogrocer.config.BaseURL.NOTICE_U_RL;

public class NotificationFragment extends Fragment {

    ShimmerRecyclerView recyclerNotification;
    LinearLayout back;
    RelativeLayout noData;
    NoticeAdapter noticeAdapter;
    TextView dellete;
    SessionManagement sessionManagement;
    String userId;
    private List<Notice> noticeList = new ArrayList<>();
    private LinearLayout progressBar;

    public NotificationFragment() {
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
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        recyclerNotification = view.findViewById(R.id.recyclerNotification);
        back = view.findViewById(R.id.back);
        noData = view.findViewById(R.id.noData);
        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setOnClickListener(views -> Log.i("TAG","not work"));
        dellete = view.findViewById(R.id.dellete);
        dellete.setOnClickListener(v -> deleteorder());
        hitUrlNotice();
        return view;
    }


    private void hitUrlNotice() {
        show();
        sessionManagement = new SessionManagement(getContext());
        userId = sessionManagement.getUserDetails().get(BaseURL.KEY_ID);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NOTICE_U_RL, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                if (status.equalsIgnoreCase("1")) {

                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObject3 = jsonArray.getJSONObject(i);
                        Notice nn = new Notice();
                        nn.contentId = jsonObject3.getString("noti_id");
                        nn.contentName = jsonObject3.getString("noti_title");  //heading
                        nn.contentDate = jsonObject3.getString("noti_message");  //title data
                        noticeList.add(nn);
                    }

                    if (!noticeList.isEmpty()) {
                        noData.setVisibility(View.GONE);
                        recyclerNotification.setVisibility(View.VISIBLE);
                    }
                    noticeAdapter = new NoticeAdapter(noticeList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                    recyclerNotification.setLayoutManager(mLayoutManager);
                    recyclerNotification.setAdapter(noticeAdapter);
                    noticeAdapter.notifyDataSetChanged();
                } else {
                    noData.setVisibility(View.VISIBLE);
                    recyclerNotification.setVisibility(View.GONE);
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
                params.put("user_id", userId);
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

    private void deleteorder() {
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);
        CustomVolleyJsonRequest jsonObjReq = new CustomVolleyJsonRequest(Request.Method.POST,
                BaseURL.DELETE_ALL_NOTIFICATION, params, response -> {
            try {
                String status = response.getString("status");
                String message = response.getString("message");
                if (status.contains("1")) {
                    startActivity(new Intent(getActivity(), MainActivity.class));
                    Toast.makeText(getActivity(), "" + message, Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
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

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq);

    }

    public class NoticeAdapter extends RecyclerView.Adapter<NoticeAdapter.MyViewHolder> {

        private List<Notice> noticeList;

        public NoticeAdapter(List<Notice> noticeList) {
            this.noticeList = noticeList;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.layout_notificationlist, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Notice nn = noticeList.get(position);
            holder.noticeName.setText(nn.getContentName());
            holder.noticeDate.setText(nn.getContentDate());
        }

        @Override
        public int getItemCount() {
            return noticeList.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView noticeName;
            TextView noticeDate;

            public MyViewHolder(View view) {
                super(view);
                noticeName = (TextView) view.findViewById(R.id.txt_notice);
                noticeDate = (TextView) view.findViewById(R.id.txt_date);
            }
        }
    }
}
