package com.tecmanic.gogrocer.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.modelclass.NewPendingOrderModel;
import com.tecmanic.gogrocer.util.SessionManagement;
import com.tecmanic.gogrocer.util.TodayOrderClickListner;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class MyPendingOrderAdapter extends RecyclerView.Adapter<MyPendingOrderAdapter.MyViewHolder> {

    SharedPreferences preferences;
    private List<NewPendingOrderModel> modelList;
    private Context context;
    private SessionManagement sessionManagement;
    private TodayOrderClickListner todayOrderClickListner;
    private String pendingKey = "Pending";
    private String pendingViewKey = "pending";

    public MyPendingOrderAdapter(List<NewPendingOrderModel> modelList, TodayOrderClickListner todayOrderClickListner) {
        this.modelList = modelList;
        this.todayOrderClickListner = todayOrderClickListner;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listtem_pendingorder, parent, false);
        context = parent.getContext();
        sessionManagement = new SessionManagement(context);
        return new MyViewHolder(itemView);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NewPendingOrderModel mList = modelList.get(position);
        holder.reorderBtn.setVisibility(View.GONE);
        holder.tvOrderno.setText(mList.getCartId());
        if (mList.getOrderStatus().equalsIgnoreCase("Completed")) {
            holder.relativeBackground.setCardBackgroundColor(getColor(0, 128, 0));
            holder.relativetextstatus.setText("Completed");
            holder.l1.setVisibility(View.VISIBLE);
            holder.btnLay.setVisibility(View.VISIBLE);
            holder.canclebtn.setVisibility(View.GONE);
            holder.confirm.setVisibility(View.GONE);
            holder.outForDeliverde.setVisibility(View.GONE);
            holder.delivered.setVisibility(View.GONE);
            holder.confirm1.setVisibility(View.VISIBLE);
            holder.outForDeliverde1.setVisibility(View.VISIBLE);
            holder.delivered1.setVisibility(View.VISIBLE);

        } else if (mList.getOrderStatus().equalsIgnoreCase(pendingKey)) {
            holder.relativetextstatus.setText(pendingKey);
            holder.l1.setVisibility(View.VISIBLE);
            holder.btnLay.setVisibility(View.VISIBLE);
            holder.confirm.setVisibility(View.VISIBLE);
            holder.canclebtn.setVisibility(View.VISIBLE);
            holder.outForDeliverde.setVisibility(View.VISIBLE);
            holder.delivered.setVisibility(View.VISIBLE);
            holder.confirm1.setVisibility(View.GONE);
            holder.outForDeliverde1.setVisibility(View.GONE);
            holder.delivered1.setVisibility(View.GONE);
        } else if (mList.getOrderStatus().equalsIgnoreCase("Confirmed")) {
            holder.relativetextstatus.setText("Confirmed");
            holder.l1.setVisibility(View.VISIBLE);
            holder.btnLay.setVisibility(View.VISIBLE);
            holder.canclebtn.setVisibility(View.GONE);
            holder.confirm.setVisibility(View.GONE);
            holder.outForDeliverde.setVisibility(View.VISIBLE);
            holder.delivered.setVisibility(View.VISIBLE);
            holder.confirm1.setVisibility(View.VISIBLE);
            holder.outForDeliverde1.setVisibility(View.GONE);
            holder.delivered1.setVisibility(View.GONE);
        } else if (mList.getOrderStatus().equalsIgnoreCase("Out_For_Delivery")) {
            holder.relativetextstatus.setText("Out For Delivery");
            holder.btnLay.setVisibility(View.VISIBLE);
            holder.canclebtn.setVisibility(View.GONE);
            holder.l1.setVisibility(View.VISIBLE);
            holder.confirm.setVisibility(View.GONE);
            holder.outForDeliverde.setVisibility(View.GONE);
            holder.delivered.setVisibility(View.VISIBLE);
            holder.confirm1.setVisibility(View.VISIBLE);
            holder.outForDeliverde1.setVisibility(View.VISIBLE);
            holder.delivered1.setVisibility(View.GONE);
        } else if (mList.getOrderStatus().equalsIgnoreCase("Cancelled")) {
            holder.relativeBackground.setCardBackgroundColor(getColor(255, 0, 0));
            holder.relativetextstatus.setText("Cancelled");
            holder.canclebtn.setVisibility(View.GONE);
            holder.reorderBtn.setVisibility(View.VISIBLE);
            holder.orderDetails.setVisibility(View.VISIBLE);
            holder.l1.setVisibility(View.GONE);
        }

        if (mList.getPaymentStatus() == null) {
            holder.tvStatus.setText("Payment" + " " + pendingKey);
        } else {
            if (mList.getPaymentStatus().equalsIgnoreCase("success") || mList.getPaymentStatus().equalsIgnoreCase("failed") || mList.getPaymentStatus().equalsIgnoreCase("COD")) {
                holder.tvStatus.setText("Payment" + " " + mList.getPaymentStatus());
            }
        }

        if (mList.getPaidByWallet() != null && !mList.getPaidByWallet().equalsIgnoreCase("") && !mList.getPaidByWallet().equalsIgnoreCase("0")) {
            holder.walletLayout.setVisibility(View.VISIBLE);
            holder.tvWalletAmount.setText("- " + sessionManagement.getCurrency() + "" + mList.getPaidByWallet());
        } else {
            holder.walletLayout.setVisibility(View.GONE);
        }

        if (mList.getCouponDiscount() != null && !mList.getCouponDiscount().equalsIgnoreCase("") && !mList.getCouponDiscount().equalsIgnoreCase("0")) {
            holder.couponLayout.setVisibility(View.VISIBLE);
            holder.tvCouponAmount.setText("- " + sessionManagement.getCurrency() + "" + mList.getCouponDiscount());
        } else {
            holder.couponLayout.setVisibility(View.GONE);
        }

        if (mList.getDelCharge() != null && !mList.getDelCharge().equalsIgnoreCase("")) {
            holder.tvDeliveryAmount.setText(sessionManagement.getCurrency() + "" + mList.getDelCharge());
            holder.tvOrderPrice2.setText(sessionManagement.getCurrency() + "" + ((int) (Double.parseDouble(mList.getPrice()) - Double.parseDouble(mList.getDelCharge()))));
        } else {
            holder.tvOrderPrice2.setText(sessionManagement.getCurrency() + "" + mList.getPrice());
            holder.tvDeliveryAmount.setText(sessionManagement.getCurrency() + " 0");
        }

        if (mList.getDboyName() != null && !mList.getDboyName().equalsIgnoreCase("")) {
            holder.orderAssingLay.setVisibility(View.VISIBLE);
            holder.ivOrderBoyName.setText(mList.getDboyName());
            holder.ivDeliveryNumber.setText(mList.getDboyPhone());
        } else {
            holder.orderAssingLay.setVisibility(View.GONE);
        }

        holder.ivOrderDetailImg.setOnClickListener(v -> {
            if (holder.deliveryBoyDetails.getVisibility() == View.VISIBLE) {
                holder.deliveryBoyDetails.setVisibility(View.GONE);
            } else {
                holder.deliveryBoyDetails.setVisibility(View.VISIBLE);
            }
        });

        holder.ivCallOrder.setOnClickListener(v -> todayOrderClickListner.onCallToDeliveryBoy(mList.getDboyPhone()));

        holder.infoPrice.setOnClickListener(v -> {
            if (holder.priceDeatils.getVisibility() == View.VISIBLE) {
                holder.priceDeatils.setVisibility(View.GONE);
            } else {
                holder.priceDeatils.setVisibility(View.VISIBLE);
            }
        });


        holder.tvPendingDate.setText(mList.getDeliveryDate());
        holder.tvConfirmDate.setText(mList.getDeliveryDate());
        holder.tvDeleveredDate.setText(mList.getDeliveryDate());
        holder.tvCancelDate.setText(mList.getDeliveryDate());

        if (mList.getPaymentMethod().equals("Store Pick Up")) {
            holder.tvMethid1.setText(mList.getPaymentMethod());
        } else if (mList.getPaymentMethod().equalsIgnoreCase("COD")) {
            holder.tvMethid1.setText("Cash On Delivery");
        } else if (mList.getPaymentMethod().equalsIgnoreCase("card")||mList.getPaymentMethod().equalsIgnoreCase("Cards")) {
            holder.tvMethid1.setText("PrePaid");
        } else if (mList.getPaymentMethod().equalsIgnoreCase("net_banking")) {
            holder.tvMethid1.setText("PrePaid");
        } else if (mList.getPaymentMethod().equalsIgnoreCase("Wallet")) {
            holder.tvMethid1.setText("Wallet");
        }
        holder.tvDate.setText(mList.getDeliveryDate());
        holder.tvTrackingDate.setText(mList.getDeliveryDate());

        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        String language = preferences.getString("language", "");

        if (language.contains("spanish")) {
            String timefrom = mList.getTimeSlot();


            timefrom = timefrom.replace("pm", "م");
            timefrom = timefrom.replace("am", "ص");


            String time = timefrom;

            holder.tvTime.setText(time);
        } else {
            holder.tvTime.setText(mList.getTimeSlot());
        }

        holder.tvPrice.setText(sessionManagement.getCurrency() + "" + mList.getPrice());
        if (mList.getRemainingAmount() != null && !mList.getRemainingAmount().equalsIgnoreCase("")) {
            holder.tvPayAbleamount.setText(sessionManagement.getCurrency() + "" + mList.getRemainingAmount());
            holder.tvTotalPay.setText(sessionManagement.getCurrency() + "" + mList.getRemainingAmount());
        } else {
            holder.tvPayAbleamount.setText(sessionManagement.getCurrency() + "" + mList.getPrice());
        }

        holder.tvItem.setText("" + mList.getData().size());

        holder.orderDetails.setOnClickListener(v -> todayOrderClickListner.onClickForOrderDetails(holder.getAdapterPosition(), pendingViewKey));

        holder.reorderBtn.setOnClickListener(v -> todayOrderClickListner.onReorderClick(position, pendingViewKey));

        holder.canclebtn.setOnClickListener(v -> todayOrderClickListner.onCancelClick(position, pendingViewKey));

    }


    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public int getColor(int r, int g, int b) {
        return Color.rgb(r, g, b);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderno;
        TextView tvStatus;
        TextView tvDate;
        TextView tvTime;
        TextView tvPrice;
        TextView tvItem;
        TextView relativetextstatus;
        TextView tvTrackingDate;
        TextView tvPayAbleamount;
        TextView tvOrderPrice2;
        TextView tvWalletAmount;
        TextView tvCouponAmount;
        TextView tvDeliveryAmount;
        TextView tvTotalPay;
        TextView tvPendingDate;
        TextView tvConfirmDate;
        TextView tvDeleveredDate;
        TextView tvCancelDate;
        View view1;
        View view2;
        View view3;
        View view4;
        View view5;
        View view6;
        CardView relativeBackground;
        LinearLayout rr;
        LinearLayout priceDeatils;
        CircleImageView confirm;
        CircleImageView outForDeliverde;
        CircleImageView delivered;
        CircleImageView confirm1;
        CircleImageView outForDeliverde1;
        CircleImageView delivered1;
        TextView tvMethid1;
        ImageView infoPrice;
        TextView canclebtn;
        TextView reorderBtn;
        TextView orderDetails;
        TextView ivOrderBoyName;
        TextView ivDeliveryNumber;
        LinearLayout deliveryBoyDetails;
        LinearLayout orderAssingLay;
        ImageView ivCallOrder;
        ImageView ivOrderDetailImg;
        LinearLayout l1;
        LinearLayout btnLay;
        LinearLayout walletLayout;
        LinearLayout couponLayout;
        LinearLayout deliveryLayout;

        public MyViewHolder(View view) {

            super(view);
            tvOrderno = view.findViewById(R.id.tv_order_no);
            tvPayAbleamount = view.findViewById(R.id.tv_pay_ableamount);
            orderAssingLay = view.findViewById(R.id.order_assing_lay);
            ivOrderDetailImg = view.findViewById(R.id.iv_order_detail_img);
            ivDeliveryNumber = view.findViewById(R.id.iv_delivery_number);
            ivOrderBoyName = view.findViewById(R.id.iv_order_boy_name);
            deliveryBoyDetails = view.findViewById(R.id.delivery_boy_details);
            ivCallOrder = view.findViewById(R.id.iv_call_order);
            tvOrderPrice2 = view.findViewById(R.id.tv_order_price_2);
            tvCouponAmount = view.findViewById(R.id.tv_coupon_amount);
            tvTotalPay = view.findViewById(R.id.tv_total_pay);
            tvDeliveryAmount = view.findViewById(R.id.tv_delivery_amount);
            tvWalletAmount = view.findViewById(R.id.tv_wallet_amount);
            deliveryLayout = view.findViewById(R.id.delivery_layout);
            walletLayout = view.findViewById(R.id.wallet_layout);
            couponLayout = view.findViewById(R.id.coupon_layout);
            priceDeatils = view.findViewById(R.id.price_deatils);
            infoPrice = view.findViewById(R.id.info_price);
            tvStatus = view.findViewById(R.id.tv_order_status);
            relativetextstatus = view.findViewById(R.id.status);
            tvTrackingDate = view.findViewById(R.id.tracking_date);
            tvDate = view.findViewById(R.id.tv_order_date);
            tvTime = view.findViewById(R.id.tv_order_time);
            tvPrice = view.findViewById(R.id.tv_order_price);
            tvItem = view.findViewById(R.id.tv_order_item);
            canclebtn = view.findViewById(R.id.canclebtn);
            reorderBtn = view.findViewById(R.id.reorder_btn);
            l1 = view.findViewById(R.id.l1);
            btnLay = view.findViewById(R.id.btn_lay);
            rr = view.findViewById(R.id.rrrr);
            orderDetails = view.findViewById(R.id.order_details);
            tvMethid1 = view.findViewById(R.id.method1);
            tvPendingDate = view.findViewById(R.id.pending_date);
            tvConfirmDate = view.findViewById(R.id.confirm_date);
            tvDeleveredDate = view.findViewById(R.id.delevered_date);
            tvCancelDate = view.findViewById(R.id.cancel_date);
            view1 = view.findViewById(R.id.view1);
            view2 = view.findViewById(R.id.view2);
            view3 = view.findViewById(R.id.view3);
            view4 = view.findViewById(R.id.view4);
            view5 = view.findViewById(R.id.view5);
            view6 = view.findViewById(R.id.view6);
            relativeBackground = view.findViewById(R.id.relative_background);

            confirm = view.findViewById(R.id.confirm_image);
            outForDeliverde = view.findViewById(R.id.delivered_image);
            delivered = view.findViewById(R.id.cancal_image);
            confirm1 = view.findViewById(R.id.confirm_image1);
            outForDeliverde1 = view.findViewById(R.id.delivered_image1);
            delivered1 = view.findViewById(R.id.cancal_image1);
        }
    }

}

