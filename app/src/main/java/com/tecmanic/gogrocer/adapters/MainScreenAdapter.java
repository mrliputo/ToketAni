package com.tecmanic.gogrocer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tecmanic.gogrocer.R;
import com.tecmanic.gogrocer.modelclass.MainScreenList;
import com.tecmanic.gogrocer.util.ProdcutDetailsVerifier;

import java.util.List;

public class MainScreenAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<MainScreenList> screenLists;

    private ProdcutDetailsVerifier prodcutDetailsVerifier;

    public MainScreenAdapter(Context context, List<MainScreenList> screenLists, ProdcutDetailsVerifier prodcutDetailsVerifier) {
        this.context = context;
        this.screenLists = screenLists;
        this.prodcutDetailsVerifier = prodcutDetailsVerifier;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topsell, parent, false);
                return new TopSelling(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topsell, parent, false);
                return new RecentDeal(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topsell, parent, false);
                return new DealOfTheDay(view);
            default:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.topsell, parent, false);
                return new WhatsNew(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MainScreenList mainScreenList = screenLists.get(position);

        switch (mainScreenList.getViewType()) {
            case "TOP SELLING":
                TopSelling topSelling = (TopSelling) holder;
                LinearLayoutManager linearLayoutManagers = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                topSelling.topSelling1.setLayoutManager(linearLayoutManagers);
                topSelling.topSelling1.setAdapter(new CartAdapter(context, mainScreenList.getTopSelling(),prodcutDetailsVerifier));
                break;
            case "RECENT SELLING":
                RecentDeal recentDeal = (RecentDeal) holder;
                LinearLayoutManager linearLayoutManagerss = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                recentDeal.topSelling.setLayoutManager(linearLayoutManagerss);
                recentDeal.topSelling.setAdapter(new CartAdapter(context, mainScreenList.getRecentSelling(),prodcutDetailsVerifier));
                break;
            case "DEALS OF THE DAY":
                DealOfTheDay dealOfTheDay = (DealOfTheDay) holder;
                LinearLayoutManager dealofter = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                dealOfTheDay.topSelling.setLayoutManager(dealofter);
                dealOfTheDay.topSelling.setAdapter(new DealAdapter(context, mainScreenList.getDealoftheday(),prodcutDetailsVerifier));
                break;
            case "WHAT'S NEW":
                WhatsNew whatsNew = (WhatsNew) holder;
                LinearLayoutManager linearLayoutManagersss = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
                whatsNew.topSelling.setLayoutManager(linearLayoutManagersss);
                whatsNew.topSelling.setAdapter(new CartAdapter(context, mainScreenList.getWhatsNew(),prodcutDetailsVerifier));
                break;
            default:
                break;
        }

    }

    @Override
    public int getItemViewType(int position) {
        switch (screenLists.get(position).getViewType()) {
            case "TOP SELLING":
                return 0;
            case "RECENT SELLING":
                return 1;
            case "DEALS OF THE DAY":
                return 2;
            case "WHAT'S NEW":
                return 3;
            default:
                return super.getItemViewType(position);
        }

    }

    @Override
    public int getItemCount() {
        return screenLists.size();
    }

    public class TopSelling extends RecyclerView.ViewHolder {
        RecyclerView topSelling1;

        public TopSelling(@NonNull View itemView) {
            super(itemView);
            topSelling1 = itemView.findViewById(R.id.top_selling);
        }
    }

    public class WhatsNew extends RecyclerView.ViewHolder {
        RecyclerView topSelling;

        public WhatsNew(@NonNull View itemView) {
            super(itemView);
            topSelling = itemView.findViewById(R.id.top_selling);
        }
    }

    public class RecentDeal extends RecyclerView.ViewHolder {
        RecyclerView topSelling;

        public RecentDeal(@NonNull View itemView) {
            super(itemView);
            topSelling = itemView.findViewById(R.id.top_selling);
        }
    }

    public class DealOfTheDay extends RecyclerView.ViewHolder {
        RecyclerView topSelling;

        public DealOfTheDay(@NonNull View itemView) {
            super(itemView);
            topSelling = itemView.findViewById(R.id.top_selling);
        }
    }
}
