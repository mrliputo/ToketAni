package com.tecmanic.gogrocer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.config.BaseURL;
import com.tecmanic.gogrocer.modelclass.homemodel.Banner2;

import java.util.List;

public class BannerAdapter2 extends RecyclerView.Adapter<BannerAdapter2.BannerImage> {


    private Context context;
    private List<Banner2> images;

    public BannerAdapter2(Context context, List<Banner2> images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public BannerAdapter2.BannerImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.image_layout, parent, false);
        return new BannerImage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerAdapter2.BannerImage holder, int position) {
        Picasso.with(context).load(BaseURL.BANN_IMG_URL+images.get(position).getBanner_image()).memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE).error(R.drawable.splashicon).into(holder.img);
    }

    @Override
    public int getItemCount() {
        if (images.size() > 0) {
            return images.size();
        } else {
            return 0;
        }

    }

    public static class BannerImage extends RecyclerView.ViewHolder {

        private ImageView img;

        public BannerImage(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.banner_image);
        }
        // each data item is just a string in this case

    }
}
