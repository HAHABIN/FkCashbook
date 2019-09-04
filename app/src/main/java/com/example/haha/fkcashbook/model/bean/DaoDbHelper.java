package com.example.haha.fkcashbook.model.bean;

import android.database.sqlite.SQLiteDatabase;

import com.example.haha.fkcashbook.MyApplication;
import com.example.haha.fkcashbook.model.gen.DaoMaster;
import com.example.haha.fkcashbook.model.gen.DaoSession;

public class DaoDbHelper {
    private static final String DB_NAME = "KfCashbook";

    private static volatile DaoDbHelper sInstance;
    private SQLiteDatabase mDb;
    private DaoMaster mDaoMaster;
    private DaoSession mDaoSession;

    private DaoDbHelper(){
        //封装数据库的创建、更新、删除
        DaoMaster.DevOpenHelper openHelper = new DaoMaster.DevOpenHelper(MyApplication.getContext(),DB_NAME,null);
        //获取数据库
        mDb = openHelper.getWritableDatabase();
        //封装数据库中表的创建、更新、删除
        mDaoMaster = new DaoMaster(mDb);  //合起来就是对数据库的操作
        //对表操作的对象。
        mDaoSession = mDaoMaster.newSession(); //可以认为是对数据的操作
    }


    public static DaoDbHelper getInstance(){
        if (sInstance == null){
            synchronized (DaoDbHelper.class){
                if (sInstance == null){
                    sInstance = new DaoDbHelper();
                }
            }
        }
        return sInstance;
    }

    public DaoSession getSession(){
        return mDaoSession;
    }

    public SQLiteDatabase getDatabase(){
        return mDb;
    }

    public DaoSession getNewSession(){
        return mDaoMaster.newSession();
    }

    /**
     * 关闭数据库
     */
    public void closeDataBase() {
        closeHelper();
        closeDaoSession();
    }
    public void closeDaoSession() {
        if (null != mDaoSession) {
            mDaoSession.clear();
            mDaoSession = null;
        }
    }

    public void closeHelper() {
        if (sInstance != null) {
            sInstance.closeHelper();
            sInstance = null;
        }
    }
}
