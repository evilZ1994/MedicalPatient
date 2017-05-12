package com.example.r2d2.medicalpatient.mvp.presenter;

import com.example.r2d2.medicalpatient.data.response.PatientUserInfoResponse;
import com.example.r2d2.medicalpatient.mvp.model.DataManager;
import com.example.r2d2.medicalpatient.mvp.view.UserInfoView;
import com.example.r2d2.medicalpatient.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Lollipop on 2017/5/11.
 */

public class UserInfoPresenter extends BasePresenter<UserInfoView> {
    private DataManager dataManager;

    @Inject
    public UserInfoPresenter(DataManager dataManager){
        this.dataManager = dataManager;
    }

    public void getUserInfo(String type, int id){
        Observer<PatientUserInfoResponse> observer = new Observer<PatientUserInfoResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull PatientUserInfoResponse patientUserInfoResponse) {
                List<Map<String, String>> items = new ArrayList<>();
                Map<String, String> item1 = new HashMap<>();
                item1.put("title", "用户名");
                item1.put("content", patientUserInfoResponse.getUsername());
                item1.put("tag", "username");
                items.add(item1);
                Map<String, String> item2 = new HashMap<>();
                item2.put("title", "姓名");
                item2.put("content", patientUserInfoResponse.getName());
                item2.put("tag", "name");
                items.add(item2);
                Map<String, String> item3 = new HashMap<>();
                item3.put("title", "性别");
                item3.put("content", patientUserInfoResponse.getSex());
                item3.put("tag", "sex");
                items.add(item3);
                Map<String, String> item4 = new HashMap<>();
                item4.put("title", "年龄");
                item4.put("content", patientUserInfoResponse.getAge());
                item4.put("tag", "age");
                items.add(item4);

                getView().show(items);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                if (e.getClass().getName().equals("java.net.ConnectException")) {
                    getView().onError("哎呀..没网了！");
                }
            }

            @Override
            public void onComplete() {

            }
        };

        dataManager.getUserInfo(type, id, observer);
    }
}
