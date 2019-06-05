package com.hrst.common.protocol;

/**
 * 接收数据监听回调
 *
 * by Sophimp
 * on 2018/4/26
 */
public interface OnDataReceiverListener {
    /**
     * 数传mcu 解析后的数据内容回调
     * @param cmd 按协议解析出的命令字
     * @param property 协议属性字, 0x18: 数传mcu, 0x1A: 433模块
     * @param data 按协议解析出的内容
     */
    void onDataReceive(short cmd, short property, byte[]data);

}
