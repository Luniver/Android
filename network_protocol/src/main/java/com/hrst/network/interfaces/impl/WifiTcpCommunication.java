package com.hrst.network.interfaces.impl;

import android.bluetooth.BluetoothDevice;

/**
 * tcp 通信
 *
 * 目前只实现, 数据的发送与回调监听
 * <p>
 * Created by Sophimp on 2018/8/22.
 */

public class WifiTcpCommunication extends WifiCommunicationImpl {

    @Override
    public void startCommunicate(BluetoothDevice device) {

    }

    @Override
    public void stopCommunicate(BluetoothDevice device) {

    }

    @Override
    public void sendData(short cmd, byte[] data) {

    }

    @Override
    public void destroy() {

    }

    @Override
    protected void reconnect() {

    }

    @Override
    protected void onWifiConnectStateChanged(boolean connected) {

    }
}
