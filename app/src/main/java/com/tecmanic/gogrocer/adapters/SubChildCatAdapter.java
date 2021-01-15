package com.tecmanic.gogrocer.adapters;

import android.content.Context;
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
import com.tecmanic.gogrocer.modelclass.SubChildCatModel;
import com.tecmanic.gogrocer.util.CategoryFragmentClick;

import java.util.ArrayList;
import java.util.List;

public class SubChildCatAdapter extends RecyclerView.Adapter<SubChildCatAdapter.MyViewHolder> {

    Context context;
    private List<SubChildCatModel> subChildCatModels = new ArrayList<>();
    private CategoryFragmentClick categoryFragmentClick;

    public SubChildCatAdapter(List<SubChildCatModel> subChildCatModels, Context context, CategoryFragmentClick categoryFragmentClick) {
        this.subChildCatModels = subChildCatModels;
        this.context = context;
        this.categoryFragmentClick = categoryFragmentClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_sub_cat_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SubChildCatModel cc = subChildCatModels.get(position);
        holder.prodNAme.setText(cc.getName());
        holder.pdetails.setText(cc.getDetail());
        holder.image.setImageResource(R.drawable.splashicon);

        holder.cardView.setOnClickListener(v -> {
            if (categoryFragmentClick != null) {
                categoryFragmentClick.onClick(cc.getId());
            }

        });
    }

    @Override
    public int getItemCount() {
        return subChildCatModels.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView prodNAme;
        TextView pdetails;
        ImageView image;
        RecyclerView recyclerSubCate;
        LinearLayout cardView;

        public MyViewHolder(View view) {
            super(view);
            prodNAme = (TextView) view.findViewById(R.id.pNAme);
            pdetails = (TextView) view.findViewById(R.id.pDetails);
            image = (ImageView) view.findViewById(R.id.Pimage);
            recyclerSubCate = view.findViewById(R.id.recyclerSubCate);
            cardView = view.findViewById(R.id.cardView);
        }
    }
}
