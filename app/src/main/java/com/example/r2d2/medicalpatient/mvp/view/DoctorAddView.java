package com.example.r2d2.medicalpatient.mvp.view;

import com.example.r2d2.medicalpatient.data.response.DoctorSearchResponse;
import com.example.r2d2.medicalpatient.ui.base.View;

/**
 * Created by Lollipop on 2017/5/1.
 */

public interface DoctorAddView extends View {
    void onSearchSuccess(DoctorSearchResponse.DoctorBean doctor);

    void onSearchFail();

    void onAddSuccess(String name);

    void onAddFail();

}
