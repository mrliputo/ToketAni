package com.tecmanic.gogrocer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.modelclass.MyCalendarModel;
import com.tecmanic.gogrocer.util.CalendarClick;

import java.util.List;

public class MyCalnderAdapter extends RecyclerView.Adapter<MyCalnderAdapter.MyCalenderSubView> {

    private List<MyCalendarModel> calendarModel;
    private int recy = 0;
    private int selectedPos = -1;
    private CalendarClick calendarClick;

    public MyCalnderAdapter(List<MyCalendarModel> calendarModel, CalendarClick calendarClick) {
        this.calendarModel = calendarModel;
        this.calendarClick = calendarClick;
        selectedPos = 0;
    }

    @NonNull
    @Override
    public MyCalenderSubView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_view_row, parent, false);

        return new MyCalenderSubView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyCalenderSubView holder, int position) {
        MyCalendarModel model = calendarModel.get(position);

        holder.monthTxt.setText(model.getMonth());
        holder.dateTxt.setText(model.getDate());
        holder.dayTxt.setText(model.getDay());
        if (selectedPos == position) {
            holder.selectedView.setVisibility(View.VISIBLE);
        } else {
            holder.selectedView.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            selectedPos = position;
            calendarClick.onCalenderClick(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return calendarModel.size();
    }

    @Override
    public void onViewRecycled(@NonNull MyCalenderSubView holder) {
        recy++;
    }

    public static class MyCalenderSubView extends RecyclerView.ViewHolder {
        TextView monthTxt;
        TextView dateTxt;
        TextView dayTxt;
        View selectedView;

        public MyCalenderSubView(@NonNull View itemView) {
            super(itemView);

            monthTxt = itemView.findViewById(R.id.month_txt);
            dateTxt = itemView.findViewById(R.id.date_txt);
            dayTxt = itemView.findViewById(R.id.day_txt);
            selectedView = itemView.findViewById(R.id.selected_view);
        }
    }
}
