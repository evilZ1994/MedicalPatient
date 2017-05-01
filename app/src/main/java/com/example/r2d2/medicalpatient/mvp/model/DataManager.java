package com.example.r2d2.medicalpatient.mvp.model;

import com.example.r2d2.medicalpatient.api.ApiService;
import com.example.r2d2.medicalpatient.data.response.LoginResponse;
import com.example.r2d2.medicalpatient.data.response.RegisterResponse;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Lollipop on 2017/4/28.
 * 封装各种网络请求，网络数据操作
 */
@Singleton
public class DataManager {
    private final ApiService apiService;
    private final Gson gson;
    private final String TYPE = "patient"; //请求类型，代表访问服务器的是patient

    @Inject
    public DataManager(ApiService apiService, Gson gson){
        this.apiService = apiService;
        this.gson = gson;
    }

    /**
     * 登陆操作
     * @param username
     * @param password
     * @param observer
     */
    public void login(String username, String password, Observer<LoginResponse> observer){
        apiService.login(TYPE, username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 注册操作，注册成功后继续执行登陆
     * @param userString
     * @param consumer
     * @param observer
     */
    public void register(String userString, Consumer<RegisterResponse> consumer, Observer<LoginResponse> observer){
        apiService.register(TYPE, userString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(consumer)
                .observeOn(Schedulers.io()) //回到io线程发出login请求
                .flatMap(new Function<RegisterResponse, ObservableSource<LoginResponse>>() {
                    @Override
                    public ObservableSource<LoginResponse> apply(@NonNull RegisterResponse registerResponse) throws Exception {
                        if (registerResponse.getStatus().equals("success")) {
                            RegisterResponse.UserBean user = registerResponse.getUser();
                            return apiService.login(TYPE, user.getUsername(), user.getPassword());
                        }
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
