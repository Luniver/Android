package com.hrst.network.interfaces;

/**
 *  接收数据, 按协议解析完回调
 *
 * Created by Sophimp on 2018/8/13.
 */
public interface DataReceiveCallback {
    void onDataReceived(short cmd, byte[] data);
}
