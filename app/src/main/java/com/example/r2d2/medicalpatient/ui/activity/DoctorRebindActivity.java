package com.example.r2d2.medicalpatient.ui.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.r2d2.medicalpatient.R;
import com.example.r2d2.medicalpatient.app.App;
import com.example.r2d2.medicalpatient.data.response.DoctorSearchResponse;
import com.example.r2d2.medicalpatient.mvp.presenter.DoctorAddPresenter;
import com.example.r2d2.medicalpatient.mvp.view.DoctorAddView;
import com.example.r2d2.medicalpatient.ui.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DoctorRebindActivity extends BaseActivity implements DoctorAddView {

    private DoctorSearchResponse.DoctorBean doctor;

    @Inject
    DoctorAddPresenter doctorAddPresenter;

    @BindView(R.id.doc_username)
    EditText doctorUsername;
    @BindView(R.id.search_doc_btn)
    Button searchBtn;
    @BindView(R.id.search_fail)
    TextView searchFailText;
    @BindView(R.id.doc_item)
    LinearLayout docItem;
    @BindView(R.id.doc_name)
    TextView docName;
    @BindView(R.id.doc_hospital)
    TextView docHospital;
    @BindView(R.id.add)
    Button addBtn;
    @BindView(R.id.add_doc_done)
    TextView addDocDone;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    @OnClick(R.id.search_doc_btn)
    void search(){
        hideAll();
        //防止重复点击
        searchBtn.setEnabled(false);
        showDialog();
        doctorAddPresenter.searchDoctor(doctorUsername.getText().toString());
    }
    @OnClick(R.id.add)
    void add(){
        //改变外观
        addBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.deepGray));
        //防止重复点击
        addBtn.setEnabled(false);
        showDialog();
        doctorAddPresenter.addDoctor(App.getCurrentUser().getId(), Integer.valueOf(doctor.getId()));
    }

    //隐藏组件
    private void hideAll() {
        docItem.setVisibility(View.INVISIBLE);
        searchFailText.setVisibility(View.INVISIBLE);
        addDocDone.setVisibility(View.INVISIBLE);
        addBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.simpleBlue));
    }
    //取消防止按钮重复点击
    private void enableButtons(){
        searchBtn.setEnabled(true);
        addBtn.setEnabled(true);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rebind_doctor);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        doctorAddPresenter.attachView(this);
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        hideDialog();
    }

    @Override
    public void showDialog() {
        //显式环形进度条
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideDialog() {
        //隐藏环形进度条
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onSearchSuccess(DoctorSearchResponse.DoctorBean doctor) {
        //查找成功后暂存医生信息用于展示
        this.doctor = doctor;
        //展示医生信息
        docName.setText(doctor.getName());
        docHospital.setText(doctor.getHospital());
        docItem.setVisibility(View.VISIBLE);
        //取消按钮保护
        enableButtons();
    }

    @Override
    public void onSearchFail() {
        //显示提示信息
        searchFailText.setVisibility(View.VISIBLE);
        //取消按钮保护
        enableButtons();
    }

    @Override
    public void onAddSuccess(String name) {
        Toast.makeText(this, "绑定成功！", Toast.LENGTH_SHORT).show();
        //显示绑定成功的提示信息
        addDocDone.setText("已成功绑定医生："+name);
        addDocDone.setVisibility(View.VISIBLE);
        //只取消search按钮保护
        searchBtn.setEnabled(true);
    }

    @Override
    public void onAddFail() {
        Toast.makeText(this, "绑定失败了..稍后再试试吧！", Toast.LENGTH_SHORT).show();
        addBtn.setBackgroundColor(ContextCompat.getColor(this, R.color.simpleBlue));
        addBtn.setEnabled(true);
        //取消按钮保护
        enableButtons();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
