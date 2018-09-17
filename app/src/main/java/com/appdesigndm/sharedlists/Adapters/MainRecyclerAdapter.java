package com.appdesigndm.sharedlists.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.appdesigndm.sharedlists.Holders.MainViewHolder;
import com.appdesigndm.sharedlists.Models.MainListsModel;
import com.appdesigndm.sharedlists.R;

import java.util.ArrayList;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainViewHolder> {

    private ArrayList<MainListsModel> list;

    public MainRecyclerAdapter(ArrayList<MainListsModel> list) {
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
        if(position <= list.size()) {
            MainListsModel item = list.get(position);
            holder.bindMainViewHolder(item);
        } else {
        }
    }

    @Override
    public int getItemCount() {
        return (list.size() + 1);
    }
}
