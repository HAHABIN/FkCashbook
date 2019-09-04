package com.example.haha.fkcashbook.ui.activity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haha.fkcashbook.R;
import com.example.haha.fkcashbook.base.BaseActivity;
import com.example.haha.fkcashbook.model.bean.Kfaccount;
import com.example.haha.fkcashbook.common.Constants;
import com.example.haha.fkcashbook.utils.HttpUtils;
import com.example.haha.fkcashbook.utils.IntentUtils;
import com.example.haha.fkcashbook.utils.StringUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by  on 2019-01-10
 * Github:
 * <p>
 * 用户登录、注册activity
 */
public class LandActivity extends BaseActivity {
    @BindView(R.id.login_et_mail)
    EditText loginEtMail;
    @BindView(R.id.login_et_username)
    EditText loginEtUsername;
    @BindView(R.id.login_et_password)
    EditText loginEtPassword;
    @BindView(R.id.login_et_rpassword)
    EditText loginEtRpassword;
    @BindView(R.id.login_btn_login)
    Button loginBtnLogin;
    @BindView(R.id.login_tv_sign)
    TextView loginTvSign;
    @BindView(R.id.land_tv_text)
    TextView landTVText;
    @BindView(R.id.login_tv_rempas)
    CheckBox loginTvRempas;


    //记住密码
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    boolean isRemember;

    private Kfaccount kfaccount = new Kfaccount();
    //是否是登陆操作
    private boolean isLogin = true;

    @Override
    protected int getLayout() {
        return R.layout.activity_user_land;
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        //检测是否记住密码
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        isRemember = pref.getBoolean("remember_password",false);
        if (isRemember) {
            //将账户和密码都设置到文本框中
            String username = pref.getString("username","");
            String password = pref.getString("password","");
            loginEtUsername.setText(username);
            loginEtPassword.setText(password);
            loginTvRempas.setChecked(true);
        }
    }

    @Override
    protected void initWidget() {
        super.initWidget();

    }

    @OnClick({R.id.login_tv_rempas, R.id.login_btn_login, R.id.login_tv_sign, R.id.land_tv_text})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.login_btn_login:
                if (isLogin) {
                    login();  //登陆
                } else {
                    sign();  //注册
                }
                break;
            case R.id.login_tv_rempas:
                break;
            case R.id.login_tv_sign:
                changeWindows();
                break;
        }
    }

    /**
     * 置换登录注册窗口
     */
    private void changeWindows() {
        if (isLogin) {
            //置换注册界面
            loginTvSign.setText("登录");
            loginBtnLogin.setText("注册");
            loginEtRpassword.setVisibility(View.VISIBLE);
            loginEtMail.setVisibility(View.VISIBLE);
            landTVText.setText("注册");
            loginTvRempas.setVisibility(View.GONE);
        } else {
            //置换登陆界面
            loginTvSign.setText("注册");
            loginBtnLogin.setText("登录");
            loginEtRpassword.setVisibility(View.GONE);
            loginEtMail.setVisibility(View.GONE);
            landTVText.setText("登录");
            loginTvRempas.setVisibility(View.VISIBLE);
        }
        isLogin = !isLogin;
    }

    /**
     * 执行登陆动作
     */
    public void login() {
        String loginAddress = Constants.USER_LOGIN;
        String username = loginEtUsername.getText().toString().trim();
        String password = loginEtPassword.getText().toString().trim();
        String kfmail =loginEtMail.getText().toString().trim();
        if (username.length() == 0 || password.length() == 0) {
            Toast.makeText(mContext, "账户密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //检测是否记住密码
        pref = getSharedPreferences("User", MODE_PRIVATE);

        //执行登录
        loginWithOkHttp(loginAddress, username,password);

    }

    /**
     * 执行注册动作
     */
    public void sign() {

        String registerAddress = Constants.USER_SIGN;
        String mail = loginEtMail.getText().toString().trim();
        String username = loginEtUsername.getText().toString().trim();
        String password = loginEtPassword.getText().toString().trim();
        String rpassword = loginEtRpassword.getText().toString().trim();
        if (mail.length() == 0 || username.length() == 0 || password.length() == 0 || rpassword.length() == 0) {
            Toast.makeText(mContext, "请填写必要信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtils.checkEmail(mail)) {
            Toast.makeText(mContext, "请输入正确的邮箱格式", Toast.LENGTH_SHORT).show();
        }
        if (!password.equals(rpassword)) {
            Toast.makeText(mContext, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return;
        }
        registerWithOkHttp(registerAddress, username, password, mail);

    }

    //实现登录
    public void loginWithOkHttp(String address,String username,String password ) {
        HttpUtils.loginWithOkHttp(address, username,password, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                //在这里对异常情况进行处理
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //得到服务器返回的具体内容
                final String responseData = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseData.equals("true")) {
                            Toast.makeText(mContext, "登录成功", Toast.LENGTH_SHORT).show();
                            IntentUtils.SetIntent(mContext, MainActivity.class);
                            editor = pref.edit();
                            if (loginTvRempas.isChecked()) {
                                editor.putBoolean("remember_password",true);
                                editor.putString("username",username);
                                editor.putString("password",password);
                            } else {
                                editor.clear();
                            }
                            editor.apply();
                            mContext.finish();
                        } else {
                            Toast.makeText(mContext, "登录失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    //实现注册
    public void registerWithOkHttp(String address, String username, String password, String kfmail) {
        HttpUtils.registerWithOkHttp(address, username, password, kfmail, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final String responseData = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (responseData.equals("true")) {
                            Toast.makeText(mContext, "注册成功", Toast.LENGTH_SHORT).show();
                            isLogin = false;
                            changeWindows();
                        } else {
                            Toast.makeText(mContext, "注册失败,用户名已存在", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }

}
