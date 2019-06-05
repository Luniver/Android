package com.hrst.common.protocol;

/**
 * 数据包校验协议
 * Created by Sophimp on 2018/4/25.
 */
public interface IPacket {

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
    void setDataReceiverListener(OnDataReceiverListener listener);
}
