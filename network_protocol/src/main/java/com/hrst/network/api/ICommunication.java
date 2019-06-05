package com.hrst.network.api;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import com.hrst.network.interfaces.BluetoothStateCallback;
import com.hrst.network.interfaces.DataReceiveCallback;
import com.hrst.network.interfaces.WifiStateCallback;
import com.inuker.bluetooth.library.search.response.SearchResponse;

/**
 * 通信交互接口
 *
 * Created by Sophimp on 2018/8/13.
 */

public interface ICommunication {

    /**
     * 打开通信开关( 蓝牙或wifi)
     */
    void open();

    /**
     * 关闭通信开关
     */
    void close();

    /**
     * 蓝牙扫描
     * @param  response 扫描设备回调
     */
    default void startScan(@NonNull SearchResponse response) {
    }

    /**
     * 停止扫描
     */
    default void stopScan() {
    }

    /**
     * 开始通信
     * @param  device 蓝牙设备信息, wifi通信可填null
     */
    void startCommunicate(BluetoothDevice device);

    /**
     * 停止通信
     * @param  device 蓝牙设备信息, wifi通信可填null
     */
    void stopCommunicate(BluetoothDevice device);

    /**
     * 发送数据
     * @param  cmd 命令字
     * @param  data 发送内容, 不带协议封闭的原数据
     */
    void sendData(short cmd, byte[] data);

    /**
     * 蓝牙或wifi 开关是否打开
     */
    boolean isOpen();

    /**
     * 蓝牙或wifi 是否可通信
     */
    boolean isCommunicating();

    /**
     * 添加数据监听
     */
    default void addDataReceiveListener(DataReceiveCallback callback){
        // 对外接口依旧是ICommunication
        CommunicateHelper.addDataReceivedListener(callback);
    }

    /**
     * 移除数据监听
     */
    default void removeDataReceiveListener(DataReceiveCallback callback){
        CommunicateHelper.removeDataReceivedListener(callback);
    }

    /**
     * 添加蓝牙状态监听
     */
    default void addBluetoothStateCallback(@NonNull BluetoothStateCallback callback) {
    }

    /**
     * 移除蓝牙状态监听
     */
    default void removeBluetoothStateCallback(@NonNull BluetoothStateCallback callback) {
    }

    /**
     * 添加wifi状态监听
     */
    default void addWifiStateCallback(@NonNull WifiStateCallback callback) {
    }

    /**
     * 移除wifi状态监听
     */
    default void removeWifiStateCallback(@NonNull WifiStateCallback callback) {
    }

    /**
     * 主动销毁
     */
    void destroy();
}
