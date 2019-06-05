
package com.hrst.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.hrst.network.interfaces.WifiStateCallback;
import com.orhanobut.logger.Logger;

/**
 * wifi连接状态广播监听
 *
 * create by Sophimp on 2018/8/22
 */
public class WifiStateReceiver extends BroadcastReceiver {

    private WifiStateCallback wifiStateCallback;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TextUtils.isEmpty(action))
            return;

        switch (action){
            case WifiManager.RSSI_CHANGED_ACTION:
                break;
            case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                networkStateChangedAction(context, intent);
                break;
            case WifiManager.WIFI_STATE_CHANGED_ACTION:
                wifiStateChangedAction(intent);
                break;
        }
    }

    /**
     * 网络状态变更操作
     *
     */
    private void networkStateChangedAction(Context context, Intent intent) {
        NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
        if (null != wifiStateCallback){
            wifiStateCallback.onWifiConnected(info);
        }
    }

    /**
     * wifi状态改变时操作
     * 1. wifi断开
     * 2. wifi连接
     * 3. wifi 连接状态下直接切换
     */
    private void wifiStateChangedAction(Intent intent) {
        int wifistate = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_DISABLED);
        Logger.d("wifi state: " + wifistate);
        if (wifiStateCallback != null){
            wifiStateCallback.onWifiSwitch(wifistate == WifiManager.WIFI_STATE_ENABLED);
        }
    }

    public void setWifiStateCallback(WifiStateCallback wifiStateCallback) {
        this.wifiStateCallback = wifiStateCallback;
    }
}
