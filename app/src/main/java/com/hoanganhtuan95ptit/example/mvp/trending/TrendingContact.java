package com.hoanganhtuan95ptit.example.mvp.trending;

import com.hoanganhtuan95ptit.example.data.model.Video;
import com.hoanganhtuan95ptit.example.mvp.BaseView;
import com.hoanganhtuan95ptit.example.mvp.Presenter;

import java.util.ArrayList;

public interface TrendingContact {
     interface TrendingView extends BaseView {
         void bindTrendingVideo(ArrayList<Video> results);
     }

     interface TrendingPresenter extends Presenter{
         void getTrendingVideo();
     }
}
