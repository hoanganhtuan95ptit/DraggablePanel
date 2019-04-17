package com.hoanganhtuan95ptit.drag.mvp.trending;

import com.hoanganhtuan95ptit.drag.data.model.Video;
import com.hoanganhtuan95ptit.drag.data.network.ApiHelper;
import com.hoanganhtuan95ptit.drag.mvp.BasePresenter;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

public class TrendingPresenterImpl extends BasePresenter<TrendingContact.TrendingView> implements TrendingContact.TrendingPresenter {

    private ApiHelper apiHelper;

    public TrendingPresenterImpl(TrendingContact.TrendingView view, ApiHelper apiHelper) {
        super(view);
        this.apiHelper = apiHelper;
    }

    @Override
    public void getTrendingVideo() {
        compositeDisposable.add(getTrendingVideoObservable().subscribe(new Consumer<ArrayList<Video>>() {
            @Override
            public void accept(ArrayList<Video> videos) throws Exception {
                view.bindTrendingVideo(videos);
            }
        }, throwable -> {
        }, () -> {
        }, disposable -> {
        }));
    }

    private Observable<ArrayList<Video>> getTrendingVideoObservable() {
        return Observable.just("").map(s -> apiHelper.getTrendingVideo());
    }
}
