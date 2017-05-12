package com.example.r2d2.medicalpatient.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.view.View;

import com.example.r2d2.medicalpatient.app.App;
import com.example.r2d2.medicalpatient.injector.component.DaggerFragmentComponent;
import com.example.r2d2.medicalpatient.injector.component.FragmentComponent;
import com.example.r2d2.medicalpatient.injector.module.FragmentModule;

/**
 * Created by Lollipop on 2017/4/28.
 */

public abstract class BaseFragment extends Fragment {
    private FragmentComponent fragmentComponent;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public FragmentComponent getFragmentComponent(){
        if (fragmentComponent == null){
            fragmentComponent = DaggerFragmentComponent.builder()
                    .applicationComponent(App.getApplicationComponent()).build();
        }
        return fragmentComponent;
    }
}
