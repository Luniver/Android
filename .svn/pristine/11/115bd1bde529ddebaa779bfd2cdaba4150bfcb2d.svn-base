## 功能
    1. 经典蓝牙, ble功能统一封装
    2. 数据协议解析 封装在 PacketImpl, 放在 common_lib 模块

## 使用方法
### 1. 蓝牙打开/关闭, 及打开/关闭状态监听

```` java
    // 打开
    BluetoothCommHelper.get().openBluetooth();
    // 关闭
    BluetoothCommHelper.get().closeBluetooth();
    // 资源释放
    BluetoothCommHelper.get().releaseResource();
    // 蓝牙打开/关闭状态监听
    BluetoothCommHelper.get()
            .registerBluetoothStateListener(new BluetoothStateListener() {
                                 @Override
                                 public void onBluetoothStateChanged(boolean openOrClosed) {
                                     Log.d(TAG, "onBluetoothStateChanged: " + openOrClosed);
                                     if (openOrClosed) {
                                        // todo open
                                     } else {
                                        // todo close
                                     }
                                 }
                             };);

    // 蓝牙连接状态监听
    BluetoothCommHelper.get().addBluetoothConnectedListener((code, data) -> {
        switch (code) {
            case Constants.REQUEST_SUCCESS:
                setBluetoothState(true);
                break;
            case Constants.REQUEST_FAILED:
                setBluetoothState(false);
                break;
        }
    });

    // 蓝牙配对状态监听
    BluetoothBondListener bondListener = new BluetoothBondListener() {
        @Override
        public void onBondStateChanged(String mac, int bondState) {
            Log.d(TAG, "onBondStateChanged: " + bondState);
            if (bondState == BluetoothDevice.BOND_BONDED) {
                connectSelectBleDevice();
            }
        }
    };
    BluetoothCommHelper.get().registerBondStateListener(bondListener);
````

### 2. 搜索蓝牙

``` java
    // 搜索选项
    bleSearchRequest = new SearchRequest.Builder()
                    // 再扫描经典设备
                    .searchBluetoothClassicDevice(3000, 2)
                    // 先扫描ble设备
                    .searchBluetoothLeDevice(2000, 2)
                    .build();
    // 开始搜索
    BluetoothCommHelper.get().search(bleSearchRequest, new SearchResponse() {
                    @Override
                    public void onSearchStarted() {
                        Log.d(TAG, "onScanStarted");
                    }

                    @Override
                    public void onDeviceFounded(SearchResult device) {
                        Log.d(TAG, "result: " + device.getName() + " - " + device.toString() + " - rssi: " + device.rssi);
                        String key = device.getName() + device.getAddress();
                        if (device.rssi != 0) {
                            if (!bleDevices.containsKey(key)) {
                                bleDevices.put(key, device);
                                pairItemInfos.add(convertToViewModel(device));
                                pairAdapter.setList(pairItemInfos);
                                if (pairItemInfos.size() > 1) {
                                    pairAdapter.notifyDataSetChanged();
                                } else {
                                    blePairView.setBleDeviceAdapter(pairAdapter);
                                }
                            }
                        }
                    }

                    @Override
                    public void onSearchStopped() {
                        Log.d(TAG, "onSearchStopped: ");
                        blePairView.refreshFished();
                    }

                    @Override
                    public void onSearchCanceled() {
                        Log.d(TAG, "onSearchCanceled: ");
                    }
                });
    // 停止搜索
   BluetoothCommHelper.get().stopSearch();
   
```

### 3. 连接蓝牙

#### 3.1. 经典蓝牙通信

``` java
    // 添加数据解析回调listener
   BluetoothCommHelper.get().addOnDataReceiveListener(this::dataReceiveListener);

    // 开始连接
    /**
     * 经典蓝牙连接服务端
     *
     * @param serviceDevice 服务端的蓝牙设备
     * @param statusListener 连接状态监听
     */
    BluetoothCommHelper.get().classicConnect(curClickDevice.device, this::socketConnectListener);

    /**
     * 经典蓝牙socket连接监听
     */
    private void socketConnectListener(BlueSocketListener.BlueSocketStatus status, BluetoothDevice remoteDevice) {
        switch (status) {
            case CONNECTED:
                // todo 成功业务
                break;
            case CONNECTING:
                break;
            case DISCONNECT:
                // todo 失败业务
                break;
        }
    }
``` 

#### 3.2. ble 蓝牙通信

``` java

    // ble连接
    BluetoothCommHelper.get().connect(curClickDevice,
            (code, data) -> {
                Log.d(TAG, "on ble connect Response: code: " + code + "--data: " + data.toString());
                switch (code) {
                    case Constants.REQUEST_SUCCESS:
                        // `注册蓝牙数据回调`
                        BluetoothCommHelper.get().notify(null,null);
                        // todo 成功业务
                        break;
                    case Constants.REQUEST_FAILED:
                        // todo 失败业务
                        
                        break;
                }
            });
``` 

#### 3.3 经典和ble蓝牙的数据统一回调
    BluetoothCommonHelper.get().addOnDataReceiveListener(OnDataReceiverListener);
