package com.example.r2d2.medicalpatient.mvp.presenter;

import com.example.r2d2.medicalpatient.data.response.DoctorUserInfoResponse;
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

public class DoctorInfoPresenter extends BasePresenter<UserInfoView> {
    private DataManager dataManager;

    @Inject
    public DoctorInfoPresenter(DataManager dataManager){
        super();
        this.dataManager = dataManager;
    }

    public void getDoctorInfo(String type, int id){
        Observer<DoctorUserInfoResponse> observer = new Observer<DoctorUserInfoResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull DoctorUserInfoResponse doctorUserInfoResponse) {
                List<Map<String, String>> items = new ArrayList<>();
                Map<String, String> item1 = new HashMap<>();
                item1.put("title", "用户名");
                item1.put("content", doctorUserInfoResponse.getUsername());
                item1.put("tag", "username");
                items.add(item1);
                Map<String, String> item2 = new HashMap<>();
                item2.put("title", "姓名");
                item2.put("content", doctorUserInfoResponse.getName());
                item2.put("tag", "name");
                items.add(item2);
                Map<String, String> item3 = new HashMap<>();
                item3.put("title", "性别");
                item3.put("content", doctorUserInfoResponse.getSex());
                item3.put("tag", "sex");
                items.add(item3);
                Map<String, String> item4 = new HashMap<>();
                item4.put("title", "医院");
                item4.put("content", doctorUserInfoResponse.getHospital());
                item4.put("tag", "age");
                items.add(item4);
                Map<String, String> item5 = new HashMap<>();
                item5.put("title", "科室");
                item5.put("content", doctorUserInfoResponse.getDepartment());
                item5.put("tag", "department");
                items.add(item5);

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
        dataManager.getDoctorInfo(type, id, observer);
    }
}
