package com.tecmanic.gogrocer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.modelclass.SearchModel;

import java.util.List;

public class CustomAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflter;
    List<SearchModel> citylist;

    public CustomAdapter(Context applicationContext, List<SearchModel> citylist) {
        this.context = applicationContext;
        inflter = (LayoutInflater.from(applicationContext));
        this.citylist = citylist;
    }


    @Override
    public int getCount() {
        return citylist.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View views, ViewGroup viewGroup) {
        View view = inflter.inflate(R.layout.custom_spinner_items, null);
        TextView textcities = view.findViewById(R.id.textcities);
        textcities.setText(citylist.get(i).getpNAme());
        return view;
    }
}
