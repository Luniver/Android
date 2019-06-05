package com.hrst.induce;

import android.os.Handler;
import android.os.HandlerThread;

import com.hrst.induce.annotation.InduceType;
import com.hrst.induce.interfaces.CallInduceCallBack;
import com.hrst.induce.interfaces.InduceCallBack;
import com.hrst.induce.interfaces.SmsInduceCallBack;
import com.hrst.induce.sms.HrstSmsMessage;
import com.hrst.induce.utils.LogUitl;
import com.orhanobut.logger.Logger;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 对外接口
 * <p>
 * 开关机检测 {@link #addDetetionTask(String, HrstSmsMessage, int, int)}
 * 短信诱发 {@link #addSmsInduceTask(String, HrstSmsMessage, int, int, int, int)}
 * 电话诱发 {@link #addCallInduceTask(String, int, int, int)}
 * <p>
 * 上层可改变诱发参数直接调用短信/电话诱发功能, 不用关心上一次诱发是否完成
 * Created by Lin on 2018/6/4.
 */

public class InduceFactory {

    private Queue<InduceTask> mInduceTasks;
    private InduceCallBack mInduceCallBack;

    private boolean isInducing = false;

    /**
     * 诱发线程
     */
    private Handler induceHandler;

    /**
     * 当前诱发任务
     */
    private InduceTask curInduceTask;

    private InduceFactory() {
        mInduceTasks = new ConcurrentLinkedQueue<>();
        HandlerThread induceThread = new HandlerThread(getClass().getName());
        induceThread.start();
        induceHandler = new Handler(induceThread.getLooper());
    }

    public static InduceFactory create() {
        return new InduceFactory();
    }

    public void setInduceCallBack(InduceCallBack induceCallBack) {
        this.mInduceCallBack = induceCallBack;
    }

    /**
     * 开关机检测
     * @param number     目标号码
     * @param smsMessage 消息对象
     * @param timeout    超时时间
     * @param cardId     sim卡id
     */
    public void addDetetionTask(String number, HrstSmsMessage smsMessage, int timeout, int cardId) {
        smsMessage.setSwitchDetection(true);
        mInduceTasks.add(InduceTask.buildSmsInduceTask(number, smsMessage, timeout, 1000, cardId, smsInduceCallBack));
    }

    /**
     * 短信诱发
     * @param number     目标号码
     * @param smsMessage 消息对象
     * @param count       次数
     * @param timeout    超时时间
     * @param interval   间隔时间
     * @param cardId     sim卡id
     */
    public void addSmsInduceTask(String number, HrstSmsMessage smsMessage, int count, int timeout, int interval, int cardId) {
        for (int i = 0; i < count; i++) {
            mInduceTasks.add(InduceTask.buildSmsInduceTask(number, smsMessage, timeout, interval, cardId, smsInduceCallBack));
        }
    }

    /**
     * 拨打诱发
     * @param number     目标号码
     * @param count       次数
     * @param interval   间隔时间
     * @param hangup     挂断时间
     */
    public void addCallInduceTask(String number, int count, int interval, int hangup) {
        for (int i = 0; i < count; i++) {
            mInduceTasks.add(InduceTask.buildCallIncduceTask(number, interval, hangup, callInduceCallBack));
        }
    }

    /**
     * 开始诱发
     */
    public boolean startInduce() {
        if (mInduceTasks.size() == 0) {
            return false;
        }
        if (!isInducing) {
            isInducing = true;
            induce(mInduceTasks.poll());
        }
        return true;
    }

    /**
     * 结束诱发
     */
    public void stopInduce() {
        LogUitl.i("stop induce");
        finish();
        Logger.d("sms is inducing: " + HrstSmsManager.get().isInducing());
        if(HrstSmsManager.get().isInducing()){
            HrstSmsManager.get().stopInduce();
        }
        if(HrstCallManager.get().isInducing()){
            HrstCallManager.get().stopInduce();
        }
        induceHandler.removeCallbacks(induceRunnable);
    }

    /**
     * 是否正在诱发
     * @return
     */
    public boolean isInducing(){
        return isInducing;
    }

    /**
     * 诱发结束
     */
    private void finish(){
        isInducing = false;
        if (mInduceTasks.size()>0){
            mInduceTasks.clear();
        }
        // 不管如何, 都需要结束诱发
        onInduceFinish();
        curInduceTask = null;
    }

    /**
     * 诱发执行
     *
     * @param induceTask
     */
    private void induce(InduceTask induceTask) {
        // 第一次诱发
        if (null == curInduceTask){
            induceTask.setmInterval(0);
        }
        curInduceTask = induceTask;
        // 开始定时诱发
        induceHandler.postDelayed(induceRunnable, curInduceTask.getInterval());
    }

    private Runnable induceRunnable = new Runnable() {
        @Override
        public void run() {
            curInduceTask.induce();//诱发
        }
    };

    /**
     * 短信诱发回调, 只需要一个对象即可, 不建议每次都匿名创建
     */
    private SmsInduceCallBack smsInduceCallBack = new SmsInduceCallBack() {

        @Override
        public void onSmsStart() {
            onInduceStart(InduceType.SMS_INDUCE);
        }

        @Override
        public void onSmsSend(String resutlCode) {
            onSend(resutlCode);
        }

        @Override
        public void onSmsDelivered() {
            onDelivered();
        }

        @Override
        public void onSmsResult(boolean isSuccess, String resutlCode) {
            onInduceResult(InduceType.SMS_INDUCE, isSuccess, resutlCode);
            if (mInduceTasks.size() > 0){
                // 继续诱发
               induce(mInduceTasks.poll());
            }else{
               finish();
            }
        }
    };

    /**
     * 电话诱发回调, 只需要一个对象即可, 不建议每次都匿名创建
     */
    private CallInduceCallBack callInduceCallBack = new CallInduceCallBack() {
        @Override
        public void onCallStart() {
            onInduceStart(InduceType.CALL_INDUCE);
        }

        @Override
        public void onCallResult(boolean isSuccess) {
            onInduceResult(InduceType.CALL_INDUCE, isSuccess, "");
            if (mInduceTasks.size() > 0){
                // 继续诱发
                induce(mInduceTasks.poll());
            }else{
                finish();
            }
        }
    };

    private void onInduceStart(@InduceType int type){
        if(mInduceCallBack != null){
            mInduceCallBack.onInduceStart(type);
        }
    }

    private void onSend(String resutlCode){
        if(mInduceCallBack != null){
            mInduceCallBack.onSend(resutlCode);
        }
    }

    private void onDelivered(){
        if(mInduceCallBack != null){
            mInduceCallBack.onDelivered();
        }
    }

    private void onInduceResult(@InduceType int type, boolean isSuccess, String resutlCode){
        if(mInduceCallBack != null){
            mInduceCallBack.onInduceResult(type, isSuccess, resutlCode);
        }
    }

    private void onInduceFinish(){
        if(mInduceCallBack != null){
            mInduceCallBack.onInduceFinish();
        }
    }
}
