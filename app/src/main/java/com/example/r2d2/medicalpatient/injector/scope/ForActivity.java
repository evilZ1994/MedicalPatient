package com.example.r2d2.medicalpatient.injector.scope;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Lollipop on 2017/4/29.
 */
@Scope
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ForActivity {
}
