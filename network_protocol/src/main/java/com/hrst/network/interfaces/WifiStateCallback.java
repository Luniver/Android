package com.hrst.network.interfaces;

import android.net.NetworkInfo;

/**
 * wifi连接状态监听
 * Created by Sophimp on 2018/8/13.
 */
public interface WifiStateCallback {

    /**
     * wifi 开关状态监听
     */
    void onWifiSwitch(boolean openOrClose);

    /**
     * wifi 连接状态监听
     */
    void onWifiConnected(NetworkInfo networkInfo);
}
