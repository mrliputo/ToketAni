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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tecmanic.toketani.R;
import com.tecmanic.toketani.modelclass.NewCategoryVarientList;
import com.tecmanic.toketani.util.Communicator;
import com.tecmanic.toketani.util.DatabaseHandler;
import com.tecmanic.toketani.util.SessionManagement;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import static com.tecmanic.toketani.config.BaseURL.IMG_URL;

public class AdapterPopup extends RecyclerView.Adapter<AdapterPopup.Holder> {

    List<NewCategoryVarientList> varientProductList;
    Context context;
    String id;
    private DatabaseHandler dbcart;
    private SessionManagement sessionManagement;
    private Communicator communicator;
    private String varientID = "varient_id";
    private DecimalFormat dFormat;

    public AdapterPopup(Context context, List<NewCategoryVarientList> varientProductList, String id, Communicator communicator) {
        this.varientProductList = varientProductList;
        this.id = id;
        this.dbcart = new DatabaseHandler(context);
        this.context = context;
        this.communicator = communicator;
        sessionManagement = new SessionManagement(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.listitem_popup, viewGroup, false);
        context = viewGroup.getContext();
        if (sessionManagement == null) {
            sessionManagement = new SessionManagement(context);
        }
        dFormat = new DecimalFormat("#.##");
        return new Holder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int i) {
        final NewCategoryVarientList selectAreaModel = varientProductList.get(i);
        if (Integer.parseInt(selectAreaModel.getStock()) > 0) {
            holder.outofs.setVisibility(View.GONE);
            holder.outofsIn.setVisibility(View.VISIBLE);
        } else {
            holder.outofsIn.setVisibility(View.GONE);
            holder.outofs.setVisibility(View.VISIBLE);
        }
        holder.unit.setText(selectAreaModel.getQuantity());
        holder.unitvalue.setText(selectAreaModel.getUnit());
        Picasso.with(context).load(IMG_URL + selectAreaModel.getVarientImage()).networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE).placeholder(R.drawable.splashicon).into(holder.prodImage);
        double price = Double.parseDouble(varientProductList.get(i).getPrice());
        double mrp = Double.parseDouble(varientProductList.get(i).getMrp());

        int qtyd = Integer.parseInt(dbcart.getInCartItemQtys(selectAreaModel.getVarientId()));
        if (qtyd > 0) {
            holder.btnAdd.setVisibility(View.GONE);
            holder.llAddQuan.setVisibility(View.VISIBLE);
            holder.txtQuan.setText("" + qtyd);
            holder.mprice.setText(sessionManagement.getCurrency() + " " + dFormat.format(price * qtyd));
            holder.mrp.setText(sessionManagement.getCurrency() + " " + dFormat.format((mrp * qtyd)));
            holder.mrp.setPaintFlags(holder.mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.btnAdd.setVisibility(View.VISIBLE);
            holder.llAddQuan.setVisibility(View.GONE);
            holder.mprice.setText(sessionManagement.getCurrency() + " " + selectAreaModel.getPrice());
            holder.mrp.setText(sessionManagement.getCurrency() + " " + selectAreaModel.getMrp());
            holder.mrp.setPaintFlags(holder.mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.txtQuan.setText("" + 0);
        }
        holder.plus.setOnClickListener(v -> {
            int qnty = (int) Double.parseDouble(dbcart.getInCartItemQty(selectAreaModel.getVarientId()));
            if (qnty < Integer.parseInt(selectAreaModel.getStock())) {
                holder.btnAdd.setVisibility(View.GONE);
                holder.llAddQuan.setVisibility(View.VISIBLE);
                holder.txtQuan.setText("" + (qnty + 1));
                holder.mprice.setText("" + dFormat.format((price * (qnty + 1))));
                holder.mrp.setText("" + dFormat.format((mrp * (qnty + 1))));
                updateMultiply(i, holder);
            }
        });
        holder.minus.setOnClickListener(v -> {
            int qnty = (int) Double.parseDouble(dbcart.getInCartItemQty(selectAreaModel.getVarientId()));
            if ((qnty - 1) < 0 || (qnty - 1) == 0) {
                holder.btnAdd.setVisibility(View.VISIBLE);
                holder.llAddQuan.setVisibility(View.GONE);
                holder.txtQuan.setText("" + 0);
                holder.mprice.setText("" + dFormat.format(price));
                holder.mrp.setText("" + dFormat.format(mrp));
            } else {
                holder.txtQuan.setText("" + (qnty - 1));
                holder.mprice.setText("" + dFormat.format((price * (qnty - 1))));
                holder.mrp.setText("" + dFormat.format((mrp * (qnty - 1))));
            }
            updateMultiply(i, holder);
        });
        holder.btnAdd.setOnClickListener(v -> {
            holder.btnAdd.setVisibility(View.GONE);
            holder.llAddQuan.setVisibility(View.VISIBLE);
            holder.txtQuan.setText("1");
            updateMultiply(i, holder);
        });
    }

    @Override
    public int getItemCount() {
        return varientProductList.size();
    }

    private void updateMultiply(int pos, Holder holder) {
        HashMap<String, String> map = new HashMap<>();
        map.put(varientID, varientProductList.get(pos).getVarientId());
        map.put("product_name", id);
        map.put("title", id);
        map.put("price", varientProductList.get(pos).getPrice());
        map.put("mrp", varientProductList.get(pos).getMrp());
        map.put("product_image", varientProductList.get(pos).getVarientImage());
        map.put("unit_value", varientProductList.get(pos).getQuantity());
        map.put("unit", varientProductList.get(pos).getUnit());
        map.put("increament", "0");
        map.put("rewards", "0");
        map.put("stock", varientProductList.get(pos).getStock());
        map.put("product_description", varientProductList.get(pos).getDescription());
        if (!holder.txtQuan.getText().toString().equalsIgnoreCase("0")) {
            dbcart.setCart(map, Integer.valueOf(holder.txtQuan.getText().toString()));
        } else {
            dbcart.removeItemFromCart(map.get(varientID));
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                if (communicator != null) {
                    communicator.onClick(pos);
                }
                SharedPreferences preferences = context.getSharedPreferences("toketani", Context.MODE_PRIVATE);
                preferences.edit().putInt("cardqnty", dbcart.getCartCount()).apply();
            }
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView minus;
        TextView plus;
        TextView txtQuan;
        TextView unit;
        TextView unitvalue;
        TextView mprice;
        TextView mrp;
        ImageView prodImage;
        LinearLayout btnAdd;
        LinearLayout llAddQuan;
        LinearLayout outofsIn;
        LinearLayout outofs;

        public Holder(@NonNull View itemView) {
            super(itemView);
            unit = itemView.findViewById(R.id.unit);
            unitvalue = itemView.findViewById(R.id.unitvalue);
            mprice = itemView.findViewById(R.id.price);
            mrp = itemView.findViewById(R.id.producrmrp);
            prodImage = itemView.findViewById(R.id.prodImage);
            btnAdd = itemView.findViewById(R.id.btn_Add);
            outofsIn = itemView.findViewById(R.id.outofs_in);
            outofs = itemView.findViewById(R.id.outofs);
            llAddQuan = itemView.findViewById(R.id.ll_addQuan);
            txtQuan = itemView.findViewById(R.id.txtQuan);
            minus = itemView.findViewById(R.id.minus);
            plus = itemView.findViewById(R.id.plus);
        }
    }
}
