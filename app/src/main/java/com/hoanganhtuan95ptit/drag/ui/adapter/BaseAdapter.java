package com.hoanganhtuan95ptit.drag.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoanganhtuan95ptit.drag.Constants;
import com.hoanganhtuan95ptit.drag.R;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import butterknife.ButterKnife;

public abstract class BaseAdapter extends RecyclerView.Adapter implements Constants {

    public static final int LOAD_MORE_TYPE = Integer.MIN_VALUE;
    public static final int SPACE_TYPE = Integer.MIN_VALUE + 1;
    public static final int END_TYPE = Integer.MIN_VALUE + 2;

    public static final int NORMAL_TYPE = 0;

    protected final Activity activity;
    protected final LayoutInflater layoutInflater;
    protected final ArrayList<Object> items = new ArrayList<>();

    protected int numberHolderLoadMore = 3;
    protected OnLoadMoreListener onLoadMoreListener;

    protected OnItemListener onItemListener;

    public BaseAdapter(Activity act) {
        activity = act;
        layoutInflater = LayoutInflater.from(activity);
    }

    public void setNumberHolderLoadMore(int numberHolderLoadMore) {
        this.numberHolderLoadMore = numberHolderLoadMore;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    public ArrayList<Object> getItems() {
        return items;
    }

    public Object getItem(int position) {
        if (position < 0 || position > items.size()) return null;
        return items.get(position);
    }

    public final void bindData(ArrayList<Object> results) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(results, items));
        items.clear();
        items.addAll(results);
        diffResult.dispatchUpdatesTo(this);
    }

    public final void update(Object item) {
        int position = items.indexOf(item);
        if (position < 0) return;
        items.set(position, item);
        notifyItemChanged(position, item);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerView.ViewHolder holder, int position, @androidx.annotation.NonNull List payloads) {
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads);
        } else if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).bindData(items.get(position), items, position, payloads);
        }
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).bindData(items.get(position), items, position);
        }
    }

    @Override
    public void onViewAttachedToWindow(@androidx.annotation.NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        int position = holder.getAdapterPosition();
        if (getItemCount() - numberHolderLoadMore < position && onLoadMoreListener != null) {
            onLoadMoreListener.onLoadMore();
        }
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).onViewAttachedToWindow(this);
        }
    }

    @Override
    public void onViewDetachedFromWindow(@androidx.annotation.NonNull RecyclerView.ViewHolder holder) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).onViewDetachedFromWindow();
        }
        super.onViewDetachedFromWindow(holder);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public static abstract class ViewHolder extends RecyclerView.ViewHolder implements Constants {

        @androidx.annotation.Nullable
        protected BaseAdapter baseAdapter;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindData(Object item, ArrayList<Object> items, int position) {
        }

        public void bindData(Object item, ArrayList<Object> items, int position, @androidx.annotation.NonNull List<Object> payloads) {
        }

        public void onViewAttachedToWindow(BaseAdapter baseAdapter) {
            this.baseAdapter = baseAdapter;
        }

        public void onViewDetachedFromWindow() {
            this.baseAdapter = null;
        }
    }

    public static class LoadMoreHolder extends ViewHolder {

        public LoadMoreHolder(LayoutInflater layoutInflater, ViewGroup parent) {
            this(layoutInflater.inflate(R.layout.holder_loading, parent, false));
        }

        public LoadMoreHolder(View view) {
            super(view);
        }

        @Override
        public void bindData(Object item, ArrayList<Object> items, int position) {
            super.bindData(item, items, position);
            ViewGroup.LayoutParams layoutParams = itemView.getLayoutParams();
            if (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams staggeredGridParams = (StaggeredGridLayoutManager.LayoutParams) layoutParams;
                staggeredGridParams.setFullSpan(true);
                itemView.setLayoutParams(staggeredGridParams);
            }
        }
    }

    public static class SpaceHolder extends ViewHolder {

        public SpaceHolder(LayoutInflater layoutInflater, ViewGroup parent) {
            this(layoutInflater.inflate(R.layout.holder_space, parent, false));
        }

        public SpaceHolder(View view) {
            super(view);
        }
    }

    public static class EndHolder extends ViewHolder {

        public EndHolder(LayoutInflater layoutInflater, ViewGroup parent) {
            this(layoutInflater.inflate(R.layout.holder_end, parent, false));
        }

        public EndHolder(View view) {
            super(view);
        }
    }

    private static class DiffCallback extends DiffUtil.Callback {

        List<Object> oldObjects;

        List<Object> newObjects;

        DiffCallback(List<Object> newObjects, List<Object> oldObjects) {
            this.newObjects = newObjects;
            this.oldObjects = oldObjects;
        }

        @Override
        public int getOldListSize() {
            return oldObjects.size();
        }

        @Override
        public int getNewListSize() {
            return newObjects.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldObjects.get(oldItemPosition).equals(newObjects.get(newItemPosition));
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            return oldObjects.get(oldItemPosition).equals(newObjects.get(newItemPosition));
        }

        @androidx.annotation.Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            return super.getChangePayload(oldItemPosition, newItemPosition);
        }

    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }

    public interface OnItemListener<T> {
        void onItemClickedListener(T t);
    }

}





