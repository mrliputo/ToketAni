package com.tecmanic.toketani.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tecmanic.toketani.R;
import com.tecmanic.toketani.modelclass.ComplainModel;

import java.util.List;

public class ComplainAdapter extends RecyclerView.Adapter<ComplainAdapter.MyViewHolder> {

    private List<ComplainModel> modelList;

    public ComplainAdapter(List<ComplainModel> modelList) {
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cancel_listitem, parent, false);
        return new ComplainAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        ComplainModel mList = modelList.get(position);
        holder.title.setText(mList.getReason());
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;

        public MyViewHolder(View view) {

            super(view);
            title = view.findViewById(R.id.ask);

        }
    }

}
