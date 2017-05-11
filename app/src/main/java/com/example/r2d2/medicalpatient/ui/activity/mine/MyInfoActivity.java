package com.example.r2d2.medicalpatient.ui.activity.mine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;

import com.example.r2d2.medicalpatient.R;
import com.example.r2d2.medicalpatient.app.App;
import com.example.r2d2.medicalpatient.mvp.presenter.UserInfoPresenter;
import com.example.r2d2.medicalpatient.mvp.view.UserInfoView;
import com.example.r2d2.medicalpatient.ui.adapter.InfoAdapter;
import com.example.r2d2.medicalpatient.ui.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyInfoActivity extends BaseActivity implements UserInfoView{
    private InfoAdapter adapter;

    private List<Map<String, String>> items = new ArrayList<>();

    @Inject
    UserInfoPresenter presenter;

    @BindView(R.id.info_list)
    ListViewCompat infoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        presenter.attachView(this);

        //初始化adapter
        adapter = new InfoAdapter(items);
        //添加adapter
        infoList.setAdapter(adapter);

        //读取数据
        presenter.getUserInfo("patient", App.getCurrentUser().getId());
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

    /**
     * 获取到用户信息后进行展示
     * @param items
     */
    @Override
    public void show(List<Map<String, String>> items) {
        adapter.setItems(items);
        adapter.notifyDataSetChanged();
    }
}
