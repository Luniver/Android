package com.hrst.network.api;


import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hrst.network.constants.CommunicateType;
import com.hrst.network.constants.ProtocolType;
import com.hrst.network.interfaces.DataReceiveCallback;
import com.hrst.network.interfaces.impl.BleCommunicationImpl;
import com.hrst.network.interfaces.impl.BltCommunicationImpl;
import com.hrst.network.interfaces.impl.WifiUdpCommunication;
import com.hrst.network.protocol.IProtocol;
import com.hrst.network.protocol.impl.SCZDProtocol;
import com.hrst.network.protocol.impl.WifiLocationProtocol;
import com.orhanobut.logger.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;


/**
 * 通信辅助类, 静态类, 提供全局 ICommunication 唯一实例, 可动态更新
 * 网络通信配置
 * 连接类型: ble, blt, tcp/udp, wifi
 * 通信协议: 车载, 单兵, 无人机, 数传, wifi定位, wifi测向
 * <p>
 * 因为初始化时不会涉及到多线程场景, 所以使用静态类, 更方便
 * <p>
 * 使用步骤:
 * 1. 初始化 {@link #init(Context, CommunicateType, ProtocolType)}, 建议在 application 中初始化
 * 2. 若通信方式动态改变, 可调用 {@link #config(CommunicateType, ProtocolType)}
 * 3. 非初始化场景的其他场景通过{@link #getCommunicator()} 获取当前通信实例
 * 4.
 * <p>
 * Created by Sophimp on 2018/8/21.
 */

public class CommunicateHelper {

    private static List<DataReceiveCallback> dataReceiverListeners = new LinkedList<>();

    /**
     * 通信类型
     */
    private static CommunicateType communicateType = null;

    /**
     * 协议类型
     */
    private static ProtocolType protocolType = null;

    private static Context ctx;

    /**
     * 当前通信实例
     */
    private static ICommunication communicator;
    /**
     * 协议解析实现
     */
    private static IProtocol extractClient;

    /**
     * wifi client host
     * 需要初始化配置, 默认 为 wifi定位参数设置
     * {@link #configWifiParams(String, int, String, int)}
     */
    private static String wifiClientHost = "";

    /**
     * wifi client port
     * 需要初始化配置, 默认 为 wifi定位参数设置
     */
    private static int wifiClientPort;

    /**
     * wifi server host
     * 需要初始化配置, 默认 为 wifi定位参数设置
     */
    private static String wifiServerHost = "";

    /**
     * wifi server port
     * 需要初始化配置, 默认 为 wifi定位参数设置
     */
    private static int wifiServerPort;

    /**
     * 蓝牙通信 服务端 UUID
     * 经典蓝牙, 默认配置数传终端蓝牙设备服务号, 若其他设备不一样, 需要初始化配置 {@link #configBluetoothParams(String, String, String)}
     * ble蓝牙, 默认配置功德环设备服务号
     */
    private static UUID RX_SERVICE_UUID;

    /**
     * ble蓝牙, 默认配置功德环设备服务号
     */
    private static UUID RX_CHAR_UUID;

    /**
     * ble蓝牙, 默认配置功德环设备服务号
     */
    private static UUID TX_CHAR_UUID;

    private static DataReceiveCallback dataReceiveCallback = (cmd, data) -> {
//            Logger.d( "dispatcher data: " + Integer.toHexString(0xff & cmd) + "---" + ConvertUtil.toHexString(data));
//            Logger.d("listeners : " + dataReceiverListeners.size());
        for (DataReceiveCallback listener : dataReceiverListeners) {
            listener.onDataReceived(cmd, data);
        }
    };

    /**
     * 初始使化 通信类型 与 协议类型
     * 建议在 application 中初始化
     *
     * @param context         当前应用上下文
     * @param communicateType 通信类型
     * @param protocolType    协议类型
     */
    public static void init(Context context, CommunicateType communicateType, ProtocolType protocolType) {
        ctx = context;
        communicator = config(communicateType, protocolType);
    }

    /**
     * 可能会存在连接方式动态改变的情况, 如数传终端, 会在传统蓝牙与低功耗蓝牙切换
     * 此方式更便于动态改变的配置, 但是仍然需要先初始化 {@link #init(Context, CommunicateType, ProtocolType)}
     */
    public static ICommunication config(CommunicateType commType, ProtocolType proType) {
        CommunicateType lastCommType = communicateType;
        ProtocolType lastProType = protocolType;
        communicateType = commType;
        protocolType = proType;
        if (lastProType != proType) {
            extractClient = createPacketExtractor();
            // 全局数据分发
            extractClient.setDataReceiverListener(dataReceiveCallback);
        }
        if (lastCommType != commType) {
            communicator = createCommunicator();
        }
        return communicator;
    }

