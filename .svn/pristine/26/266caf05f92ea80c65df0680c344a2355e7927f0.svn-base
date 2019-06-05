package com.hrst.helper.blt;

import android.bluetooth.BluetoothDevice;

/**
 * com.hrst.helper.blt
 * by Sophimp
 * on 2018/4/27
 */

public interface BlueSocketListener {

    enum BlueSocketStatus {

        NONE,           //初始状态
        LISTENING,      //服务端正在监听被连接状态
        ACCEPTED,       //服务端接受到连接申请   或者客户端响应到服务端的连接
        CONNECTING,  //正在建立连接通道
        CONNECTED,   //已经连接
        DISCONNECT,  //断开连接
        DATA_RECEIVE  //消息达到
    }

    /**
     * 蓝牙socket连接状态该改变
     *
     * @param status       当前状态
     * @param remoteDevice 连接成功的时候,device 是代表连接的设备,其他状态remoteDevice 为null
     */
    void onBlueSocketStatusChange(BlueSocketStatus status, BluetoothDevice remoteDevice);
}
