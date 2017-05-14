package com.dev.mevur.cloudwave;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class AlarmActivity extends AppCompatActivity {
    private ListView mAlarmList;
    private View mTips;
    private List<String> mAlarmData;
    private Toolbar mToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        init();
    }
    public void init() {
        mAlarmList = (ListView) findViewById(R.id.list_view);
        mTips = findViewById(R.id.tips);
        mAlarmData = new ArrayList<>();
        if (mAlarmData.size() == 0) {
            mAlarmList.setVisibility(View.GONE);
            mTips.setVisibility(View.VISIBLE);
        }
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        this.setSupportActionBar(mToolBar);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolBar.setTitle("预警数据");
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}
