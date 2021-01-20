package com.tecmanic.toketani.modelclass;

import java.util.List;

public class ListAssignAndUnassigned {

    private String viewType;
    private List<NewPendingOrderModel> todayOrderModels;
    private List<NewPendingOrderModel> nextDayOrders;

    public ListAssignAndUnassigned(String viewType, List<NewPendingOrderModel> todayOrderModels, List<NewPendingOrderModel> nextDayOrders) {
        this.viewType = viewType;
        this.todayOrderModels = todayOrderModels;
        this.nextDayOrders = nextDayOrders;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public List<NewPendingOrderModel> getTodayOrderModels() {
        return todayOrderModels;
    }

    public void setTodayOrderModels(List<NewPendingOrderModel> todayOrderModels) {
        this.todayOrderModels = todayOrderModels;
    }

    public List<NewPendingOrderModel> getNextDayOrders() {
        return nextDayOrders;
    }

    public void setNextDayOrders(List<NewPendingOrderModel> nextDayOrders) {
        this.nextDayOrders = nextDayOrders;
    }

}
