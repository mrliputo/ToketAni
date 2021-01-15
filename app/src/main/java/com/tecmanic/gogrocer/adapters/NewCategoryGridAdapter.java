package com.tecmanic.gogrocer.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tecmanic.gogrocer.Categorygridquantity;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.modelclass.NewCategoryDataModel;
import com.tecmanic.gogrocer.util.DatabaseHandler;
import com.tecmanic.gogrocer.util.SessionManagement;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import static com.tecmanic.gogrocer.config.BaseURL.IMG_URL;

public class NewCategoryGridAdapter extends RecyclerView.Adapter<NewCategoryGridAdapter.MyViewHolder> {
    Context context;
    Categorygridquantity categorygridquantity;
    private List<NewCategoryDataModel> categoryDataModels;
    private DatabaseHandler dbcart;
    private SessionManagement sessionManagement;
    private String varientKey = "varient_id";
    private DecimalFormat dFormat;

    public NewCategoryGridAdapter(List<NewCategoryDataModel> categoryGridList, Context context, Categorygridquantity categorygridquantity) {
        this.categoryDataModels = categoryGridList;
        this.dbcart = new DatabaseHandler(context);
        this.sessionManagement = new SessionManagement(context);
        this.categorygridquantity = categorygridquantity;

    }

    @NonNull
    @Override
    public NewCategoryGridAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_category_listd, parent, false);
        context = parent.getContext();
        dbcart = new DatabaseHandler(context);
        if (sessionManagement == null) {
            sessionManagement = new SessionManagement(context);
        }
        dFormat = new DecimalFormat("#.##");
        return new MyViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NewCategoryDataModel cc = categoryDataModels.get(position);

        holder.currencyIndicator.setText(sessionManagement.getCurrency());
        holder.currencyIndicator2.setText(sessionManagement.getCurrency());
        holder.prodNAme.setText(cc.getProductName());
//        holder.pPrice.setText(cc.getPrice());
        holder.txtUnitvalue.setText(cc.getUnit());
        holder.pQuan.setText(cc.getQuantity());
