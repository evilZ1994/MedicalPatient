package com.example.r2d2.medicalpatient.mvp.presenter;

import com.example.r2d2.medicalpatient.app.App;
import com.example.r2d2.medicalpatient.data.response.DoctorAddResponse;
import com.example.r2d2.medicalpatient.data.response.DoctorSearchResponse;
import com.example.r2d2.medicalpatient.mvp.model.DataManager;
import com.example.r2d2.medicalpatient.mvp.model.RealmManager;
import com.example.r2d2.medicalpatient.mvp.view.DoctorAddView;
import com.example.r2d2.medicalpatient.ui.base.BasePresenter;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.realm.Realm;

/**
 * Created by Lollipop on 2017/5/1.
 */
public class DoctorAddPresenter extends BasePresenter<DoctorAddView> {
    private CompositeDisposable compositeDisposable;
    private DataManager dataManager;
    private RealmManager realmManager;

    @Inject
    public DoctorAddPresenter(DataManager dataManager, RealmManager realmManager){
        this.dataManager = dataManager;
        this.realmManager = realmManager;
        compositeDisposable = new CompositeDisposable();
    }

    /**
     * 解除订阅
     */
    @Override
    public void detachView() {
        super.detachView();
        if (!compositeDisposable.isDisposed()){
            compositeDisposable.dispose();
        }
    }

    /**
     * 查找医生
     * @param username
     */
    public void searchDoctor(String username){
        Observer<DoctorSearchResponse> observer = new Observer<DoctorSearchResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(@NonNull DoctorSearchResponse doctorSearchResponse) {
                String status = doctorSearchResponse.getStatus();
                if (status.equals("success")){
                    getView().onSearchSuccess(doctorSearchResponse.getDoctor());
                } else
                    getView().onSearchFail();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
                if (e.getClass().getName().equals("java.net.ConnectException")) {
                    getView().onError("哎呀..没网了！");
                } else if (e.getClass().getName().equals("java.lang.NullPointerException")){
                    getView().onError("程序崩溃了..T_T");
                }
            }

            @Override
            public void onComplete() {
                //这里不用Dialog，而是使用环形进度条(隐藏）
                getView().hideDialog();
            }
        };

        dataManager.searchDoctor(username, observer);
    }

    public void addDoctor(int id, int doctor_id){
        Observer<DoctorAddResponse> observer = new Observer<DoctorAddResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                compositeDisposable.add(d);
            }

            @Override
            public void onNext(@NonNull DoctorAddResponse doctorAddResponse) {
                if (doctorAddResponse.getStatus().equals("success")) {
                    //添加医生成功后，更新本地用户数据
                    updateDoctor(App.getCurrentUser().getId(), doctorAddResponse.getDoctor_id());
                    getView().onAddSuccess(doctorAddResponse.getDoctor_name());
                }else
                    getView().onAddFail();
            }

            @Override
            public void onError(@NonNull Throwable e) {
                e.printStackTrace();
                if (e.getClass().getName().equals("java.net.ConnectException")) {
                    getView().onError("哎呀..没网了！");
                } else if (e.getClass().getName().equals("java.lang.NullPointerException")){
                    getView().onError("程序崩溃了..T_T");
                }
            }

            @Override
            public void onComplete() {
                //这里不用Dialog，而是使用环形进度条(隐藏）
                getView().hideDialog();
            }
        };

        dataManager.addDoctor(id, doctor_id, observer);
    }

    public void updateDoctor(int id, int doctor_id){
        realmManager.updateDoctor(id, doctor_id);
    }
}
