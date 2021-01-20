package com.tecmanic.toketani.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
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
import com.tecmanic.toketani.util.ProdcutDetailsVerifier;
import com.tecmanic.toketani.util.SessionManagement;
import com.tecmanic.toketani.util.ViewNotifier;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import static com.tecmanic.toketani.config.BaseURL.IMG_URL;

public class DealAdapter extends RecyclerView.Adapter<DealAdapter.MyViewHolder> {
    private static final int LIMIT = 6;
    Context context;
    private List<NewCartModel> dealoftheday;
    private DatabaseHandler dbcart;
    private SessionManagement sessionManagement;
    private ViewNotifier viewNotifier;
    private String varientKey = "varient_id";
    private ProdcutDetailsVerifier prodcutDetailsVerifier;
    private DecimalFormat dFormat;


    public DealAdapter(Context context, List<NewCartModel> dealoftheday, ProdcutDetailsVerifier prodcutDetailsVerifier) {
        this.context = context;
        this.dealoftheday = dealoftheday;
        this.prodcutDetailsVerifier = prodcutDetailsVerifier;
        dbcart = new DatabaseHandler(context);
        sessionManagement = new SessionManagement(context);
    }

    public DealAdapter(Context context, List<NewCartModel> dealoftheday, ViewNotifier viewNotifier) {
        this.context = context;
        this.dealoftheday = dealoftheday;
        this.viewNotifier = viewNotifier;
        dbcart = new DatabaseHandler(context);
        sessionManagement = new SessionManagement(context);
    }

