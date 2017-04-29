package com.example.r2d2.medicalpatient.injector.component;

import com.example.r2d2.medicalpatient.injector.module.FragmentModule;
import com.example.r2d2.medicalpatient.injector.scope.PerActivity;
import com.example.r2d2.medicalpatient.ui.base.BaseFragment;
import com.example.r2d2.medicalpatient.ui.fragment.LoginFragment;
import com.example.r2d2.medicalpatient.ui.fragment.RegisterFragment;

import dagger.Component;

/**
 * Created by Lollipop on 2017/4/28.
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {

    void inject(LoginFragment loginFragment);

    void inject(RegisterFragment registerFragment);
}
