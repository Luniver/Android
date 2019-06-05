package com.hrst.induce;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.SmsManager;

import com.hrst.induce.bean.InduceResult;
import com.hrst.induce.interfaces.InduceInterface;
import com.hrst.induce.sms.SmsSendManager;
import com.hrst.induce.utils.InduceConstants;
import com.hrst.induce.utils.LogUitl;

import java.util.List;

/**
 * 短信诱发
 * Created by Lin on 2018/6/4.
 */

public class HrstSmsManager implements InduceInterface {

    private static final int STATE_SMS_START = 0x01;
    private static final int STATE_SMS_SENT = 0x02;
    private static final int STATE_SMS_DELIVERED = 0x03;
    private static final int STATE_SMS_END = 0x04;

    private final static String SMS_SEND_ACTION = "SMS_SEND_ACTION";
    private final static String SMS_DELIVERED_ACTION = "SMS_DELIVERED_ACTION";

    private PendingIntent mSendIntent;
    private PendingIntent mDeliverIntent;
    private BroadcastReceiver mSentBroadcast;
    private BroadcastReceiver mDeliveredBroadcast;

    //是否正在诱发
    private volatile boolean isInducing = false;

    private String resultCode = "0";

    private static HrstSmsManager mInstance = new HrstSmsManager();

    /**
     * 当前诱发任务
     */
    private InduceTask curInduceTask;

    protected static HrstSmsManager get(){
        if(mInstance == null){
            mInstance = new HrstSmsManager();
        }
        return mInstance;
    }

