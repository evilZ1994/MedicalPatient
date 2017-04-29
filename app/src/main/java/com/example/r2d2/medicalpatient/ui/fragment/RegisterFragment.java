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
import com.example.r2d2.medicalpatient.R2;
import com.example.r2d2.medicalpatient.data.request.User;
import com.example.r2d2.medicalpatient.mvp.presenter.RegisterPresenter;
import com.example.r2d2.medicalpatient.mvp.view.RegisterView;
import com.example.r2d2.medicalpatient.ui.base.BaseFragment;
import com.google.gson.Gson;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends BaseFragment implements RegisterView{
    private ProgressDialog progressDialog;

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
        Log.e("onError",msg);
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
    public void onLogin() {
        progressDialog.setTitle("登陆");
        progressDialog.setMessage("正在登陆...");
    }

    @Override
    public void onComplete() {
        //登陆成功之后的跳转动作
        Toast.makeText(getContext(), "登陆成功！", Toast.LENGTH_SHORT).show();
    }
}
