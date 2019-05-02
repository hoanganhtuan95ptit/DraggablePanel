package com.hoanganhtuan95ptit.example.mvp.relation;

import com.hoanganhtuan95ptit.example.data.model.Video;
import com.hoanganhtuan95ptit.example.data.network.ApiHelper;
import com.hoanganhtuan95ptit.example.mvp.BasePresenter;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class RelationPresenterImpl extends BasePresenter<RelationContact.RelationView> implements RelationContact.RelationPresenter {

    private ApiHelper apiHelper;

    public RelationPresenterImpl(RelationContact.RelationView view, ApiHelper apiHelper) {
        super(view);
        this.apiHelper = apiHelper;
    }

    @Override
    public void getRelationVideo() {
        compositeDisposable.add(getRelationVideoObservable().subscribe(new Consumer<ArrayList<Video>>() {
            @Override
            public void accept(ArrayList<Video> videos) throws Exception {
                view.bindRelationVideo(videos);
            }
        }, throwable -> {
        }, () -> {
        }, disposable -> {
        }));
    }

    private Observable<ArrayList<Video>> getRelationVideoObservable() {
        return Observable.just("").map(s -> apiHelper.getRelationVideo());
    }
}
