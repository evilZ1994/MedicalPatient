package com.example.r2d2.medicalpatient.ui.activity.mine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.view.View;
import android.widget.AdapterView;

import com.example.r2d2.medicalpatient.R;
import com.example.r2d2.medicalpatient.app.App;
import com.example.r2d2.medicalpatient.data.response.UpdateInfoResponse;
import com.example.r2d2.medicalpatient.mvp.presenter.UserInfoPresenter;
import com.example.r2d2.medicalpatient.mvp.view.UserInfoView;
import com.example.r2d2.medicalpatient.ui.adapter.InfoAdapter;
import com.example.r2d2.medicalpatient.ui.base.BaseActivity;
import com.example.r2d2.medicalpatient.ui.dialog.PatientInfoChangeDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

import static com.example.r2d2.medicalpatient.app.App.getContext;

public class MyInfoActivity extends BaseActivity implements UserInfoView{
    private InfoAdapter adapter;

    private List<Map<String, String>> items = new ArrayList<>();

    @Inject
    UserInfoPresenter presenter;

    @BindView(R.id.info_list)
    ListViewCompat infoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        getActivityComponent().inject(this);
        ButterKnife.bind(this);
        presenter.attachView(this);

        //初始化adapter
        adapter = new InfoAdapter(items);
        //添加adapter
        infoList.setAdapter(adapter);
        infoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final InfoAdapter.ViewHolder viewHolder = (InfoAdapter.ViewHolder) view.getTag();
                Map<String, String> item = (Map<String, String>)adapter.getItem(position);
                if (!item.get("tag").equals("username")){
                    //Dialog修改完成后的回调操作，更新这个Item的值
                    Consumer<UpdateInfoResponse> consumer = new Consumer<UpdateInfoResponse>() {
                        @Override
                        public void accept(@NonNull UpdateInfoResponse response) throws Exception {
                            String result = response.getStatus();
                            if (result.equals("success")){
                                viewHolder.content.setText(response.getContent());
                            }
                        }
                    };
                    PatientInfoChangeDialog dialog = new PatientInfoChangeDialog(getContext(), R.style.InfoChangeDialogTheme, item.get("tag"), item.get("title"), consumer);
                    dialog.show();
                }
            }
        });
        //读取数据
        presenter.getUserInfo("patient", App.getCurrentUser().getId());
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public void showDialog() {

    }

    @Override
    public void hideDialog() {

    }

    /**
     * 获取到用户信息后进行展示
     * @param items
     */
    @Override
    public void show(List<Map<String, String>> items) {
        adapter.setItems(items);
        adapter.notifyDataSetChanged();
    }
}
