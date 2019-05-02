package com.hoanganhtuan95ptit.example.ui.adapter.holder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Space;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.hoanganhtuan95ptit.example.App;
import com.hoanganhtuan95ptit.example.R;
import com.hoanganhtuan95ptit.example.data.model.Video;
import com.hoanganhtuan95ptit.example.ui.adapter.BaseAdapter;
import com.hoanganhtuan95ptit.example.ui.widget.round.RoundImageView;
import com.hoanganhtuan95ptit.example.ui.widget.round.RoundTextView;
import com.hoanganhtuan95ptit.example.utils.Utils;
import com.hoanganhtuan95ptit.example.utils.images.ImageUtils;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import butterknife.BindView;

public class HeaderDetailHolder extends BaseAdapter.ViewHolder {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_views)
    TextView tvViews;
    @BindView(R.id.space_1)
    Space space1;
    @BindView(R.id.iv_channel_avatar)
    ImageView ivChannelAvatar;
    @BindView(R.id.tv_channel_title)
    TextView tvChannelTitle;
    @BindView(R.id.tv_channel_time)
    TextView tvChannelTime;
    @BindView(R.id.tv_sub)
    RoundTextView tvSub;
    @BindView(R.id.space_2)
    Space space2;
    @BindView(R.id.iv_like)
    RoundImageView ivLike;
    @BindView(R.id.iv_un_like)
    RoundImageView ivUnLike;
    @BindView(R.id.iv_share)
    RoundImageView ivShare;
    @BindView(R.id.iv_download)
    RoundImageView ivDownload;
    @BindView(R.id.iv_playlist)
    RoundImageView ivPlaylist;

    @Nullable
    private Video video;

    public HeaderDetailHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        this(layoutInflater.inflate(R.layout.holder_detail_header, viewGroup, false));
    }

    private HeaderDetailHolder(View view) {
        super(view);
    }

    @Override
    public void bindData(Object item, ArrayList<Object> items, int position) {
        super.bindData(item, items, position);
        if (item instanceof Video) {
            video = (Video) item;

            tvTitle.setText(Utils.fromHtml(App.self().getString(video.getTitle())));
//            ImageUtils.showImage(App.self().getApplicationContext(), ivThumb, video.getThumb(), new CenterCrop(), new RoundedCorners(App.self().getMargin()));
//
            tvChannelTitle.setText(Utils.fromHtml(App.self().getString(video.getChannel().getTitle())));
            ImageUtils.showImage(App.self().getApplicationContext(), ivChannelAvatar, video.getChannel().getThumb(), new CenterCrop(), new CircleCrop());
        } else {
            video = null;
        }
    }

}