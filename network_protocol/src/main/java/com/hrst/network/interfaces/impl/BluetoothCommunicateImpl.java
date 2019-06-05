package com.hrst.network.interfaces.impl;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;

import com.hrst.helper.SendMessageRunnable;
import com.hrst.network.api.CommunicateHelper;
import com.hrst.network.api.ICommunication;
import com.hrst.network.constants.BluetoothState;
import com.hrst.network.constants.CommunicateType;
import com.hrst.network.interfaces.BluetoothStateCallback;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.connect.listener.BleConnectStatusListener;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.orhanobut.logger.Logger;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.inuker.bluetooth.library.Constants.STATUS_CONNECTED;
import static com.inuker.bluetooth.library.Constants.STATUS_DISCONNECTED;

/**
 * 低功耗和经典蓝牙连接公用父类
 * <p>
 * 蓝牙的打开关闭, 搜索, 状态监听, 数据发送, 监听管理
 * <p>
 * Created by Sophimp on 2018/8/14.
 */

public abstract class BluetoothCommunicateImpl implements ICommunication {

    // ble蓝牙一包数据的最大值
    private static final int MAX_PACKET = 20;

    protected BluetoothClient bleClient;

    protected ExecutorService mSendThread;

    protected SendMessageRunnable sendMessageRunnable;

    /**
     * 消息队列
     */
    protected ConcurrentLinkedQueue<byte[]> mQueue = new ConcurrentLinkedQueue<>();

    private SearchRequest searchRequest;

    /**
     * 状态监听回调
     */
    List<BluetoothStateCallback> stateCallbacks = new LinkedList<>();

    /**
     * 当前蓝牙状态
     */
    protected BluetoothState curState = BluetoothState.CLOSED;

    /**
     * 当前连接蓝牙mac
     */
    protected BluetoothDevice curDevice;
    private final BleConnectStatusListener connectStatusListener;

    public BluetoothCommunicateImpl() {
        super();

        bleClient = new BluetoothClient(CommunicateHelper.getCtx());

        mSendThread = Executors.newFixedThreadPool(1);

        // 初始化当前蓝牙状态
        if (bleClient.isBluetoothOpened()) {
            curState = BluetoothState.OPENED;
        } else {
            curState = BluetoothState.CLOSED;
        }

        /*
         * 蓝牙搜索参数配置, 暂不提供出去, 按经验, 使用默认配置即可满足搜索效果
         */
        searchRequest = new SearchRequest.Builder()
                .searchBluetoothClassicDevice(5000, 1)
                .searchBluetoothLeDevice(3000, 2)
                .build();

        // 蓝牙打开/关闭监听
        bleClient.registerBluetoothStateListener(new BluetoothStateListener() {
            @Override
            public void onBluetoothStateChanged(boolean openOrClosed) {
                if (openOrClosed) {
                    curState = BluetoothState.OPENED;
                } else {
                    curState = BluetoothState.CLOSED;
                }
                onStateCallback();
            }
        });

        // 连接状态回调, 保证唯一性
        connectStatusListener = new BleConnectStatusListener() {
            @Override
            public void onConnectStatusChanged(String mac, int status) {
                switch (status) {
                    case STATUS_CONNECTED:
                        curState = BluetoothState.CONNECTED;
                        break;
                    case STATUS_DISCONNECTED:
                        curState = BluetoothState.DISCONNECTED;
                        break;
                }
                // 回调
                onStateCallback();
            }
        };
    }

    @Override
    public void open() {
        bleClient.openBluetooth();
    }

    @Override
    public void startScan(@NonNull SearchResponse response) {
        bleClient.search(searchRequest, response);
    }

    @Override
    public void startCommunicate(BluetoothDevice device) {
        if (null != device) {
            curDevice = device;
            bleClient.registerConnectStatusListener(device.getAddress(), connectStatusListener);
        }
    }

    @Override
    public void close() {
        bleClient.closeBluetooth();
    }

    @Override
    public void addBluetoothStateCallback(@NonNull BluetoothStateCallback callback) {
        stateCallbacks.add(callback);
    }

    @Override
    public boolean isOpen() {
        return bleClient.isBluetoothOpened();
    }

    @Override
    public boolean isCommunicating() {
        return bleClient.isBluetoothOpened() && curState == BluetoothState.CONNECTED;
    }

    protected void onStateCallback() {
        for (BluetoothStateCallback callback : stateCallbacks) {
            callback.onBluetoothStateCallback(curState);
        }
    }

    protected void write(SendMessageRunnable messageRunnable, short cmd, byte[] content) {
        if (curState == BluetoothState.CONNECTED) {
            byte[] data = CommunicateHelper.getExtractClient().packetProtocolData(cmd, content);
            // 协议封装
            Logger.d("goo bluetooth write:" + Arrays.toString(data));
            // 先缓存起来
            if (null != data && data.length >= 10) {
                // 经典蓝牙, 不用分包
                if (CommunicateHelper.getCommunicateType() == CommunicateType.BLT) {
                    mQueue.add(data);
                } else {
                    dissectPacket(data);
                }
            }
            // 发送数据
            if (messageRunnable.isSendFinish()) {
                mSendThread.execute(messageRunnable);
            }
        }
    }


    /**
     * 分包发送处理
     *
     * @param data 总数据包
     */
    private void dissectPacket(byte[] data) {
        if (data.length <= MAX_PACKET) {
            mQueue.add(data);
        } else {
            int lastVolume = data.length % MAX_PACKET;
            int pc = data.length / MAX_PACKET;
            byte[] tmpBuf;
            for (int i = 0; i < pc; i++) {
                tmpBuf = new byte[MAX_PACKET];
                System.arraycopy(data, i * MAX_PACKET, tmpBuf, 0, MAX_PACKET);
//                Log.d(TAG, "dissectPacket: "+ Arrays.toString(tmpBuf));
                mQueue.add(tmpBuf);
            }
            // 最后一包
            if (lastVolume > 0) {
                tmpBuf = new byte[lastVolume];
                System.arraycopy(data, pc * MAX_PACKET, tmpBuf, 0, lastVolume);
//                Log.d(TAG, "dissectPacket last: "+ Arrays.toString(tmpBuf));
                mQueue.add(tmpBuf);
            }
        }
    }

}
