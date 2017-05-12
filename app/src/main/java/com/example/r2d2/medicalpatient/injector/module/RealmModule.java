package com.example.r2d2.medicalpatient.injector.module;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Lollipop on 2017/5/1.
 */
@Module
public class RealmModule {
    private String REALM_NAME = "medical.realm";

    @Provides
    Realm provideRealm(){
        return Realm.getInstance(new RealmConfiguration.Builder().name(REALM_NAME).deleteRealmIfMigrationNeeded().build());
    }
}
