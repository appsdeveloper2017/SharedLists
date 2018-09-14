package com.appdesigndm.sharedlists.Holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.appdesigndm.sharedlists.Main.MainList;
import com.appdesigndm.sharedlists.Models.MainListsModel;
import com.appdesigndm.sharedlists.R;

public class MainViewHolder extends RecyclerView.ViewHolder {

    private TextView mainTitle;

    public MainViewHolder(View itemView) {
        super(itemView);

        mainTitle = itemView.findViewById(R.id.title_main);
    }

    public void bindMainViewHolder(MainListsModel list) {
        mainTitle.setText(list.getListName());
    }
}
