package com.tecmanic.toketani.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;
import com.tecmanic.toketani.R;
import com.tecmanic.toketani.config.BaseURL;
import com.tecmanic.toketani.modelclass.homemodel.Banner1;

import java.util.List;


public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerImage> {


    private Context context;
    private List<Banner1> images;

    public BannerAdapter(Context context, List<Banner1> images) {
        this.context = context;
        this.images = images;
    }

    @NonNull
    @Override
    public BannerAdapter.BannerImage onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_layout, parent, false);
        context = parent.getContext();
        return new BannerImage(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerAdapter.BannerImage holder, int position) {
//        Log.e("tag", "onBindViewHolder: " + images.get(position));
        Picasso.with(context).load(BaseURL.BANN_IMG_URL + images.get(position).getBanner_image()).memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).error(R.drawable.splashicon).into(holder.img);
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
