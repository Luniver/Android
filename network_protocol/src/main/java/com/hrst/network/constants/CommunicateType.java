package com.hrst.network.constants;

/**
 * 通信方式
 *
 * Created by Sophimp on 2018/8/13.
 */

public enum CommunicateType {

    /**
     * 低功耗蓝牙, 必须要先配对模式
     */
    BLE_BOND,

    /**
     * 低功耗蓝牙, 不需要配对, 直接连接
     */
    BLE_CONNECT,

    /**
     * 经典蓝牙
     */
    BLT,

    /**
     * wifi通信
     */
    WIFI_UDP

}
