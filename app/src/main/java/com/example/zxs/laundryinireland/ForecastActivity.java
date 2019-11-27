package com.example.zxs.laundryinireland;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import info.hoang8f.android.segmented.SegmentedGroup;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ForecastActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private SegmentedGroup mSegmentedGroup;
    private RadioButton radioButtonOne;
    private RadioButton radioButtonTwo;
    private static final String url = "http://api.openweathermap.org/data/2.5/forecast?q=Dublin,ie&APPID=1f46aa0715e3f8c52ea3d1fd985ccf8a";

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
