package com.tecmanic.toketani.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.tecmanic.toketani.R;
import com.tecmanic.toketani.util.DatabaseHandler;

public class OrderSuccessful extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_successful);
        Button btnShopMore = findViewById(R.id.btn_ShopMore);
        try (DatabaseHandler dbHandler = new DatabaseHandler(OrderSuccessful.this)) {
            dbHandler.clearCart();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SharedPreferences preferences = getSharedPreferences("toketani", Context.MODE_PRIVATE);
        preferences.edit().putInt("cardqnty", 0).apply();
        btnShopMore.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
