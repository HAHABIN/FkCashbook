package com.example.haha.fkcashbook;

import android.app.Application;
import android.content.Context;

import com.example.haha.fkcashbook.model.bean.LocalRepository;

/**
 * Created by Administrator on 2019/8/23 0023.
 */

public class MyApplication extends Application {

    public static MyApplication instances;
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
        context = getApplicationContext();

    }
    public static MyApplication getInstances(){
        return instances;
    }


    /**
     * 获取上下文
     *
     * @return
     */
    public static Context getContext() {
        return context;
    }

    public static LocalRepository getLocalRepository(){
        return LocalRepository.getInstance();
    }


}
