package com.example.r2d2.medicalpatient.ui.fragment;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.ListViewCompat;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.r2d2.medicalpatient.R;
import com.example.r2d2.medicalpatient.mvp.presenter.BluetoothPresenter;
import com.example.r2d2.medicalpatient.mvp.view.BluetoothView;
import com.example.r2d2.medicalpatient.service.BluetoothService;
import com.example.r2d2.medicalpatient.ui.activity.MainActivity;
import com.example.r2d2.medicalpatient.ui.adapter.DevicesAdapter;
import com.example.r2d2.medicalpatient.ui.base.BaseFragment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class BluetoothFragment extends BaseFragment implements BluetoothView{
    private DevicesAdapter bondedAdapter;
    private DevicesAdapter usableAdapter;
    private List<Map<String, String>> bondedList = new ArrayList<>();//已配对设备
    private List<Map<String, String>> usableList = new ArrayList<>();//可用设备
    private BluetoothDeviceReceiver receiver;
    private Animation rotate;
    private ProgressDialog progressDialog;

    private boolean isReceiverUnregistered = true;//标志receiver是否被解除

    @BindView(R.id.switch_btn)
    SwitchCompat switchBtn;
    @BindView(R.id.bluetooth_name)
    TextView bluetoothName;
    @BindView(R.id.scan_btn)
    AppCompatImageButton scanBtn;
    @BindView(R.id.bonded_devices_view)
    LinearLayout bondedDevicesView;
    @BindView(R.id.bonded_list_view)
    ListViewCompat bondedListView;
    @BindView(R.id.usable_list_view)
    ListViewCompat usableListView;

    @OnClick(R.id.switch_btn)
    void bluetoothSwitch(){
        boolean isChecked = switchBtn.isChecked();
        if (isChecked){
            //开启蓝牙
            bluetoothPresenter.enableBluetooth();
        } else {
            //隐藏搜索按钮
            hidScanBtn();
            //隐藏配对列表
            hideBondedDevice();
            //关闭蓝牙，清空设备
            bluetoothPresenter.disableBluetooth(bondedList, usableList);
        }
    }
    @OnClick(R.id.scan_btn)
    void scan(){
        //开始旋转动画
        scanBtn.startAnimation(rotate);
        //清空设备
        usableList.clear();
        bondedList.clear();
        notifyAdapter();
        //获取已配对设备
        bluetoothPresenter.getBondedList(bondedList);
        //开始搜索
        //如果已开始搜索，就取消搜索
        if (bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
        }
        bluetoothAdapter.startDiscovery();
        //启动计时器，10秒后停止搜索
        bluetoothPresenter.startTimer();
    }
    @OnClick(R.id.jump)
    void jump(){
        //对蓝牙状态做一些处理
        if (bluetoothAdapter!=null && bluetoothAdapter.isEnabled() && bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
        }
        //跳过设备连接，直接进入主界面
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
        getActivity().finish();
    }

    @Inject
    BluetoothAdapter bluetoothAdapter;
    @Inject
    BluetoothPresenter bluetoothPresenter;

    public BluetoothFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册广播接收者
        receiver = new BluetoothDeviceReceiver();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);//发现设备时
        getActivity().registerReceiver(receiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//搜索完成时
        getActivity().registerReceiver(receiver, filter);
        isReceiverUnregistered = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bluetooth, container, false);
        ButterKnife.bind(this, view);
        getFragmentComponent().inject(this);
        bluetoothPresenter.attachView(this);
        init();
        return view;
    }

    private void init() {
        //显示本机蓝牙名称
        bluetoothName.setText(bluetoothAdapter.getName());
        //蓝牙没打开
        if (bluetoothAdapter.isEnabled()){
            switchBtn.setChecked(true);
            scanBtn.setVisibility(View.VISIBLE);
        }else {
            switchBtn.setChecked(false);
            scanBtn.setVisibility(View.GONE);
        }
        //初始化adapter
        bondedAdapter = new DevicesAdapter(getContext(), bondedList, R.layout.device_item, new String[]{"name"}, new int[]{R.id.bluetooth_name});
        usableAdapter = new DevicesAdapter(getContext(), usableList, R.layout.device_item, new String[]{"name"}, new int[]{R.id.bluetooth_name});
        //为ListViewCompat设置适配器
        bondedListView.setAdapter(bondedAdapter);
        usableListView.setAdapter(usableAdapter);
        //设置监听器
        bondedListView.setOnItemClickListener(new DeviceClickListener(bondedAdapter));
        usableListView.setOnItemClickListener(new DeviceClickListener(usableAdapter));
        //旋转动画
        rotate = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anim);
        rotate.setInterpolator(new AccelerateDecelerateInterpolator());//减速效果
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSuccess() {
        //连接成功，下一步启动服务
        progressDialog.setTitle("连接成功");
        progressDialog.setMessage("正在启动服务");
    }

    @Override
    public void onError(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDialog() {
        progressDialog = ProgressDialog.show(getContext(), "连接设备", "正在连接", true, false);
    }

    @Override
    public void hideDialog() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void showScanBtn() {
        scanBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void hidScanBtn() {
        scanBtn.setVisibility(View.INVISIBLE);
        scanBtn.clearAnimation();
    }

    @Override
    public void startScan() {
        scanBtn.performClick();
    }

    @Override
    public void stopScan() {
        if (bluetoothAdapter.isDiscovering()){
            bluetoothAdapter.cancelDiscovery();
        }
        scanBtn.clearAnimation();
        if (!isReceiverUnregistered){
            getActivity().unregisterReceiver(receiver);
            isReceiverUnregistered = true;
        }
    }

    @Override
    public void showBondedDevice() {
        bondedDevicesView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBondedDevice() {
        bondedDevicesView.setVisibility(View.GONE);
    }

    @Override
    public void notifyAdapter() {
        bondedAdapter.notifyDataSetChanged();
        usableAdapter.notifyDataSetChanged();
    }

    @Override
    public void startService() {
        Intent intent = new Intent(getContext(), BluetoothService.class);
        getActivity().startService(intent);
        Toast.makeText(getContext(), "连接成功，服务启动", Toast.LENGTH_SHORT).show();
        hideDialog();
        //跳转到主界面,MainActivity启动服务
        startActivity(new Intent(getContext(), MainActivity.class));
        getActivity().finish();
    }

    /**
     * 广播接收者，处理扫描出的可用设备
     */
    public class BluetoothDeviceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_FOUND)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //未配对
                if (device.getBondState() != BluetoothDevice.BOND_BONDED){
                    Map<String, String> map = new HashMap<>();
                    map.put("name", device.getName());
                    map.put("address", device.getAddress());
                    usableList.add(map);
                    usableAdapter.notifyDataSetChanged();
                }
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
                //搜索完成
                stopScan();
            }
        }
    }

    /**
     * 给adapter设置item click listener
     */
    public class DeviceClickListener implements AdapterView.OnItemClickListener {
        private DevicesAdapter adapter;

        public DeviceClickListener(DevicesAdapter adapter){
            this.adapter = adapter;
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            //如果正在搜索，取消当前搜索
            if (bluetoothAdapter.isDiscovering()){
                bluetoothAdapter.cancelDiscovery();
            }
            Map<String, String> deviceInfo = adapter.getItem(i);
            String address = deviceInfo.get("address");
            bluetoothPresenter.connect(address);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (!isReceiverUnregistered) {
            getActivity().unregisterReceiver(receiver);
            isReceiverUnregistered = true;
        }
    }
}
