package com.tecmanic.toketani.fragments;

import android.app.AlertDialog;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tecmanic.toketani.R;
import com.tecmanic.toketani.activity.LoginActivity;
import com.tecmanic.toketani.activity.MainActivity;
import com.tecmanic.toketani.activity.OrderSummary;
import com.tecmanic.toketani.adapters.CartAdapters;
import com.tecmanic.toketani.util.DatabaseHandler;
import com.tecmanic.toketani.util.SessionManagement;
import com.tecmanic.toketani.util.ViewNotifier;

import java.util.HashMap;
import java.util.List;

public class CartFragment extends Fragment {

    TextView tvTotal;
    Button btnShopNOw;
    RecyclerView recyclerView;
    LinearLayout llCheckout;
    RelativeLayout noData;
    RelativeLayout viewCart;
    TextView totalItems;
    private DatabaseHandler db;
    private SessionManagement sessionManagement;

    public CartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView = view.findViewById(R.id.recyclerCart);
        btnShopNOw = view.findViewById(R.id.btn_ShopNOw);
        viewCart = view.findViewById(R.id.viewCartItems);
        tvTotal = view.findViewById(R.id.txt_totalamount);
        totalItems = view.findViewById(R.id.txt_totalQuan);
        noData = view.findViewById(R.id.noData);
        sessionManagement = new SessionManagement(getActivity());
        db = new DatabaseHandler(getActivity());

        llCheckout = view.findViewById(R.id.ll_Checkout);
        btnShopNOw.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        });
        llCheckout.setOnClickListener(v -> {
            if (isOnline(container.getContext()) && sessionManagement.isLoggedIn()) {

                if (sessionManagement.userBlockStatus().equalsIgnoreCase("2")) {
                    if (db.getCartCount() == 0) {
                        noData.setVisibility(View.VISIBLE);
                        viewCart.setVisibility(View.GONE);
                    } else {
                        Intent intent = new Intent(getActivity(), OrderSummary.class);
                        startActivity(intent);
                    }
                } else {
                    showBloackDialog();
                }
            } else {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false));

        db = new DatabaseHandler(getActivity());

        setViewVisibility();
        List<HashMap<String, String>> map = db.getCartAll();

        CartAdapters adapter = new CartAdapters(getActivity(), map, new ViewNotifier() {
            @Override
            public void onViewNotify() {
                if (db.getCartCount() == 0) {
                    noData.setVisibility(View.VISIBLE);
                    viewCart.setVisibility(View.GONE);
                }
                tvTotal.setText(sessionManagement.getCurrency() + " " + db.getTotalAmount());
            }

            @Override
            public void onProductDetailClick(int position) {
                Log.i("TAG",""+position);
            }
        });
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        updateData();
        return view;


    }

    private void setViewVisibility() {
        if (sessionManagement.isLoggedIn() && db.getCartCount() == 0) {
            noData.setVisibility(View.VISIBLE);
            viewCart.setVisibility(View.GONE);
        } else {
            if (db.getCartCount() == 0) {
                noData.setVisibility(View.VISIBLE);
                viewCart.setVisibility(View.GONE);
            }
        }
    }


    public void updateData() {
        tvTotal.setText(sessionManagement.getCurrency() + " " + db.getTotalAmount());
        totalItems.setText("" + db.getCartCount() + "  Total Items:");
    }

    private void showBloackDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setCancelable(true);
        alertDialog.setMessage("You are blocked from backend.\n Please Contact with customer care!");
        alertDialog.setPositiveButton("Ok", (dialogInterface, i) -> dialogInterface.dismiss());
        alertDialog.show();
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
