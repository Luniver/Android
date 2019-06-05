package com.hrst.induce.interfaces;

import android.app.PendingIntent;

/**
 * 短信发送接口
 * Created by Lin on 2018/6/5.
 */

public interface SmsSendInterface {

    /**
     * @param dstAddress 目标号码
     * @param scAddress 本机号码
     * @param text 内容
     * @param sentIntent 发送回执处理器
     * @param deliveryIntent 接收回执处理器
     * @param cardId 双卡ID
     */
    void sendTextMessage(String dstAddress, String scAddress,
                                String text, PendingIntent sentIntent,
                                PendingIntent deliveryIntent, int cardId);
}
