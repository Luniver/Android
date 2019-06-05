package com.hrst.network.interfaces.impl;


import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.hrst.blt.BlueClientThread;
import com.hrst.blt.BlueDataThread;
import com.hrst.blt.BlueSocketBaseThread;
import com.hrst.blt.BlueSocketListener;
import com.hrst.helper.SendMessageRunnable;
import com.hrst.network.api.CommunicateHelper;
import com.hrst.network.constants.BluetoothState;

/**
 * 经典蓝牙通信实现
 * Created by Sophimp on 2018/8/13.
 */
public class BltCommunicationImpl extends BluetoothCommunicateImpl {

    private BlueSocketBaseThread mTargetThread;
    private BlueDataThread mDataThread;

    /**
     * 当前连接状态
     */
    private BlueSocketListener.BlueSocketStatus mNowStatus = BlueSocketListener.BlueSocketStatus.NONE;

    /**
     * 状态处理
     */
    private Handler mSocketHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mNowStatus = BlueSocketListener.BlueSocketStatus.values()[msg.what];
            synchronized (CommunicateHelper.class) {
                switch (mNowStatus) {
                    case CONNECTED:
                        curState = BluetoothState.CONNECTED;
                        onStateCallback();
                        break;
                    case ACCEPTED:  //当服务端或者客户端监听到对方已经同意连接,则分别开启数据通道线程,接受数据
                        mQueue.clear();
                        mDataThread = new BlueDataThread(mTargetThread.getSocket(), mSocketHandler);
                        mDataThread.start();
                        break;
                    case DISCONNECT: //如果连接断开,则停止数据通道线程
                        curState = BluetoothState.DISCONNECTED;
                        if (mDataThread != null) {
                            mDataThread.cancel();
                            mDataThread = null;
                        }
                        onStateCallback();
                        break;
                    case DATA_RECEIVE:  // 接收数据
                        // 加入缓存队列
                        CommunicateHelper.getExtractClient().fillRawData((byte) msg.arg1);
                        break;
                }
            }
        }
    };

    public BltCommunicationImpl() {
        super();
    }

    @Override
    public void startCommunicate(BluetoothDevice device) {
        super.startCommunicate(device);
        // 清空未发完的数据
        mQueue.clear();
        // 先停止扫描
        bleClient.stopSearch();

        if (mTargetThread != null) {
            mTargetThread.cancel();
        }

        if (mDataThread != null) {
            mDataThread.cancel();
        }
        // 发送任务清空
        if (null != sendMessageRunnable) {
            sendMessageRunnable.cancel();
            sendMessageRunnable = null;
        }
        mTargetThread = new BlueClientThread(device, mSocketHandler);
        mTargetThread.start();
    }

    @Override
    public void stopCommunicate(BluetoothDevice device) {
        if (mTargetThread != null) {
            mTargetThread.cancel();
            mTargetThread = null;
        }
        if (mDataThread != null) {
            mDataThread.cancel();
            mDataThread = null;
        }
    }

    @Override
    public void sendData(short cmd, byte[] data) {
        if (null == sendMessageRunnable) {
            sendMessageRunnable = new SendMessageRunnable(mSocketHandler, mDataThread.getOutputStream(), mQueue);
        }
        // 发送数据
        write(sendMessageRunnable, cmd, data);
    }

    @Override
    public void destroy() {

    }
}
