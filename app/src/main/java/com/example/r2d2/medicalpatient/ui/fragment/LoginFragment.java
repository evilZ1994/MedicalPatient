package com.example.r2d2.medicalpatient.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.r2d2.medicalpatient.R;
import com.example.r2d2.medicalpatient.mvp.presenter.LoginPresenter;
import com.example.r2d2.medicalpatient.mvp.view.LoginView;
import com.example.r2d2.medicalpatient.ui.activity.RegisterActivity;
import com.example.r2d2.medicalpatient.ui.base.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

public class LoginFragment extends BaseFragment implements LoginView {
    private ProgressDialog progressDialog;
    private Realm realm;

    @Inject
    LoginPresenter loginPresenter;

    @BindView(R.id.login_name)
    EditText username;
    @BindView(R.id.login_pass)
    EditText password;
    //登陆按钮点击，执行登陆操作
    @OnClick(R.id.login)
    void login(){
        loginPresenter.login(username.getText().toString(), password.getText().toString());
    }
    //注册按钮点击，跳转到注册界面
    @OnClick(R.id.register)
    void register(){
        Intent intent = new Intent(getContext(), RegisterActivity.class);
        startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        getFragmentComponent().inject(this);
        //将View传递给Presenter
        loginPresenter.attachView(this);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //解除订阅
        loginPresenter.detachView();
    }

    @Override
    public void onSuccess() {
        Toast.makeText(getContext(), "登陆成功啦！", Toast.LENGTH_SHORT).show();
        //执行登陆成功后的跳转动作
        //对用户进行检查，是否已经添加医生
        //已添加医生，进入主界面（跳转前，连接蓝牙设备）

        //未添加医生，进入医生添加界面
    }

    @Override
    public void onError(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDialog() {
        progressDialog = ProgressDialog.show(getContext(), "登陆", "正在登陆...", true, false);
    }

    @Override
    public void hideDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void closeRealm(Realm realm) {
        this.realm = realm;
        //在fragment的onDestroy()方法中close realm
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //关闭realm
        if (!realm.isClosed())
            realm.close();
    }
}
