package com.tecmanic.toketani.adapters;

import android.content.Context;
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
import com.tecmanic.toketani.R;
import com.tecmanic.toketani.modelclass.NewCartModel;
import com.tecmanic.toketani.util.DatabaseHandler;
import com.tecmanic.toketani.util.SessionManagement;
import com.tecmanic.toketani.util.ViewNotifier;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import static com.tecmanic.toketani.config.BaseURL.IMG_URL;

public class ViewAllAdapter extends RecyclerView.Adapter<ViewAllAdapter.MyViewHolder> {
    Context context;
    private DatabaseHandler dbcart;
    private List<NewCartModel> cartList;
    private SessionManagement sessionManagement;
    private ViewNotifier viewNotifier;
    private String varientKey = "varient_id";
    private DecimalFormat dFormat;

    public ViewAllAdapter(List<NewCartModel> cartList, Context context, ViewNotifier viewNotifier) {
        this.cartList = cartList;
        this.context = context;
        this.viewNotifier = viewNotifier;
        dbcart = new DatabaseHandler(context);
        sessionManagement = new SessionManagement(context);
    }

    @NonNull
    @Override
    public ViewAllAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_product_add, parent, false);
        context = parent.getContext();
        dbcart = new DatabaseHandler(context);
        sessionManagement = new SessionManagement(context);
        dFormat = new DecimalFormat("#.##");
        return new ViewAllAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewAllAdapter.MyViewHolder holder, int position) {
        NewCartModel cc = cartList.get(position);
        holder.prodNAme.setText(cc.getProductName());
        holder.pDescrptn.setText(cc.getDescription());
        holder.pQuan.setText(cc.getQuantity()+""+cc.getUnit());
//        holder.pPrice.setText(cc.getPrice());
//        String totalOff = String.valueOf(Double.parseDouble(cc.getMrp()) - Double.parseDouble(cc.getPrice()));
//        holder.pdiscountOff.setText(sessionManagement.getCurrency() + totalOff + " " + "Off");
        double totalOff = Double.parseDouble(cc.getMrp()) - Double.parseDouble(cc.getPrice());
        holder.pdiscountOff.setText(sessionManagement.getCurrency() + dFormat.format(totalOff) + " " + "Off");
//        holder.pMrp.setText(cc.getMrp());
        holder.pMrp.setPaintFlags(holder.pMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        sessionManagement.setStoreId(cc.getStoreId());
        holder.currency_indicator.setText(sessionManagement.getCurrency());
        holder.currency_indicator_2.setText(sessionManagement.getCurrency());

        if (Integer.parseInt(cc.getStock()) > 0) {
            holder.outofs.setVisibility(View.GONE);
            holder.outofsIn.setVisibility(View.VISIBLE);
        } else {
            holder.outofsIn.setVisibility(View.GONE);
            holder.outofs.setVisibility(View.VISIBLE);
        }
        int qtyd = Integer.parseInt(dbcart.getInCartItemQtys(cartList.get(position).getVarientId()));
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

        holder.itemView.setOnClickListener(v -> {
           if (viewNotifier!=null){
               viewNotifier.onProductDetailClick(position);
           }
        });

        double price = Double.parseDouble(cartList.get(position).getPrice());
        double mrp = Double.parseDouble(cartList.get(position).getMrp());


        holder.plus.setOnClickListener(v -> {
            holder.btnAdd.setVisibility(View.GONE);
            holder.llAddQuan.setVisibility(View.VISIBLE);
            int i = Integer.parseInt(dbcart.getInCartItemQtys(cartList.get(position).getVarientId()));
            if (i < Integer.parseInt(cc.getStock())) {
                holder.txtQuan.setText("" + (i + 1));
                holder.pPrice.setText("" + dFormat.format((price * (i + 1))));
                holder.pMrp.setText("" + dFormat.format((mrp * (i + 1))));
                updateMultiply(position, (i + 1));
            }
        });
        holder.minus.setOnClickListener(v -> {
            int i = Integer.parseInt(dbcart.getInCartItemQtys(cartList.get(position).getVarientId()));

            if ((i - 1) < 0 || (i - 1) == 0) {
                holder.btnAdd.setVisibility(View.VISIBLE);
                holder.llAddQuan.setVisibility(View.GONE);
                holder.txtQuan.setText("" + (i - 1));
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
        return cartList.size();
    }

    private void updateMultiply(int pos, int i) {
        HashMap<String, String> map = new HashMap<>();
        map.put(varientKey, cartList.get(pos).getVarientId());
        map.put("product_name", cartList.get(pos).getProductName());
        map.put("category_id", cartList.get(pos).getProductId());
        map.put("title", cartList.get(pos).getProductName());
        map.put("price", cartList.get(pos).getPrice());
        map.put("mrp", cartList.get(pos).getMrp());
        map.put("product_image", cartList.get(pos).getProductImage());
        map.put("status", "0");
        map.put("in_stock", "0");
        map.put("unit_value", cartList.get(pos).getQuantity());
        map.put("unit", cartList.get(pos).getUnit());
        map.put("increament", "0");
        map.put("rewards", "0");
        map.put("stock", cartList.get(pos).getStock());
        map.put("product_description", cartList.get(pos).getDescription());
        if (i > 0) {
            dbcart.setCart(map, i);
        } else {
            dbcart.removeItemFromCart(map.get(varientKey));
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (viewNotifier != null) {
                    viewNotifier.onViewNotify();
                }
                SharedPreferences preferences = context.getSharedPreferences("toketani", Context.MODE_PRIVATE);
                preferences.edit().putInt("cardqnty", dbcart.getCartCount()).apply();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView prodNAme;
        TextView pDescrptn;
        TextView pQuan;
        TextView pPrice;
        TextView pdiscountOff;
        TextView pMrp;
        TextView minus;
        TextView plus;
        TextView txtQuan;
        TextView currency_indicator;
        TextView currency_indicator_2;
        ImageView image;
        LinearLayout btnAdd;
        LinearLayout llAddQuan;
        LinearLayout outofs;
        LinearLayout outofsIn;
        RelativeLayout rlQuan;

        public MyViewHolder(View view) {
            super(view);
            prodNAme = view.findViewById(R.id.txt_pName);
            pDescrptn = view.findViewById(R.id.txt_pInfo);
            pQuan = view.findViewById(R.id.txt_unit);
            pPrice = view.findViewById(R.id.txt_Pprice);
            image = view.findViewById(R.id.prodImage);
            pdiscountOff = view.findViewById(R.id.txt_discountOff);
            pMrp = view.findViewById(R.id.txt_Mrp);
            rlQuan = view.findViewById(R.id.rlQuan);
            btnAdd = view.findViewById(R.id.btn_Add);
            llAddQuan = view.findViewById(R.id.ll_addQuan);
            txtQuan = view.findViewById(R.id.txtQuan);
            minus = view.findViewById(R.id.minus);
            plus = view.findViewById(R.id.plus);
            outofs = view.findViewById(R.id.outofs);
            outofsIn = view.findViewById(R.id.outofs_in);
            currency_indicator = view.findViewById(R.id.currency_indicator);
            currency_indicator_2 = view.findViewById(R.id.currency_indicator_2);
        }
    }
}