    /**
     * 配置 wifi 通信参数
     */
    public static void configWifiParams(@NonNull String clientHost, int clientPort, @NonNull String serverHost, int serverPort) {
        wifiClientHost = clientHost;
        wifiClientPort = clientPort;
        wifiServerHost = serverHost;
        wifiServerPort = serverPort;
    }

    /**
     * 配置蓝牙通信参数
     * 注: 经典蓝牙只需要配置 rxServer, 后两个参数可以只填null
     *
     * @param rxServer    服务号
     * @param rxCharacter 以外围设备作参数, 接收character
     * @param txCharacter 以外围设备作参数, 发送character
     */
    public static void configBluetoothParams(@NonNull String rxServer, String rxCharacter, String txCharacter) {
        RX_SERVICE_UUID = UUID.fromString(rxServer);
        if (!TextUtils.isEmpty(rxCharacter)) {
            RX_CHAR_UUID = UUID.fromString(rxCharacter);
        }
        if (!TextUtils.isEmpty(txCharacter)) {
            TX_CHAR_UUID = UUID.fromString(txCharacter);
        }
    }

    public static Context getCtx() {
        if (null == ctx) {
            throw new ExceptionInInitializerError("Please config communication first!");
        }
        return ctx;
    }

    public static CommunicateType getCommunicateType() {
        return communicateType;
    }

    public static ProtocolType getProtocolType() {
        return protocolType;
    }

    public static ICommunication getCommunicator() {
        return communicator;
    }

    public static IProtocol getExtractClient() {
        return extractClient;
    }

    static void addDataReceivedListener(DataReceiveCallback listener) {
        if (null != listener && !dataReceiverListeners.contains(listener)) {
            dataReceiverListeners.add(listener);
        }
    }

    static void removeDataReceivedListener(DataReceiveCallback listener) {
        if (null != listener && dataReceiverListeners.contains(listener)) {
            dataReceiverListeners.remove(listener);
        }
    }

    /**
     * 根据配置创建通信方式
     * <p>
     * 经典蓝牙, 低功耗蓝牙, wifi udp, wifi tcp
     * <p>
     * 上层调用自行控制是否需要单例, 这里不作控制
     */
    public static ICommunication createCommunicator() {
        ICommunication communication = null;
        Logger.d("goo cur commutype: " + communicateType);
        switch (communicateType) {
            case BLE_BOND:
            case BLE_CONNECT:
                communication = new BleCommunicationImpl();
                break;
            case BLT:
                communication = new BltCommunicationImpl();
                break;
            case WIFI_UDP:
                communication = new WifiUdpCommunication();
                break;
        }
        return communication;
    }


    /**
     * 根据配置创建协议解析器
     * >
     * 单兵, 车载, 无人机, 数值, wifi
     * <p>
     * 上层调用自行控制是否需要单例, 这里不作控制
     */
    private static IProtocol createPacketExtractor() {
        IProtocol packet = null;
        switch (protocolType) {
            case SINGLE_PAWN:
            case VEHICLE_MOUNTED:
            case DATA_TRANSPORT:
                packet = new SCZDProtocol();
                break;
            case WIFI_LOCATION:
                packet = new WifiLocationProtocol();
                break;
            case UNMANNED_AERIAL_VEHICLE: // 无人机
                break;
            default:
                packet = new SCZDProtocol();
                break;
        }
        return packet;
    }

    /**
     * 默认配置wifi 定位设备的参数
     */
    public static String getWifiClientHost() {
        if (TextUtils.isEmpty(wifiClientHost)) {
            wifiClientHost = "192.168.4.1";
        }
        return wifiClientHost;
    }

    public static int getWifiClientPort() {
        if (wifiClientPort == 0) {
            wifiClientPort = 5333;
        }
        return wifiClientPort;
    }

    public static String getWifiServerHost() {
        if (TextUtils.isEmpty(wifiServerHost)) {
            wifiServerHost = "192.168.4.2";
        }
        return wifiServerHost;
    }

    public static int getWifiServerPort() {
        if (wifiServerPort == 0) {
            wifiServerPort = 5333;
        }
        return wifiServerPort;
    }

    public static UUID getRxServiceUuid() {
        if (RX_SERVICE_UUID == null) {
            RX_SERVICE_UUID = UUID.fromString("6e400001-b5a3-f393-e0a9-e50e24dcca9e");
        }
        return RX_SERVICE_UUID;
    }

    public static UUID getRxCharUuid() {
        if (RX_CHAR_UUID == null) {
            RX_CHAR_UUID = UUID.fromString("6e400002-b5a3-f393-e0a9-e50e24dcca9e");
        }
        return RX_CHAR_UUID;
    }

    public static UUID getTxCharUuid() {
        if (TX_CHAR_UUID == null) {
            TX_CHAR_UUID = UUID.fromString("6e400003-b5a3-f393-e0a9-e50e24dcca9e");
        }
        return TX_CHAR_UUID;
    }
}
