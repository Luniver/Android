
## 概述
    网络通信模块, 集成 wifi, bluetooth, 协议解析,
    通过初始化相应配置, 提供统一调用接口调用, 数据监听, 状态监听

## 框架说明
- 经典蓝牙

- ble蓝牙

- wifi_upd

- 协议解决

    数据分发单独抽出来, 保证唯一数据监听, 涉及到数据监听场合不只一处

## 使用方法
1. 初始化配置
   1. CommunicateHelper.init(Context, CommunicateType, ProtocolType)}, 建议在 application 中初始化
   2. 若通信方式动态改变, 可调用
        * CommunicateHelper.config(CommunicateType, ProtocolType) 更新通信模式与协议类型
        * CommunicateHelper.configBluetoothParams(String, String, String) 更新蓝牙通信参数
        * CommunicateHelper.configWifiParams(String, int, String, int) 更新wifi通信参数
   3. (非初始化场景的)其他场景通过 CommunicateHelper.getCommunicator()} 获取当前通信实例 ICommunication
   4. 添加数据监听接口, ICommunication.addDataReceiveListener(listener), 此接口可以多次添加, 建议各应用在 application 中添加一个全局的数据分发中心, 用来处理数据

2. 蓝牙/wifi 打开/关闭
    ICommunication.open();
    ICommunication.close();

    > 注: ICommunication 是通过 CommunicateHelper.getCommunicator() 获取的实例, 下同

3. 通信连接与断开
    ICommunication.startCommunicate();
    ICommunication.stopCommunicate();

4. 状态监听
    // 蓝牙状态监听
    ICommunicateion.addBluetoothStateCallback();
    ICommunicateion.removeBluetoothStateCallback();

    // wifi状态监听
    ICommunicateion.addWifiStateCallback();
    ICommunicateion.removeWifiStateCallback();

## 后续工作: (实时更新)
1. wifi扫描, 连接
    目前是借助于系统的wifi管理, 后续有自定义wifi扫描页面, 再继续完善

2. 资源释放
    目前未实现资源释放, 但也没发现大的问题, 后续根据问题优化

