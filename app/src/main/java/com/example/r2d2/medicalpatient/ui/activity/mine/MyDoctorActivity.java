package com.example.r2d2.medicalpatient.ui.activity.mine;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;

import com.example.r2d2.medicalpatient.R;
import com.example.r2d2.medicalpatient.app.App;
import com.example.r2d2.medicalpatient.mvp.presenter.DoctorInfoPresenter;
import com.example.r2d2.medicalpatient.mvp.view.UserInfoView;
import com.example.r2d2.medicalpatient.ui.adapter.InfoAdapter;
import com.example.r2d2.medicalpatient.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyDoctorActivity extends BaseActivity implements UserInfoView {
    private List<Map<String, String>> items = new ArrayList<>();
    private InfoAdapter adapter;

    @BindView(R.id.doctor_info_list)
    ListViewCompat infoList;

    @Inject
    DoctorInfoPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_doctor);

        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);

        setTitle("我的医生");

        //初始化adapter
        adapter = new InfoAdapter(items);
        //设置adapter
        infoList.setAdapter(adapter);

        //读取数据
        presenter.getDoctorInfo("doctor", App.getCurrentUser().getDoctor_id());
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public void showDialog() {

    }

    @Override
    public void hideDialog() {

    }

    @Override
    public void show(List<Map<String, String>> items) {
        adapter.setItems(items);
        adapter.notifyDataSetChanged();
    }
}
