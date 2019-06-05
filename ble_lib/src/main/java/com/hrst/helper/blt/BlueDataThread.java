package com.hrst.helper.blt;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Queue;

/**
 * 客户端数据阻塞读取线程
 * <p>
 * 对于分包机制, 上层可以实现, 将拆分的包发连续添加到队列中即可
 * by Sophimp
 * on 2018/4/26
 */
public class BlueDataThread extends BlueSocketBaseThread {

    private BluetoothSocket mBlueSocket;
    private OutputStream mBlueSocketOutputStream;
    private InputStream mBlueSocketInputStream;

    public BlueDataThread(BluetoothSocket bluetoothSocket, Handler handler) {
        super(handler);
        mBlueSocket = bluetoothSocket;
    }

    @Override
    public void run() {
        super.run();
        if (!isRunning) return;
        try {
            mBlueSocketOutputStream = mBlueSocket.getOutputStream();
            mBlueSocketInputStream = mBlueSocket.getInputStream();
            sendMessage(BlueSocketListener.BlueSocketStatus.CONNECTED, mBlueSocket.getRemoteDevice());
        } catch (IOException e) {
            e.printStackTrace();
            sendMessage(BlueSocketListener.BlueSocketStatus.DISCONNECT);
            return;
        }

        //监听通道后,死循环监听通道,去读取message ,
        while (isRunning) {
            try {
                int d = mBlueSocketInputStream.read();
                // 说好的阻塞, 根本读不到-1, 没有数据的时候 d=0, 这里还是要采用缓冲模式, 逐个解析
//                Log.d(TAG, "read data:" + d);
                sendMessage(BlueSocketListener.BlueSocketStatus.DATA_RECEIVE, d);

            } catch (Exception e) {
                e.printStackTrace();
                sendMessage(BlueSocketListener.BlueSocketStatus.DISCONNECT);
                return;
            }

        }
    }

    @Override
    public BluetoothSocket getSocket() {
        return mBlueSocket;
    }

    @Override
    public void cancel() {
        super.cancel();

        try {
            if (mBlueSocketInputStream != null)
                mBlueSocketInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (mBlueSocket != null)
                mBlueSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (mBlueSocketOutputStream != null)
                mBlueSocketOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mBlueSocketInputStream = null;
        mBlueSocketOutputStream = null;
        mBlueSocket = null;

    }

    public OutputStream getOutputStream() {
        return mBlueSocketOutputStream;
    }
}
