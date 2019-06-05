package com.hrst.blt;

import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import com.hrst.network.api.CommunicateHelper;

import java.io.IOException;

/**
 * Service 端等待被连接的线程
 * by Sophimp
 * on 2018/4/26
 */
public class BlueServiceThread extends BlueSocketBaseThread {


    private BluetoothServerSocket mBlueServiceSocket;
    private BluetoothSocket mBlueSocket;

    public BlueServiceThread(Handler handler) {
        super(handler);
    }

    @Override
    public BluetoothSocket getSocket() {
        return mBlueSocket;
    }

    @Override
    public void run() {

        if (!isRunning) return;

        try {
            mBlueServiceSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(NAME_SECURE, CommunicateHelper.getRxServiceUuid());
            sendMessage(BlueSocketListener.BlueSocketStatus.LISTENING);
            //监听连接,等待客户端连接,此处会阻塞线程,如果客户端没有连接服务端,此处一直会等待,知道有设备连接
            mBlueSocket = mBlueServiceSocket.accept();
            if (mBlueSocket != null) {
                sendMessage(BlueSocketListener.BlueSocketStatus.ACCEPTED);
            } else {
                sendMessage(BlueSocketListener.BlueSocketStatus.DISCONNECT);
            }
        } catch (IOException e) {
            sendMessage(BlueSocketListener.BlueSocketStatus.DISCONNECT);
        }
    }

    @Override
    public void cancel() {
        super.cancel();
        try {
            if (mBlueServiceSocket != null)
                mBlueServiceSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (mBlueSocket != null)
                mBlueSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mBlueServiceSocket = null;
        mBlueSocket = null;
    }
}
