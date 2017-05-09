package com.example.r2d2.medicalpatient.injector.module;

import android.content.Context;

import com.example.r2d2.medicalpatient.ui.base.BaseActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Lollipop on 2017/5/9.
 */
@Module
public class ActivityModule {
    private BaseActivity activity;

    public ActivityModule(BaseActivity activity){
        this.activity = activity;
    }

    @Provides
    BaseActivity provideActivity(){
        return activity;
    }

    @Provides
    Context provideContext(){
        return activity;
    }
}
