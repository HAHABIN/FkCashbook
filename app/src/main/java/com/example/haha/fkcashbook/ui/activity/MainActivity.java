package com.example.haha.fkcashbook.ui.activity;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haha.fkcashbook.R;
import com.example.haha.fkcashbook.base.BaseActivity;
import com.example.haha.fkcashbook.ui.LunchActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.text_demo)
    TextView textDemo;
    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.main_drawer_ly)
    DrawerLayout mainDrawerLy;


    //启动页面跳转
    public static Intent newInstance(LunchActivity lunchActivity) {
        return new Intent(lunchActivity, MainActivity.class);
    }


    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {
        //将Toolbal实例传入
        setSupportActionBar(mainToolbar);
        if (mainToolbar != null) {
            supportActionBar(mainToolbar);
        }

    }


    @OnClick(R.id.text_demo)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.text_demo:
                Toast.makeText(mContext, "demo", Toast.LENGTH_SHORT).show();
                break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.toolbar_date:
                Toast.makeText(mContext, "日历", Toast.LENGTH_SHORT).show();
                //时间选择器
//                new TimePickerBuilder(mContext, (Date date, View v) -> {
//                    monthListFragment.changeDate(DateUtils.date2Str(date, "yyyy"), DateUtils.date2Str(date, "MM"));
//                    monthChartFragment.changeDate(DateUtils.date2Str(date, "yyyy"), DateUtils.date2Str(date, "MM"));
//                }).setType(new boolean[]{true, true, false, false, false, false})
//                        .setRangDate(null, Calendar.getInstance())
//                        .isDialog(true)//是否显示为对话框样式
//                        .build().show();
                break;
            case android.R.id.home:
                mainDrawerLy.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Toobar配置
    protected ActionBar supportActionBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_drag);
        }
//        mainToolbar.setNavigationOnClickListener(
//                (v) -> finish()
//        );
        return actionBar;
    }

}
