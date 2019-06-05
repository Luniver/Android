package com.hrst.induce.sms;

import android.app.PendingIntent;
import android.os.Build;
import android.telephony.SmsManager;

import com.hrst.induce.interfaces.SmsSendInterface;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 短信发送管理类
 * Created by Lin on 2018/6/5.
 */

public class SmsSendManager implements SmsSendInterface {

    private static Object mSmsManager;
    private static Field mCardIdfield;

    private static boolean isCdmaCardSendSms = false;  //是否是电信卡诱发

    private static final SmsSendManager mInstance = new SmsSendManager();

    /**
     * 默认非电信卡诱发
     * @return
     */
    public static SmsSendManager getDefault(){
        isCdmaCardSendSms = false;
        return  mInstance;
    }

    public static SmsSendManager getDefault(boolean isCdmaSendSms){
        isCdmaCardSendSms = isCdmaSendSms;
        return mInstance;
    }

    /**
     * 初始化对象
     * @throws Exception
     */
    private void init() throws Exception{
        if (android.os.Build.VERSION.SDK_INT == 19) {
            mSmsManager = Class.forName("android.telephony.MSimSmsManager")
                    .getDeclaredMethod("getDefault").invoke(null);
            mCardIdfield = null;
        } else if (android.os.Build.VERSION.SDK_INT > 20) {
            if (android.os.Build.MODEL.contains("Mi") && !isCdmaCardSendSms){
                mSmsManager = Class.forName("miui.telephony.SmsManager")
                        .getDeclaredMethod("getDefault").invoke(null);
                mCardIdfield = mSmsManager.getClass().getDeclaredField("mSlotId");
                mCardIdfield.setAccessible(true);
            } else {
                mSmsManager = Class.forName("android.telephony.SmsManager")
                        .getDeclaredMethod("getDefault").invoke(null);
                mCardIdfield = mSmsManager.getClass().getDeclaredField("mSubId");
                mCardIdfield.setAccessible(true);
            }
        } else {
            mSmsManager = null;
            mSmsManager = null;
        }
    }

    @Override
    public void sendTextMessage(String dstAddress, String scAddress, String text,
                                PendingIntent sentIntent, PendingIntent deliveryIntent, int cardId) {
        try {
            init(); //初始化对象

            if (android.os.Build.VERSION.SDK_INT < 19
                    && android.os.Build.MODEL.equals("HUAWEI U9508")) {
                SmsManager.getDefault().sendTextMessage(dstAddress, scAddress, text,
                        sentIntent, deliveryIntent);
            }else {

                if(Build.VERSION.SDK_INT == 19){
                    Class[] params = { String.class, String.class, String.class,
                            PendingIntent.class,  PendingIntent.class, int.class };
                    Object[] invParams = { dstAddress, scAddress, text,
                            sentIntent, deliveryIntent, cardId };
                    Method method = mSmsManager.getClass()
                            .getDeclaredMethod("sendTextMessage", params);
                    method.invoke(mSmsManager, invParams);

                }else {
                    if(android.os.Build.VERSION.SDK_INT == 21){
                        mCardIdfield.setLong(mSmsManager, cardId);
                    }else if(android.os.Build.VERSION.SDK_INT >= 22){
                        if ("mSlotId".equals(mCardIdfield.getName())){
                            cardId = cardId == 0 ? 0 : 1;
                        }
                        mCardIdfield.setInt(mSmsManager, cardId);
                    }
                    Class[] params = { String.class, String.class, String.class,
                            PendingIntent.class, PendingIntent.class };
                    Object[] invParams = { dstAddress, scAddress, text,
                            sentIntent, deliveryIntent };
                    Method method = mSmsManager.getClass()
                            .getDeclaredMethod("sendTextMessage", params);
                    method.invoke(mSmsManager, invParams);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
