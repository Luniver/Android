package com.hrst.common.base;

import android.app.Application;
import android.content.Context;

import com.hrst.common.util.SharedPreferencesUtils;
import com.hrst.common.util.ToastUtils;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.Logger;

/**
 *  初始化公用工具类, 日志类
 *
 * Created by Sophimp on 2018/3/30.
 */
public abstract class BaseApp extends Application {

    private static BaseApp instance;
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        // ToastUitl 初始化
        ToastUtils.init(this);

        // SharedPreference初始化
        SharedPreferencesUtils.init(this);

        // Logger  初始化
        Logger.addLogAdapter(new AndroidLogAdapter());
        Logger.addLogAdapter(new DiskLogAdapter());

        // 初始化工作
        createInit();
    }

    public abstract void createInit();

    /**
     * 获取全局Context
     */
    public static Context getCtx(){
        return instance.getApplicationContext();
    }
}
