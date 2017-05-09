package com.example.r2d2.medicalpatient.injector.component;

import com.example.r2d2.medicalpatient.injector.module.ActivityModule;
import com.example.r2d2.medicalpatient.injector.scope.PerActivity;
import com.example.r2d2.medicalpatient.ui.activity.DataDetailActivity;

import dagger.Component;

/**
 * Created by Lollipop on 2017/5/9.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {
    void inject(DataDetailActivity dataDetailActivity);
}
