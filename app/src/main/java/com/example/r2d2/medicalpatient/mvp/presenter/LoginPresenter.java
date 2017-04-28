package com.example.r2d2.medicalpatient.mvp.presenter;

import android.util.Log;

import com.example.r2d2.medicalpatient.api.ApiService;
import com.example.r2d2.medicalpatient.data.response.LoginResponse;
import com.example.r2d2.medicalpatient.mvp.model.DataManager;
import com.example.r2d2.medicalpatient.mvp.view.LoginView;
import com.example.r2d2.medicalpatient.ui.base.BasePresenter;
import com.example.r2d2.medicalpatient.ui.base.View;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Lollipop on 2017/4/28.
 */

public class LoginPresenter extends BasePresenter<LoginView>{
    private DataManager dataManager;
    private Disposable disposable;

    @Inject
    ApiService apiService;

    @Inject
    public LoginPresenter(DataManager dataManager){
        this.dataManager = dataManager;
    }

    @Override
    public void detachView(){
        super.detachView();
        if (disposable!=null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }

    public void login(String username, String password){
        getView().showDialog();
        Observer<LoginResponse> observer = new Observer<LoginResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                disposable = d;
            }

            @Override
            public void onNext(@NonNull LoginResponse loginResponse) {
                getView().hideDialog();
                String status = loginResponse.getStatus();
                if (status.equals("success")){
                    getView().onSuccess();
                } else{
                    getView().onError(loginResponse.getError());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                getView().hideDialog();
                getView().onError("登陆失败！");
                e.printStackTrace();
            }

            @Override
            public void onComplete() {

            }
        };
        dataManager.login(username, password, observer);
    }
}
