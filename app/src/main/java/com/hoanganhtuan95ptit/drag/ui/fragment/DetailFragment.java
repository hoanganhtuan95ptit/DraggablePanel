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
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.hoanganhtuan95ptit.drag.ui.activity.MainActivity.ratios;

public class DetailFragment extends Fragment {

    @BindView(R.id.rv_detail)
    RecyclerView rvDetail;

    public static DetailFragment newInstance() {

        Bundle args = new Bundle();

        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private ArrayList<Object> feeds;
    private DetailAdapter detailAdapter;

    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        feeds = new ArrayList<>();
        detailAdapter = new DetailAdapter(getActivity());

        rvDetail.setLayoutManager(new LinearLayoutManager(App.self().getApplicationContext()));
        rvDetail.setAdapter(detailAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVideoEvent(Video video) {
        TypedArray videoTitleArray = getResources().obtainTypedArray(R.array.video_title);
        TypedArray videoThumbArray = getResources().obtainTypedArray(R.array.video_thumb);
        TypedArray videoTitle2Array = getResources().obtainTypedArray(R.array.video_title_2);
        TypedArray videoThumb2Array = getResources().obtainTypedArray(R.array.video_thumb);
        TypedArray channelTitleArray = getResources().obtainTypedArray(R.array.channel_title);
        TypedArray channelThumbArray = getResources().obtainTypedArray(R.array.video_thumb);
        feeds.clear();
        feeds.add(video);
        for (int i = 0; i < 10; i++) {
            Channel channel = new Channel(String.valueOf(i), channelThumbArray.getResourceId(i, -1), channelTitleArray.getResourceId(i, -1));

            Video item = new Video(String.valueOf(i), videoThumbArray.getResourceId(i, -1), videoTitleArray.getResourceId(i, -1));
            video.setRatio(ratios[i % ratios.length]);
            video.setChannel(channel);

            feeds.add(item);
        }
        feeds.add(BaseAdapter.END_TYPE);
        videoTitleArray.recycle();
        videoThumbArray.recycle();
        videoTitle2Array.recycle();
        videoThumb2Array.recycle();
        channelTitleArray.recycle();
        channelThumbArray.recycle();

        detailAdapter.bindData(feeds);
        rvDetail.scrollToPosition(0);
    }


    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
