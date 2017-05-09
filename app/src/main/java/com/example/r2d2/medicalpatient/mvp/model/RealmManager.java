package com.example.r2d2.medicalpatient.mvp.model;


import android.util.Log;

import com.example.r2d2.medicalpatient.app.App;
import com.example.r2d2.medicalpatient.data.realm.Data;
import com.example.r2d2.medicalpatient.data.realm.User;
import com.example.r2d2.medicalpatient.data.response.LoginResponse;
import com.example.r2d2.medicalpatient.util.DateUtil;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by Lollipop on 2017/5/1.
 * 封装本地数据操作
 */
@Singleton
public class RealmManager {
    private Realm realm;
    private Gson gson;

    @Inject
    public RealmManager(Realm realm, Gson gson){
        this.realm = realm;
        this.gson = gson;
    }

    /**
     * 登陆成功后将用户信息保存到Realm数据库中
     * @param loginResponse 登陆成功后的返回信息（包含用户信息）
     * @param onSuccess 保存成功的回调函数
     * @param onError 保存失败的回调函数
     */
    public void storeUser(LoginResponse loginResponse, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError){
        final LoginResponse response = loginResponse;
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User user = new User();
                user.setId(response.getUser().getId());
                user.setUsername(response.getUser().getUsername());
                user.setName(response.getUser().getName());
                user.setPassword(response.getUser().getPassword());
                if (response.getUser().getDoctor_id() != null)
                    user.setDoctor_id(Integer.valueOf(response.getUser().getDoctor_id()));
                user.setLastLogin(new Date());
                realm.copyToRealmOrUpdate(user);
            }
        }, onSuccess, onError);
    }

    /**
     * 通过id获取用户
     * @param id
     * @return
     */
    public User getUserById(int id){
        return realm.where(User.class).equalTo("id", id).findFirst();
    }

    /**
     * 保存当前登陆的用户到application
     * @param id
     */
    public void storeCurrentUser(int id){
        User user = getUserById(id);
        com.example.r2d2.medicalpatient.data.bean.User userBean = new com.example.r2d2.medicalpatient.data.bean.User();
        userBean.setId(user.getId());
        userBean.setName(user.getName());
        userBean.setUsername(user.getUsername());
        userBean.setPassword(user.getPassword());
        userBean.setDoctor_id(user.getDoctor_id());
        userBean.setLastLogin(user.getLastLogin());
        App.setCurrentUser(userBean);
    }

    /**
     * 更新本地用户的doctor绑定
     * @param id
     * @param doctor_id
     */
    public void updateDoctor(final int id, final int doctor_id){
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User user = getUserById(id);
                user.setDoctor_id(doctor_id);
            }
        });
        //同时更新application里的用户
        storeCurrentUser(id);
    }


}
