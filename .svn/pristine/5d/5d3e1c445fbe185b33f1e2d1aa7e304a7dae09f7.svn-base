package com.hrst.induce;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.hrst.induce.bean.InduceResult;
import com.hrst.induce.interfaces.InduceInterface;
import com.hrst.induce.utils.LogUitl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 拨号诱发
 * Created by Lin on 2018/6/4.
 */

public class HrstCallManager implements InduceInterface {

    private static final int STATE_CALL_START = 0x01;
    private static final int STATE_CALL_END = 0x02;

    private TelephonyManager mTelephonyManager;
    private ITelephony mITelephony;

    //权限授予标志
    private volatile boolean permission = false;
    //是否正在诱发
    private volatile boolean isInducing = false;

    private static HrstCallManager mInstance = new HrstCallManager();

    /**
     * 当前诱发任务
     */
    private InduceTask curInduceTask;

    protected static HrstCallManager get() {
        if(mInstance == null){
            mInstance = new HrstCallManager();
        }
        return mInstance;
    }

    /**
     * 主线程回调
     */
    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if(curInduceTask.getCallInduceCallBack() == null){
                return;
            }
            switch (msg.what){
                case STATE_CALL_START:
                    curInduceTask.getCallInduceCallBack().onCallStart();
                    break;
                case STATE_CALL_END:
                    InduceResult result = (InduceResult) msg.obj;
                    if(result != null){
                        curInduceTask.getCallInduceCallBack().onCallResult(result.isSuccess);
                    }
                    break;
            }
        }
    };

    protected HrstCallManager() {
        mITelephony = getITelephony();

        mTelephonyManager = (TelephonyManager) InduceClient.client()
                .getSystemService(InduceClient.client().TELEPHONY_SERVICE);
        mTelephonyManager.listen(new PhoneListener(), PhoneStateListener.LISTEN_CALL_STATE);
    }

    /**
     * 电话监听
     */
    private class PhoneListener extends PhoneStateListener {

        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            if (isInducing && state == TelephonyManager.CALL_STATE_OFFHOOK) {
                sendResult(STATE_CALL_START, null); //开始拨打诱发
                mMainHandler.removeCallbacks(permissionRunnable);
                permission = true;
                mMainHandler.postDelayed(hangUpRunnable, curInduceTask.getHangup());
            }
        }
    }

    @Override
    public boolean isInducing() {
        return isInducing;
    }

    @Override
    public void startInduce(InduceTask induceTask) {
        curInduceTask = induceTask;
        isInducing = true;
        //获取电话拨打状态
        int state = mTelephonyManager.getCallState();
        //电话拨打状态在无显示状
        if (state == TelephonyManager.CALL_STATE_IDLE) {
            if (android.os.Build.MODEL.contains("Mi") || android.os.Build.MODEL.contains("Redmi")) {
                //针对小米手机，做10秒权限超时处理
                permission = false;
                mMainHandler.postDelayed(permissionRunnable, 10000);
            }
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + curInduceTask.getNumber()));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            InduceClient.client().startActivity(intent);
        } else {
            sendResult(STATE_CALL_END, InduceResult.createCallResult(false));
            finish();
        }
    }

    @Override
    public void stopInduce() {
        try {
            if(isInducing){
                mITelephony.endCall();
            }
            finish();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void finish() {
        isInducing = false;
        mMainHandler.removeCallbacks(permissionRunnable);
        mMainHandler.removeCallbacks(hangUpRunnable);
    }

    /**
     * 结束通话线程
     */
    private Runnable hangUpRunnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            LogUitl.i("hangup runnable run");
            try {
                mITelephony.endCall();  //挂断电话
                sendResult(STATE_CALL_END, InduceResult.createCallResult(true));
                //诱发结束
                finish();
            } catch (RemoteException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    private Runnable permissionRunnable = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            LogUitl.i("permission runnable run");
            //未授权，结束拨打诱发
            if (!permission) {
                finish();
            }
        }
    };

    private void sendResult(int state, InduceResult result){
        Message message = mMainHandler.obtainMessage(state);
        message.obj = result;
        mMainHandler.sendMessage(message);
    }

    /**
     * 获取ITelephone对象
     *
     * @return
     */
    private ITelephony getITelephony() {
        TelephonyManager mTelephonyManager = (TelephonyManager) InduceClient.client()
                .getSystemService(InduceClient.client().TELEPHONY_SERVICE);
        Class<TelephonyManager> clz = TelephonyManager.class;
        Method method = null;
        ITelephony iTelephony = null;
        try {
            method = clz.getDeclaredMethod("getITelephony", (Class[]) null); //获取声明方法
            method.setAccessible(true);
            iTelephony = (ITelephony) method.invoke(
                    mTelephonyManager, (Object[]) null); // 获取实例
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return iTelephony;
    }
}
