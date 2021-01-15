package com.tecmanic.gogrocer.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.config.BaseURL;
import com.tecmanic.gogrocer.modelclass.Popular_model;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class Popular_adapter extends RecyclerView.Adapter<Popular_adapter.MyViewHolder> {

    private List<Popular_model> modelList;
    private Context context;
    String language;
    SharedPreferences preferences;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView image;
        LinearLayout linearLayout ;
        CardView cardview1;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tv_home_title);
            image = (ImageView) view.findViewById(R.id.iv_home_img);
            linearLayout =  view.findViewById(R.id.ll1);
            cardview1 =  view.findViewById(R.id.cardview1);
        }
    }

    public Popular_adapter(List<Popular_model> modelList) {
        this.modelList = modelList;
    }

    @Override
    public Popular_adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_home_rv, parent, false);

        context = parent.getContext();

        return new Popular_adapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(Popular_adapter.MyViewHolder holder, int position) {
        Popular_model mList = modelList.get(position);
        Picasso.with(context)
                .load(BaseURL.IMG_URL + mList.getImage())
                .into(holder.image);
        preferences = context.getSharedPreferences("lan", MODE_PRIVATE);
        language=preferences.getString("language","");
        holder.title.setText(mList.getTitle());


    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

}
