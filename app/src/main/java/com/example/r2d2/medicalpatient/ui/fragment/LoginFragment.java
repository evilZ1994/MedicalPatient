package com.example.r2d2.medicalpatient.ui.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.r2d2.medicalpatient.R;
import com.example.r2d2.medicalpatient.mvp.presenter.LoginPresenter;
import com.example.r2d2.medicalpatient.mvp.view.LoginView;
import com.example.r2d2.medicalpatient.ui.base.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment implements LoginView {
    private ProgressDialog progressDialog;

    @Inject
    LoginPresenter loginPresenter;

    @BindView(R.id.login_name)
    EditText username;
    @BindView(R.id.login_pass)
    EditText password;
    @OnClick(R.id.login)
    void login(){
        loginPresenter.login(username.getText().toString(), password.getText().toString());
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
        loginPresenter.detachView();
    }

    @Override
    public void onSuccess() {
        Toast.makeText(getContext(), "登陆成功！", Toast.LENGTH_SHORT).show();
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
}
