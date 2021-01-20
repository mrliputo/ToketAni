package com.tecmanic.toketani.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tecmanic.toketani.R;
import com.tecmanic.toketani.modelclass.ListAssignAndUnassigned;
import com.tecmanic.toketani.util.TodayOrderClickListner;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ListAssignAndUnassigned> listAssignAndUnassigneds;
    private Context context;
    private TodayOrderClickListner todayOrderClickListner;

    public OrderAdapter(Context context, List<ListAssignAndUnassigned> listAssignAndUnassigneds, TodayOrderClickListner todayOrderClickListner) {
        this.listAssignAndUnassigneds = listAssignAndUnassigneds;
        this.context = context;
        this.todayOrderClickListner = todayOrderClickListner;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == 0) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.assign_view, parent, false);
            return new MyAssignView(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.unassign_view, parent, false);
            return new MyUnAssignView(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ListAssignAndUnassigned assignAndUnassigned = listAssignAndUnassigneds.get(position);
        switch (assignAndUnassigned.getViewType()) {
            case "assigned":
                MyAssignView myAssignView = (MyAssignView) holder;
                myAssignView.assignRecy.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                myAssignView.assignRecy.setItemAnimator(new DefaultItemAnimator());
                myAssignView.assignRecy.setAdapter(new MyPendingOrderAdapter(assignAndUnassigned.getTodayOrderModels(), todayOrderClickListner));
                break;
            case "unassigned":
                MyUnAssignView myUnAssignView = (MyUnAssignView) holder;
                myUnAssignView.unAssignRecy.setLayoutManager(new LinearLayoutManager(context, RecyclerView.VERTICAL, false));
                myUnAssignView.unAssignRecy.setItemAnimator(new DefaultItemAnimator());
                myUnAssignView.unAssignRecy.setAdapter(new MyPastOrderAdapter(assignAndUnassigned.getNextDayOrders(), todayOrderClickListner));
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (listAssignAndUnassigneds.get(position).getViewType()) {
            case "assigned":
                return 0;
            case "unassigned":
                return 1;
            default:
                return super.getItemViewType(position);
        }

    }

    @Override
    public int getItemCount() {
        return listAssignAndUnassigneds.size();
    }

    public class MyAssignView extends RecyclerView.ViewHolder {
        RecyclerView assignRecy;

        public MyAssignView(@NonNull View itemView) {
            super(itemView);
            assignRecy = itemView.findViewById(R.id.assign_recy);
        }
    }

    public class MyUnAssignView extends RecyclerView.ViewHolder {
        RecyclerView unAssignRecy;

        public MyUnAssignView(@NonNull View itemView) {
            super(itemView);
            unAssignRecy = itemView.findViewById(R.id.unassign_recy);
        }
    }
}
