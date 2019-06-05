package com.hrst.network.interfaces.impl;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;

import com.hrst.helper.SendMessageRunnable;
import com.hrst.network.api.CommunicateHelper;
import com.hrst.network.constants.BluetoothState;
import com.hrst.network.constants.CommunicateType;
import com.inuker.bluetooth.library.Code;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.options.BleConnectOptions;
import com.inuker.bluetooth.library.connect.response.BleNotifyResponse;
import com.inuker.bluetooth.library.connect.response.BleUnnotifyResponse;
import com.inuker.bluetooth.library.receiver.listener.BluetoothBondListener;
import com.orhanobut.logger.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

/**
 * 低功耗蓝牙通信实现
 * Created by Sophimp on 2018/8/13.
 */
public class BleCommunicationImpl extends BluetoothCommunicateImpl {

//    private static final UUID RX_SERVICE_UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e");
//    private static final UUID RX_CHAR_UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e");
//    private static final UUID TX_CHAR_UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");

    // 经典蓝牙一包数据的最大值
    private static final int MAX_PACKET = 20;

    /**
     * 蓝牙连接配置
     */
    private final BleConnectOptions connectOptions;

    public BleCommunicationImpl() {
        super();

        connectOptions = new BleConnectOptions.Builder()
                .setConnectRetry(2)
                .setServiceDiscoverRetry(2)
                .build();


        // 蓝牙配对监听
        bleClient.registerBluetoothBondListener(new BluetoothBondListener() {
            @Override
            public void onBondStateChanged(String mac, int bondState) {
                switch (bondState) {
                    case BluetoothDevice.BOND_NONE:
                        curState = BluetoothState.UNBOND;
                        break;
                    case BluetoothDevice.BOND_BONDING:
                        curState = BluetoothState.BONDING;
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        curState = BluetoothState.BONDED;
                        connect(curDevice);
                        break;
                }
                // 回调
                onStateCallback();
            }
        });

    }

    /**
     * 数据监听回调
     */
    private BleNotifyResponse notifyResponse = new BleNotifyResponse() {
        @Override
        public void onNotify(UUID service, UUID character, byte[] value) {
            // 蓝牙原始包test code
//                Log.d(TAG, "on notify Notify: " + new String(value));
//                dataCounter += value.length;
//            Log.d(TAG, "raw data: " + ConvertUtil.toHexString(value));
            CommunicateHelper.getExtractClient().fillRawData(value);
        }

        @Override
        public void onResponse(int code) {

//                Log.d(TAG, "notify onResponse: " + Code.toString(code));
        }
    };

    @Override
    public void startCommunicate(BluetoothDevice device) {
        super.startCommunicate(device);
        if (CommunicateHelper.getCommunicateType() == CommunicateType.BLE_BOND) {
            if (BluetoothDevice.BOND_BONDED == device.getBondState()) {
                removeBond(device);
            } else {
                device.createBond();
            }
        } else if (CommunicateHelper.getCommunicateType() == CommunicateType.BLE_CONNECT) {
            connect(device);
        }
    }

    private void connect(BluetoothDevice device) {
        bleClient.connect(device.getAddress(), connectOptions, (code, data) -> {
            switch (code) {
                case Constants.REQUEST_SUCCESS:
                    bleClient.notify(curDevice.getAddress(), CommunicateHelper.getRxServiceUuid(), CommunicateHelper.getTxCharUuid(), notifyResponse);
//                        curState = BluetoothState.CONNECTED;
                    break;
                case Constants.REQUEST_FAILED:
//                        curState = BluetoothState.DISCONNECTED;
                    break;
            }
            // 已经注册了连接状态监听, todo 暂不在此处做回调处理, 如果连接监听有问题, 再处理
        });
    }

    @Override
    public void stopCommunicate(BluetoothDevice device) {
        bleClient.disconnect(device.getAddress());
    }

    @Override
    public void sendData(short cmd, byte[] data) {
        if (null == sendMessageRunnable) {
            sendMessageRunnable = new SendMessageRunnable(curDevice.getAddress(), CommunicateHelper.getRxServiceUuid(), CommunicateHelper.getRxCharUuid(), bleClient, mQueue);
        }
        //
        write(sendMessageRunnable, cmd, data);
    }

    @Override
    public void destroy() {

    }

    /**
     * 移除连接配对
     */
    @SuppressLint("PrivateApi")
    private void removeBond(BluetoothDevice device) {
        if (null != device) {
            bleClient.unnotify(device.getAddress(), CommunicateHelper.getRxServiceUuid(), CommunicateHelper.getTxCharUuid(), new BleUnnotifyResponse() {
                @Override
                public void onResponse(int code) {
                    Logger.d("onu unnotify Response: " + Code.toString(code));
                }
            });
        }
        try {
            device.getClass().getDeclaredMethod("removeBond").invoke(device);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
