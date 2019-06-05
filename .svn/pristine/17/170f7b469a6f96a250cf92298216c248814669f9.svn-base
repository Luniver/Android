package com.hrst.induce;

import android.app.Application;

import com.hrst.induce.utils.LogUitl;

/**
 * 诱发初始化模块
 * Created by Lin on 2018/6/4.
 */

public class InduceClient {

    /**
     * 全局上下文
     */
    private static Application mApplication;

    /**
     * 初始化
     * @param application
     */
    public static void init(Application application){
        mApplication = application;
    }

    /**
     * 获取上下文
     * @return
     */
    public static Application client(){
        if (mApplication == null) throw new IllegalStateException("必须先调用InduceClient.init()");
        return mApplication;
    }

    /**
     * 日志打印设置：默认关闭
     * @param isDebug
     */
    public static void setIsDebug(boolean isDebug) {
        LogUitl.setIsDebug(isDebug);
    }
}
