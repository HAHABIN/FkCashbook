package com.example.haha.fkcashbook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.haha.fkcashbook.ui.activity.MainActivity;

import static java.lang.Thread.sleep;
/**
 * Created by  on 2019-08-10
 * Github:
 * <p>
 * 启动页面
 */
public class LunchActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Thread( new Runnable( ) {
            @Override
            public void run() {
                //耗时任务，比如加载网络数据
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 这里可以睡几秒钟，如果要放广告的话
                        try {
                            sleep(1000);
                            Intent intent = MainActivity.newInstance(LunchActivity.this);
                            startActivity(intent);
                            LunchActivity.this.finish();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        } ).start();
    }

}
