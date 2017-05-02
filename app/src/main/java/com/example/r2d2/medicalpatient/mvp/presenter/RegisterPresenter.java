package com.example.r2d2.medicalpatient.mvp.presenter;

import com.example.r2d2.medicalpatient.api.ApiService;
import com.example.r2d2.medicalpatient.data.response.LoginResponse;
import com.example.r2d2.medicalpatient.data.response.RegisterResponse;
import com.example.r2d2.medicalpatient.mvp.model.DataManager;
import com.example.r2d2.medicalpatient.mvp.model.RealmManager;
import com.example.r2d2.medicalpatient.mvp.view.RegisterView;
import com.example.r2d2.medicalpatient.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.realm.Realm;

/**
 * Created by Lollipop on 2017/4/29.
 * 协调注册的各种操作以及视图的展示
 */

public class RegisterPresenter extends BasePresenter<RegisterView> {
    private DataManager dataManager;
    private Disposable disposable;
    private RealmManager realmManager;

    @Inject
    ApiService apiService;

    @Inject
    public RegisterPresenter(DataManager dataManager, RealmManager realmManager){
        this.dataManager = dataManager;
        this.realmManager = realmManager;
    }

    /**
     * 解除订阅
     */
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
                    //注册成功后将进度框改为正在登陆
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
                //保存Disposable对象，以便之后解除订阅
                disposable = d;
            }

            @Override
            public void onNext(@NonNull LoginResponse loginResponse) {
                if (loginResponse.getStatus().equals("success")){
                    //登陆成功保存用户信息
                    //保存用户信息到本地数据库
                    storeUser(loginResponse);
                } else {
                    getView().hideDialog();
                    getView().onError(loginResponse.getMessage());
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
                getView().hideDialog();
                if (e.getClass().getName().equals("java.net.ConnectException")) {
                    getView().onError("哎呀..没网了！");
                }/* else if (e.getClass().getName().equals("java.lang.NullPointerException")){
                    getView().onError("程序崩溃了..T_T");//正常原因注册失败时，flatMap会返回一个null值，导致此异常触发
                }*/
            }

            @Override
            public void onComplete() {

            }
        };
        dataManager.register(userString, consumer, observer);
    }

    public void storeUser(LoginResponse loginResponse){
        final LoginResponse response = loginResponse;
        //保存用户成功后的回调
        Realm.Transaction.OnSuccess onSuccess = new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                //保存当前登陆用户到application
                storeCurrentUser(response.getUser().getId());
                getView().hideDialog();
                getView().onComplete();
            }
        };
        //保存用户失败后的回调
        Realm.Transaction.OnError onError = new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                getView().hideDialog();
                getView().onError("数据库出错了- -!");
            }
        };
        realmManager.storeUser(loginResponse, onSuccess, onError);
    }

    public void storeCurrentUser(int id){
        realmManager.storeCurrentUser(id);
    }
}
