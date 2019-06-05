package com.hrst.network.interfaces.impl;

import android.bluetooth.BluetoothDevice;
import android.net.wifi.WifiManager;

import com.hrst.network.api.CommunicateHelper;
import com.hrst.wifi.UDPSocket;
import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.net.SocketException;

/**
 * udp 通信
 * <p>
 * 目前只实现, 数据的发送与回调监听
 * <p>
 * Created by Sophimp on 2018/8/22.
 */

public class WifiUdpCommunication extends WifiCommunicationImpl {

    private UDPSocket client;

    private UDPSocket server;

    public WifiUdpCommunication() {
        super();
        if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_ENABLED){
            initClientAndServer();
        }
    }

    private void initClientAndServer() {
        if (client == null) {
            client = new UDPSocket(CommunicateHelper.getWifiClientHost(), CommunicateHelper.getWifiClientPort());
            client.asClient();
        }
        if (server == null) {
            server = new UDPSocket(CommunicateHelper.getWifiServerHost(), CommunicateHelper.getWifiServerPort());
            server.asServer();
        }
        communicating = true;
        startCommunicate(null);
    }

    @Override
    protected void reconnect() {
        Logger.d("wifi reconnect");
        stopCommunicate(null);
        initClientAndServer();
    }

    @Override
    protected void onWifiConnectStateChanged(boolean connected) {
        if (connected) {
            initClientAndServer();
        } else {
            stopCommunicate(null);
        }
    }

    @Override
    public void startCommunicate(BluetoothDevice device) {
        if (server != null) {
            receiveExecutor.execute(new ReceiveRunnable());
        }
    }

    @Override
    public void stopCommunicate(BluetoothDevice device) {
//        if (client != null) {
//            client.close();
//            client = null;
//        }
//
//        if (server != null) {
//            server.close();
//            server = null;
//        }
    }

    @Override
    public void sendData(short cmd, byte[] data) {
//        Logger.d("is communicating: " + isCommunicating());
        if (isCommunicating()) {
            sendExecutor.execute(() -> {
                byte[] datagrams = CommunicateHelper.getExtractClient().packetProtocolData(cmd, data);
                try {
                    if (client != null) {
                        client.send(datagrams);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public void destroy() {

    }

    class ReceiveRunnable implements Runnable {

        @Override
        public void run() {
            // 容器
            byte[] bytes = null;
            // 读取值
            while (communicating) {
                // 获取
                try {
                    bytes = server.receive();
                } catch (IOException e) {
                    communicating = false;
                    e.printStackTrace();
                }
                // 发送
                if (null != bytes && bytes.length > 0) {
                    CommunicateHelper.getExtractClient().fillRawData(bytes);
                }
            }
        }
    }
}
