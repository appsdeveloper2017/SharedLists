package com.appdesigndm.sharedlists.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appdesigndm.sharedlists.Holders.MainViewHolder;
import com.appdesigndm.sharedlists.Main.MainList;
import com.appdesigndm.sharedlists.R;

import java.util.ArrayList;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainViewHolder> {

    private ArrayList<MainList> list;

    public MainRecyclerAdapter(ArrayList<MainList> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.item_main_recycler, parent, false);

        MainViewHolder viewHolder = new MainViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
        MainList item = list.get(position);
        holder.bindMainViewHolder(item);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
