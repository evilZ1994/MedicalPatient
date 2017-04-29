package com.example.r2d2.medicalpatient.api;

import com.example.r2d2.medicalpatient.data.response.LoginResponse;
import com.example.r2d2.medicalpatient.data.response.RegisterResponse;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Lollipop on 2017/4/28.
 */

public interface ApiService {

    @FormUrlEncoded
    @POST("LoginServlet")
    Observable<LoginResponse> login(@Field("type") String type,
                                    @Field("username") String username,
                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("RegisterServlet")
    Observable<RegisterResponse> register(@Field("type") String type,
                                          @Field("user") String userString);
}
