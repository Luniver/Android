package hrst.sczd.ui.manager;

import android.bluetooth.BluetoothDevice;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.hrst.BluetoothCommHelper;
import com.hrst.common.ui.adapter.BluetoothPairAdapter;
import com.hrst.common.ui.interfaces.IBlePairView;
import com.hrst.common.ui.view.PullToRefreshView;
import com.hrst.common.ui.view.gifplay.MyAlertDialog;
import com.hrst.common.ui.vo.BlePairItemInfo;
import com.hrst.common.util.SharedPreferencesUtils;
import com.hrst.common.util.ToastUtils;
import com.hrst.helper.blt.BlueSocketListener;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.inuker.bluetooth.library.receiver.listener.BluetoothBondListener;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hrst.sczd.R;

import static hrst.sczd.ui.activity.BluetoothPairActivity.HAS_BONDED;

/**
 * 蓝牙配对 presenter
 * <p>
 * by Sophimp
 * on 2018/4/24
 */

public class BluetoothPairPresenter implements AdapterView.OnItemClickListener, PullToRefreshView.OnHeaderRefreshListener, View.OnClickListener {
    private static final String TAG = "goo-pair-presenter";
    private IBlePairView blePairView;

    private Map<String, SearchResult> bleDevices;
    private List<BlePairItemInfo> pairItemInfos;
    private BluetoothPairAdapter pairAdapter;
    private BluetoothDevice curClickDevice;
    private int curClickPosition;
    private int lastClickPosition;
    private SearchRequest bleSearchRequest;
    private Handler uiHandler;

    private BluetoothStateListener bluetoothStateListener;

    private BluetoothBondListener bondListener;

    private boolean isReconnecting = false;

    public BluetoothPairPresenter(IBlePairView blePairView, Handler handler) {
        uiHandler = handler;
        this.blePairView = blePairView;
        pairItemInfos = new ArrayList<>();
        bleDevices = new HashMap<>();
        pairAdapter = new BluetoothPairAdapter(blePairView.getCtx());
        blePairView.setBleDeviceAdapter(pairAdapter);
        initBluetooth();
    }

    /**
     * 蓝牙初使化, 不可以放在 application中
     * 在application中获取不到 service
     */
    private void initBluetooth() {
        // 先清空缓存, 防止多次连接造成监听多次回调
        BluetoothCommHelper.get().clearCache();
        bleSearchRequest = new SearchRequest.Builder()
                // 再扫描经典设备
                .searchBluetoothClassicDevice(6000, 1)
                // 先扫描ble设备
                .searchBluetoothLeDevice(2000, 2)
                .build();
//        connectOptions = new BleConnectOptions.Builder()
//                .setConnectRetry(3)   // 连接如果失败重试3次
//                .setConnectTimeout(10000)   // 连接超时10s
//                .setServiceDiscoverRetry(3)  // 发现服务如果失败重试3次
//                .setServiceDiscoverTimeout(20000)  // 发现服务超时20s
//                .build();

        // 注: 所有的状态监听 都要对应消毁
        // 蓝牙打开/关闭监听
        bluetoothStateListener = new BluetoothStateListener() {
            @Override
            public void onBluetoothStateChanged(boolean openOrClosed) {
                Log.d(TAG, "onBluetoothStateChanged: " + openOrClosed);
                if (openOrClosed) {
                    blePairView.setBluetoothSwitchState(true);
                    startScanBleDevices();
                } else {
                    blePairView.setBluetoothSwitchState(false);
                    onBluetoothClosed();
                }
            }
        };
        BluetoothCommHelper.get().registerBluetoothStateListener(bluetoothStateListener);

        // 注册配对连接
        bondListener = new BluetoothBondListener() {
            @Override
            public void onBondStateChanged(String mac, int bondState) {
                Log.d(TAG, "onBondStateChanged: " + bondState);
                handleBondResult(bondState);
            }
        };

        BluetoothCommHelper.get().registerBondStateListener(bondListener);

        // 初始化配对连接页面拦截广播
    }

