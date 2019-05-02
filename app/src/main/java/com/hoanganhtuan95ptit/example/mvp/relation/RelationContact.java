package com.hoanganhtuan95ptit.example.mvp.relation;

import com.hoanganhtuan95ptit.example.data.model.Video;
import com.hoanganhtuan95ptit.example.mvp.BaseView;
import com.hoanganhtuan95ptit.example.mvp.Presenter;

import java.util.ArrayList;

public interface RelationContact {
     interface RelationView extends BaseView {
         void bindRelationVideo(ArrayList<Video> results);
     }

     interface RelationPresenter extends Presenter{
         void getRelationVideo();
     }
}
