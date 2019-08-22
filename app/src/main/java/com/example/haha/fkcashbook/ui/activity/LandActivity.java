package com.example.haha.fkcashbook.ui.activity;


import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haha.fkcashbook.R;
import com.example.haha.fkcashbook.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * Created by  on 2019-01-10
 * Github:
 * <p>
 * 用户登录、注册activity
 */
public class LandActivity extends BaseActivity {
    @BindView(R.id.login_et_phone)
    EditText loginEtPhone;
    @BindView(R.id.login_et_username)
    EditText loginEtUsername;
    @BindView(R.id.login_et_password)
    EditText loginEtPassword;
    @BindView(R.id.login_et_rpassword)
    EditText loginEtRpassword;
    @BindView(R.id.login_tv_forget)
    TextView loginTvForget;
    @BindView(R.id.login_btn_login)
    Button loginBtnLogin;
    @BindView(R.id.login_tv_sign)
    TextView loginTvSign;
    @BindView(R.id.land_tv_text)
    TextView landTVText;


    //是否是登陆操作
    private boolean isLogin = true;

    @Override
    protected int getLayout() {
        return R.layout.activity_user_land;
    }

    @Override
    protected void initEventAndData() {

    }


    @OnClick({R.id.login_tv_forget, R.id.login_btn_login, R.id.login_tv_sign, R.id.land_tv_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_btn_login:
                if (isLogin) {
                    login();  //登陆
                } else {
                    sign();  //注册
                }
                break;
            case R.id.login_tv_forget:

                break;

            case R.id.login_tv_sign:
                if (isLogin) {
                    //置换注册界面
                    loginTvSign.setText("登录");
                    loginBtnLogin.setText("注册");
                    loginEtRpassword.setVisibility(View.VISIBLE);
                    loginEtPhone.setVisibility(View.VISIBLE);
                    landTVText.setText("注册");
                } else {
                    //置换登陆界面
                    loginTvSign.setText("注册");
                    loginBtnLogin.setText("登录");
                    loginEtRpassword.setVisibility(View.GONE);
                    loginEtPhone.setVisibility(View.GONE);
                    landTVText.setText("登录");
                }
                isLogin = !isLogin;
                break;
        }
    }

    /**
     * 执行登陆动作
     */
    public void login() {
        String username = loginEtUsername.getText().toString().trim();
        String password = loginEtPassword.getText().toString().trim();
        if (username.length() == 0 || password.length() == 0) {
            Toast.makeText(mContext, "账户密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }

//        ProgressUtils.show(this, "正在登陆...");

//        mPresenter.login(username, password);
    }

    /**
     * 执行注册动作
     */
    public void sign() {
        String phone = loginEtPhone.getText().toString().trim();
        String username = loginEtUsername.getText().toString().trim();
        String password = loginEtPassword.getText().toString().trim();
        String rpassword = loginEtRpassword.getText().toString().trim();
        if (phone.length() == 0 || username.length() == 0 || password.length() == 0 || rpassword.length() == 0) {
//            SnackbarUtils.show(mContext, "请填写必要信息");
            Toast.makeText(mContext, "请填写必要信息", Toast.LENGTH_SHORT).show();
            return;
        }
//        if (!StringUtils.checkEmail(phone)) {
//            SnackbarUtils.show(mContext, "请输入正确的邮箱格式");
//            return;
//        }
        if (!password.equals(rpassword)) {
//            SnackbarUtils.show(mContext, "两次密码不一致");
            Toast.makeText(mContext, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }

//        ProgressUtils.show(this, "正在注册...");

//        mPresenter.signup(username,password,email);

    }

}
