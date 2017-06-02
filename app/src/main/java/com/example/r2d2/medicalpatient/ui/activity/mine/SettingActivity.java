package com.example.r2d2.medicalpatient.ui.activity.mine;

import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.r2d2.medicalpatient.R;
import com.example.r2d2.medicalpatient.app.App;
import com.example.r2d2.medicalpatient.service.BluetoothService;
import com.example.r2d2.medicalpatient.ui.activity.ChangePassActivity;
import com.example.r2d2.medicalpatient.ui.activity.DoctorBindActivity;
import com.example.r2d2.medicalpatient.ui.activity.DoctorRebindActivity;
import com.example.r2d2.medicalpatient.ui.activity.LoginActivity;
import com.example.r2d2.medicalpatient.ui.activity.MainActivity;
import com.example.r2d2.medicalpatient.ui.base.BaseActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

public class SettingActivity extends BaseActivity {

    @OnClick(R.id.rebind)
    void rebind() {
        startActivity(new Intent(this, DoctorRebindActivity.class));
    }

    @OnClick(R.id.change_pass)
    void changePass() {
        startActivity(new Intent(this, ChangePassActivity.class));
    }

    @OnClick(R.id.exit)
    void exit() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
        MainActivity.instance.finish();

        //销毁服务，关闭蓝牙socket和输入流
        Observable<String> observable = Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                Intent intent = new Intent(SettingActivity.this, BluetoothService.class);
                stopService(intent);

                App.tag = 0;
                InputStream inputStream = App.getApp().getInputStream();
                BluetoothSocket socket = App.getApp().getSocket();
                BufferedReader reader = App.getApp().getReader();
                InputStreamReader inputStreamReader = App.getApp().getInputStreamReader();
                if (reader != null){
                    reader.close();
                }
                if (inputStreamReader != null){
                    inputStreamReader.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                    App.getApp().setInputStream(null);
                }
                if (socket!=null && socket.isConnected()){
                    socket.close();
                    App.getApp().setSocket(null);
                }
            }
        });
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        setTitle("设置");
    }
}
