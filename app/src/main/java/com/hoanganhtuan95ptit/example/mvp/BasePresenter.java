package com.hoanganhtuan95ptit.example.mvp;

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
