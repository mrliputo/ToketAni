package com.tecmanic.gogrocer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.config.BaseURL;
import com.tecmanic.gogrocer.modelclass.NewPendingDataModel;
import com.tecmanic.gogrocer.util.SessionManagement;

import java.util.List;

public class MyOrderDetailAdapter extends RecyclerView.Adapter<MyOrderDetailAdapter.MyViewHolder> {

    private List<NewPendingDataModel> modelList;
    private Context context;
    private SessionManagement sessionManagement;

    public MyOrderDetailAdapter(List<NewPendingDataModel> modelList) {
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_my_order_detail_rv, parent, false);

        context = parent.getContext();
        sessionManagement = new SessionManagement(context);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyOrderDetailAdapter.MyViewHolder holder, int position) {
        NewPendingDataModel mList = modelList.get(position);

        Picasso.with(context)
                .load(BaseURL.IMG_URL + mList.getVarientImage()).networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                .placeholder(R.drawable.splashicon)
                .into(holder.ivImg);

        if (mList.getDescription() != null && !mList.getDescription().equalsIgnoreCase("")) {
            holder.tvOrderDescrp.setVisibility(View.VISIBLE);
            holder.tvOrderDescrp.setText(mList.getDescription());
        } else {
            holder.tvOrderDescrp.setVisibility(View.GONE);
        }

        holder.tvTitle.setText(mList.getProductName());
        holder.tvPrice.setText(sessionManagement.getCurrency() + " " + mList.getPrice());
        holder.txtQty.setText("Qty - " + "" + mList.getQuantity() + "" + mList.getUnit());

    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        TextView tvPrice;
        TextView txtQty;
        TextView tvOrderDescrp;
        ImageView ivImg;

        public MyViewHolder(View view) {
            super(view);
            tvTitle = view.findViewById(R.id.tv_order_Detail_title);
            tvPrice = view.findViewById(R.id.tv_order_Detail_price);
            tvOrderDescrp = view.findViewById(R.id.tv_order_descrp);
            txtQty = view.findViewById(R.id.txtQty);
            ivImg = view.findViewById(R.id.iv_order_detail_img);

        }
    }

}