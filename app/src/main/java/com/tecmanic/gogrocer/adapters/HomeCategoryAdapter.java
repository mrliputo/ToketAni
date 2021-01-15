package com.tecmanic.gogrocer.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.modelclass.HomeCate;
import com.tecmanic.gogrocer.modelclass.SubCatModel;
import com.tecmanic.gogrocer.util.CategoryFragmentClick;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.tecmanic.gogrocer.config.BaseURL.IMG_URL;

public class HomeCategoryAdapter extends RecyclerView.Adapter<HomeCategoryAdapter.MyViewHolder> {

    private List<HomeCate> homeCateList;
    private Context context;
    private List<SubCatModel> subCatModels = new ArrayList<>();
    private CategoryFragmentClick categoryFragmentClick;
    private Random rnd = null;

    public HomeCategoryAdapter(List<HomeCate> homeCateList, Context context, CategoryFragmentClick categoryFragmentClick) {
        this.homeCateList = homeCateList;
        this.context = context;
        this.categoryFragmentClick = categoryFragmentClick;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_homeshopcate, parent, false);
        enableRnd();
        return new MyViewHolder(itemView);
    }

    private void enableRnd() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            try {
                rnd = SecureRandom.getInstanceStrong();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        } else {
            rnd = new Random();
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        int currentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.linearLayout.setBackgroundColor(currentColor);
        HomeCate cc = homeCateList.get(position);
        holder.prodNAme.setText(cc.getName());
        holder.pdetails.setText(cc.getDetail());
        holder.image.setImageResource(R.drawable.splashicon);
        Picasso.with(context)
                .load(IMG_URL + cc.getImages()).networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                .placeholder(R.drawable.splashicon)
                .into(holder.image);
        if (cc.getSubArray() == null || cc.getSubArray().length() == 0) {
            holder.pimage.setVisibility(View.GONE);
        }
        holder.cardView.setOnClickListener(v -> {
            if (holder.mines) {
                holder.mines = false;
                if (cc.getSubArray() != null) {
                    holder.recyclerSubCate.setVisibility(View.VISIBLE);
                    holder.pimage.setVisibility(View.GONE);
                    holder.image1.setVisibility(View.VISIBLE);
                    getSubcateory(cc.getSubArray(), holder.recyclerSubCate, cc.getId());
                }
            } else {
                holder.mines = true;
                holder.recyclerSubCate.setVisibility(View.GONE);
                holder.pimage.setVisibility(View.VISIBLE);
                holder.image1.setVisibility(View.GONE);

            }
        });
    }

    @Override
    public int getItemCount() {
        return homeCateList.size();
    }

    private void getSubcateory(JSONArray response, RecyclerView recyclerView, String catId) {
        subCatModels.clear();
        if (response.length() == 0) {
            if (categoryFragmentClick != null) {
                categoryFragmentClick.onClick(catId);
            }
        } else {
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject object = response.getJSONObject(i);
                    SubCatModel model = new SubCatModel();
                    model.setDetail(object.getString("description"));
                    model.setId(object.getString("cat_id"));
                    model.setImages(object.getString("image"));
                    model.setName(object.getString("title"));
                    if (object.has("subchild")) {
                        model.setSubArray(object.getJSONArray("subchild"));
                    }
                    subCatModels.add(model);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            SubCatAdapter cateAdapter = new SubCatAdapter(subCatModels, context, categoryFragmentClick);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(cateAdapter);
            cateAdapter.notifyDataSetChanged();

        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView prodNAme;
        TextView pdetails;
        ImageView image;
        ImageView pimage;
        ImageView image1;
        boolean mines = true;
        RecyclerView recyclerSubCate;
        CardView cardView;
        RelativeLayout linearLayout;

        public MyViewHolder(View view) {
            super(view);
            prodNAme = view.findViewById(R.id.pNAme);
            pdetails = view.findViewById(R.id.pDetails);
            pimage = view.findViewById(R.id.image);
            image1 = view.findViewById(R.id.image1);
            image = view.findViewById(R.id.Pimage);
            recyclerSubCate = view.findViewById(R.id.recyclerSubCate);
            cardView = view.findViewById(R.id.cardView);
            linearLayout = view.findViewById(R.id.ll1);
        }
    }

}
