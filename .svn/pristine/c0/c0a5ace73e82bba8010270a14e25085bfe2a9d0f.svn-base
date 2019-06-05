package hrst.sczd.ui.activity;

import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hrst.common.ui.animation.CommonAnimation;
import com.hrst.common.ui.animation.PushDownAnimation;
import com.hrst.common.ui.interfaces.Constants;
import com.hrst.common.ui.interfaces.IBlePairView;
import com.hrst.common.ui.view.PullToRefreshView;
import com.hrst.common.ui.view.gifplay.MyAlertDialog;
import com.hrst.common.util.SharedPreferencesUtils;
import com.hrst.common.util.ToastUtils;
import com.orhanobut.logger.Logger;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import hrst.sczd.R;
import hrst.sczd.agreement.manager.InteractiveManager;
import hrst.sczd.agreement.sczd.vo.BluetoothReconnectCmd;
import hrst.sczd.agreement.sczd.vo.FPGA_TO_PDA_HEART;
import hrst.sczd.agreement.sczd.vo.MCU_TO_PDA_TEMPERATURE_AND_ELECTRICITY_STATE;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_TIMING_ACK;
import hrst.sczd.ui.base.NavigationActivity;
import hrst.sczd.ui.manager.BluetoothPairPresenter;

/**
 * 蓝牙配对页面
 * 集成新的蓝牙模块
 *
 * 若公用此代码, 默认下一步跳转 {@link InduceConfigActivity}
 * 若自定义下一步跳转页, 可按如下方式传参:
 * 下一页的目标跳转activity
 * intent.putExtraString(Constants.Common.KEY_NEXT_STEP, activityName);
 * 再下一页的目标跳转activity
 * intent.putExtraString(Constants.Common.KEY_DEFAULT_INDEX_NAME, activityName);
 * by Sophimp
 * on 2018/4/24
 */
public class BluetoothPairActivity extends NavigationActivity implements IBlePairView {
    public static final String HAS_BONDED = "has_bonded";
    private CheckBox cbBluetooth;// 蓝牙开关
    private ListView lvBluetooth;// 蓝牙设备列表
    private RelativeLayout rlHint;// 提示连接断开的布局
    private LinearLayout llHasNoShow, llLargetLayout;
    private Button btnNext;// 下一步按钮
    String option = "";// 检测版本用来显示dialog
    private View headview;
    // DSP状态
    long state = -1;
    // 蓝牙设备列表
    private PullToRefreshView pullToRefreshView = null;// 下拉刷新

    private BluetoothPairPresenter presenter;

