package com.example.r2d2.medicalpatient.mvp.presenter;

import com.example.r2d2.medicalpatient.api.ApiService;
import com.example.r2d2.medicalpatient.data.response.LoginResponse;
import com.example.r2d2.medicalpatient.mvp.model.DataManager;
import com.example.r2d2.medicalpatient.mvp.model.RealmManager;
import com.example.r2d2.medicalpatient.mvp.view.LoginView;
import com.example.r2d2.medicalpatient.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.realm.Realm;

/**
 * Created by Lollipop on 2017/4/28.
 * 协调登陆相关操作及View展示
 */

public class LoginPresenter extends BasePresenter<LoginView>{
    private DataManager dataManager;
    private Disposable disposable;
    private RealmManager realmManager;

    @Inject
    ApiService apiService;

    @Inject
    public LoginPresenter(DataManager dataManager, RealmManager realmManager){
        this.dataManager = dataManager;
        this.realmManager = realmManager;
    }

    /**
     * 解除订阅
     */
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
                //保存Disposable对象，以便之后解除订阅
                disposable = d;
            }

            @Override
            public void onNext(@NonNull LoginResponse loginResponse) {
                String status = loginResponse.getStatus();
                if (status.equals("success")){
                    //登陆成功后将用户保存到Realm数据库
                    storeUser(loginResponse);
                } else{
                    getView().hideDialog();
                    getView().onError(loginResponse.getMessage());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                getView().hideDialog();
                e.printStackTrace();
                if (e.getClass().getName().equals("java.lang.NullPointerException")){
                    getView().onError("程序崩溃了..T_T");
                } else if (e.getClass().getName().equals("java.net.ConnectException")){
                    getView().onError("哎呀..没网了！");
                }
            }

            @Override
            public void onComplete() {

            }
        };
        dataManager.login(username, password, observer);
    }

    public void storeUser(LoginResponse response){
        //用户保存成功后的回调
        Realm.Transaction.OnSuccess onSuccess = new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                getView().hideDialog();
                getView().onSuccess();
            }
        };
        //用户保存失败后的回调
        Realm.Transaction.OnError onError = new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                getView().hideDialog();
                getView().onError("数据库出错了- -!");
                error.printStackTrace();
            }
        };

        Realm realm = realmManager.storeUser(response, onSuccess, onError);
        //将real对象传递给fragment
        getView().closeRealm(realm);
    }
}
