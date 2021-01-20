package com.tecmanic.toketani.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tecmanic.toketani.R;
import com.tecmanic.toketani.modelclass.SearchModel;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> {

private List<SearchModel> searchList;

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView title;

    public MyViewHolder(View view) {
        super(view);
        title = (TextView) view.findViewById(R.id.pNAme);

    }
}


    public SearchAdapter(List<SearchModel> searchList) {
        this.searchList = searchList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_searchlist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SearchModel ss = searchList.get(position);
        holder.title.setText(ss.getpNAme());

    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }
}