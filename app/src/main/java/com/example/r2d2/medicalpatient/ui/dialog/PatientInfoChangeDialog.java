package com.example.r2d2.medicalpatient.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.design.widget.TextInputEditText;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.r2d2.medicalpatient.R;
import com.example.r2d2.medicalpatient.api.ApiService;
import com.example.r2d2.medicalpatient.app.App;
import com.example.r2d2.medicalpatient.data.response.UpdateInfoResponse;
import com.example.r2d2.medicalpatient.injector.component.DaggerApiComponent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by R2D2 on 2017/5/23.
 */

public class PatientInfoChangeDialog extends Dialog {
    private String tag;
    private String title;
    private Consumer<UpdateInfoResponse> consumer;

    @Inject
    ApiService apiService;

    @BindView(R.id.title)
    TextView titleText;
    @BindView(R.id.content)
    TextInputEditText contentInput;
    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.ok)
    TextView ok;

    @OnClick(R.id.cancel)
    void clickCancel(){
        this.dismiss();
    }
    @OnClick(R.id.ok)
    void clickOk(){
        //点击确定后，修改信息
        String content = contentInput.getText().toString();
        if (content.length()>0) {
            apiService.updateInfo("patient", App.getCurrentUser().getId(), tag, content)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(consumer);
        }
        this.dismiss();
    }

    public PatientInfoChangeDialog(@NonNull Context context, @StyleRes int themeResId, String tag, String title, Consumer<UpdateInfoResponse> consumer) {
        super(context, themeResId);
        this.tag = tag;
        this.title = title;
        this.consumer = consumer;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏提示框的标题，此项必须写在setContentView之前
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.info_change_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ButterKnife.bind(this);
        DaggerApiComponent.builder().build().inject(this);

        titleText.setText("修改"+title);
        //获取焦点并弹出软键盘
        contentInput.setFocusable(true);
        contentInput.setFocusableInTouchMode(true);
        contentInput.requestFocus();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }
}
