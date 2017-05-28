package com.example.r2d2.medicalpatient.ui.fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.r2d2.medicalpatient.R;
import com.example.r2d2.medicalpatient.data.request.User;
import com.example.r2d2.medicalpatient.mvp.presenter.RegisterPresenter;
import com.example.r2d2.medicalpatient.mvp.view.RegisterView;
import com.example.r2d2.medicalpatient.ui.activity.DoctorBindActivity;
import com.example.r2d2.medicalpatient.ui.base.BaseFragment;
import com.example.r2d2.medicalpatient.util.CheckChineseUtil;
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
    TextInputEditText usernameText;
    @BindView(R.id.reg_name)
    TextInputEditText nameText;
    @BindView(R.id.reg_pass)
    TextInputEditText passwordText;
    @BindView(R.id.reg_pass_repeat)
    TextInputEditText passwordRepeatText;
    //点击后执行注册操作
    @OnClick(R.id.register)
    void register(){
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        String passwordRepeat = passwordRepeatText.getText().toString();
        if (username.length()<4){
            Toast.makeText(getContext(), "用户名长度至少为4", Toast.LENGTH_SHORT).show();
        } else if (CheckChineseUtil.isContainsChinese(username)){
            Toast.makeText(getContext(), "用户名只能包含字母或数字", Toast.LENGTH_SHORT).show();
        } else if (password.length()<4){
            Toast.makeText(getContext(), "密码长度至少为4", Toast.LENGTH_SHORT).show();
        }else if (!password.equals(passwordRepeat)){
            Toast.makeText(getContext(), "两次输入的密码不一致", Toast.LENGTH_SHORT).show();
        }else {
            User user = new User();
            user.setName(nameText.getText().toString());
            user.setUsername(username);
            user.setPassword(password);
            String userString = new Gson().toJson(user);
            registerPresenter.register(userString);
        }
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
        gotoDoctorAdd();
    }

    private void gotoDoctorAdd() {
        Intent intent = new Intent(getContext(), DoctorBindActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //解除订阅
        registerPresenter.detachView();
    }
}
