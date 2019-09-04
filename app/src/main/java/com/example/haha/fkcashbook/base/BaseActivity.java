package com.example.haha.fkcashbook.base;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.haha.fkcashbook.MyApplication;
import com.example.haha.fkcashbook.R;
import com.example.haha.fkcashbook.model.bean.Kfaccount;
import com.example.haha.fkcashbook.model.bean.LocalRepository;
import com.example.haha.fkcashbook.model.bean.local.NoteBean;
import com.example.haha.fkcashbook.utils.ActivityManagerUtil;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {
    protected static String TAG;

    protected Activity mContext;
    private Unbinder mUnBinder;
    protected Toolbar mToolbar;
    private Kfaccount currentUser;
    //记住密码
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    boolean isRemember;

    private NoteBean mNoteBean;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        ActivityManagerUtil.mActivities.add(this);
        TAG = this.getClass().getSimpleName();
        mUnBinder = ButterKnife.bind(this);
        mContext = this;

        initData(savedInstanceState);
        initToolbar();
        initWidget();
        initEvent();
        initClick();

    }
    /**
     * 初始化数据
     *
     * @param savedInstanceState
     */
    protected void initData(Bundle savedInstanceState) {
    }

    /**
     * 初始化零件
     */
    protected void initWidget() {
    }

    /**
     * 初始化事件
     */
    protected void initEvent() {
    }

    /**
     * 初始化点击事件
     */
    protected void initClick() {
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
     * 配置Toolbar
     */
    protected void setUpToolbar(Toolbar toolbar) {
    }

    protected LocalRepository getLocalRepository(){
        return MyApplication.getLocalRepository();
    }
    protected NoteBean getNoteBean(){
        mNoteBean = getLocalRepository().getBillNote();
        return mNoteBean;
    }

    public Kfaccount setDrawerHeader(){
        currentUser = new Kfaccount();
        //检测是否登录
        pref = getSharedPreferences("User", MODE_PRIVATE);
        isRemember = pref.getBoolean("remember_password",false);
        if (isRemember) {
            //将账户和密码都设置到文本框中
            String username = pref.getString("username","");
            String password = pref.getString("password","");
            String kfmail = pref.getString("password","");
            currentUser.setUsername(username);
            currentUser.setPassword(password);
            currentUser.setKfmail(kfmail);
            return currentUser;
        }
        return null;
    }
    private void initToolbar() {
        mToolbar = findViewById(R.id.main_toolbar);
        if (mToolbar != null) {
            supportActionBar(mToolbar);
            setUpToolbar(mToolbar);
        }
    }
    /**************************used method*******************************************/

    protected ActionBar supportActionBar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
        mToolbar.setNavigationOnClickListener(
                (v) -> finish()
        );
        return actionBar;
    }

}
