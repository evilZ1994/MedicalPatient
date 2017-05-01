package com.example.r2d2.medicalpatient.ui.base;

import io.realm.Realm;

/**
 * Created by Lollipop on 2017/4/28.
 */

public interface View {
    void onSuccess();

    void onError(String msg);

    void showDialog();

    void hideDialog();

    void closeRealm(Realm realm);
}
