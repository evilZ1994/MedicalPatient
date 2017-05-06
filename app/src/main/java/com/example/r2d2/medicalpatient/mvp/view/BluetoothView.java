package com.example.r2d2.medicalpatient.mvp.view;

import com.example.r2d2.medicalpatient.ui.base.View;

import java.io.InputStream;

/**
 * Created by Lollipop on 2017/5/5.
 */

public interface BluetoothView extends View {
    void showScanBtn();

    void hidScanBtn();

    void startScan();

    void stopScan();

    void showBondedDevice();

    void hideBondedDevice();

    void notifyAdapter();

    void startService();
}
