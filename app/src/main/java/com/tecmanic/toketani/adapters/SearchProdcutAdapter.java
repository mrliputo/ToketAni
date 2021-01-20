package com.tecmanic.toketani.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tecmanic.toketani.R;
import com.tecmanic.toketani.modelclass.SearchProductModel;

import java.util.List;

public class SearchProdcutAdapter extends RecyclerView.Adapter<SearchProdcutAdapter.SearchProductView> {

    private List<SearchProductModel> searchList;

    public class SearchProductView extends RecyclerView.ViewHolder {
        TextView title;

        public SearchProductView(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.pNAme);

        }
    }


    public SearchProdcutAdapter(List<SearchProductModel> searchList) {
        this.searchList = searchList;
    }

    @NonNull
    @Override
    public SearchProductView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_searchlist, parent, false);

        return new SearchProductView(itemView);
    }

    @Override
    public void onBindViewHolder(SearchProductView holder, int position) {
        SearchProductModel ss = searchList.get(position);
        holder.title.setText(ss.getpNAme());

    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }
}
