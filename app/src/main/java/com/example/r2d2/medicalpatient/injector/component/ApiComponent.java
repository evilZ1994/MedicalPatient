package com.example.r2d2.medicalpatient.injector.component;

import com.example.r2d2.medicalpatient.injector.module.ApiModule;
import com.example.r2d2.medicalpatient.ui.dialog.PatientInfoChangeDialog;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by R2D2 on 2017/5/23.
 */
@Singleton
@Component(modules = ApiModule.class)
public interface ApiComponent {
    void inject(PatientInfoChangeDialog patientInfoChangeDialog);
}
