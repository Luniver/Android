package hrst.sczd.ui.activity.interfaces;

import android.content.Context;
import android.widget.BaseAdapter;

/**
 * hrst.sczd.ui.activity.interfaces
 * by Sophimp
 * on 2018/4/24
 */

public interface IBlePairView {

    /**
     * 设置蓝牙连接列表
     */
    void setBleDeviceAdapter(BaseAdapter adapter);

    /**
     * 蓝牙连接失败, UI处理
     */
    void onBleConnectFailed();

    /**
     * 蓝牙连接成功, UI处理
     */
    void onBleConnectSuccessful();

    /**
     * 结束下拉刷新
     */
    void refreshFished();

    /**
     * toast 提示
     */
    void showToast(String string);

    Context getCtx();

    /**
     * 设置蓝牙swith 状态
     */
    void setBluetoothSwitchState(boolean state);
}
