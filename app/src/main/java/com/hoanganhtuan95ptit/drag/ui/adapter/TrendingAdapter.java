package com.hoanganhtuan95ptit.drag.ui.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import com.hoanganhtuan95ptit.drag.ui.adapter.holder.TrendingHolder;
import com.hoanganhtuan95ptit.drag.utils.Utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrendingAdapter extends BaseAdapter {

    public TrendingAdapter(Activity act) {
        super(act);
    }

    @Override
    public int getItemViewType(int position) {
        Object item = getItem(position);
        if (Utils.equals(item, SPACE_TYPE)) {
            return SPACE_TYPE;
        } else {
            return NORMAL_TYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case SPACE_TYPE:
                return new SpaceHolder(layoutInflater, parent);
            default:
                return new TrendingHolder(layoutInflater, parent);
        }
    }
}