package com.hrst.helper.blt;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.io.IOException;

/**
 * 蓝牙连接客户端连接线程
 * by Sophimp
 * on 2018/4/26
 */

public class BlueClientThread extends BlueSocketBaseThread {

    private BluetoothDevice mServiceDevice;
    private BluetoothSocket mBlueSocket;

    public BlueClientThread(BluetoothDevice serviceDevice, Handler handler) {
        super(handler);
        mServiceDevice = serviceDevice;
    }

    @Override
    public void run() {
        super.run();
        if (!isRunning) return;
        sendMessage(BlueSocketListener.BlueSocketStatus.CONNECTING);
        // 连接放在主线程
        mHandler.postDelayed(() -> {
            try {
                mBlueSocket = mServiceDevice.createRfcommSocketToServiceRecord(MY_UUID);
//                if (mBlueSocket.isConnected()) {
//                    mBlueSocket.close();
//                }
                mBlueSocket.connect();
                sendMessage(BlueSocketListener.BlueSocketStatus.ACCEPTED);
            } catch (IOException e) {
                sendMessage(BlueSocketListener.BlueSocketStatus.DISCONNECT);
            }
        }, 50);
    }


    @Override
    public BluetoothSocket getSocket() {
        return mBlueSocket;
    }

    @Override
    public void cancel() {
        super.cancel();

        try {
            if (mBlueSocket != null && mBlueSocket.isConnected())
                mBlueSocket.close();
                Thread.interrupted();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mServiceDevice = null;
        mBlueSocket = null;
    }
}
