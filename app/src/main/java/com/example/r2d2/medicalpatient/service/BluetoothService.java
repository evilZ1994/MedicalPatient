package com.example.r2d2.medicalpatient.service;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.r2d2.medicalpatient.app.App;
import com.example.r2d2.medicalpatient.injector.component.ApplicationComponent;
import com.example.r2d2.medicalpatient.injector.component.DaggerBluetoothManagerComponent;
import com.example.r2d2.medicalpatient.mvp.model.BluetoothManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.inject.Inject;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * Created by Lollipop on 2017/5/6.
 */

public class BluetoothService extends Service {
    @Inject
    BluetoothManager bluetoothManager;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerBluetoothManagerComponent.builder().applicationComponent(App.getApplicationComponent()).build().inject(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        InputStream inputStream = App.getApp().getInputStream();
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                //蓝牙连接断开
                //do something

            }
        };
        bluetoothManager.readData(inputStream, consumer);
        return super.onStartCommand(intent, flags, startId);
    }
}
