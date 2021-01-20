package com.tecmanic.toketani.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tecmanic.toketani.R;
import com.tecmanic.toketani.modelclass.TimingModel;
import com.tecmanic.toketani.util.ForClicktimings;

import java.util.List;


public class TimingAdapter extends RecyclerView.Adapter<TimingAdapter.MyViewHolder> {

    Context context;

    boolean showingfirst = true;
    int myPos = 0;

    String timeslot;
    ForClicktimings forClicktimings;
    private List<TimingModel> offerList;
    private int lastSelectedPosition = -1;

    public TimingAdapter(Context context, List<TimingModel> offerList, ForClicktimings forClicktimings) {
        this.offerList = offerList;
        this.context = context;
        this.forClicktimings = forClicktimings;
        lastSelectedPosition = 0;
    }

    @NonNull
    @Override
    public TimingAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lay_time, parent, false);
        return new TimingAdapter.MyViewHolder(itemView);


    }

    @Override
    public void onBindViewHolder(@NonNull final TimingAdapter.MyViewHolder holder, final int position) {
        final TimingModel lists = offerList.get(position);

        holder.time.setText(lists.getTiming());


        if (lastSelectedPosition == position) {
            timeslot = lists.getTiming();
            holder.time.setTextColor(Color.parseColor("#3c9658"));
            holder.slotname.setTextColor(Color.parseColor("#3c9658"));
        } else {
            holder.time.setTextColor(Color.parseColor("#8f909e"));
            holder.slotname.setTextColor(Color.parseColor("#8f909e"));
        }
        holder.time.setChecked(lastSelectedPosition == position);

        holder.time.setOnClickListener(view -> {
            try {
                if (!holder.time.isChecked()) {
                    lastSelectedPosition = -1;
                    notifyDataSetChanged();
                    forClicktimings.getTimeSlot("");
                } else {
                    lastSelectedPosition = position;
                    notifyDataSetChanged();
                    forClicktimings.getTimeSlot(offerList.get(lastSelectedPosition).getTiming());
                }
            } catch (IndexOutOfBoundsException ex) {
                ex.printStackTrace();
            }


        });


    }

    public String gettimeslot() {
        return timeslot;
    }

    @Override
    public int getItemCount() {
        return offerList.size();

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        RadioButton time;
        TextView slotname;

        public MyViewHolder(View view) {
            super(view);
            time = view.findViewById(R.id.time);
            slotname = view.findViewById(R.id.slotname);
        }

    }

}




