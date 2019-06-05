package com.hrst.network.protocol;

import android.support.annotation.NonNull;

import com.hrst.network.interfaces.DataReceiveCallback;

/**
 * 协议数据包封装解析接口
 *
 * create by Sophimp on 2018/8/13
 */
public interface IProtocol {

    /**
     * 填充原始数据
     * 传统socket读取
     */
    void fillRawData(byte rawData);

    /**
     * 填充原始数据
     * ble 的回调
     */
    void fillRawData(byte[] rawDatas);

    /**
     * 数据监听
     */
    void setDataReceiverListener(DataReceiveCallback listener);

    /**
     * 组装包
     * @param contentData 待组装内容
     */
    byte[] packetProtocolData(short cmd, byte[] contentData);

    /**
     * @return 协议头
     */
    @NonNull byte[] headers();

    /**
     * @return 协议属性字
     */
    byte property();

}
