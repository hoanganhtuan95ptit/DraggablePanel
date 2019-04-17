package com.hoanganhtuan95ptit.drag.mvp.relation;

import com.hoanganhtuan95ptit.drag.data.model.Video;
import com.hoanganhtuan95ptit.drag.mvp.BaseView;
import com.hoanganhtuan95ptit.drag.mvp.Presenter;

import java.util.ArrayList;

public interface RelationContact {
     interface RelationView extends BaseView {
         void bindRelationVideo(ArrayList<Video> results);
     }

     interface RelationPresenter extends Presenter{
         void getRelationVideo();
     }
}
