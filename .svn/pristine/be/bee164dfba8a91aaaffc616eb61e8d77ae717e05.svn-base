package com.hrst;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.hrst.common.base.BaseApp;
import com.hrst.common.protocol.IPacket;
import com.hrst.common.protocol.OnDataReceiverListener;
import com.hrst.common.protocol.PacketImpl;
import com.hrst.helper.SendMessageRunnable;
import com.hrst.helper.blt.BlueClientThread;
import com.hrst.helper.blt.BlueDataThread;
import com.hrst.helper.blt.BlueSocketBaseThread;
import com.hrst.helper.blt.BlueSocketListener;
import com.hrst.mcu.ConvertUtil;
import com.inuker.bluetooth.library.BluetoothClient;
import com.inuker.bluetooth.library.Code;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleConnectResponse;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.model.BleGattProfile;
import com.inuker.bluetooth.library.receiver.listener.BluetoothBondListener;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.orhanobut.logger.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 经典蓝牙 和 ble 通用辅助类
 * ************** 经典蓝牙
 * 两个主要线程:
 * 连接: {@link BlueClientThread}, 负责连接BlueSocket
 * 通信: {@link BlueDataThread {, 负责设备间的通信
 * <p>
 * <p>
 * ************** ble蓝牙
 * 对{@link com.inuker.bluetooth.library.BluetoothClient} 封装
 * 经典和ble蓝牙的数据统一回调 {@link #addOnDataReceiveListener(OnDataReceiverListener)}
 * <p>
 * 能过连接方式, 自动走相关流程
 * 经典蓝牙连接 {@link #classicConnect(BluetoothDevice, BlueSocketListener)}
 * ble蓝牙连接 {@link #connect(String, BleConnectResponse)}
 * <p>
 * 配对状态监听
 * 连接状态监听
 * 都只保证只监听一次
 * <p>
 * by Sophimp
 * on 2018/4/26
 */
public class BluetoothCommHelper {
    private static final String TAG = "goo-BluetoothCommHelper";

    private static final UUID RX_SERVICE_UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e");
    // pda 写 蓝牙
    private static final UUID RX_CHAR_UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e");
    // 蓝牙notify pda
    private static final UUID TX_CHAR_UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");

    // 经典蓝牙一包数据的最大值
    private static final int MAX_PACKET = 20;
    private static BluetoothCommHelper instance;
    private BlueSocketBaseThread mTargetThread;
    private BlueDataThread mDataThread;
    /**
     * 发送线程
     */
    private ExecutorService mSendThread;

    /**
     * 当前连接状态
     */
    private BlueSocketListener.BlueSocketStatus mNowStatus = BlueSocketListener.BlueSocketStatus.NONE;

    /**
     * 消息队列
     */
    private ConcurrentLinkedQueue<byte[]> mQueue = new ConcurrentLinkedQueue<>();
    /**
     * 连接状态监听
     */
    private BlueSocketListener mStatusListener;

    private List<OnDataReceiverListener> dataReceiverListeners;
    /**
     * 数据校验集
     */
    IPacket verifyClient;

    /**
     * 连接状态标志
     */
    private boolean isConnected = false;

    private BluetoothClient bleClient;
    private String curMac = "-";
    private BluetoothDevice curDevice;
    private final BleConnectOptions bleConnectOptions;
    private SendMessageRunnable sendMessageRunnable;
    private Map<String, BleConnectResponse> responses;

    /**
     * 当前蓝牙模式
     */
    private boolean isClassicBluetooth = false;

    private long dataCounter = 0;

    private BluetoothBondListener bondstateListener;

    private final static String defaultSecret = "123456";
    private String currentDeviceName = "-";

    private BluetoothCommHelper() {
        responses = new LinkedHashMap<>();
        verifyClient = new PacketImpl();
        bleClient = new BluetoothClient(BaseApp.getCtx());
        dataReceiverListeners = new LinkedList<>();
        mSendThread = Executors.newFixedThreadPool(1);
        verifyClient.setDataReceiverListener((cmd, property, data) -> {
//            Logger.d( "dispatcher data: " + Integer.toHexString(0xff & cmd) + "---" + ConvertUtil.toHexString(data));
//            Logger.d("listeners : " + dataReceiverListeners.size());
            for (OnDataReceiverListener listener : dataReceiverListeners) {
                listener.onDataReceive(cmd, property, data);
            }
        });
        bleConnectOptions = new BleConnectOptions.Builder()
                .setConnectRetry(2)
                .setServiceDiscoverRetry(2)
                .build();

        /*
         * 配对状态监听
         * 连接状态监听
         * 都只保证只监听一次
         */
        bleClient.registerBluetoothBondListener(new BluetoothBondListener() {
            @Override
            public void onBondStateChanged(String mac, int bondState) {
                if (bondstateListener != null) {
                    bondstateListener.onBondStateChanged(mac, bondState);
                }
            }
        });

    }

    public static BluetoothCommHelper get() {
        if (null == instance) {
            synchronized (BluetoothCommHelper.class) {
                if (null == instance) {
                    instance = new BluetoothCommHelper();
                }
            }
        }
        return instance;
    }

    /**
     * 设置数据验证规则
     */
    public void setVerifyClient(IPacket verify) {
        verifyClient = verify;
//        verifyClient.setDataReceiverListener(dataReceiverListener);
    }

    /**
     * 添加数据监听
     */
    public void addOnDataReceiveListener(OnDataReceiverListener listener) {
        dataReceiverListeners.add(listener);
    }

    /**
     * 移除数据监听
     */
    public void removeDataReceiveListener(OnDataReceiverListener listener) {
        dataReceiverListeners.remove(listener);
    }

    /**
     * 客户端主动发起连接,去连接服务端
     *
     * @param serviceDevice  服务端的蓝牙设备
     * @param statusListener 连接状态监听
     */
    public void classicConnect(BluetoothDevice serviceDevice, BlueSocketListener statusListener) {
        mStatusListener = statusListener;
        isClassicBluetooth = true;
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
        mTargetThread = new BlueClientThread(serviceDevice, mSocketHandler);
        mTargetThread.start();
    }

    public synchronized void write(byte[] data) {
        if (null == sendMessageRunnable && null != mDataThread) {
            sendMessageRunnable = new SendMessageRunnable(mSocketHandler, mDataThread.getOutputStream(), mQueue);
        }
        // 发送数据
        write(sendMessageRunnable, data);
    }

    public void bleDisconnect(String deviceMac){
        bleClient.disconnect(deviceMac);
    }

    public void classicDisconnect() {
        if (mTargetThread != null) {
            mTargetThread.cancel();
            mTargetThread = null;
        }
        if (mDataThread != null) {
            mDataThread.cancel();
            mDataThread = null;
        }
    }


    /**
     * 状态处理
     */
    private Handler mSocketHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mNowStatus = BlueSocketListener.BlueSocketStatus.values()[msg.what];
            // 连接状态监听回调
            if (null != mStatusListener) {
                BluetoothDevice remoteDevice = null;
                if (null != msg.obj) {
                    remoteDevice = (BluetoothDevice) msg.obj;
                }
                mStatusListener.onBlueSocketStatusChange(mNowStatus, remoteDevice);
            }

            synchronized (BluetoothCommHelper.class) {
                switch (mNowStatus) {
                    case CONNECTED:
                        isConnected = true;
                        break;
                    case ACCEPTED:  //当服务端或者客户端监听到对方已经同意连接,则分别开启数据通道线程,接受数据
                        mQueue.clear();
                        mDataThread = new BlueDataThread(mTargetThread.getSocket(), mSocketHandler);
                        mDataThread.start();
                        break;
                    case DISCONNECT: //如果连接断开,则停止数据通道线程
                        isConnected = false;
                        if (mDataThread != null) {
                            mDataThread.cancel();
                            mDataThread = null;
                        }
                        break;
                    case DATA_RECEIVE:  // 接收数据
                        // 加入缓存队列
                        verifyClient.fillRawData((byte) msg.arg1);
                        break;
                }
            }
        }
    };

    public void registerBluetoothStateListener(BluetoothStateListener bluetoothStateListener) {
        bleClient.registerBluetoothStateListener(bluetoothStateListener);
    }

    public void refreshCache(String address) {
        bleClient.refreshCache(address);
    }

    public void stopSearch() {
        bleClient.stopSearch();
    }

    public void search(SearchRequest bleSearchRequest, SearchResponse onScanStarted) {
        bleClient.search(bleSearchRequest, onScanStarted);
    }

    public int getBondState(String address) {
        return bleClient.getBondState(address);
    }

    public boolean isBluetoothOpened() {
        return bleClient.isBluetoothOpened();
    }

    public void unregisterBluetoothStateListener(BluetoothStateListener bluetoothStateListener) {
        bleClient.unregisterBluetoothStateListener(bluetoothStateListener);
    }

    public void openBluetooth() {
        bleClient.openBluetooth();
    }

    public void closeBluetooth() {
        bleClient.closeBluetooth();
    }

    public void connect(BluetoothDevice searchDevice, BleConnectResponse response) {
        // 清空未发完的数据
        mQueue.clear();
        // 发送任务清空
        if (null != sendMessageRunnable) {
            sendMessageRunnable.cancel();
            sendMessageRunnable = null;
        }
        isClassicBluetooth = false;
        // 先停止扫描
        bleClient.stopSearch();
        curDevice = searchDevice;
        curMac = searchDevice.getAddress();

        addBluetoothConnectedListener(curMac, response);

        bleClient.connect(curMac, bleConnectOptions, bleConnectResponse);
    }

    /**
     * 连接状态监听
     */
    private BleConnectResponse bleConnectResponse = new BleConnectResponse() {
        @Override
        public void onResponse(int code, BleGattProfile data) {
            switch (code) {
                case Code.REQUEST_SUCCESS:
                    isConnected = true;
                    break;
                case Code.REQUEST_FAILED:
                    isConnected = false;
                    break;
            }
            for (BleConnectResponse res : responses.values()) {
                res.onResponse(code, data);
            }
        }
    };

    /**
     * 连接状态监听
     */
    public void addBluetoothConnectedListener(String key, BleConnectResponse response) {
        responses.put(key, response);
    }

    public void write(UUID service, UUID character, byte[] data) {
        if (null == sendMessageRunnable) {
            sendMessageRunnable = new SendMessageRunnable(curMac, RX_SERVICE_UUID, RX_CHAR_UUID, bleClient, mQueue);
        }
        //
        write(sendMessageRunnable, data);
    }

    private void write(SendMessageRunnable messageRunnable, byte[] data) {
        if (isConnected) {
            synchronized (BluetoothCommHelper.class) {
                if (isConnected) {
                    // 先缓存起来
                    if (null != data && data.length >= 10) {
                        if (isClassicBluetooth) {
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

    /**
     * 数据监听回调
     */
    private BleNotifyResponse notifyResponse = new BleNotifyResponse() {
        @Override
        public void onNotify(UUID service, UUID character, byte[] value) {
            dataCounter += value.length;
            // 蓝牙原始包test code todo 测试代码
//            ToastUtils.showToast("receiver data length: " + dataCounter);
//                Log.d(TAG, "on notify Notify: " + new String(value));
//            Logger.d("goo raw data: " + ConvertUtil.toHexString(value));
            verifyClient.fillRawData(value);
        }

        @Override
        public void onResponse(int code) {

//                Log.d(TAG, "notify onResponse: " + Code.toString(code));
        }
    };

    public void notify(UUID service, UUID character) {
        // 开启写数据线程
        if (mDataThread != null) {
            mDataThread.cancel();
        }
        // 监听数据获取
        bleClient.notify(curMac, RX_SERVICE_UUID, TX_CHAR_UUID, notifyResponse);
    }

    public void clearCounter() {
        dataCounter = 0;
        Log.d(TAG, "clear count: " + dataCounter);
    }

    public boolean isClassicBluetooth() {
        return isClassicBluetooth;
    }

    public void registerBondStateListener(BluetoothBondListener bondListener) {
        this.bondstateListener = bondListener;
    }

    public void unregisterBluetoothBondListener() {
        this.bondstateListener = null;
    }

    /**
     * 移除连接配对
     */
    @SuppressLint("PrivateApi")
    public void removeBond(BluetoothDevice curClickDevice) {
        if (BluetoothDevice.BOND_BONDED == curClickDevice.getBondState()) {
            if (!TextUtils.isEmpty(curMac)) {
                responses.remove(curMac);
                bleClient.unnotify(curMac, RX_SERVICE_UUID, TX_CHAR_UUID, new BleUnnotifyResponse() {
                    @Override
                    public void onResponse(int code) {
                        Log.d(TAG, "onu unnotify Response: " + Code.toString(code));
                    }
                });
            }
            try {
                curClickDevice.getClass().getDeclaredMethod("removeBond").invoke(curClickDevice);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 蓝牙是否连接
     */
    public boolean isConnected() {
        return isConnected;
    }

    public void setConnected(boolean connected) {
        isConnected = connected;
    }

    public void clearCache() {
        responses.clear();
        mQueue.clear();
    }

    /**
     * 不弹配对密码框, 通过代码设置配对
     */
    public boolean connectWithPin() {
        if (null != curDevice){
//            curDevice.device.setPairingConfirmation(false);
            boolean res = curDevice.setPin(defaultSecret.getBytes());
//            try {
//                Method cancelParingInput = curDevice.device.getClass().getDeclaredMethod("cancelPairingUserInput");
//                cancelParingInput.invoke(curDevice.device);
//            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
//                e.printStackTrace();
//            }
            Logger.d("set pin: " + res);
            return res;
        }
        return false;
    }

    /**
     * 配对连接
     */
    public void createBond(BluetoothDevice curClickDevice) {
        curDevice = curClickDevice;
        curDevice.createBond();
    }

    public String getCurrentDeviceMac() {
        return curMac;
    }
}
