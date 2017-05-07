package com.example.r2d2.medicalpatient.ui.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.r2d2.medicalpatient.R;
import com.example.r2d2.medicalpatient.api.ApiService;
import com.example.r2d2.medicalpatient.app.App;
import com.example.r2d2.medicalpatient.service.DataUploadService;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i("main activity", "service start");
        startService(new Intent(this, DataUploadService.class));
    }
}