//        holder.pMrp.setText(cc.getMrp());
        holder.pDescrptn.setText(cc.getDescription());
        sessionManagement.setStoreId(cc.getStoreId());

        if (Integer.parseInt(cc.getStock()) > 0) {
            holder.outofs.setVisibility(View.GONE);
            holder.outofsIn.setVisibility(View.VISIBLE);
        } else {
            holder.outofsIn.setVisibility(View.GONE);
            holder.outofs.setVisibility(View.VISIBLE);
        }

        int qtyd = Integer.parseInt(dbcart.getInCartItemQtys(cc.getVarientId()));
        double priced = Double.parseDouble(cc.getPrice());
        double mrpd = Double.parseDouble(cc.getMrp());
        if (qtyd > 0) {
            holder.btnAdd.setVisibility(View.GONE);
            holder.llAddQuan.setVisibility(View.VISIBLE);
            holder.txtQuan.setText("" + qtyd);
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
                .load(IMG_URL + cc.getVarientImage()).networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
                .placeholder(R.drawable.splashicon)
                .into(holder.image);


        holder.pdiscountOff.setText(sessionManagement.getCurrency() + "" + ((int) (mrpd - priced)) + " " + "Off");
        holder.pMrp.setPaintFlags(holder.pMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.itemView.setOnClickListener(v -> {
            categorygridquantity.onProductDetials(position);
        });

//        Picasso.with(context)
//                .load(IMG_URL + cc.getProductImage()).networkPolicy(NetworkPolicy.NO_CACHE, NetworkPolicy.NO_STORE)
//                .placeholder(R.drawable.splashicon)
//                .into(holder.image);


//        holder.rlQuan.setOnClickListener(view -> categorygridquantity.onClick(view, position, cc.getProductId(), cc.getProductName()));

        holder.plus.setOnClickListener(v -> {
            holder.btnAdd.setVisibility(View.GONE);
            holder.llAddQuan.setVisibility(View.VISIBLE);
            int i = Integer.parseInt(dbcart.getInCartItemQtys(cc.getVarientId()));
            if (i < Integer.parseInt(cc.getStock())) {
                holder.txtQuan.setText("" + (i + 1));
                holder.pPrice.setText("" + dFormat.format((priced * (i + 1))));
                holder.pMrp.setText("" + dFormat.format((mrpd * (i + 1))));
                updateMultiply(position, i + 1);
            }
        });
        holder.minus.setOnClickListener(v -> {
            int i = Integer.parseInt(dbcart.getInCartItemQtys(cc.getVarientId()));
            if ((i - 1) < 0 || (i - 1) == 0) {
                holder.btnAdd.setVisibility(View.VISIBLE);
                holder.llAddQuan.setVisibility(View.GONE);
                holder.txtQuan.setText("" + 0);
                holder.pPrice.setText("" + dFormat.format(priced));
                holder.pMrp.setText("" + dFormat.format(mrpd));
            } else {
                holder.txtQuan.setText("" + (i - 1));
                holder.pPrice.setText("" + dFormat.format((priced * (i - 1))));
                holder.pMrp.setText("" + dFormat.format((mrpd * (i - 1))));
            }
            updateMultiply(position, i - 1);
        });
        holder.btnAdd.setOnClickListener(v -> {
            holder.btnAdd.setVisibility(View.GONE);
            holder.llAddQuan.setVisibility(View.VISIBLE);
            holder.txtQuan.setText("1");
            updateMultiply(position, 1);
        });


    }

    private void updateMultiply(int pos, int i) {
        HashMap<String, String> map = new HashMap<>();
        map.put(varientKey, categoryDataModels.get(pos).getVarientId());
        map.put("product_name", categoryDataModels.get(pos).getProductName());
        map.put("category_id", categoryDataModels.get(pos).getProductId());
        map.put("title", categoryDataModels.get(pos).getProductName());
        map.put("price", categoryDataModels.get(pos).getPrice());
        map.put("mrp", categoryDataModels.get(pos).getMrp());
        map.put("product_image", categoryDataModels.get(pos).getVarientImage());
        map.put("status", "0");
        map.put("in_stock", "");
        map.put("unit_value", categoryDataModels.get(pos).getQuantity());
        map.put("unit", categoryDataModels.get(pos).getUnit() != null ? categoryDataModels.get(pos).getUnit() : "");
        map.put("increament", "0");
        map.put("rewards", "0");
        map.put("stock", categoryDataModels.get(pos).getStock());
        map.put("product_description", categoryDataModels.get(pos).getDescription());
        if (i > 0) {
            dbcart.setCart(map, i);
        } else {
            dbcart.removeItemFromCart(map.get(varientKey));
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                SharedPreferences preferences = context.getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                preferences.edit().putInt("cardqnty", dbcart.getCartCount()).apply();
                categorygridquantity.onCartItemAddOrMinus();
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d("qwer", e.toString());
        }
    }

    @Override
    public int getItemCount() {
        return categoryDataModels.size();
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
        TextView txtUnitvalue;
        TextView currencyIndicator;
        TextView currencyIndicator2;
        ImageView image;
        LinearLayout btnAdd;
        LinearLayout llAddQuan;
        LinearLayout outofsIn;
        LinearLayout outofs;
        LinearLayout rlQuan;

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
            txtQuan = view.findViewById(R.id.txtQuan);
            minus = view.findViewById(R.id.minus);
            plus = view.findViewById(R.id.plus);
            txtUnitvalue = view.findViewById(R.id.txt_unitvalue);
            llAddQuan = view.findViewById(R.id.ll_addQuan);
            outofsIn = view.findViewById(R.id.outofs_in);
            outofs = view.findViewById(R.id.outofs);
        }


    }
}