    /**
     * 主线程回调
     */
    private Handler mMainHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            if(curInduceTask.getSmsInduceCallBack() == null){
                return;
            }
            switch (msg.what){
                case STATE_SMS_START:
                    curInduceTask.getSmsInduceCallBack().onSmsStart();
                    break;
                case STATE_SMS_SENT:
                    InduceResult sendResult = (InduceResult) msg.obj;
                    if(sendResult != null){
                        curInduceTask.getSmsInduceCallBack().onSmsSend(sendResult.code);
                    }
                    break;
                case STATE_SMS_DELIVERED:
                    curInduceTask.getSmsInduceCallBack().onSmsDelivered();
                    break;
                case STATE_SMS_END:
                    mMainHandler.removeCallbacks(timeOutRunnable);
                    InduceResult induceResult = (InduceResult) msg.obj;
                    if(induceResult != null){
                        curInduceTask.getSmsInduceCallBack().onSmsResult(induceResult.isSuccess, induceResult.code);
                    }
                    finish();  //结束诱发
                    break;
            }
        }
    };

    protected HrstSmsManager(){
        initPendingIntent();
    }

    @Override
    public boolean isInducing() {
        return isInducing;
    }

    private void initPendingIntent(){
        Intent sendIntent = new Intent(SMS_SEND_ACTION);
        mSendIntent = PendingIntent.getBroadcast(InduceClient.client(), 3, sendIntent, PendingIntent.FLAG_IMMUTABLE);
        Intent deliverIntent = new Intent(SMS_DELIVERED_ACTION);
        mDeliverIntent = PendingIntent.getBroadcast(InduceClient.client(),4, deliverIntent, PendingIntent.FLAG_IMMUTABLE);

        mSentBroadcast = new SentBroadcastReceiver();
        mDeliveredBroadcast = new DeliveredBroadcastReceiver();

        // 默认肯定是要注册的
        register();
    }

    /**
     * 诱发注册
     */
    private void register(){
        InduceClient.client().registerReceiver(mSentBroadcast, new IntentFilter(SMS_SEND_ACTION));
        InduceClient.client().registerReceiver(mDeliveredBroadcast, new IntentFilter(SMS_DELIVERED_ACTION));
    }

    /**
     * 诱发结束取消注册
     */
    protected void unRegister(){
        if(mSentBroadcast != null){
            InduceClient.client().unregisterReceiver(mSentBroadcast);
        }
        if(mDeliveredBroadcast != null){
            InduceClient.client().unregisterReceiver(mDeliveredBroadcast);
        }
    }

    @Override
    public void startInduce(InduceTask induceTask) {
        curInduceTask = induceTask;
        isInducing = true;
        List<String> messages = curInduceTask.getSmsMessage().getSafeContents();
        if(messages == null || messages.size() == 0){
            //短信内容为空，结束诱发
            sendResult(STATE_SMS_END, InduceResult.createSmsResult(true, InduceConstants.SMS_SENDDING_ERROR_GENERIC_FAILURE));
            return;
        }
        for(String message : messages){
            //循环发送短信
            LogUitl.i("sms induce message:" + message);
            SmsSendManager.getDefault(curInduceTask.getSmsMessage().isCdmaSendSms())
                    .sendTextMessage(curInduceTask.getNumber(),
                            "", message,
                            mSendIntent,
                            mDeliverIntent,
                            curInduceTask.getCardId());
        }
        sendResult(STATE_SMS_START, null);  //发送开始诱发回调
        mMainHandler.postDelayed(timeOutRunnable, curInduceTask.getTimeout());    //开启计时线程
    }

    @Override
    public void stopInduce() {
        finish();
    }

    /**
     * 超时线程
     */
    private Runnable timeOutRunnable = new Runnable() {
        @Override
        public void run() {
            //超时
            LogUitl.i("sms send timeout");
            if(InduceConstants.SMS_SENDDING_OK.equals(resultCode) && curInduceTask.getSmsMessage().isSwitchDetection()){
                //开关机检测，等待超时时间反馈发送失败
                sendResult(STATE_SMS_END, InduceResult.createSmsResult(false, resultCode));
            }else{
                //超时
                sendResult(STATE_SMS_END, InduceResult.createSmsResult(false, InduceConstants.SMS_SENDDING_TIME_OUT));
            }

        }
    };

    /**
     * 结束诱发
     */
    private void finish(){
        isInducing = false;
        mMainHandler.removeCallbacks(timeOutRunnable);
    }

    private void sendResult(int state, InduceResult result){
        Message message = mMainHandler.obtainMessage(state);
        message.obj = result;
        mMainHandler.sendMessage(message);
    }

    class SentBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUitl.i("sms send result code is " + this.getResultCode());
            if(this.getResultCode() == Activity.RESULT_OK){
                // 短信发送成功
                resultCode = InduceConstants.SMS_SENDDING_OK;
                sendResult(STATE_SMS_SENT, InduceResult.createSmsResult(true, resultCode));
                if(!curInduceTask.getSmsMessage().hasReceive()){
                    sendResult(STATE_SMS_END, InduceResult.createSmsResult(true, resultCode));
                }
                return;
            }
            /** 发送失败处理 */
            switch (this.getResultCode()) {
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    resultCode = InduceConstants.SMS_SENDDING_ERROR_GENERIC_FAILURE;
                    break;
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    resultCode = InduceConstants.SMS_SENDDING_ERROR_RADIO_OFF;
                    break;
                case SmsManager.RESULT_ERROR_NULL_PDU:
                    resultCode = InduceConstants.SMS_SENDDING_ERROR_NULL_PDU;
                    break;
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                    resultCode = InduceConstants.SMS_SENDDING_ERROR_NO_SERVICE;
                    break;
                default:
                    resultCode = InduceConstants.SMS_SENDDING_UNKNOWN_ERROR;
                    break;
            }
            sendResult(STATE_SMS_SENT, InduceResult.createSmsResult(false, resultCode));
            if(!curInduceTask.getSmsMessage().isSwitchDetection()){
                //非开关机检测，反馈发送失败原因
                sendResult(STATE_SMS_END, InduceResult.createSmsResult(false, resultCode));
            }
        }

    }

    class DeliveredBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // 对方接收到短信
            LogUitl.i("sms send delivered success ");
            sendResult(STATE_SMS_DELIVERED, null);
            if(curInduceTask.getSmsMessage().hasReceive()){
                //有回执
                sendResult(STATE_SMS_END, InduceResult.createSmsResult(true, InduceConstants.SMS_SENDDING_OK));
            }
        }
    }
}
