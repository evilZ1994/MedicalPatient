package com.example.r2d2.medicalpatient.api;

import com.example.r2d2.medicalpatient.data.response.DataUploadResponse;
import com.example.r2d2.medicalpatient.data.response.DoctorAddResponse;
import com.example.r2d2.medicalpatient.data.response.DoctorSearchResponse;
import com.example.r2d2.medicalpatient.data.response.DoctorUserInfoResponse;
import com.example.r2d2.medicalpatient.data.response.LoginResponse;
import com.example.r2d2.medicalpatient.data.response.PatientUserInfoResponse;
import com.example.r2d2.medicalpatient.data.response.RegisterResponse;
import com.example.r2d2.medicalpatient.data.response.UpdateInfoResponse;

import io.reactivex.Observable;
import io.reactivex.Observer;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

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

    @GET("DoctorAddServlet")
    Observable<DoctorSearchResponse> searchDoctor(@Query("type") String type,
                                                  @Query("username") String username);

    @GET("DoctorAddServlet")
    Observable<DoctorAddResponse> addDoctor(@Query("type") String type,
                                            @Query("id") int id,
                                            @Query("doctor_id") int doctor_id);

    @FormUrlEncoded
    @POST("DataUploadServlet")
    Observable<DataUploadResponse> uploadData(@Field("data") String data);

    @GET("GetUserInfoServlet")
    Observable<PatientUserInfoResponse> getUserInfo(@Query("type") String type,
                                                    @Query("id") int id);

    @GET("GetUserInfoServlet")
    Observable<DoctorUserInfoResponse> getDoctorInfo(@Query("type") String type,
                                                     @Query("id") int id);

    @FormUrlEncoded
    @POST("InfoChangeServlet")
    Observable<UpdateInfoResponse> updateInfo(@Field("type") String type,
                                              @Field("id") int id,
                                              @Field("tag") String tag,
                                              @Field("content") String content);
}
