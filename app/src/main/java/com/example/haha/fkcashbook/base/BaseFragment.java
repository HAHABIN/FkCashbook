package com.example.haha.fkcashbook.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.haha.fkcashbook.MyApplication;
import com.example.haha.fkcashbook.model.bean.LocalRepository;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by codeest on 16/8/11.
 * 无MVP的Fragment基类
 */

public abstract class BaseFragment extends Fragment {

    protected View mView;
    protected Activity mActivity;
    protected Context mContext;
    private Unbinder mUnBinder;

    @Override
    public void onAttach(Context context) {
        mActivity = (Activity) context;
        mContext = context;
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), null);
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnBinder = ButterKnife.bind(this, view);
        initEventAndData();
        initClick();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnBinder.unbind();
    }
    protected LocalRepository getLocalRepository(){
        return MyApplication.getLocalRepository();
    }
    protected void initData(Bundle savedInstanceState){
    }

    /**
     * 初始化点击事件
     */
    protected void initClick(){
    }

    /**
     * 逻辑使用区
     */
    protected void processLogic(){
    }

    /**
     * 初始化零件
     */
    protected void initWidget(Bundle savedInstanceState){
    }
    protected abstract int getLayoutId();
    protected abstract void initEventAndData();
}
