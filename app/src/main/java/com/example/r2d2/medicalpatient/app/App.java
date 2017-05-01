package com.example.r2d2.medicalpatient.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.r2d2.medicalpatient.injector.component.ApplicationComponent;
import com.example.r2d2.medicalpatient.injector.component.DaggerApplicationComponent;
import com.example.r2d2.medicalpatient.injector.module.ApplicationModule;
import com.facebook.stetho.InspectorModulesProvider;
import com.facebook.stetho.Stetho;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.util.regex.Pattern;

import io.realm.Realm;

/**
 * Created by Lollipop on 2017/4/28.
 */

public class App extends Application {
    private static Context mContext;
    private static ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
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

    public static ApplicationComponent getApplicationComponent(){
        return mApplicationComponent;
    }
}
