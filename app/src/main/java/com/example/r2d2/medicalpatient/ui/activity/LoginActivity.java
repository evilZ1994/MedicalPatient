package com.example.r2d2.medicalpatient.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.r2d2.medicalpatient.R;

public class LoginActivity extends AppCompatActivity {

    //静态变量，用于在RegisterActivity跳转前结束此LoginActivity
    public static LoginActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //在RegisterActivity中可以获取到LoginActivity的实例
        instance = this;
    }
}