    private void handleBondResult(int bondState) {
        if (curClickPosition < pairItemInfos.size() && lastClickPosition < pairItemInfos.size()) {
            pairItemInfos.get(lastClickPosition).setPaired(false);
            pairItemInfos.get(lastClickPosition).setChecked(false);
            switch (bondState) {
                case BluetoothDevice.BOND_NONE:
                    if (lastClickPosition != curClickPosition) {
                        ToastUtils.showToast("取消配对");
                    }
                    MyAlertDialog.getInstance(blePairView.getCtx()).loadingDiss();
                    pairItemInfos.get(curClickPosition).setChecked(false);
                    break;
                case BluetoothDevice.BOND_BONDING:
                    break;
                case BluetoothDevice.BOND_BONDED:
                    pairItemInfos.get(curClickPosition).setPaired(true);
                    // 记录连接, 第一次弹窗
                    SharedPreferencesUtils.put(HAS_BONDED, true);
                    connectSelectBleDevice();
                    break;
            }
            lastClickPosition = curClickPosition;
            pairAdapter.setList(pairItemInfos);
            pairAdapter.notifyDataSetChanged();
        } else {
            lastClickPosition = 0;
        }
    }

    /**
     * 蓝牙关闭操作
     */
    private void onBluetoothClosed() {
        disconnect();
        pairItemInfos.clear();
        pairAdapter.setList(pairItemInfos);
        pairAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        curClickPosition = position;
        BlePairItemInfo pairItemInfo = pairItemInfos.get(position);
        // 取消上次的连接
        disconnect();
        curClickDevice = bleDevices.get(pairItemInfo.getName() + pairItemInfo.getAddress()).device;
        isReconnecting = false;
        // 连接设备
        connectSelectDevice();
    }


    private void connectSelectDevice() {
        String name = curClickDevice.getName();
        if (isReconnecting || (!TextUtils.isEmpty(name) && name.toLowerCase().contains("ble"))) {
            // ble蓝牙连接流程
//            connectSelectBleDevice();
            // 监听配对状态, 进行连接
            // 移除配对, 现在硬件端的蓝牙设备必须要每次都重新输入密码连接
            if (curClickDevice.getBondState() == BluetoothDevice.BOND_BONDED) {
                BluetoothCommHelper.get().removeBond(curClickDevice);
//                connectSelectBleDevice();
            } else {
                MyAlertDialog.getInstance(blePairView.getCtx()).showLoading("正在连接蓝牙...");
                BluetoothCommHelper.get().createBond(curClickDevice);
            }
        } else {
            if (null != curClickDevice) {
                BluetoothCommHelper.get().classicDisconnect();
            }
            // 经典蓝牙连接流程
            connectSelectClassicalDevice();
        }
    }

    private void connectSelectClassicalDevice() {
        MyAlertDialog.getInstance(blePairView.getCtx()).showLoading("正在连接蓝牙...");
        // 开始连接
        BluetoothCommHelper.get().classicConnect(curClickDevice, this::socketConnectListener);
    }

    private void connectSelectBleDevice() {
        BluetoothCommHelper.get().connect(curClickDevice,
                (code, data) -> {
                    Log.d(TAG, "on ble connect Response: code: " + code + "--data: " + data.toString());
                    switch (code) {
                        case Constants.REQUEST_SUCCESS:
                            BluetoothCommHelper.get().notify(null, null);
                            MyAlertDialog.getInstance(blePairView.getCtx()).loadingDiss();
                            connectSuccess();
                            break;
                        case Constants.REQUEST_FAILED:
                            MyAlertDialog.getInstance(blePairView.getCtx()).loadingDiss();
                            if (isReconnecting){
                                // 如果是重新连接, 连接失败继续连
                                connectSelectBleDevice();
                            }
                            connectFailed();
                            break;
                    }
                });
//        }

    }

    /**
     * 经典蓝牙socket连接监听
     */
    private void socketConnectListener(BlueSocketListener.BlueSocketStatus status, BluetoothDevice remoteDevice) {
        switch (status) {
            case CONNECTED:
                MyAlertDialog.getInstance(blePairView.getCtx()).loadingDiss();
                connectSuccess();
                break;
            case CONNECTING:
                break;
            case DISCONNECT:
                MyAlertDialog.getInstance(blePairView.getCtx()).loadingDiss();
                connectFailed();
                break;
        }
    }