    @NonNull
    @Override
    public DealAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_topsell, parent, false);
        context = parent.getContext();
        dbcart = new DatabaseHandler(context);
        if (sessionManagement == null) {
            sessionManagement = new SessionManagement(parent.getContext());
        }
        dFormat = new DecimalFormat("#.##");
        return new DealAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DealAdapter.MyViewHolder holder, int position) {
        NewCartModel cc = dealoftheday.get(position);

        holder.currencyIndicator.setText(sessionManagement.getCurrency());
        holder.currencyIndicator2.setText(sessionManagement.getCurrency());
        sessionManagement.setStoreId(cc.getStoreId());
        holder.prodNAme.setText(cc.getProductName());
        holder.pDescrptn.setText(cc.getDescription());
        holder.pQuan.setText("" + cc.getQuantity() + " " + cc.getUnit());
        sessionManagement.setStoreId(cc.getStoreId());
        int qtyd = Integer.parseInt(dbcart.getInCartItemQtys(dealoftheday.get(position).getVarientId()));
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
        if (Double.parseDouble(cc.getMrp())>0){
            holder.pdiscountOff.setVisibility(View.VISIBLE);
            holder.pMrp.setVisibility(View.VISIBLE);
            double totalOff = Double.parseDouble(cc.getMrp()) - Double.parseDouble(cc.getPrice());
            holder.pdiscountOff.setText(sessionManagement.getCurrency() + dFormat.format(totalOff) + " " + "Off");
        }else {
            holder.pdiscountOff.setVisibility(View.GONE);
            holder.pMrp.setVisibility(View.GONE);
        }

        holder.pMrp.setPaintFlags(holder.pMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


        if (holder.timer != null) {
            holder.timer.cancel();
        }
        Long timer = Long.parseLong(dealoftheday.get(position).getTimediff());
        timer = timer * 60 * 1000;
        holder.timer = new CountDownTimer(timer, 1000) {
            public void onTick(long millisUntilFinished) {
                int sec = (int) (millisUntilFinished / 1000);
                int min = sec / 60;
                int our = min / 60;
                sec = sec % 60;
                min = min % 60;
                holder.time.setText(String.format(" %02d", our) + ":" + String.format("%02d", min) + ":" + String.format("%02d", sec));
            }

            public void onFinish() {
                holder.time.setText("00:00:00");
            }
        }.start();


        Picasso.with(context)
                .load(IMG_URL + cc.getProductImage()).networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                .placeholder(R.drawable.splashicon)
                .into(holder.image);

        holder.itemView.setOnClickListener(v -> {

            if (viewNotifier!=null){
                viewNotifier.onProductDetailClick(position);
            }else if (prodcutDetailsVerifier!=null){
                prodcutDetailsVerifier.onProductClick(position,"");
            }

        });


        holder.plus.setOnClickListener(v -> {
            try{
                holder.btnAdd.setVisibility(View.GONE);
                holder.llAddQuan.setVisibility(View.VISIBLE);
                int i = Integer.parseInt(dbcart.getInCartItemQtys(dealoftheday.get(position).getVarientId()));
                if (i<Integer.parseInt(cc.getStock())){
                    holder.txtQuan.setText("" + (i + 1));
                    double priced = Double.parseDouble(cc.getPrice());
                    double mrpd = Double.parseDouble(cc.getMrp());
                    holder.pPrice.setText("" + dFormat.format((priced * (i + 1))));
                    holder.pMrp.setText("" + dFormat.format((mrpd * (i + 1))));
                    updateMultiply(position, (i + 1));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        holder.minus.setOnClickListener(v -> {
            int i = Integer.parseInt(dbcart.getInCartItemQtys(dealoftheday.get(position).getVarientId()));
            i = i - 1;
            if (i < 1) {
                holder.btnAdd.setVisibility(View.VISIBLE);
                holder.llAddQuan.setVisibility(View.GONE);
                holder.txtQuan.setText("0");
                double priced = Double.parseDouble(cc.getPrice());
                double mrpd = Double.parseDouble(cc.getMrp());
                holder.pPrice.setText("" + dFormat.format(priced));
                holder.pMrp.setText("" +dFormat.format(mrpd));
            } else {
                holder.btnAdd.setVisibility(View.GONE);
                holder.llAddQuan.setVisibility(View.VISIBLE);
                holder.txtQuan.setText("" + (i));
                double priced = Double.parseDouble(cc.getPrice());
                double mrpd = Double.parseDouble(cc.getMrp());
                holder.pPrice.setText("" + dFormat.format((priced * i)));
                holder.pMrp.setText("" + dFormat.format((mrpd * i)));
            }
            updateMultiply(position, i);
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
        map.put(varientKey, dealoftheday.get(pos).getVarientId());
        map.put("product_name", dealoftheday.get(pos).getProductName());
        map.put("category_id", dealoftheday.get(pos).getProductId());
        map.put("title", dealoftheday.get(pos).getProductName());
        map.put("price", dealoftheday.get(pos).getPrice());
        map.put("mrp", dealoftheday.get(pos).getMrp());
        map.put("product_image", dealoftheday.get(pos).getProductImage());
        map.put("status", "");
        map.put("in_stock", "");
        map.put("unit_value", dealoftheday.get(pos).getQuantity());
        map.put("unit", dealoftheday.get(pos).getUnit());
        map.put("increament", "0");
        map.put("rewards", "0");
        map.put("stock", dealoftheday.get(pos).getStock());
        map.put("product_description", dealoftheday.get(pos).getDescription());
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
                SharedPreferences preferencess = context.getSharedPreferences("toketani", Context.MODE_PRIVATE);
                preferencess.edit().putInt("cardqnty", dbcart.getCartCount()).apply();
            }
        } catch (IndexOutOfBoundsException e) {
            Log.d("qwer", e.toString());
        }
    }

    @Override
    public int getItemCount() {

        if (dealoftheday.size() > LIMIT) {
            return LIMIT;
        } else {
            return dealoftheday.size();
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
        TextView time;
        TextView currencyIndicator;
        TextView currencyIndicator2;
        ImageView image;
        LinearLayout btnAdd;
        LinearLayout llAddQuan;
        CountDownTimer timer;
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
            txtQuan = view.findViewById(R.id.txtQuan);
            minus = view.findViewById(R.id.minus);
            plus = view.findViewById(R.id.plus);
            time = view.findViewById(R.id.time);

        }
    }


}

