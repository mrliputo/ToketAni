package com.tecmanic.toketani.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.tecmanic.toketani.R;

public class CustomSlider extends BaseSliderView {

    ImageView target;

    public CustomSlider(Context context) {
        super(context);

    }

    @Override
    public View getView() {
        CardView v = (CardView) LayoutInflater.from(getContext()).inflate(R.layout.home_slider, null);
        target = (ImageView) v.findViewById(R.id.image);
        bindEventAndShow(v, target);
        return v;
    }

}