    /**
     * 连接失败的操作
     */
    public void connectFailed() {
        Log.d(TAG, "connectFailed: ");
        blePairView.onBleConnectFailed();
        if (curClickPosition < pairItemInfos.size()) {
            pairItemInfos.get(lastClickPosition).setChecked(false);
            pairItemInfos.get(curClickPosition).setChecked(false);
            lastClickPosition = curClickPosition;
            pairAdapter.setList(pairItemInfos);
            pairAdapter.notifyDataSetChanged();
        }
        lastClickPosition = 0;

    }

    /**
     * 连接成功的操作
     */
    private void connectSuccess() {
        Log.d(TAG, "connectSuccess: ");
        blePairView.onBleConnectSuccessful();
        // 要先设置last, 因为可能存在两次点一样的情况, 且并不都连接上
        if (curClickPosition < pairItemInfos.size() && lastClickPosition < pairItemInfos.size()) {
            pairItemInfos.get(lastClickPosition).setChecked(false);
            pairItemInfos.get(curClickPosition).setChecked(true);
            lastClickPosition = curClickPosition;
            pairAdapter.setList(pairItemInfos);
            pairAdapter.notifyDataSetChanged();
//            blePairView.setBleDeviceAdapter(pairAdapter);
        } else {
            lastClickPosition = 0;
        }
    }

    @Override
    public void onHeaderRefresh(PullToRefreshView view) {
        if (bltIsOpened()) {
            startScanBleDevices();
        } else {
            blePairView.refreshFished();
        }
    }

    /**
     * 开始扫描蓝牙设备
     */
    public void startScanBleDevices() {
        bleDevices.clear();
        pairItemInfos.clear();
        uiHandler.post(() -> {
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
        });
    }

    private BlePairItemInfo convertToViewModel(SearchResult device) {
        BlePairItemInfo info = new BlePairItemInfo();
        info.setAddress(device.getAddress());
        info.setName(device.getName());
        info.setRssi(device.rssi + "");
        if (BluetoothDevice.BOND_BONDED == device.device.getBondState()) {
            info.setPaired(true);
        } else {
            info.setPaired(false);
        }
        return info;
    }

    /**
     * 重新连接蓝牙
     * @param connectDevice
     */
    public void reconnectBluetooth(BluetoothDevice connectDevice) {
        isReconnecting = true;
        curClickDevice = connectDevice;
        if (BluetoothCommHelper.get().isBluetoothOpened()) {
            connectSelectDevice();
        } else {
            BluetoothCommHelper.get().openBluetooth();
        }
    }

    public boolean bltIsOpened() {
        return BluetoothCommHelper.get().isBluetoothOpened();
    }

    public void destroy() {
        if (bluetoothStateListener != null) {
            BluetoothCommHelper.get().unregisterBluetoothStateListener(bluetoothStateListener);
        }
        BluetoothCommHelper.get().unregisterBluetoothBondListener();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.cb_bluetooth) {
            cbSwitchCheckedChange(!bltIsOpened());
        }
    }

    // 蓝牙开关操作
    private void cbSwitchCheckedChange(boolean isChecked) {
        if (isChecked) {
            BluetoothCommHelper.get().openBluetooth();
        } else {
            BluetoothCommHelper.get().stopSearch();
//            BluetoothCommHelper.get().releaseResource();
            BluetoothCommHelper.get().classicDisconnect();
            BluetoothCommHelper.get().closeBluetooth();
            blePairView.refreshFished();
            onBluetoothClosed();
        }
    }

    public void disconnect() {
        BluetoothCommHelper.get().stopSearch();
        if (null != curClickDevice) {
            String deviceName = curClickDevice.getName();
            if (!TextUtils.isEmpty(deviceName) && curClickDevice.getName().contains("ble")) {
                BluetoothCommHelper.get().removeBond(curClickDevice);
//                BluetoothCommHelper.get().bleDisconnect(curClickDevice.getAddress());
            } else {
                BluetoothCommHelper.get().classicDisconnect();
            }
        }
//        BluetoothCommHelper.get().releaseResource();
    }


    public void connectWithPin() {
        boolean res = BluetoothCommHelper.get().connectWithPin();
    }
}
