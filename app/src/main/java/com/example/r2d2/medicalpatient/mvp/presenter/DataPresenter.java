package com.example.r2d2.medicalpatient.mvp.presenter;

import android.util.Log;

import com.example.r2d2.medicalpatient.data.realm.Data;
import com.example.r2d2.medicalpatient.mvp.model.RealmManager;
import com.example.r2d2.medicalpatient.ui.base.BasePresenter;
import com.example.r2d2.medicalpatient.ui.fragment.main.DataFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmChangeListener;

/**
 * Created by Lollipop on 2017/5/8.
 */

public class DataPresenter extends BasePresenter<DataFragment>{
    private Realm realm;
    private RealmManager realmManager;

    @Inject
    public DataPresenter(Realm realm, RealmManager realmManager){
        this.realm = realm;
        this.realmManager = realmManager;
    }

    public void updateData(){
        realm.addChangeListener(new RealmChangeListener<Realm>() {
            @Override
            public void onChange(Realm element) {
                List<Data> list = realmManager.getData();
                //传递data，刷新图表
                if (list.size()>0){
                    List<Integer> dataList = new ArrayList<Integer>();
                    for (Data data : list){
                        dataList.add(data.getPressure());
                    }
                    //刷新图表
                    getView().refreshChart(dataList);
                    //刷新数据
                    Data data = list.get(list.size()-1);
                    Map<String, Object> map = new HashMap<>();
                    map.put("pressure", data.getPressure());
                    map.put("angle", data.getAngle());
                    map.put("temperature", data.getTemperature());
                    map.put("pulse", data.getPulse());
                    getView().refreshData(map);
                }
            }
        });
    }
}
