package com.example.zxs.laundryinireland;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import info.hoang8f.android.segmented.SegmentedGroup;

public class ForecastActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private SegmentedGroup mSegmentedGroup;
    private RadioButton radioButtonOne;
    private RadioButton radioButtonTwo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);
        initWidget();
        initData();
        initEvent();
        Toolbar toolbar = findViewById(R.id.toolbar_fore);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void initEvent() {
        radioButtonOne.setChecked(true);//默认选择第一个（案例展示碎片）
    }

    private void initData() {
    }

    private void initWidget() {
        mSegmentedGroup = (SegmentedGroup) findViewById(R.id.mSegmentedGroup);
        radioButtonOne = (RadioButton) findViewById(R.id.radioButtonOne);
        radioButtonTwo = (RadioButton) findViewById(R.id.radioButtonTwo);

        mSegmentedGroup.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.radioButtonOne:
                FragmentManager fm = this.getSupportFragmentManager();
                FragmentTransaction tr = fm.beginTransaction();

                tr.replace(R.id.foundFrameLayout, new TodayFragment());
                tr.commit();
                break;
            case R.id.radioButtonTwo:
                FragmentManager fm2 = this.getSupportFragmentManager();
                FragmentTransaction tr2 = fm2.beginTransaction();
                tr2.replace(R.id.foundFrameLayout, new AfterFragment());
                tr2.commit();
                break;
        }
    }
}
