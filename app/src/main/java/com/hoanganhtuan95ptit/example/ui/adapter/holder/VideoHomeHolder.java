package com.hoanganhtuan95ptit.example.ui.adapter.holder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.hoanganhtuan95ptit.example.App;
import com.hoanganhtuan95ptit.example.R;
import com.hoanganhtuan95ptit.example.data.model.Video;
import com.hoanganhtuan95ptit.example.ui.adapter.BaseAdapter;
import com.hoanganhtuan95ptit.example.utils.Utils;
import com.hoanganhtuan95ptit.example.utils.images.ImageUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.OnClick;

public class VideoHomeHolder extends BaseAdapter.ViewHolder {

    @BindView(R.id.iv_thumb)
    ImageView ivThumb;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.iv_channel_avatar)
    ImageView ivChannelAvatar;
    @BindView(R.id.tv_channel_title)
    TextView tvChannelTitle;
    @BindView(R.id.root_holder_home_video)
    ConstraintLayout rootHolderHomeVideo;

    @Nullable
    private Video video;

    public VideoHomeHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        this(layoutInflater.inflate(R.layout.holder_home_video, viewGroup, false));
    }

    private VideoHomeHolder(View view) {
        super(view);
    }

    @Override
    public void bindData(Object item, ArrayList<Object> items, int position) {
        super.bindData(item, items, position);
        if (item instanceof Video) {
            video = (Video) item;

            tvTitle.setText(Utils.fromHtml(App.self().getString(video.getTitle())));
            ImageUtils.showImage(App.self().getApplicationContext(), ivThumb, video.getThumb(), new CenterCrop(), new RoundedCorners(App.self().getMargin()));

            tvChannelTitle.setText(Utils.fromHtml(App.self().getString(video.getChannel().getTitle())));
            ImageUtils.showImage(App.self().getApplicationContext(), ivChannelAvatar, video.getChannel().getThumb(), new CenterCrop(), new CircleCrop());
        } else {
            video = null;
        }
    }

    @OnClick(R.id.root_holder_home_video)
    public void onViewClicked() {
        if(video!=null){
            EventBus.getDefault().post(video);
        }
    }
}
