package com.example.r2d2.medicalpatient.ui.activity.mine;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.r2d2.medicalpatient.R;
import com.example.r2d2.medicalpatient.ui.activity.LoginActivity;
import com.example.r2d2.medicalpatient.ui.activity.MainActivity;
import com.example.r2d2.medicalpatient.ui.base.BaseActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @OnClick(R.id.exit)
    void exit(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        this.finish();
        MainActivity.instance.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        setTitle("设置");
    }
}
