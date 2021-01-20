package com.tecmanic.toketani.util;

import com.tecmanic.toketani.modelclass.NewPendingDataModel;

import java.util.ArrayList;

public interface ForReorderListner {

    void onReorderClick(ArrayList<NewPendingDataModel> pastOrderSubModelArrayList);
}
