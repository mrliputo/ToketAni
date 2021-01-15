package com.tecmanic.gogrocer.util;

public interface FragmentClickListner {
    void onFragmentClick(boolean open);
    void onChangeHome(boolean open);
    void onLocationPermission(boolean check);
}
