package com.hrst.induce.interfaces;

import com.hrst.induce.annotation.InduceType;

/**
 * 诱发回调
 * Created by Lin on 2018/6/4.
 */

public interface InduceCallBack {

    /**
     * @param type  诱发类型
     * 诱发开始
     */
    void onInduceStart(@InduceType int type);

    /**
     * 已发送
     * @param resutlCode 发送回执
     */
    void onSend(String resutlCode);

    /**
     * 对方已接收
     */
    void onDelivered();

    /**
     * 诱发消息回调
     * @param type  诱发类型
     * @param isSuccess 诱发是否成功
     * @param resutlCode    诱发回执code
     */
    void onInduceResult(@InduceType int type, boolean isSuccess, String resutlCode);

    /**
     * 诱发结束
     */
    void onInduceFinish();
}
