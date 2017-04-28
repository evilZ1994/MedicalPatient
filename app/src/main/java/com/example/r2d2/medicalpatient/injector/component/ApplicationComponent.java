package com.example.r2d2.medicalpatient.injector.component;

import com.example.r2d2.medicalpatient.api.ApiService;
import com.example.r2d2.medicalpatient.app.App;
import com.example.r2d2.medicalpatient.injector.module.ApiModule;
import com.example.r2d2.medicalpatient.injector.module.ApplicationModule;
import com.example.r2d2.medicalpatient.mvp.model.DataManager;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Lollipop on 2017/4/28.
 */
@Singleton
@Component(modules = {ApplicationModule.class, ApiModule.class})
public interface ApplicationComponent {
    App application();

    ApiService apiService();

    Gson gson();

    DataManager dataManager();
}
