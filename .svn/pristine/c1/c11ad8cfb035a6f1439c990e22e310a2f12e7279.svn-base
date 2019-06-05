package com.hrst.induce;

import com.hrst.induce.annotation.InduceType;
import com.hrst.induce.interfaces.CallInduceCallBack;
import com.hrst.induce.interfaces.SmsInduceCallBack;
import com.hrst.induce.sms.HrstSmsMessage;

/**
 * 诱发任务
 * Created by Lin on 2018/7/2.
 */
public class InduceTask {

    protected  @InduceType int induceType;

    private String mNumber;  //目标号码
    private int mInterval;   //间隔时间
    private int mHangup; //挂断时间
    private int mTimeout;    //超时时间
    private int mCardId; //CardId
    private HrstSmsMessage mSmsMessage;
    private CallInduceCallBack mCallInduceCallBack;
    private SmsInduceCallBack mSmsInduceCallBack;

    private InduceTask() {
    }

    /**
     * 构建短信诱发任务
     * @return
     */
    public static InduceTask buildSmsInduceTask(String number, HrstSmsMessage smsMessage, int timeout, int interval, int cardId, SmsInduceCallBack smsInduceCallBack){
        InduceTask induceTask = new InduceTask();
        induceTask.induceType = InduceType.SMS_INDUCE;
        induceTask.mNumber = number;
        induceTask.mSmsMessage = smsMessage;
        induceTask.mTimeout = timeout;
        induceTask.mInterval = interval;
        induceTask.mCardId = cardId;
        induceTask.mSmsInduceCallBack = smsInduceCallBack;
        return induceTask;
    }

    /**
     * 构建拨打诱发任务
     * @return
     */
    public static InduceTask buildCallIncduceTask(String number, int interval, int hangup, CallInduceCallBack callInduceCallBack){
        InduceTask induceTask = new InduceTask();
        induceTask.induceType = InduceType.CALL_INDUCE;
        induceTask.mNumber = number;
        induceTask.mInterval = interval;
        induceTask.mHangup = hangup;
        induceTask.mCallInduceCallBack = callInduceCallBack;
        return induceTask;
    }

    public int getCardId() {
        return mCardId;
    }

    public CallInduceCallBack getCallInduceCallBack() {
        return mCallInduceCallBack;
    }

    public HrstSmsMessage getSmsMessage() {
        return mSmsMessage;
    }

    public int getHangup() {
        return mHangup;
    }

    public void setmInterval(int mInterval) {
        this.mInterval = mInterval;
    }

    public int getInterval() {
        return mInterval;
    }

    public int getTimeout() {
        return mTimeout;
    }

    public SmsInduceCallBack getSmsInduceCallBack() {
        return mSmsInduceCallBack;
    }

    public String getNumber() {
        return mNumber;
    }

    /**
     * 设置拨打诱发回调
     * @param callInduceCallBack
     */
    public void setCallInduceCallBack(CallInduceCallBack callInduceCallBack) {
        mCallInduceCallBack = callInduceCallBack;
    }

    /**
     * 设置短信诱发回调
     * @param smsInduceCallBack
     */
    public void setSmsInduceCallBack(SmsInduceCallBack smsInduceCallBack) {
        mSmsInduceCallBack = smsInduceCallBack;
    }

    /**
     * 诱发执行
     */
    public void induce(){
        if(induceType == InduceType.SMS_INDUCE){
            //短信诱发
            HrstSmsManager.get().startInduce(this);
        }else{
            //拨打诱发
            HrstCallManager.get().startInduce(this);
        }

    }

    @Override
    public String toString() {
        return "InduceTask{" +
                "induceType=" + induceType +
                ", mNumber='" + mNumber + '\'' +
                ", mInterval=" + mInterval +
                ", mHangup=" + mHangup +
                ", mTimeout=" + mTimeout +
                ", mCardId=" + mCardId +
                ", mSmsMessage=" + mSmsMessage.toString() +
                '}';
    }
}
