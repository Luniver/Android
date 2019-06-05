package com.hrst.induce.interfaces;

/**
 * 短信诱发回调
 * Created by Lin on 2018/6/4.
 */

public interface SmsInduceCallBack {

    /**
     * 诱发开始：每条短信开始发送时回调
     */
    void onSmsStart();

    /**
     * 已发送
     * @param resutlCode 发送回执
     */
    void onSmsSend(String resutlCode);

    /**
     * 对方已接收
     */
    void onSmsDelivered();

    /**
     * 诱发消息回调
     * @param isSuccess 诱发是否成功
     * @param resutlCode    诱发回执code
     */
    void onSmsResult(boolean isSuccess, String resutlCode);
}
