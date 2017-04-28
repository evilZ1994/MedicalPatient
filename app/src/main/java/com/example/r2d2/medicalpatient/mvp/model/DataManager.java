package com.example.r2d2.medicalpatient.mvp.model;

import com.example.r2d2.medicalpatient.api.ApiService;
import com.example.r2d2.medicalpatient.data.response.LoginResponse;
import com.google.gson.Gson;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Lollipop on 2017/4/28.
 */
@Singleton
public class DataManager {
    private final ApiService apiService;
    private final Gson gson;
    private final String TYPE = "patient";

    @Inject
    public DataManager(ApiService apiService, Gson gson){
        this.apiService = apiService;
        this.gson = gson;
    }

    public void login(String username, String password, Observer<LoginResponse> observer){
        apiService.login(TYPE, username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
