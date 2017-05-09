package com.example.r2d2.medicalpatient.injector.component;

import com.example.r2d2.medicalpatient.injector.module.FragmentModule;
import com.example.r2d2.medicalpatient.injector.scope.PerActivity;
import com.example.r2d2.medicalpatient.ui.fragment.BluetoothFragment;
import com.example.r2d2.medicalpatient.ui.fragment.DoctorAddFragment;
import com.example.r2d2.medicalpatient.ui.fragment.LoginFragment;
import com.example.r2d2.medicalpatient.ui.fragment.RegisterFragment;
import com.example.r2d2.medicalpatient.ui.fragment.main.DataFragment;

import dagger.Component;

/**
 * Created by Lollipop on 2017/4/28.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    void inject(LoginFragment loginFragment);

    void inject(RegisterFragment registerFragment);

    void inject(DoctorAddFragment doctorAddFragment);

    void inject(BluetoothFragment bluetoothFragment);

    void inject(DataFragment dataFragment);
}
