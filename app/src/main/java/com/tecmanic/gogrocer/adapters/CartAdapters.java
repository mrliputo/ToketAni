package com.tecmanic.gogrocer.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.util.DatabaseHandler;
import com.tecmanic.gogrocer.util.SessionManagement;
import com.tecmanic.gogrocer.util.ViewNotifier;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import static com.tecmanic.gogrocer.config.BaseURL.IMG_URL;

/**
 * Created by Rajesh Dabhi on 26/6/2017.
 */

public class CartAdapters extends RecyclerView.Adapter<CartAdapters.ProductHolder> {
    List<HashMap<String, String>> list;
    Activity activity;
    DatabaseHandler dbHandler;
    private ViewNotifier notifier;
    private SessionManagement sessionManagement;
    private Context context;
    private String varientKey = "varient_id";
    private String priceKey = "price";
    private DecimalFormat dFormat;

    public CartAdapters(Activity activity, List<HashMap<String, String>> list, ViewNotifier viewNotifier) {
        this.list = list;
        this.activity = activity;
        notifier = viewNotifier;
        context = activity;
        dbHandler = new DatabaseHandler(activity);
        sessionManagement = new SessionManagement(activity);
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cart, parent, false);
        context = parent.getContext();
        dFormat = new DecimalFormat("#.##");
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {
        HashMap<String, String> map = list.get(position);
        holder.currencyIndicator.setText(sessionManagement.getCurrency());
        Picasso.with(activity)
                .load(IMG_URL + map.get("product_image")).networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                .placeholder(R.drawable.splashicon)
                .into(holder.ivLogo);
        holder.tvTitle.setText(map.get("product_name"));
        holder.pDescrptn.setText(map.get("product_description"));
        double sprice = Double.parseDouble(map.get(priceKey));
        int qtyd = Integer.parseInt(dbHandler.getInCartItemQtys(map.get(varientKey)));
        if (qtyd > 0) {
            holder.tvAdd.setVisibility(View.GONE);
            holder.llAddQuan.setVisibility(View.VISIBLE);
            holder.tvContetiy.setText("" + qtyd);
            holder.pPrice.setText("" + dFormat.format((sprice * qtyd)));
        } else {
            holder.tvAdd.setVisibility(View.VISIBLE);
            holder.llAddQuan.setVisibility(View.GONE);
            holder.pPrice.setText("" + dFormat.format(sprice));
            holder.tvContetiy.setText("" + 0);
        }
        holder.pQuan.setText("" + map.get("unit_value")+""+map.get("unit"));
        holder.tvAdd.setOnClickListener(view -> {
            holder.tvAdd.setVisibility(View.GONE);
            holder.llAddQuan.setVisibility(View.VISIBLE);
            dbHandler.setCart(map, Integer.valueOf(holder.tvContetiy.getText().toString()));
            Double items1 = Double.parseDouble(dbHandler.getInCartItemQty(map.get(varientKey)));
            Double price = Double.parseDouble(map.get(priceKey));
            holder.pPrice.setText("" + dFormat.format((price * items1)));
            updateintent(dbHandler, view.getContext());
        });

        holder.txtClose.setOnClickListener(view -> {
            dbHandler.removeItemFromCart(map.get(varientKey));
            list.remove(position);
            notifyDataSetChanged();
            updateintent(dbHandler, view.getContext());
        });

        holder.ivMinus.setOnClickListener(v -> {

            int i = Integer.parseInt(dbHandler.getInCartItemQtys(map.get(varientKey)));
            double price = Double.parseDouble(map.get(priceKey));
            if ((i - 1) < 0 || (i - 1) == 0) {
                holder.tvAdd.setVisibility(View.VISIBLE);
                holder.llAddQuan.setVisibility(View.GONE);
                holder.tvContetiy.setText("" + 0);
                holder.pPrice.setText("" + dFormat.format(price));
            } else {
                holder.tvContetiy.setText("" + (i - 1));
                holder.pPrice.setText("" + dFormat.format((price * (i - 1))));
            }
            updateMultiply(position, (i - 1));
        });

        holder.ivPlus.setOnClickListener(v -> {
            try {
                holder.tvAdd.setVisibility(View.GONE);
                holder.llAddQuan.setVisibility(View.VISIBLE);
                if (dbHandler == null) {
                    dbHandler = new DatabaseHandler(v.getContext());
                }
                double price = Double.parseDouble(map.get(priceKey));
                int i = Integer.parseInt(dbHandler.getInCartItemQtys(map.get(varientKey)));

                if (i < Integer.parseInt(map.get("stock"))) {
                    holder.tvContetiy.setText("" + (i + 1));
                    holder.pPrice.setText("" + dFormat.format((price * (i + 1))));
                    updateMultiply(position, (i + 1));
                }else {
                    Toast.makeText(context,"No more stock available!",Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void updateMultiply(int pos, int i) {
        try {

            if (i > 0) {
                dbHandler.setCart(list.get(pos), i);
            } else {
                dbHandler.removeItemFromCart(list.get(pos).get(varientKey));
                list.remove(pos);
                notifyDataSetChanged();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                SharedPreferences preferences = context.getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                preferences.edit().putInt("cardqnty", dbHandler.getCartCount()).apply();
            }
            updateintent(dbHandler, context);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void updateintent(DatabaseHandler dbHandler, Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                SharedPreferences preferences = context.getSharedPreferences("GOGrocer", Context.MODE_PRIVATE);
                preferences.edit().putInt("cardqnty", dbHandler.getCartCount()).apply();
                if (notifier != null) {
                    notifier.onViewNotify();
                }
            }
            Intent updates = new Intent("Grocery_cart");
            updates.putExtra("type", "update");
            activity.sendBroadcast(updates);
        } catch (Exception ep) {
            ep.printStackTrace();
        }

    }

    class ProductHolder extends RecyclerView.ViewHolder {
        ImageView ivLogo;
        TextView tvTitle;
        TextView txtClose;
        TextView tvContetiy;
        TextView ivPlus;
        TextView ivMinus;
        TextView pDescrptn;
        TextView pQuan;
        TextView pPrice;
        TextView pdiscountOff;
        TextView pMrp;
        TextView currencyIndicator;
        LinearLayout tvAdd;
        LinearLayout llAddQuan;

        public ProductHolder(View view) {
            super(view);

            tvTitle = view.findViewById(R.id.txt_pName);
            currencyIndicator = view.findViewById(R.id.currency_indicator);
            ivLogo = view.findViewById(R.id.prodImage);

            tvContetiy = view.findViewById(R.id.txtQuan);
            tvAdd = view.findViewById(R.id.btn_Add);
            llAddQuan = view.findViewById(R.id.ll_addQuan);
            ivPlus = view.findViewById(R.id.plus);
            ivMinus = view.findViewById(R.id.minus);

            pDescrptn = view.findViewById(R.id.txt_pInfo);
            pQuan = view.findViewById(R.id.txt_unit);
            pPrice = view.findViewById(R.id.txt_Pprice);
            pdiscountOff = view.findViewById(R.id.txt_discountOff);
            pMrp = view.findViewById(R.id.txt_Mrp);
            txtClose = view.findViewById(R.id.txt_close);
        }
    }

}