    private BluetoothParingReceiver paringReceiver = new BluetoothParingReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        pullToRefreshView.onHeaderRefreshComplete();// 下拉刷新完成
        pullToRefreshView.onFooterRefreshComplete();// 上拉刷新完成
        pullToRefreshView.setNoFlashByFoot(true);// 屏蔽上拉刷新功能
        moreButton.setVisibility(View.INVISIBLE);
        setBluetoothState(false);
    }

    @Override
    public void setContentView() {
        this.setTheme(android.R.style.Theme_Black_NoTitleBar);
        setContentView(R.layout.activity_bluetooth_pair);
        setTitle("蓝牙配对");
        leftButton.setVisibility(View.GONE);
        moreButton.setVisibility(View.VISIBLE);
        blueButton.setVisibility(View.VISIBLE);
        Intent intent = getIntent();
        option = intent.getStringExtra("option");
//        EventBus.getDefault().register(this);
    }

    /**
     * 初始化控件
     */
    private void init() {

        presenter = new BluetoothPairPresenter(this, myHandler);

        btnNext = (Button) findViewById(R.id.btn_next);
        rlHint = (RelativeLayout) findViewById(R.id.rl_hint);
        pullToRefreshView = (PullToRefreshView) findViewById(R.id.pullToRefreshView);
        pullToRefreshView.setOnHeaderRefreshListener(presenter);// 下拉刷新
        cbBluetooth = (CheckBox) findViewById(R.id.cb_bluetooth);
        cbBluetooth.setOnClickListener(presenter);
        lvBluetooth = (ListView) findViewById(R.id.lsv_bluetooth);
        headview = View.inflate(this, R.layout.hint_layout, null);
        llHasNoShow = (LinearLayout) headview.findViewById(R.id.ll_hasno_devices);
        llHasNoShow.setVisibility(View.GONE);
        llLargetLayout = (LinearLayout) findViewById(R.id.ll_largetlayout);
        lvBluetooth.addFooterView(headview);
        lvBluetooth.setOnItemClickListener(presenter);

        btnNext.setOnClickListener(this::nextStep);
        if (presenter.bltIsOpened()) {
            presenter.startScanBleDevices();
        } else {
            cbBluetooth.setChecked(false);
//            btnNext.setEnabled(false);
            ToastUtils.showToast( "亲，蓝牙未打开，请先打开蓝牙");
        }

        // 注册蓝牙配对连接广播 拦截
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_PAIRING_REQUEST);
        registerReceiver(paringReceiver,filter);
    }

    /**
     * 重新连接蓝牙
     * (按钮点击事件)
     */
    public void connectBluetoothAgain(View view) {
//        hideBleFailedPush();
//        presenter.startScanBleDevices();
//        presenter.reconnectBluetooth(cmd.getCurDevice());
    }

    /**
     * 关闭蓝牙断开连接提示栏
     */
    public void closeHintLayout(View view) {
        hideBleFailedPush();
    }


    // 注册一下, 屏蔽日志报错
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void fpgaHeart(FPGA_TO_PDA_HEART obj) {}

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void batterInfo(MCU_TO_PDA_TEMPERATURE_AND_ELECTRICITY_STATE obj) {}

    /**
     * 获取授时应答
     *
     * @param obj
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getTiming(PDA_TO_MCU_TIMING_ACK obj) {
        //0：授时成功 1：授时失败
        if (obj.getcResult_a() == 0) {
            ToastUtils.showToast("授时成功");
        } else {
            ToastUtils.showToast("授时失败");
        }
    }
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void reconnectBluetooth(BluetoothReconnectCmd cmd){
        presenter.reconnectBluetooth(cmd.getCurDevice());
    }

    /**
     * 下一步操作
     *
     * @param view
     */
    public void nextStep(View view) {
        // 发送授时
        InteractiveManager.getInstance().sendTiming();
        // 发送调试开关命令
        InteractiveManager.getInstance().sendDebugging(SharedPreferencesUtils.getBoolean(Constants.Common.KEY_DEBUG));

        Intent intentNext = new Intent(this, InduceConfigActivity.class);
        intentNext.putExtra(Constants.Common.KEY_BACK_STACK, getTitle());
        // 默认的流程 蓝牙配对 -> 诱发配置 -> 各应用主页
        String nextActivityName = getIntent().getStringExtra(Constants.Common.KEY_DEFAULT_INDEX_NAME);
        intentNext.putExtra(Constants.Common.KEY_NEXT_STEP, nextActivityName);
        startActivity(intentNext);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    protected void onDestroy() {
        MyAlertDialog.getInstance(this).loadingDiss();
//        EventBus.getDefault().unregister(this);
        presenter.destroy();
        unregisterReceiver(paringReceiver);
        super.onDestroy();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN
                && event.getRepeatCount() == 0) {
            // 关闭蓝牙连接, 不需要解除配对
            presenter.disconnect();
//            startActivity(new Intent(BluetoothPairActivity.this, hrst.InduceConfigActivity.class));
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setTitle("确定退出程序吗？");
            dialog.setPositiveButton("确定", (dialog1, which) -> finish());
            dialog.setNegativeButton("取消", (dialog12, which) -> {});
            dialog.create();
            dialog.show();
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void handleMessage(Message msg) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void setBleDeviceAdapter(BaseAdapter adapter) {
        if (lvBluetooth != null) {
            lvBluetooth.setAdapter(adapter);
        }
    }

    @Override
    public void onBleConnectFailed() {
        showToast("连接断开");
//        btnNext.setEnabled(false);
        setBluetoothState(false);
        refreshFished();
//        showBleFailedPush();
    }

    @Override
    public void onBleConnectSuccessful() {
        showToast("连接成功");
//        btnNext.setEnabled(true);
        setBluetoothState(true);
        refreshFished();
    }


    /**
     * 显示蓝牙失去连接提示
     */
    private void showBleFailedPush() {
        if (llLargetLayout.getVisibility() != View.VISIBLE) {
            int childHeight = llLargetLayout.getMeasuredHeight();
            PushDownAnimation expandAni = new PushDownAnimation(context, llLargetLayout, 300, (int) childHeight);
            llLargetLayout.startAnimation(expandAni);
            CommonAnimation.translate(context, 0, 0, -childHeight, 0, rlHint, 300, 0, false, 0);
        }
    }

    /**
     * 隐藏蓝牙连接失败提示
     */
    private void hideBleFailedPush() {
        if (llLargetLayout.getVisibility() == View.VISIBLE) {
            int childHeight = llLargetLayout.getMeasuredHeight();
            PushDownAnimation expandAni = new PushDownAnimation(context, llLargetLayout, 300, (int) childHeight);
            llLargetLayout.startAnimation(expandAni);
            CommonAnimation.translate(context, 0, 0, 0, -childHeight, rlHint, 300, 0, false, 0);
        }
    }

    @Override
    public void refreshFished() {
        pullToRefreshView.onHeaderRefreshComplete();
    }

    public void showToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getCtx() {
        return this;
    }

    @Override
    public void setBluetoothSwitchState(boolean state) {
        cbBluetooth.setChecked(state);
    }

        /**
     * 蓝牙配对页面拦截 broadcast
     *
     * Created by Sophimp on 2018/10/29.
     */
    public class BluetoothParingReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(BluetoothDevice.ACTION_PAIRING_REQUEST.equalsIgnoreCase(intent.getAction())){
//                Logger.d("ACTION_PAIRING_REQUEST");
                if(SharedPreferencesUtils.getBoolean(HAS_BONDED)){
                    if (isOrderedBroadcast()){
                        abortBroadcast();
                        Logger.d("abort paring broadcast");
                    }
                    presenter.connectWithPin();
                }
            }
        }
    }

}
