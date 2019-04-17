package com.hoanganhtuan95ptit.drag.mvp;

import io.reactivex.disposables.CompositeDisposable;

public class BasePresenter<T extends BaseView> implements Presenter {

    protected CompositeDisposable compositeDisposable;
    protected T view;


    public BasePresenter(T view) {
        this.view = view;
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void dispose() {
        compositeDisposable.dispose();
    }
}
