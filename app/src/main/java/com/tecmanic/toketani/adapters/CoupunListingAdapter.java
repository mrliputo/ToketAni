package com.tecmanic.toketani.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tecmanic.toketani.R;
import com.tecmanic.toketani.modelclass.CoupunModel;
import com.tecmanic.toketani.util.CoupounClickListner;

import java.util.List;

public class CoupunListingAdapter extends RecyclerView.Adapter<CoupunListingAdapter.MyViewHolder> {

    private List<CoupunModel> modelList;
    private CoupounClickListner coupounClickListner;

    public CoupunListingAdapter(List<CoupunModel> coupunModelList, CoupounClickListner coupounClickListner) {
        this.modelList = coupunModelList;
        this.coupounClickListner = coupounClickListner;
    }

    public void setList(List<CoupunModel> coupunModelList) {
        this.modelList = coupunModelList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_couponlist, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        CoupunModel mList = modelList.get(position);

        holder.applybtn.setOnClickListener(v -> coupounClickListner.onClickApply(modelList.get(position).getCouponCode()));
        holder.coupunHeading.setText(mList.getCouponName());
        holder.discripCoupon.setText(mList.getCouponDescription());
        holder.copenText.setText(mList.getCouponCode());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView coupunHeading;
        TextView discripCoupon;
        TextView copenText;
        TextView applybtn;
        LinearLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            coupunHeading = view.findViewById(R.id.coupun_heading);
            discripCoupon = view.findViewById(R.id.discrip_coupon);
            linearLayout = view.findViewById(R.id.layout);
            copenText = view.findViewById(R.id.copen_text);
            applybtn = view.findViewById(R.id.applybtn);
        }

    }


}