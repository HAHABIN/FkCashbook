package com.example.haha.fkcashbook.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.haha.fkcashbook.utils.ActivityManagerUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {
    protected static String TAG;

    protected Activity mContext;
    private Unbinder mUnBinder;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ActivityManagerUtil.mActivities.add(this);
        TAG = this.getClass().getSimpleName();
        mUnBinder = ButterKnife.bind(this);
        mContext = this;
        initEventAndData();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mUnBinder.unbind();
        ActivityManagerUtil.mActivities.remove(this);
    }

    @LayoutRes
    protected abstract int getLayout();

    /**
     * 初始化事件和加载数据
     */
    protected abstract void initEventAndData();




}
