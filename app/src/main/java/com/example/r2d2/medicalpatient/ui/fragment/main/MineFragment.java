package com.example.r2d2.medicalpatient.ui.fragment.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.ListViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;

import com.example.r2d2.medicalpatient.R;
import com.example.r2d2.medicalpatient.app.App;
import com.example.r2d2.medicalpatient.ui.activity.BluetoothActivity;
import com.example.r2d2.medicalpatient.ui.activity.mine.MyDoctorActivity;
import com.example.r2d2.medicalpatient.ui.activity.mine.MyInfoActivity;
import com.example.r2d2.medicalpatient.ui.activity.mine.SettingActivity;
import com.example.r2d2.medicalpatient.ui.adapter.MineItemAdapter;
import com.example.r2d2.medicalpatient.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends BaseFragment {
    private MineItemAdapter adapter;

    private List<Map<String, Object>> items;

    @BindView(R.id.mine)
    LinearLayoutCompat mineLayout;
    @BindView(R.id.username)
    AppCompatTextView usernameText;
    @BindView(R.id.name)
    AppCompatTextView nameText;
    @BindView(R.id.mine_list)
    ListViewCompat mineList;


    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_mine, container, false);
        ButterKnife.bind(this, view);
        getFragmentComponent().inject(this);

        //初始化个人信息栏
        usernameText.setText(App.getCurrentUser().getUsername());
        nameText.setText(App.getCurrentUser().getName());
        //添加监听器
        mineLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MyInfoActivity.class);
                //姓名被修改后返回，这里也要修改姓名的显示
                startActivityForResult(intent, 1);
            }
        });

        //初始items
        init();

        //初始化adapter
        adapter = new MineItemAdapter(items);
        mineList.setAdapter(adapter);
        //添加监听器
        mineList.setOnItemClickListener(new MineOnItemClickListener());

        return view;
    }

    private void init() {
        items = new ArrayList<>();
        Map<String, Object> item1 = new HashMap<>();
        item1.put("resourceId", R.drawable.ic_heart);
        item1.put("text", "我的医生");
        item1.put("target", MyDoctorActivity.class);
        items.add(item1);
        Map<String, Object> item2 = new HashMap<>();
        item2.put("resourceId", R.drawable.ic_bluetooth);
        item2.put("text", "蓝牙设备");
        item2.put("target", BluetoothActivity.class);
        items.add(item2);
        Map<String, Object> item3 = new HashMap<>();
        item3.put("resourceId", R.drawable.ic_setting);
        item3.put("text", "设置");
        item3.put("target", SettingActivity.class);
        items.add(item3);
    }

    private class MineOnItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            Map<String, Object> item = adapter.getItem(i);
            Intent intent = new Intent(getContext(), (Class<?>) item.get("target"));
            startActivity(intent);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 1){
            nameText.setText(data.getStringExtra("name"));
        }
    }
}
