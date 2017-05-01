package com.example.r2d2.medicalpatient.ui.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.r2d2.medicalpatient.R;
import com.example.r2d2.medicalpatient.data.request.User;
import com.example.r2d2.medicalpatient.mvp.presenter.RegisterPresenter;
import com.example.r2d2.medicalpatient.mvp.view.RegisterView;
import com.example.r2d2.medicalpatient.ui.base.BaseFragment;
import com.google.gson.Gson;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends BaseFragment implements RegisterView{
    private ProgressDialog progressDialog;
    private Realm realm;

    @Inject
    RegisterPresenter registerPresenter;
    @Inject
    Gson gson;

    @BindView(R.id.reg_username)
    TextInputEditText username;
    @BindView(R.id.reg_name)
    TextInputEditText name;
    @BindView(R.id.reg_pass)
    TextInputEditText password;
    @BindView(R.id.reg_pass_repeat)
    TextInputEditText password_repeat;
    //点击后执行注册操作
    @OnClick(R.id.register)
    void register(){
        if (password.getText().toString().equals(password_repeat.getText().toString())){
            User user = new User();
            user.setName(name.getText().toString());
            user.setUsername(username.getText().toString());
            user.setPassword(password.getText().toString());
            String userString = new Gson().toJson(user);
            registerPresenter.register(userString);
        } else
            Toast.makeText(getContext(), "两次输入的密码不一致！", Toast.LENGTH_SHORT).show();
    }

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ButterKnife.bind(this, view);
        getFragmentComponent().inject(this);
        //将View传递给Presenter
        registerPresenter.attachView(this);

        return view;
    }

    @Override
    public void onSuccess() {
        Toast.makeText(getContext(), "注册成功！正在登陆...", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDialog() {
        progressDialog = ProgressDialog.show(getContext(), "注册", "正在注册...", true, false);
    }

    @Override
    public void hideDialog() {
        progressDialog.dismiss();
    }

    @Override
    public void closeRealm(Realm realm) {
        this.realm = realm;
        //在fragment的onDestroy()方法里close realm
    }

    //注册成功后修改进度框的显示
    @Override
    public void onLogin() {
        progressDialog.setTitle("登陆");
        progressDialog.setMessage("正在登陆...");
    }

    @Override
    public void onComplete() {
        Toast.makeText(getContext(), "登陆成功啦！", Toast.LENGTH_SHORT).show();
        //执行登陆成功之后的跳转动作
        //直接跳转到添加Doctor的界面
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //关闭realm
        if (!realm.isClosed()){
            realm.close();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //解除订阅
        registerPresenter.detachView();
    }
}
