package com.example.r2d2.medicalpatient.app;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.r2d2.medicalpatient.injector.component.ApplicationComponent;
import com.example.r2d2.medicalpatient.injector.component.DaggerApplicationComponent;
import com.example.r2d2.medicalpatient.injector.module.ApplicationModule;

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
