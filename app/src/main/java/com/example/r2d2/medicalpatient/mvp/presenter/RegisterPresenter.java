package com.example.r2d2.medicalpatient.mvp.presenter;

import android.util.Log;

import com.example.r2d2.medicalpatient.api.ApiService;
import com.example.r2d2.medicalpatient.data.response.LoginResponse;
import com.example.r2d2.medicalpatient.data.response.RegisterResponse;
import com.example.r2d2.medicalpatient.mvp.model.DataManager;
import com.example.r2d2.medicalpatient.mvp.view.RegisterView;
import com.example.r2d2.medicalpatient.ui.base.BasePresenter;

import java.net.ConnectException;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Lollipop on 2017/4/29.
 */

public class RegisterPresenter extends BasePresenter<RegisterView> {
    private DataManager dataManager;
    private Disposable disposable;

    @Inject
    ApiService apiService;

    @Inject
    public RegisterPresenter(DataManager dataManager){
        this.dataManager = dataManager;
    }

    @Override
    public void detachView() {
        super.detachView();
        if (disposable!=null && !disposable.isDisposed()){
            disposable.dispose();
        }
    }

    public void register(String userString){
        getView().showDialog();
        Consumer<RegisterResponse> consumer = new Consumer<RegisterResponse>() {
            @Override
            public void accept(@NonNull RegisterResponse registerResponse) throws Exception {
                if (registerResponse.getStatus().equals("success")){
                    getView().onSuccess();
                    getView().onLogin();
                } else {
                    getView().hideDialog();
                    getView().onError(registerResponse.getMessage());
                }
            }
        };
        Observer<LoginResponse> observer = new Observer<LoginResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull LoginResponse loginResponse) {
                if (loginResponse.getStatus().equals("success")){
                    getView().hideDialog();
                    getView().onComplete();
                } else {
                    getView().hideDialog();
                    getView().onError(loginResponse.getMessage());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                getView().hideDialog();
                if (e.getClass().getName().equals("java.lang.NullPointerException")){

                } else if (e.getClass().getName().equals("java.net.ConnectException")){
                    getView().onError("哎呀..没网了！");
                }
            }

            @Override
            public void onComplete() {

            }
        };
        dataManager.register(userString, consumer, observer);
    }
}
