package com.example.r2d2.medicalpatient.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.r2d2.medicalpatient.api.ApiService;
import com.example.r2d2.medicalpatient.app.App;
import com.example.r2d2.medicalpatient.data.realm.DataCache;
import com.example.r2d2.medicalpatient.data.response.CommonResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by Lollipop on 2017/5/7.
 */

public class DataUploadService extends Service {

    @Inject
    ApiService apiService;
    @Inject
    Realm realm;

    private RealmResults<DataCache> results;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        App.getApplicationComponent().inject(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("datauploadservice", "upload data");
        results = realm.where(DataCache.class).findAll();
        uploadData();
        results.addChangeListener(new RealmChangeListener<RealmResults<DataCache>>() {
            @Override
            public void onChange(RealmResults<DataCache> element) {
                uploadData();
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    private void uploadData(){
        JSONArray dataArray;
        if (results.size()>0) {
            dataArray = new JSONArray();
            for (DataCache dataCache : results){
                JSONObject data = null;
                try {
                    data = new JSONObject();
                    data.put("pressure", dataCache.getPressure());
                    data.put("angle", dataCache.getAngle());
                    data.put("temperature", dataCache.getTemperature());
                    data.put("pulse", dataCache.getPulse());
                    data.put("create_time", dataCache.getCreate_time());
                    data.put("patient_id", dataCache.getPatient_id());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dataArray.put(data);
            }
            Observable<CommonResponse> observable = apiService.uploadData(dataArray.toString());
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<CommonResponse>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {

                        }

                        @Override
                        public void onNext(@NonNull CommonResponse commonResponse) {
                            if (commonResponse.getStatus().equals("success")){
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        onDelete();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }
    }

    private void onDelete(){
        results.deleteAllFromRealm();
    }
}
