package com.example.r2d2.medicalpatient.app;

import android.app.Application;
import android.content.Context;

import com.example.r2d2.medicalpatient.data.bean.User;
import com.example.r2d2.medicalpatient.injector.component.ApplicationComponent;
import com.example.r2d2.medicalpatient.injector.component.DaggerApplicationComponent;
import com.example.r2d2.medicalpatient.injector.module.ApplicationModule;
import com.facebook.stetho.Stetho;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.io.InputStream;

import io.realm.Realm;

/**
 * Created by Lollipop on 2017/4/28.
 */

public class App extends Application {
    private static Context mContext;
    private static App app;
    private static ApplicationComponent mApplicationComponent;
    //当前登陆用户,bean包下的User，不是realm包下的User
    private static User currentUser;
    //蓝牙数据流，找不到其他方式可以传递给service了
    private InputStream inputStream;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
        app = this;
        setupInjector();
        initRealm();
        initStethoRealm();
        initTreeTen();
    }

    private void initTreeTen() {
        AndroidThreeTen.init(this);
    }

    private void initRealm() {
        Realm.init(this);
    }

    private void initStethoRealm(){
        //Chrome上的Android（Realm）管理插件
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(RealmInspectorModulesProvider.builder(this).build())
                .build());
    }

    private void setupInjector() {
        mApplicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }

    public static Context getContext(){
        return mContext;
    }

    public static App getApp(){
        return app;
    }

    public static ApplicationComponent getApplicationComponent(){
        return mApplicationComponent;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    //登陆后调用该方法，保存当前登陆用户
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }
}
