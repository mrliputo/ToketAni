package com.tecmanic.gogrocer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.modelclass.SubCatModel;
import com.tecmanic.gogrocer.modelclass.SubChildCatModel;
import com.tecmanic.gogrocer.util.CategoryFragmentClick;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SubCatAdapter extends RecyclerView.Adapter<SubCatAdapter.MyViewHolder> {

    Context context;
    private List<SubChildCatModel> subChildCatModels = new ArrayList<>();
    private List<SubCatModel> homeCateList;
    private CategoryFragmentClick categoryFragmentClick;

    public SubCatAdapter(List<SubCatModel> homeCateList, Context context, CategoryFragmentClick categoryFragmentClick) {
        this.homeCateList = homeCateList;
        this.context = context;
        this.categoryFragmentClick = categoryFragmentClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_sub_cat_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        SubCatModel cc = homeCateList.get(position);
        holder.prodNAme.setText(cc.getName());
        holder.pdetails.setText(cc.getDetail());
        holder.cardView.setOnClickListener(v -> getSubcateory(cc.getSubArray(), holder.recyclerSubCate, cc.getId()));
    }

    @Override
    public int getItemCount() {
        return homeCateList.size();
    }

    private void getSubcateory(JSONArray response, RecyclerView recyclerView, String catId1) {


        if (response.length() == 0) {
            if (categoryFragmentClick != null) {
                categoryFragmentClick.onClick(catId1);
            }

        } else {


            for (int i = 0; i < response.length(); i++) {

                try {
                    JSONObject object = response.getJSONObject(i);

                    SubChildCatModel model = new SubChildCatModel();


                    model.setDetail(object.getString("description"));
                    model.setId(object.getString("cat_id"));
                    model.setImages(object.getString("image"));
                    model.setName(object.getString("title"));
                    subChildCatModels.add(model);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            SubChildCatAdapter cateAdapter = new SubChildCatAdapter(subChildCatModels, context, categoryFragmentClick);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            recyclerView.setAdapter(cateAdapter);
            cateAdapter.notifyDataSetChanged();

        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView prodNAme;
        TextView pdetails;
        ImageView image;
        RecyclerView recyclerSubCate;
        LinearLayout cardView;

        public MyViewHolder(View view) {
            super(view);
            prodNAme = view.findViewById(R.id.pNAme);
            pdetails = view.findViewById(R.id.pDetails);
            image = view.findViewById(R.id.Pimage);
            recyclerSubCate = view.findViewById(R.id.recyclerSubCate);
            cardView = view.findViewById(R.id.cardView);
        }
    }

}
