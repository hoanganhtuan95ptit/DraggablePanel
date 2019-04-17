package com.hoanganhtuan95ptit.drag.ui.adapter;

import android.app.Activity;
import android.view.ViewGroup;

import com.hoanganhtuan95ptit.drag.ui.adapter.holder.HeaderDetailHolder;
import com.hoanganhtuan95ptit.drag.ui.adapter.holder.NormalDetailHolder;
import com.hoanganhtuan95ptit.drag.utils.Utils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DetailAdapter extends BaseAdapter {

    private static final int HEADER_TYPE = 1;

    public DetailAdapter(Activity act) {
        super(act);
    }

    @Override
    public int getItemViewType(int position) {
        Object item = getItem(position);
        if (Utils.equals(item, END_TYPE)) {
            return END_TYPE;
        } else if (position == 0) {
            return HEADER_TYPE;
        } else {
            return NORMAL_TYPE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEADER_TYPE:
                return new HeaderDetailHolder(layoutInflater, parent);
            case END_TYPE:
                return new EndHolder(layoutInflater, parent);
            default:
                return new NormalDetailHolder(layoutInflater, parent);
        }
    }
}