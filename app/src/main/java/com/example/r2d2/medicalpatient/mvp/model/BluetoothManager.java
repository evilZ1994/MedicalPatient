package com.example.r2d2.medicalpatient.mvp.model;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.r2d2.medicalpatient.app.App;
import com.example.r2d2.medicalpatient.data.realm.Data;

import org.json.JSONObject;
import org.threeten.bp.LocalDateTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * 执行蓝牙的各种操作，获取设备、读取数据、写入数据等
 * Created by Lollipop on 2017/5/5.
 */

public class BluetoothManager {
    private BluetoothAdapter bluetoothAdapter;
    private Realm realm;
    private List<Map<String, String>> bondedList;
    private List<Map<String, String>> usableList;

    private final String SERIALPORT_UUID = "00001101-0000-1000-8000-00805F9B34FB";
    UUID MY_UUID = UUID.fromString(SERIALPORT_UUID);

    @Inject
    public BluetoothManager(BluetoothAdapter bluetoothAdapter, Realm realm){
        this.bluetoothAdapter = bluetoothAdapter;
        this.realm = realm;
    }

    /**
     * 获取已配对的蓝牙设备
     * @param observer
     */
    public void getBondedDevices(Observer<Set<BluetoothDevice>> observer){
        Observable<Set<BluetoothDevice>> observable = Observable.create(new ObservableOnSubscribe<Set<BluetoothDevice>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Set<BluetoothDevice>> emitter) throws Exception {
                Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
                emitter.onNext(devices);
                emitter.onComplete();
            }
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 关闭蓝牙，清空设备列表等操作
     * @param list1 已配对列表
     * @param list2 可用设备列表
     * @param consumer
     */
    public void disableBluetooth(List<Map<String, String>> list1, List<Map<String, String>> list2, Consumer<String> consumer){
        this.bondedList = list1;
        this.usableList = list2;
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter e) throws Exception {
                //如果正在搜索，先停止搜索
                if (bluetoothAdapter.isDiscovering()){
                    bluetoothAdapter.cancelDiscovery();
                }
                bluetoothAdapter.disable();
                //清空设备
                bondedList.clear();
                usableList.clear();
                e.onNext("ok");
                e.onComplete();
            }
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }

    /**
     * 开启蓝牙
     * @param consumer
     */
    public void enableBluetooth(Consumer<String> consumer){
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                bluetoothAdapter.enable();
                //阻塞，蓝牙成功开启后再执行
                while(!bluetoothAdapter.isEnabled()){}
                e.onNext("ok");
                e.onComplete();
            }
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }

    /**
     * 开启定时器，定时10秒（取消蓝牙搜索）
     * @param consumer
     */
    public void startTimer(Consumer<String> consumer){
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                final ObservableEmitter<String> emitter = e;
                Timer timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        emitter.onNext("ok");
                        emitter.onComplete();
                    }
                };
                timer.schedule(timerTask, 10000);
            }
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }

    /**
     * 连接蓝牙设备
     * @param address 设备地址
     * @param observer
     */
    public void connect(final String address, Observer<InputStream> observer){
        Observable<InputStream> observable = Observable.create(new ObservableOnSubscribe<InputStream>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<InputStream> e) throws Exception {
                BluetoothDevice device = bluetoothAdapter.getRemoteDevice(address);
                BluetoothSocket socket = device.createRfcommSocketToServiceRecord(MY_UUID);
                socket.connect();
                InputStream inputStream = socket.getInputStream();
                if (socket.isConnected() && inputStream!=null) {
                    e.onNext(inputStream);
                    e.onComplete();
                }else
                    e.onError(new IOException());
            }
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 读取蓝牙设备发送过来的数据，并存入Realm数据库
     */
    public void readData(final InputStream inputStream, Consumer<String> consumer){
        Log.i("readdata", "read data");
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                line = reader.readLine();
                Realm realm = Realm.getInstance(new RealmConfiguration.Builder().name("medical.realm").deleteRealmIfMigrationNeeded().build());
                while (reader!=null && (line=reader.readLine())!=null){
                    JSONObject dataJson = new JSONObject(line);
                    final double angle = dataJson.getDouble("angle");
                    final int pressure = dataJson.getInt("pressure");
                    final double temperature = dataJson.getDouble("temperature");
                    int pulse = dataJson.getInt("pulse");
                    final String create_time = dataJson.getString("create_time");
                    //写入数据库
                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            Data data = new Data(pressure, angle, temperature, pressure, create_time);
                            Log.i("data", data.toString());
                            realm.copyToRealm(data);
                        }
                    });
                }
                e.onNext("disconnect");
                e.onComplete();
            }
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }
}
