package com.hrst.helper;


import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import com.hrst.helper.blt.BlueSocketListener;
import com.hrst.mcu.ConvertUtil;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.Code;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Queue;
import java.util.UUID;

/**
 * 发送数据线程
 * 数据包发送:
 * 采用队列缓存, 线程池执行, 从根本上解决延时发送问题, 上层可以连续的向队列中添加数据,
 * 由于现在业务发数据包不会太大, 所以没采取分包, 待后期扩展
 * 这里先将数据缓存, 控制发送频率在50ms
 * Created by Sophimp on 2018/6/12.
 */
public class SendMessageRunnable implements Runnable {
    private final static String TAG = "goo-SendMessageRunnable";
    private boolean isClassicBluetooth = true;
    private boolean isCancel = false;
    private boolean isSendFinish = true;  //数据是否发送完成
    private Handler mSocketHandler;
    private OutputStream mBlueSocketOutputStream;
    private String curMac;
    private UUID rxServiceUUID;
    private UUID rxCharUUID;
    private BluetoothClient bleClient;
    // ble 发送数据线程, 保证有序性
    private Handler mThreadHandler = new Handler(Looper.myLooper());

    public SendMessageRunnable(Handler mHandler, OutputStream blueSocketOutputStream, Queue<byte[]> messageQueue) {
        this.mSocketHandler = mHandler;
        this.mBlueSocketOutputStream = blueSocketOutputStream;
        this.mQueue = messageQueue;
        isClassicBluetooth = true;
    }

    public SendMessageRunnable(String curMac, UUID rxServiceUUID, UUID rxCharUUID, BluetoothClient bluetoothClient, Queue<byte[]> messageQueue) {
        isClassicBluetooth = false;
        this.curMac = curMac;
        this.rxServiceUUID = rxServiceUUID;
        this.rxCharUUID = rxCharUUID;
        this.bleClient = bluetoothClient;
        this.mQueue = messageQueue;
    }

    private Queue<byte[]> mQueue;

    @Override
    public void run() {
        isSendFinish = false;
//            Log.d(TAG, "run: queue" + mQueue.toString() + " empty:- " + !mQueue.isEmpty() + " - " + !isCancel);
        if (isClassicBluetooth) {
            sendMessageByClassicSocket();
        } else {
            sendMessageByBleClient();
        }

    }

    private void sendMessageByBleClient() {
//        while (mQueue != null && !mQueue.isEmpty() && !isCancel) {
            byte[] datas = mQueue.poll();
            Log.d(TAG, "ble encapsulateData: " + ConvertUtil.toHexString(datas));
            bleClient.write(curMac,
                    rxServiceUUID,
                    rxCharUUID,
                    datas,
                    code -> {
                        Log.d(TAG, "onResponse: write result: " + Code.toString(code));
                        switch (code) {
                            case Code.REQUEST_SUCCESS:
                                // 保证发送有序性,
                                if (!mQueue.isEmpty() && !isCancel) {
                                    mThreadHandler.postDelayed(this::sendMessageByBleClient, 40);
//                                    mThreadHandler.post(this::sendMessageByBleClient);
                                }else {
                                    isSendFinish = true;
                                }
                                break;
                            case Code.REQUEST_FAILED:
                                // 判断是否重新发送
                                requestFailedHandle();
                                break;
                        }
                    });

            // 先休眠, 休眠过程, 也算在发送过程里, 在上层根据 isSendFinished 避免多次execute
//            SystemClock.sleep(100);
//            if (mQueue.isEmpty()) {
//                isSendFinish = true;
//            }
//        }
    }

    /**
     * 发送失败处理
     */
    private void requestFailedHandle() {
        // todo 发送失败处理
        // 蓝牙是否连接
    }

    private void sendMessageByClassicSocket() {
        while (mQueue != null && !mQueue.isEmpty() && !isCancel) {
            byte[] datas = mQueue.poll();
            Log.d(TAG, "classic encapsulateData: " + ConvertUtil.toHexString(datas));
            // 经典蓝牙发送数据需要保证在主线程
            mSocketHandler.post(() -> {
                try {
                    if (null != datas) {
                        mBlueSocketOutputStream.write(datas);
                        mBlueSocketOutputStream.flush();
//                        Log.d(TAG, "发消息给客户端成功");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (mSocketHandler != null)
                        mSocketHandler.obtainMessage(BlueSocketListener.BlueSocketStatus.DISCONNECT.ordinal()).sendToTarget();
                    isCancel = true;
                }
            });
            // 先休眠, 休眠过程, 也算在发送过程里, 在上层根据 isSendFinished 避免多次execute
            SystemClock.sleep(80);
            if (mQueue.isEmpty()) {
                isSendFinish = true;
            }
        }
    }

    public void cancel() {
        isCancel = true;
        isSendFinish = true;
    }

    public boolean isSendFinish() {
        return isSendFinish;
    }
}
