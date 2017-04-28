package com.example.r2d2.medicalpatient.test;

import com.example.r2d2.medicalpatient.api.ApiService;
import com.example.r2d2.medicalpatient.data.response.LoginResponse;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Lollipop on 2017/4/29.
 */

public class test {

    public void test(){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://172.18.61.199:8080/MedicalProjectServer/").addConverterFactory(GsonConverterFactory.create(new Gson())).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build();
        ApiService apiService = retrofit.create(ApiService.class);
        apiService.login("patient", "hello", "123").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<LoginResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull LoginResponse loginResponse) {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}
