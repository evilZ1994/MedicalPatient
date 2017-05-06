package com.example.r2d2.medicalpatient.mvp.presenter;

import android.bluetooth.BluetoothDevice;
import android.util.Log;

import com.example.r2d2.medicalpatient.app.App;
import com.example.r2d2.medicalpatient.mvp.model.BluetoothManager;
import com.example.r2d2.medicalpatient.mvp.model.RealmManager;
import com.example.r2d2.medicalpatient.mvp.view.BluetoothView;
import com.example.r2d2.medicalpatient.ui.base.BasePresenter;
import com.example.r2d2.medicalpatient.util.ObjectBytesUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by Lollipop on 2017/5/5.
 */

public class BluetoothPresenter extends BasePresenter<BluetoothView>{
    private BluetoothManager bluetoothManager;
    private RealmManager realmManager;

    private List<Map<String, String>> list;

    @Inject
    public BluetoothPresenter(BluetoothManager bluetoothManager, RealmManager realmManager){
        this.bluetoothManager = bluetoothManager;
        this.realmManager = realmManager;
    }

    public void getBondedList(List<Map<String, String>> bondedList){
        list = bondedList;
        Observer<Set<BluetoothDevice>> observer = new Observer<Set<BluetoothDevice>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Set<BluetoothDevice> bluetoothDevices) {
                list.clear();
                Iterator<BluetoothDevice> iterator = bluetoothDevices.iterator();
                if (iterator.hasNext()) {
                    getView().showBondedDevice();
                    while (iterator.hasNext()) {
                        Map<String, String> map = new HashMap<>();
                        BluetoothDevice device = iterator.next();
                        map.put("name", device.getName());
                        map.put("address", device.getAddress());
                        list.add(map);
                    }
                    getView().notifyAdapter();
                } else {
                    getView().notifyAdapter();
                    getView().hideBondedDevice();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        };
        bluetoothManager.getBondedDevices(observer);
    }

    public void disableBluetooth(List<Map<String, String>> bondedList, List<Map<String, String>> usableList){
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                getView().notifyAdapter();
            }
        };
        bluetoothManager.disableBluetooth(bondedList, usableList, consumer);
    }

    public void enableBluetooth(){
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                getView().showScanBtn();
                getView().startScan();
            }
        };
        bluetoothManager.enableBluetooth(consumer);
    }

    public void startTimer(){
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(@NonNull String s) throws Exception {
                getView().stopScan();
            }
        };
        bluetoothManager.startTimer(consumer);
    }

    public void connect(String address){
        Observer<InputStream> observer = new Observer<InputStream>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull InputStream inputStream) {
                getView().onSuccess();
                //将inputStream保存到app，用于传递到service
                App.getApp().setInputStream(inputStream);
                getView().startService();
                //隐藏dialog操作在fragment startService()中
            }

            @Override
            public void onError(@NonNull Throwable e) {
                getView().onError("连接失败！");
                getView().hideDialog();
            }

            @Override
            public void onComplete() {

            }
        };
        getView().showDialog();
        bluetoothManager.connect(address, observer);
    }
}
