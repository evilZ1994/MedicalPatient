package com.example.r2d2.medicalpatient.mvp.presenter;

import com.example.r2d2.medicalpatient.app.App;
import com.example.r2d2.medicalpatient.data.response.CommonResponse;
import com.example.r2d2.medicalpatient.mvp.model.DataManager;
import com.example.r2d2.medicalpatient.ui.base.BasePresenter;
import com.example.r2d2.medicalpatient.ui.base.View;

import javax.inject.Inject;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.api.BasicCallback;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by Lollipop on 2017/5/29.
 */

public class ChangePassPresenter extends BasePresenter<View> {
    private DataManager dataManager;

    @Inject
    public ChangePassPresenter(DataManager dataManager){
        this.dataManager = dataManager;
    }

    public void updatePassword(int id, final String oldPass, final String newPass){
        getView().showDialog();
        Observer<CommonResponse> observer = new Observer<CommonResponse>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull CommonResponse commonResponse) {
                String status = commonResponse.getStatus();
                if (status.equals("success")){
                    //修改JMessage
                    JMessageClient.updateUserPassword(oldPass, newPass, new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            if (i == 0){
                                //修改成功
                                App.getCurrentUser().setPassword(newPass);
                                getView().onSuccess();
                                getView().hideDialog();
                            }else {
                                getView().onError(s);
                                getView().hideDialog();
                            }
                        }
                    });
                }else {
                    getView().onError("修改失败..");
                    getView().hideDialog();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {
                getView().onError("修改失败..");
                getView().hideDialog();
            }

            @Override
            public void onComplete() {

            }
        };
        dataManager.updatePassword(id, oldPass, newPass, observer);
    }
}
