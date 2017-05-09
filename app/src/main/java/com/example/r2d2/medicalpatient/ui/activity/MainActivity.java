package com.example.r2d2.medicalpatient.ui.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, DataUploadService.class));

        init();
    }

    private void init() {
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
                        break;
                    case R.id.message:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.mine:
                        viewPager.setCurrentItem(2);
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
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
