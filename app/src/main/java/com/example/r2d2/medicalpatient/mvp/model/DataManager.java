package com.example.r2d2.medicalpatient.mvp.model;

import com.example.r2d2.medicalpatient.api.ApiService;
import com.example.r2d2.medicalpatient.data.response.CommonResponse;
import com.example.r2d2.medicalpatient.data.response.DoctorAddResponse;
import com.example.r2d2.medicalpatient.data.response.DoctorSearchResponse;
import com.example.r2d2.medicalpatient.data.response.DoctorUserInfoResponse;
import com.example.r2d2.medicalpatient.data.response.LoginResponse;
import com.example.r2d2.medicalpatient.data.response.PatientUserInfoResponse;
import com.example.r2d2.medicalpatient.data.response.RegisterResponse;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by Lollipop on 2017/4/28.
 * 封装各种网络请求，网络数据操作
 */
@Singleton
public class DataManager {
    private final ApiService apiService;
    private final Gson gson;
    private final String TYPE = "patient"; //请求类型，代表访问服务器的是patient

    @Inject
    public DataManager(ApiService apiService, Gson gson){
        this.apiService = apiService;
        this.gson = gson;
    }

    /**
     * 登陆操作
     * @param username
     * @param password
     * @param observer
     */
    public void login(String username, String password, Observer<LoginResponse> observer){
        apiService.login(TYPE, username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 注册操作，注册成功后继续执行登陆
     * @param userString
     * @param consumer
     * @param
     */
    public void register(String userString, Consumer<RegisterResponse> consumer){
        /*apiService.register(TYPE, userString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(consumer)
                .observeOn(Schedulers.io()) //回到io线程发出login请求
                .flatMap(new Function<RegisterResponse, ObservableSource<LoginResponse>>() {
                    @Override
                    public ObservableSource<LoginResponse> apply(@NonNull RegisterResponse registerResponse) throws Exception {
                        if (registerResponse.getStatus().equals("success")) {
                            RegisterResponse.UserBean user = registerResponse.getUser();
                            return apiService.login(TYPE, user.getUsername(), user.getPassword());
                        }
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);*/

        apiService.register(TYPE, userString)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }

    /**
     * 查找医生
     * @param username 医生用户名
     * @param observer
     */
    public void searchDoctor(String username, Observer<DoctorSearchResponse> observer){
        apiService.searchDoctor("search", username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 添加医生
     * @param id
     * @param doctor_id
     * @param observer
     */
    public void addDoctor(int id, int doctor_id, Observer<DoctorAddResponse> observer){
        apiService.addDoctor("add", id, doctor_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 获取用户信息
     * @param type 用户类型（doctor\patient)
     * @param id 账户id
     * @param observer 回调
     */
    public void getUserInfo(String type, int id, Observer<PatientUserInfoResponse> observer) {
        apiService.getUserInfo(type, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 获取医生信息
     * @param type 用户类型（doctor\patient)
     * @param id 账户id
     * @param observer 回调
     */
    public void getDoctorInfo(String type, int id, Observer<DoctorUserInfoResponse> observer){
        apiService.getDoctorInfo(type, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }

    /**
     * 修改密码
     * @param id 用户id
     * @param oldPass 原密码
     * @param newPass 新密码
     * @param observer 回调
     */
    public void updatePassword(int id, String oldPass, String newPass, Observer<CommonResponse> observer){
        apiService.changePass(TYPE, id, oldPass, newPass)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}
