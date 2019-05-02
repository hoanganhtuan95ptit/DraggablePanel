package com.hoanganhtuan95ptit.example.ui.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import com.hoanganhtuan95ptit.example.ui.adapter.holder.TrendingHomeHolder;
import com.hoanganhtuan95ptit.example.ui.adapter.holder.VideoHomeHolder;
import com.hoanganhtuan95ptit.example.utils.Utils;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeAdapter extends BaseAdapter {

    private static final int TRENDING_TYPE = 1;

    public HomeAdapter(Activity act) {
        super(act);
    }

    @Override
    public int getItemViewType(int position) {
        Object item = getItem(position);
        if (Utils.equals(item, END_TYPE)) {
            return END_TYPE;
        } else if (item instanceof ArrayList) {
            return TRENDING_TYPE;
        } else {
            return NORMAL_TYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TRENDING_TYPE:
                return new TrendingHomeHolder(activity, layoutInflater, parent);
            case END_TYPE:
                return new EndHolder(layoutInflater, parent);
            default:
                return new VideoHomeHolder(layoutInflater, parent);
        }
    }
}
