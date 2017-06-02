package com.example.r2d2.medicalpatient.ui.activity;

import android.app.ProgressDialog;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.r2d2.medicalpatient.R;
import com.example.r2d2.medicalpatient.app.App;
import com.example.r2d2.medicalpatient.mvp.presenter.ChangePassPresenter;
import com.example.r2d2.medicalpatient.ui.base.BaseActivity;
import com.example.r2d2.medicalpatient.ui.base.View;
import com.example.r2d2.medicalpatient.util.CheckChineseUtil;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangePassActivity extends BaseActivity implements View{
    private ProgressDialog progressDialog;

    @Inject
    ChangePassPresenter presenter;

    @BindView(R.id.current_pass)
    TextInputEditText etCurrentPass;
    @BindView(R.id.new_pass)
    TextInputEditText etNewPass;
    @BindView(R.id.repeat_new_pass)
    TextInputEditText etRepeat;
    @OnClick(R.id.submit_new_pass)
    void changePass(){
        String currentPass = etCurrentPass.getText().toString();
        String newPass = etNewPass.getText().toString();
        String repeat = etRepeat.getText().toString();
        if (newPass.length()>=4){
            if (newPass.equals(repeat)){
                presenter.updatePassword(App.getCurrentUser().getId(), currentPass, newPass);
            }else {
                Toast.makeText(this, "两次输入不一致", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "密码长度至少为4位", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        ButterKnife.bind(this);
        getActivityComponent().inject(this);
        presenter.attachView(this);
        setTitle("修改密码");
    }

    @Override
    public void onSuccess() {
        Toast.makeText(this, "修改成功！", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDialog() {
        progressDialog = ProgressDialog.show(this, "修改密码", "正在修改密码...", true, false);
    }

    @Override
    public void hideDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
