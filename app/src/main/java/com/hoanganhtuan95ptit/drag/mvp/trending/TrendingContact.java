package com.hoanganhtuan95ptit.drag.mvp.trending;

import com.hoanganhtuan95ptit.drag.data.model.Video;
import com.hoanganhtuan95ptit.drag.mvp.BaseView;
import com.hoanganhtuan95ptit.drag.mvp.Presenter;

import java.util.ArrayList;

public interface TrendingContact {
     interface TrendingView extends BaseView {
         void bindTrendingVideo(ArrayList<Video> results);
     }

     interface TrendingPresenter extends Presenter{
         void getTrendingVideo();
     }
}
