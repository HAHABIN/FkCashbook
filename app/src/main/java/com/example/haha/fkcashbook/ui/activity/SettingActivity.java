package com.example.haha.fkcashbook.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.haha.fkcashbook.R;
import com.example.haha.fkcashbook.base.BaseActivity;

import butterknife.BindView;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;

    @Override
    protected int getLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        //初始化Toolbar
        mainToolbar.setTitle("设置");
        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mainToolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    protected void initClick() {
        super.initClick();
    }
}
