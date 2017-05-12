package com.example.r2d2.medicalpatient.injector.component;

import com.example.r2d2.medicalpatient.injector.scope.PerActivity;
import com.example.r2d2.medicalpatient.service.BluetoothService;

import dagger.Component;

/**
 * Created by Lollipop on 2017/5/7.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class)
public interface BluetoothManagerComponent {
    void inject(BluetoothService bluetoothService);
}
