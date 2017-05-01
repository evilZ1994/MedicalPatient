package com.example.r2d2.medicalpatient.mvp.model;

import com.example.r2d2.medicalpatient.data.realm.User;
import com.example.r2d2.medicalpatient.data.response.LoginResponse;
import com.google.gson.Gson;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.realm.Realm;

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
     * @return 返回当前的real对象，传递给fragment，并在fragment的onDestroy()方法中close
     */
    public Realm storeUser(LoginResponse loginResponse, Realm.Transaction.OnSuccess onSuccess, Realm.Transaction.OnError onError){
        final LoginResponse response = loginResponse;
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                User user = new User();
                user.setUsername(response.getUser().getUsername());
                user.setName(response.getUser().getName());
                user.setPassword(response.getUser().getPassword());
                if (response.getUser().getDoctor_id() != null)
                    user.setDoctor_id(Integer.valueOf(response.getUser().getDoctor_id()));
                user.setLastLogin(new Date());
                realm.copyToRealmOrUpdate(user);
            }
        }, onSuccess, onError);

        return realm;
    }
}
