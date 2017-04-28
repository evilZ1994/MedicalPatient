package com.example.r2d2.medicalpatient.injector.module;

import android.content.Context;

import com.example.r2d2.medicalpatient.app.App;
import com.example.r2d2.medicalpatient.injector.scope.PerApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Lollipop on 2017/4/28.
 */
@Module
public class ApplicationModule {
    private App mApplication;

    public ApplicationModule(App application){
        this.mApplication = application;
    }

    @Provides
    @Singleton
    public App provideApplication(){
        return mApplication;
    }

    @Provides
    @Singleton
    Context provideContext(){
        return mApplication;
    }
}
