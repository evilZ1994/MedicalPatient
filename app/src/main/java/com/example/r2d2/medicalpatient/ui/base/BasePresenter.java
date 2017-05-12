package com.example.r2d2.medicalpatient.ui.base;

/**
 * Created by Lollipop on 2017/4/28.
 */

public class BasePresenter<T extends View> implements Presenter<T>{
    private T mView;

    @Override
    public void attachView(T mView){
        this.mView = mView;
    }

    @Override
    public void detachView(){
        mView = null;
    }

    public boolean isViewAttached(){
        return mView!=null;
    }

    public T getView(){
        return mView;
    }
}
