package com.tecmanic.toketani.util;

public interface TodayOrderClickListner {
    void onCallToDeliveryBoy(String number);
    void onClickForOrderDetails(int position, String viewType);
    void onReorderClick(int position, String viewType);
    void onCancelClick(int position, String viewType);
}
