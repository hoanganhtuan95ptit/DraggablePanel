package com.hoanganhtuan95ptit.drag.ui.fragment;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoanganhtuan95ptit.drag.App;
import com.hoanganhtuan95ptit.drag.R;
import com.hoanganhtuan95ptit.drag.data.model.Channel;
import com.hoanganhtuan95ptit.drag.data.model.Video;
import com.hoanganhtuan95ptit.drag.mvp.relation.RelationContact;
import com.hoanganhtuan95ptit.drag.mvp.relation.RelationPresenterImpl;
import com.hoanganhtuan95ptit.drag.ui.adapter.BaseAdapter;
import com.hoanganhtuan95ptit.drag.ui.adapter.DetailAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.hoanganhtuan95ptit.drag.ui.activity.MainActivity.ratios;

public class DetailFragment extends Fragment implements RelationContact.RelationView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rv_detail)
    RecyclerView rvDetail;
    @BindView(R.id.sr_detail)
    SwipeRefreshLayout srDetail;

    public static DetailFragment newInstance() {

        Bundle args = new Bundle();

        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Video currentVideo;
    private ArrayList<Object> feeds;
    private DetailAdapter detailAdapter;

    private RelationContact.RelationPresenter relationPresenter;

    Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        relationPresenter = new RelationPresenterImpl(this, App.self().getApiHelper());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        feeds = new ArrayList<>();
        detailAdapter = new DetailAdapter(getActivity());

        rvDetail.setLayoutManager(new LinearLayoutManager(App.self().getApplicationContext()));
        rvDetail.setAdapter(detailAdapter);

        srDetail.setOnRefreshListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onRefresh() {
        fetchRelation();
    }

    @Override
    public void bindRelationVideo(ArrayList<Video> results) {

        feeds.clear();
        feeds.add(currentVideo);
        feeds.addAll(results);
        detailAdapter.bindData(feeds);

        srDetail.setRefreshing(false);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoEvent(Video video) {
        currentVideo = video;
        srDetail.setRefreshing(true);
        fetchRelation();
    }

    private void fetchRelation() {
        relationPresenter.getRelationVideo();
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        relationPresenter.dispose();
        unbinder.unbind();
    }
}
