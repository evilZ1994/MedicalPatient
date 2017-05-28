package com.example.r2d2.medicalpatient.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.r2d2.medicalpatient.R;
import com.example.r2d2.medicalpatient.api.ApiService;
import com.example.r2d2.medicalpatient.app.App;
import com.example.r2d2.medicalpatient.service.DataUploadService;
import com.example.r2d2.medicalpatient.ui.adapter.MyFragmentPagerAdapter;
import com.example.r2d2.medicalpatient.ui.fragment.main.DataFragment;
import com.example.r2d2.medicalpatient.ui.fragment.main.MessageFragment;
import com.example.r2d2.medicalpatient.ui.fragment.main.MineFragment;
import com.example.r2d2.medicalpatient.ui.fragment.main.NewsFragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.event.NotificationClickEvent;
import dagger.Provides;

public class MainActivity extends AppCompatActivity {
    private DataFragment dataFragment;
    private MessageFragment messageFragment;
    //private NewsFragment newsFragment;
    private MineFragment mineFragment;

    private List<Fragment> fragmentList;
    private MyFragmentPagerAdapter myFragmentPagerAdapter;
    private MenuItem menuItem;

    private ViewPager viewPager;
    private BottomNavigationView bottomNavigation;
    private InputMethodManager inputMethodManager; //软键盘

    //点击返回键时的时间，用于控制双击退出
    private long mPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, DataUploadService.class));

        init();
        //注册通知栏点击事件
        JMessageClient.registerEventReceiver(new MyNotificationClickEvent());
    }

    private void init() {
        //软键盘
        inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        viewPager = (ViewPager)findViewById(R.id.view_pager);
        bottomNavigation = (BottomNavigationView)findViewById(R.id.bottom_navigation);

        dataFragment = new DataFragment();
        messageFragment = new MessageFragment();
        //newsFragment = new NewsFragment();
        mineFragment = new MineFragment();

        fragmentList = new ArrayList<>();
        fragmentList.add(dataFragment);
        fragmentList.add(messageFragment);
        //fragmentList.add(newsFragment);
        fragmentList.add(mineFragment);
        myFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(myFragmentPagerAdapter);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.data:
                        viewPager.setCurrentItem(0);
                        bottomNavigation.setVisibility(View.VISIBLE);
                        break;
                    case R.id.message:
                        viewPager.setCurrentItem(1);
                        bottomNavigation.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.mine:
                        viewPager.setCurrentItem(2);
                        bottomNavigation.setVisibility(View.VISIBLE);
                        break;
                }
                return false;
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null){
                    menuItem.setChecked(false);
                } else {
                    menuItem = bottomNavigation.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigation.getMenu().getItem(position);
                menuItem.setChecked(true);
                if (position == 1){
                    bottomNavigation.setVisibility(View.INVISIBLE);
                }else {
                    //获取输入法打开状态
                    boolean isOpen = inputMethodManager.isActive();
                    //如果软键盘已经显示，则隐藏
                    if (isOpen) {
                        inputMethodManager.hideSoftInputFromWindow(messageFragment.getChatInputView().getInputView().getWindowToken(), 0);
                    }
                    bottomNavigation.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 通知栏点击事件，跳转到聊天窗口
     */
    public class MyNotificationClickEvent{
        public void onEvent(NotificationClickEvent event){
            viewPager.setCurrentItem(1);
        }
    }

    /**
     * 重写onBackPressed()方法，设置双击退出，时间差500毫秒
     */
    @Override
    public void onBackPressed() {
        long mNowTime = System.currentTimeMillis();
        if (mNowTime - mPressedTime > 500){
            Toast.makeText(this, "再次点击退出", Toast.LENGTH_SHORT).show();
            mPressedTime = mNowTime;
        } else {
            //退出程序
            //JMessage登出
            JMessageClient.logout();
            //结束程序
            this.finish();
            System.exit(0);
        }
    }
}
