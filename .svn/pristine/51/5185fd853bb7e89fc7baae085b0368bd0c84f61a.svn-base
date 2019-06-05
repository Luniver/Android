package com.hrst.network.interfaces.impl;

import android.content.Context;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.hrst.network.api.CommunicateHelper;
import com.hrst.network.api.ICommunication;
import com.hrst.network.interfaces.WifiStateCallback;
import com.hrst.wifi.WifiStateReceiver;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static android.net.NetworkInfo.DetailedState.CONNECTED;

/**
 * wifi 通信实现
 * <p>
 * 当前业务不需要管理wifi连接, 断开, 配置, 状态监听, 依赖系统管理
 * 待后续有需求, 再扩展以上功能
 *
 *
 * Created by Sophimp on 2018/8/13.
 */

public abstract class WifiCommunicationImpl implements ICommunication {

    protected Executor receiveExecutor = Executors.newSingleThreadExecutor();
    protected Executor sendExecutor = Executors.newSingleThreadExecutor();

    protected boolean communicating = false;

    protected final WifiManager wifiManager;
    private final WifiStateReceiver wifiStateReceiver;

    private List<WifiStateCallback> callbackList = new LinkedList<>();

    public WifiCommunicationImpl(){
        // 获取WIFI管理器
        wifiManager = (WifiManager) CommunicateHelper.getCtx().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiStateReceiver = new WifiStateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        CommunicateHelper.getCtx().registerReceiver(wifiStateReceiver, filter);

        /**
         * wifi 状态监听
         */
        wifiStateReceiver.setWifiStateCallback(new WifiStateCallback(){

            @Override
            public void onWifiSwitch(boolean openOrClose) {
                if (!openOrClose){
                    communicating = false;
                }
                for (WifiStateCallback callback : callbackList){
                    callback.onWifiSwitch(openOrClose);
                }
            }

            @Override
            public void onWifiConnected(NetworkInfo networkInfo) {

                // 不管连的是不是目标wifi, 想要通信, 总归要连上目标wifi
                communicating = networkInfo.getDetailedState() == CONNECTED;
                // todo 后续优化: 增加识别目标wifi的属性, 连上目标wifi才可通信
                if (networkInfo.getDetailedState() == CONNECTED){
                    reconnect();
                }

                for (WifiStateCallback callback : callbackList){
                    callback.onWifiConnected(networkInfo);
                }
            }
        });
    }

    @Override
    public void open() {
    }

    @Override
    public void close() {

    }

    @Override
    public boolean isOpen() {
        return wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED;
    }

    @Override
    public boolean isCommunicating() {
        return communicating;
    }

    @Override
    public void addWifiStateCallback(WifiStateCallback callback) {
        callbackList.add(callback);
    }

    @Override
    public void removeWifiStateCallback(WifiStateCallback callback) {
        if (callbackList.contains(callback)){
            callbackList.remove(callback);
        }
    }

    /**
     * wifi重新连接
     */
    protected abstract void reconnect();

    /**
     * wifi 连接状态变化
     */
    protected abstract void onWifiConnectStateChanged(boolean connected);
}
