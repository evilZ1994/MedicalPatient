package com.example.r2d2.medicalpatient.ui.base;

import android.support.v7.app.AppCompatActivity;

import com.example.r2d2.medicalpatient.app.App;
import com.example.r2d2.medicalpatient.injector.component.ActivityComponent;
import com.example.r2d2.medicalpatient.injector.component.DaggerActivityComponent;

/**
 * Created by Lollipop on 2017/5/9.
 */

public abstract class BaseActivity extends AppCompatActivity {
    private ActivityComponent activityComponent;

    public ActivityComponent getActivityComponent(){
        if (activityComponent == null){
            activityComponent = DaggerActivityComponent.builder().applicationComponent(App.getApplicationComponent()).build();
        }
        return activityComponent;
    }
}
