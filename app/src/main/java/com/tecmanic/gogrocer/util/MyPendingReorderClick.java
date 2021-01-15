package com.tecmanic.gogrocer.util;

import com.tecmanic.gogrocer.modelclass.NewPendingDataModel;

import java.util.ArrayList;

public interface MyPendingReorderClick {
    void onReorderClick(ArrayList<NewPendingDataModel> models);
}
