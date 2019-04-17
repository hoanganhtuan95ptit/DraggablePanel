package com.hoanganhtuan95ptit.drag.ui.adapter.holder;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hoanganhtuan95ptit.drag.App;
import com.hoanganhtuan95ptit.drag.R;
import com.hoanganhtuan95ptit.drag.ui.adapter.BaseAdapter;
import com.hoanganhtuan95ptit.drag.ui.adapter.TrendingAdapter;
import com.hoanganhtuan95ptit.drag.ui.widget.StartSnapHelper;
import com.hoanganhtuan95ptit.drag.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class TrendingHomeHolder extends BaseAdapter.ViewHolder {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv_trending)
    RecyclerView rvTrending;

    private ArrayList videos;

    private ArrayList<Object> feeds;
    private TrendingAdapter trendingAdapter;

    public TrendingHomeHolder(Activity activity, LayoutInflater layoutInflater, ViewGroup viewGroup) {
        this(layoutInflater.inflate(R.layout.holder_home_trending, viewGroup, false));

        tvTitle.setText(R.string.trending);

        feeds = new ArrayList<>();
        trendingAdapter = new TrendingAdapter(activity);

        rvTrending.setLayoutManager(new LinearLayoutManager(App.self().getApplicationContext(), RecyclerView.HORIZONTAL, false));
        rvTrending.setNestedScrollingEnabled(false);
        rvTrending.setAdapter(trendingAdapter);

        StartSnapHelper startSnapHelper = new StartSnapHelper();
        startSnapHelper.attachToRecyclerView(rvTrending);
    }

    private TrendingHomeHolder(View view) {
        super(view);
    }

    @Override
    public void bindData(Object item, ArrayList<Object> items, int position, @NonNull List<Object> payloads) {
        super.bindData(item, items, position, payloads);
        if (item instanceof ArrayList) {
            videos = (ArrayList) item;
            feeds.addAll(videos);
            trendingAdapter.bindData(feeds);
        }
    }

    @Override
    public void bindData(Object item, ArrayList<Object> items, int position) {
        super.bindData(item, items, position);
        if (item instanceof ArrayList && Utils.isEmpty(videos)) {
            videos = (ArrayList) item;
            feeds.addAll(videos);
            trendingAdapter.bindData(feeds);
        }
    }

}
