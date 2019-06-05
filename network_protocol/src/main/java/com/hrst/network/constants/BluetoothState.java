package com.hrst.network.constants;

/**
 * 蓝牙相关状态
 * Created by Sophimp on 2018/8/16.
 */

public enum BluetoothState {
    /**  系统蓝牙的打开或关闭 */
    CLOSED,
    OPENED,

    /** 蓝牙的配对对状态 */
    BONDING,
    BONDED,
    UNBOND,

    /** 蓝牙的连接状态 */
    CONNECTING,
    CONNECTED,
    DISCONNECTING,
    DISCONNECTED
}
