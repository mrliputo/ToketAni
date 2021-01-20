package com.tecmanic.toketani.adapters;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.tecmanic.toketani.R;
import com.tecmanic.toketani.config.BaseURL;
import com.tecmanic.toketani.modelclass.homemodel.NewTopCategory;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

/**
 * Created by Rajesh Dabhi on 22/6/2017.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyViewHolder> {

    private List<NewTopCategory> modelList;
    private Context context;
    private Random rnd = null;

    public HomeAdapter(List<NewTopCategory> modelList) {
        this.modelList = modelList;
    }

    @Override
    public HomeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_home_rv, parent, false);
        context = parent.getContext();
        enableRnd();
        return new HomeAdapter.MyViewHolder(itemView);
    }

    private void enableRnd() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                rnd = SecureRandom.getInstanceStrong();
            } catch (NoSuchAlgorithmException e) {
                rnd = new Random();
                e.printStackTrace();
            }
        } else {
            rnd = new Random();
        }
    }

    @Override
    public void onBindViewHolder(HomeAdapter.MyViewHolder holder, int position) {
        NewTopCategory mList = modelList.get(position);
        //  int currentColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));  //bright colors
        final int baseColor = Color.WHITE;

        final int baseRed = Color.red(baseColor);
        final int baseGreen = Color.green(baseColor);
        final int baseBlue = Color.blue(baseColor);

        final int red = (baseRed + rnd.nextInt(256)) / 2;
        final int green = (baseGreen + rnd.nextInt(256)) / 2;
        final int blue = (baseBlue + rnd.nextInt(256)) / 2;
        int clr1 = Color.rgb(red, green, blue);                                 //pastel colors
        holder.linearLayout.setBackgroundColor(clr1);

        Picasso.with(context)
                .load(BaseURL.IMG_URL + mList.getImage()).networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                .placeholder(R.drawable.splashicon)
                .into(holder.image);
        holder.title.setText(mList.getTitle());


    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView image;
        LinearLayout linearLayout;
        CardView cardview1;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.tv_home_title);
            image = view.findViewById(R.id.iv_home_img);
            linearLayout = view.findViewById(R.id.ll1);
            cardview1 = view.findViewById(R.id.cardview1);
        }
    }

}

