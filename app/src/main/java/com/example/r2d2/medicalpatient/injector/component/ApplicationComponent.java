package com.example.r2d2.medicalpatient.injector.component;

import android.bluetooth.BluetoothAdapter;

import com.example.r2d2.medicalpatient.api.ApiService;
import com.example.r2d2.medicalpatient.app.App;
import com.example.r2d2.medicalpatient.injector.module.ApiModule;
import com.example.r2d2.medicalpatient.injector.module.ApplicationModule;
import com.example.r2d2.medicalpatient.injector.module.BluetoothAdapterModule;
import com.example.r2d2.medicalpatient.injector.module.RealmModule;
import com.example.r2d2.medicalpatient.mvp.model.BluetoothManager;
import com.example.r2d2.medicalpatient.mvp.model.DataManager;
import com.example.r2d2.medicalpatient.mvp.model.RealmManager;
import com.example.r2d2.medicalpatient.service.DataUploadService;
import com.example.r2d2.medicalpatient.ui.activity.MainActivity;
import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Component;
import io.realm.Realm;

/**
 * Created by Lollipop on 2017/4/28.
 */
@Singleton
@Component(modules = {ApplicationModule.class, ApiModule.class, RealmModule.class, BluetoothAdapterModule.class})
public interface ApplicationComponent {
    App application();

    ApiService apiService();

    Gson gson();

    DataManager dataManager();

    RealmManager realmManager();

    Realm realm();

    BluetoothAdapter bluetoothAdapter();

    BluetoothManager bluetoothManager();

    void inject(DataUploadService dataUploadService);
}
