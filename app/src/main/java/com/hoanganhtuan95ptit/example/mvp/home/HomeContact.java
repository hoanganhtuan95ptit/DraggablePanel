package com.hoanganhtuan95ptit.example.mvp.home;

import com.hoanganhtuan95ptit.example.data.model.Video;
import com.hoanganhtuan95ptit.example.mvp.BaseView;
import com.hoanganhtuan95ptit.example.mvp.Presenter;

import java.util.ArrayList;

public interface HomeContact {
     interface HomeView extends BaseView {
         void bindHomeVideo(ArrayList<Video> results);
     }

     interface HomePresenter extends Presenter{
         void getHomeVideo();
     }
}
