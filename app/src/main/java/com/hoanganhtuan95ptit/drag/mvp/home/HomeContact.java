package com.hoanganhtuan95ptit.drag.mvp.home;

import com.hoanganhtuan95ptit.drag.data.model.Video;
import com.hoanganhtuan95ptit.drag.mvp.BaseView;
import com.hoanganhtuan95ptit.drag.mvp.Presenter;

import java.util.ArrayList;

public interface HomeContact {
     interface HomeView extends BaseView {
         void bindHomeVideo(ArrayList<Video> results);
     }

     interface HomePresenter extends Presenter{
         void getHomeVideo();
     }
}
