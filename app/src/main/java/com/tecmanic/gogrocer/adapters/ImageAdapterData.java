package com.tecmanic.gogrocer.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.util.DatabaseHandler;

import java.util.HashMap;
import java.util.List;

import static com.tecmanic.gogrocer.config.BaseURL.IMG_URL;

/**
 * Created by Rajesh Dabhi on 26/6/2017.
 */

public class ImageAdapterData extends RecyclerView.Adapter<ImageAdapterData.ProductHolder> {
    List<HashMap<String, String>> list;
    Activity activity;
    DatabaseHandler dbHandler;

    public ImageAdapterData(Activity activity, List<HashMap<String, String>> list) {
        this.list = list;
        this.activity = activity;

        dbHandler = new DatabaseHandler(activity);
    }

    @Override
    public ProductHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.imageadapter, parent, false);
        return new ProductHolder(view);
    }

    @Override
    public void onBindViewHolder(final ProductHolder holder, final int position) {

        final HashMap<String, String> map = list.get(position);
        Picasso.with(activity)
                .load(IMG_URL + map.get("product_image")).networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                .into(holder.imageData);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ProductHolder extends RecyclerView.ViewHolder {
        ImageView imageData;

        public ProductHolder(View view) {
            super(view);
            imageData = view.findViewById(R.id.image_data);
        }
    }
}

