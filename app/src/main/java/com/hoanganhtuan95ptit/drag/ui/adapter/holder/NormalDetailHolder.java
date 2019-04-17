package com.hoanganhtuan95ptit.drag.ui.adapter.holder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.hoanganhtuan95ptit.drag.App;
import com.hoanganhtuan95ptit.drag.R;
import com.hoanganhtuan95ptit.drag.data.model.Video;
import com.hoanganhtuan95ptit.drag.ui.adapter.BaseAdapter;
import com.hoanganhtuan95ptit.drag.utils.Utils;
import com.hoanganhtuan95ptit.drag.utils.images.ImageUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import butterknife.BindView;
import butterknife.OnClick;

public class NormalDetailHolder extends BaseAdapter.ViewHolder {

    @BindView(R.id.iv_thumb)
    ImageView ivThumb;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.root_holder_detail_normal)
    ConstraintLayout rootHolderDetailNormal;
    @Nullable
    private Video video;

    public NormalDetailHolder(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        this(layoutInflater.inflate(R.layout.holder_detail_normal, viewGroup, false));
    }

    private NormalDetailHolder(View view) {
        super(view);
    }

    @Override
    public void bindData(Object item, ArrayList<Object> items, int position) {
        super.bindData(item, items, position);
        if (item instanceof Video) {
            video = (Video) item;

            tvTitle.setText(Utils.fromHtml(App.self().getString(video.getTitle())));
            ImageUtils.showImage(App.self().getApplicationContext(), ivThumb, video.getThumb(), new CenterCrop(), new RoundedCorners(App.self().getMargin()));
        } else {
            video = null;
        }
    }

    @OnClick(R.id.root_holder_detail_normal)
    public void onViewClicked() {
        if (video != null) {
            EventBus.getDefault().post(video);
        }
    }
}