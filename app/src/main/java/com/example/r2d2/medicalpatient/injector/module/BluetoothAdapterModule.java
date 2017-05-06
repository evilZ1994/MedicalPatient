package com.example.r2d2.medicalpatient.injector.module;

import android.bluetooth.BluetoothAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Lollipop on 2017/5/5.
 */
@Module
public class BluetoothAdapterModule {

    @Singleton
    @Provides
    BluetoothAdapter provideBluetoothAdapter(){
        return BluetoothAdapter.getDefaultAdapter();
    }
}
