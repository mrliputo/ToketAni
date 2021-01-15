package com.tecmanic.gogrocer.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.activity.ProductDetails;
import com.tecmanic.gogrocer.modelclass.NewCartModel;
import com.tecmanic.gogrocer.util.DatabaseHandler;
import com.tecmanic.gogrocer.util.ProdcutDetailsVerifier;
import com.tecmanic.gogrocer.util.SessionManagement;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import static com.tecmanic.gogrocer.config.BaseURL.IMG_URL;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    private static final int LIMIT = 6;
    Context context;
    private List<NewCartModel> topSelling;
    private DatabaseHandler dbcart;
    private SessionManagement sessionManagement;
    private String varientKey = "varient_id";
    private ProdcutDetailsVerifier prodcutDetailsVerifier;

    public CartAdapter(Context context, List<NewCartModel> topSelling,ProdcutDetailsVerifier prodcutDetailsVerifier) {
        this.context = context;
        this.topSelling = topSelling;
        this.prodcutDetailsVerifier = prodcutDetailsVerifier;
        dbcart = new DatabaseHandler(context);
        sessionManagement = new SessionManagement(context);
    }

    @NonNull
    @Override
    public CartAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_product_add, parent, false);
        context = parent.getContext();
        dbcart = new DatabaseHandler(context);
        return new CartAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartAdapter.MyViewHolder holder, int position) {

        NewCartModel cc = topSelling.get(position);
        holder.currencyIndicator.setText(sessionManagement.getCurrency());
        holder.currencyIndicator2.setText(sessionManagement.getCurrency());
        holder.prodNAme.setText(cc.getProductName());
        holder.pDescrptn.setText(cc.getDescription());
        holder.pQuan.setText(cc.getQuantity() + "" + cc.getUnit());
        DecimalFormat dFormat = new DecimalFormat("#.##");
        holder.pPrice.setText(dFormat.format(Double.parseDouble(cc.getPrice())));
        String totalOff = String.valueOf(Double.parseDouble(cc.getMrp()) - Double.parseDouble(cc.getPrice()));
        holder.pdiscountOff.setText(sessionManagement.getCurrency() + dFormat.format(Double.parseDouble(totalOff)) + " " + "Off");
        holder.pMrp.setText(dFormat.format(Double.parseDouble(cc.getMrp())));
        holder.pMrp.setPaintFlags(holder.pMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        sessionManagement.setStoreId(cc.getStoreId());
        if (Integer.parseInt(cc.getStock()) > 0) {
            holder.outofs.setVisibility(View.GONE);
            holder.outofsIn.setVisibility(View.VISIBLE);
        } else {
            holder.outofsIn.setVisibility(View.GONE);
            holder.outofs.setVisibility(View.VISIBLE);
        }


        int qtyd = Integer.parseInt(dbcart.getInCartItemQtys(topSelling.get(position).getVarientId()));
        if (qtyd > 0) {
            holder.btnAdd.setVisibility(View.GONE);
            holder.llAddQuan.setVisibility(View.VISIBLE);
            holder.txtQuan.setText("" + qtyd);
            double priced = Double.parseDouble(cc.getPrice());
            double mrpd = Double.parseDouble(cc.getMrp());
            holder.pPrice.setText("" + dFormat.format((priced * qtyd)));
            holder.pMrp.setText("" + dFormat.format((mrpd * qtyd)));
        } else {
            holder.btnAdd.setVisibility(View.VISIBLE);
            holder.llAddQuan.setVisibility(View.GONE);
            holder.pPrice.setText(dFormat.format(Double.parseDouble(cc.getPrice())));
            holder.pMrp.setText(dFormat.format(Double.parseDouble(cc.getMrp())));
            holder.txtQuan.setText("" + 0);
        }
        Picasso.with(context)
                .load(IMG_URL + cc.getProductImage()).networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                .placeholder(R.drawable.splashicon)
                .into(holder.image);
        double price = Double.parseDouble(topSelling.get(position).getPrice());
        double mrp = Double.parseDouble(topSelling.get(position).getMrp());


        holder.itemView.setOnClickListener(v -> {
            prodcutDetailsVerifier.onProductClick(position,"");
        });


        holder.plus.setOnClickListener(v -> {
            try {
                if (dbcart == null) {
                    dbcart = new DatabaseHandler(v.getContext());
                }
                int i = Integer.parseInt(dbcart.getInCartItemQtys(topSelling.get(position).getVarientId()));
                if (i < Integer.parseInt(cc.getStock())) {
                    holder.btnAdd.setVisibility(View.GONE);
                    holder.llAddQuan.setVisibility(View.VISIBLE);
                    holder.txtQuan.setText("" + (i + 1));
                    holder.pPrice.setText("" + dFormat.format((price * (i + 1))));
                    holder.pMrp.setText("" + dFormat.format((mrp * (i + 1))));
                    updateMultiply(position, (i + 1));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        });
        holder.minus.setOnClickListener(v -> {
            int i = Integer.parseInt(dbcart.getInCartItemQtys(topSelling.get(position).getVarientId()));
            if ((i - 1) < 0 || (i - 1) == 0) {
                holder.btnAdd.setVisibility(View.VISIBLE);
                holder.llAddQuan.setVisibility(View.GONE);
                holder.txtQuan.setText("" + 0);
                holder.pPrice.setText("" + dFormat.format(price));
                holder.pMrp.setText("" + dFormat.format(mrp));
            } else {
                holder.txtQuan.setText("" + (i - 1));
                holder.pPrice.setText("" + dFormat.format((price * (i - 1))));
                holder.pMrp.setText("" + dFormat.format((mrp * (i - 1))));
            }
            updateMultiply(position, (i - 1));
        });
        holder.btnAdd.setOnClickListener(v -> {
            holder.btnAdd.setVisibility(View.GONE);
            holder.llAddQuan.setVisibility(View.VISIBLE);
            holder.txtQuan.setText("1");
            updateMultiply(position, 1);
        });

    }

    @Override
    public int getItemCount() {

        if (topSelling.size() > LIMIT) {
            return LIMIT;
        } else {
            return topSelling.size();
        }
    }

    private void updateMultiply(int pos, int i) {
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put(varientKey, topSelling.get(pos).getVarientId());
            map.put("product_name", topSelling.get(pos).getProductName());
            map.put("category_id", topSelling.get(pos).getProductId());
            map.put("title", topSelling.get(pos).getProductName());
            map.put("price", topSelling.get(pos).getPrice());
            map.put("mrp", topSelling.get(pos).getMrp());
            map.put("product_image", topSelling.get(pos).getProductImage());
            map.put("status", "");
            map.put("in_stock", "");
            map.put("unit_value", topSelling.get(pos).getQuantity());
            map.put("unit", topSelling.get(pos).getUnit());
            map.put("increament", "0");
            map.put("rewards", "0");
            map.put("stock", topSelling.get(pos).getStock());
            map.put("product_description", topSelling.get(pos).getDescription());

            if (i > 0) {
                dbcart.setCart(map, i);
            } else {
                dbcart.removeItemFromCart(map.get(varientKey));
            }


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                SharedPreferences preferencess = context.getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                preferencess.edit().putInt("cardqnty", dbcart.getCartCount()).apply();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView prodNAme;
        TextView pDescrptn;
        TextView pQuan;
        TextView pPrice;
        TextView pdiscountOff;
        TextView pMrp;
        TextView minus;
        TextView plus;
        TextView txtQuan;
        TextView currencyIndicator;
        TextView currencyIndicator2;
        ImageView image;
        LinearLayout btnAdd;
        LinearLayout llAddQuan;
        LinearLayout outofs;
        LinearLayout outofsIn;
        RelativeLayout rlQuan;

        public MyViewHolder(View view) {
            super(view);
            prodNAme = view.findViewById(R.id.txt_pName);
            currencyIndicator = view.findViewById(R.id.currency_indicator);
            currencyIndicator2 = view.findViewById(R.id.currency_indicator_2);
            pDescrptn = view.findViewById(R.id.txt_pInfo);
            pQuan = view.findViewById(R.id.txt_unit);
            pPrice = view.findViewById(R.id.txt_Pprice);
            image = view.findViewById(R.id.prodImage);
            pdiscountOff = view.findViewById(R.id.txt_discountOff);
            pMrp = view.findViewById(R.id.txt_Mrp);
            rlQuan = view.findViewById(R.id.rlQuan);
            btnAdd = view.findViewById(R.id.btn_Add);
            llAddQuan = view.findViewById(R.id.ll_addQuan);
            outofs = view.findViewById(R.id.outofs);
            outofsIn = view.findViewById(R.id.outofs_in);
            txtQuan = view.findViewById(R.id.txtQuan);
            minus = view.findViewById(R.id.minus);
            plus = view.findViewById(R.id.plus);
        }

        @Override
        public void onClick(View view) {

        }
    }

}
