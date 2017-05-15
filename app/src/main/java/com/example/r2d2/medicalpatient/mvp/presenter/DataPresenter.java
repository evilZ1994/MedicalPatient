package com.example.r2d2.medicalpatient.mvp.presenter;

import android.util.Log;

import com.example.r2d2.medicalpatient.data.realm.Data;
import com.example.r2d2.medicalpatient.mvp.model.RealmManager;
import com.example.r2d2.medicalpatient.ui.base.BasePresenter;
import com.example.r2d2.medicalpatient.ui.fragment.main.DataFragment;
import com.example.r2d2.medicalpatient.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

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
        RealmResults<Data> results = realm.where(Data.class).findAll();
        refreshChart(results);
        results.addChangeListener(new RealmChangeListener<RealmResults<Data>>() {
            @Override
            public void onChange(RealmResults<Data> results) {
                    refreshChart(results);
            }
        });
    }
    private void refreshChart(RealmResults<Data> results){
        List<Data> list = getDataList(results);
        //传递data，刷新图表
        if (list.size()>0) {
            List<Integer> dataList = new ArrayList<Integer>();
            for (Data data : list) {
                dataList.add(data.getPressure());
            }
            //刷新图表
            getView().refreshChart(dataList);
            //刷新数据
            Data data = list.get(list.size() - 1);
            Map<String, Object> map = new HashMap<>();
            map.put("pressure", data.getPressure());
            map.put("angle", data.getAngle());
            map.put("temperature", data.getTemperature());
            map.put("pulse", data.getPulse());
            getView().refreshData(map);
        }
    }
    private List<Data> getDataList(RealmResults<Data> results){
        Date now = new Date();
        int currentMinute = DateUtil.getMinute(now);
        int currentHour = DateUtil.getHour(now);
        int currentDay = DateUtil.getDay(now);
        List<Data> list = new ArrayList<>();
        if (results.size()>0) {
            //超过12条数据
            if (results.size()>12) {
                for (int i = results.size() - 12; i < results.size(); i++) {
                    Data data = results.get(i);
                    Date date = DateUtil.stringToDate(data.getCreate_time());
                    int minute = DateUtil.getMinute(date);
                    int hour = DateUtil.getHour(date);
                    int day = DateUtil.getDay(date);
                    if (day==currentDay && hour==currentHour && minute==currentMinute){
                        list.add(data);
                    }
                }
                if (list.size()>0){
                    int index = DateUtil.getSecond(DateUtil.stringToDate(list.get(0).getCreate_time()))/5;
                    List<Data> finalList = new ArrayList<>();
                    for (int i=0; i<index; i++){
                        finalList.add(new Data());
                    }
                    for (int i=0; i<list.size(); i++){
                        finalList.add(list.get(i));
                    }
                    return finalList;
                }
                return list;
            }else {//数据不足12条,时间起点不确定（第一次与设备连接）
                List<Data> list2 = new ArrayList<Data>();
                for (Data data : results){
                    Date date = DateUtil.stringToDate(data.getCreate_time());
                    int minute = DateUtil.getMinute(date);
                    int hour = DateUtil.getHour(date);
                    int day = DateUtil.getDay(date);
                    if (day==currentDay && hour==currentHour && minute==currentMinute){
                        list2.add(data);
                    }
                }
                if (list2.size()>0){
                    int index = DateUtil.getSecond(DateUtil.stringToDate(list2.get(0).getCreate_time()))/5;
                    for (int i=0; i<index; i++){
                        list.add(new Data());
                    }
                    for (int i=0; i<list2.size(); i++){
                        list.add(list2.get(i));
                    }
                }
                return list;
            }
        }else {
            //没有数据
            return list;
        }
    }
}
