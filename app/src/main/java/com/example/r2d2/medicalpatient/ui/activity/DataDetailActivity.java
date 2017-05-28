package com.example.r2d2.medicalpatient.ui.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.TextView;

import com.example.r2d2.medicalpatient.R;
import com.example.r2d2.medicalpatient.data.realm.Data;
import com.example.r2d2.medicalpatient.ui.adapter.DataDetailAdapter;
import com.example.r2d2.medicalpatient.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

public class DataDetailActivity extends BaseActivity {
    private String tag;
    private String title;

    private DataDetailAdapter adapter;

    @Inject
    Realm realm;

    @BindView(R.id.current_data)
    TextView currentDataView;
    @BindView(R.id.data_unit)
    TextView dataUnit;
    @BindView(R.id.data_list)
    ListViewCompat dataListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_detail);
        tag = getIntent().getStringExtra("tag");
        getActivityComponent().inject(this);
        ButterKnife.bind(this);

        title = getIntent().getStringExtra("title");
        setTitle(title);

        showData();
    }

    private void showData() {
        if (tag.equals("angle")){
            dataListView.setVisibility(View.GONE);
        }
        RealmResults<Data> results = realm.where(Data.class).findAll();
        //初始化当前数据
        if (results.size()>0){
            String value;
            String unit;
            int color;
            switch (tag){
                case "pressure":
                    value = results.get(results.size()-1).getPressure()+"";
                    unit = "Pa";
                    color = ContextCompat.getColor(this, R.color.simpleBlue);
                    break;
                case "angle":
                    value = results.get(results.size()-1).getAngle()+"";
                    unit = "度";
                    color = ContextCompat.getColor(this, R.color.simpleAmber);
                    break;
                case "temperature":
                    value = results.get(results.size()-1).getTemperature()+"";
                    unit = "℃";
                    color = ContextCompat.getColor(this, R.color.simpleOrange);
                    break;
                case "pulse":
                    value = results.get(results.size()-1).getPulse()+"";
                    unit = "次/分";
                    color = ContextCompat.getColor(this, R.color.simpleGreen);
                    break;
                default:
                    value = "0";
                    unit = "";
                    color = 0;
            }
            currentDataView.setText(value);
            currentDataView.setTextColor(color);
            dataUnit.setText(unit);
        }
        adapter = new DataDetailAdapter(results, tag, currentDataView, dataUnit);
        dataListView.setAdapter(adapter);

    }
}
