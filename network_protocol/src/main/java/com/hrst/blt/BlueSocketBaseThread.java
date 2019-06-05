package com.hrst.blt;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.util.UUID;

/**
 * 蓝牙线程管理基类
 * by Sophimp
 * on 2018/4/26
 */
public abstract class BlueSocketBaseThread extends Thread {

    protected final String TAG = "goo-" + this.getClass().getSimpleName();

    protected final String  NAME_SECURE = "BluetoothCommunication";

    // 服务器唯一的UUID
//    protected final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");



    /**
     * 此handler 用来和 manager 通讯
     */
    protected Handler mHandler;
    /**
     * 是否还在运行
     */
    protected boolean isRunning;

    protected BluetoothAdapter mBluetoothAdapter;

    public BlueSocketBaseThread() {
    }

    protected BlueSocketBaseThread(Handler handler) {
        super();
        mHandler = handler;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }


    /**
     * 获取当前的链接的socket 对象
     *
     * @return
     */
    public abstract BluetoothSocket getSocket();

    /**
     * 取消当前线程,释放内存
     */
    public void cancel() {
        isRunning = false;
        mBluetoothAdapter = null;
    }

    public void sendMessage(BlueSocketListener.BlueSocketStatus status) {
        if (mHandler != null && isRunning)
            mHandler.obtainMessage(status.ordinal()).sendToTarget();
    }

    public void sendMessage(BlueSocketListener.BlueSocketStatus status, Object obj) {
        if (mHandler != null && isRunning)
            mHandler.obtainMessage(status.ordinal(), obj).sendToTarget();
    }

    public void sendMessage(BlueSocketListener.BlueSocketStatus status, int data) {
        if (mHandler != null && isRunning)
            mHandler.obtainMessage(status.ordinal(), data,0).sendToTarget();
    }


    @Override
    public synchronized void start() {
        isRunning = true;
        super.start();
    }
}
