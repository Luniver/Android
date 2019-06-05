package hrst.sczd.ui.base;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hrst.BluetoothCommHelper;
import com.inuker.bluetooth.library.Constants;
import com.inuker.bluetooth.library.connect.listener.BluetoothStateListener;
import com.inuker.bluetooth.library.search.SearchRequest;
import com.inuker.bluetooth.library.search.SearchResult;
import com.inuker.bluetooth.library.search.response.SearchResponse;
import com.orhanobut.logger.Logger;

import java.util.HashMap;
import java.util.Map;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import hrst.sczd.ExitApplication;
import hrst.sczd.R;
import hrst.sczd.agreement.sczd.vo.BluetoothReconnectCmd;
import hrst.sczd.agreement.sczd.vo.JumpToBluetoothSelectCmd;
import hrst.sczd.ui.activity.SettingActivity;
import hrst.sczd.ui.model.ConnectBroken;
import hrst.sczd.utils.SaveData_withPreferences;
import hrst.sczd.utils.UserConfig;
import hrst.sczd.view.gifplay.MyAlertDialog;

/**
 * @author 刘兰
 * @version 1.1.1.3 修改者，修改日期，修改内容
 * @describe activity基类
 * @date 2014.05.23
 */
public abstract class NavigationActivity extends FragmentActivity implements
        OnClickListener {

    private static final int MSG_UI_HEART_CHECK = 0x120;
    private static final String TAG = "sfx-navigation";
    protected LinearLayout leftButton, blueButton, moreButton, numberManageButton;
    public TextView textView;
    String classname, rightbuttontext;
    RelativeLayout rlTitleMainView, rlTitleDefoutView;
    LinearLayout rlMaintitleLayout;
    public ImageButton ibNumberManage;
    public Button ibMorePic;
    public TextView tvBack;
    View mainLayout;
    View titleView;
    protected Handler myHandler;
    protected Context context;
    int thislayout = 0;
    public static int w = 0, h = 0;
    public static Map<String, Handler> gethandlerMap = new HashMap<String, Handler>();

    protected SearchRequest bleSearchRequest;
    protected boolean isLoopSearch = false;

    LayoutParams lParams = new LayoutParams(LayoutParams.FILL_PARENT,
            LayoutParams.WRAP_CONTENT);
    protected SaveData_withPreferences saveData_withPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExitApplication.getInstance().addActivity(this);
        System.gc();
        int sdk = android.os.Build.VERSION.SDK_INT;
        UserConfig.p(this, "sdk: " + sdk);
        context = this;
        double max = Runtime.getRuntime().maxMemory();
        double total = Runtime.getRuntime().totalMemory();
        double free = Runtime.getRuntime().freeMemory();

        EventBus.getDefault().register(this);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        myHandler = new Myhandler();
        gethandlerMap.put(getClass().getName(), myHandler);
        setContentView();
        if (w == 0) {
            WindowManager windowManager = getWindow().getWindowManager();
            w = windowManager.getDefaultDisplay().getWidth();
            h = windowManager.getDefaultDisplay().getHeight();

//			UserConfig.p(this, "当期屏幕宽度： " + w + "高度： " + h);
        }
        // 再扫描经典设备
        // 先扫描ble设备
        bleSearchRequest = new SearchRequest.Builder()
                // 再扫描经典设备
                .searchBluetoothClassicDevice(6000, 1)
                // 先扫描ble设备
                .searchBluetoothLeDevice(2000, 2)
                .build();
        // 蓝牙打开或关闭监听
        BluetoothCommHelper.get().registerBluetoothStateListener(new BluetoothStateListener() {
            @Override
            public void onBluetoothStateChanged(boolean openOrClosed) {
                if (!openOrClosed) {
                    // 蓝牙断开了, 重置状态
                    setBluetoothState(false);
                }
                // 打开, 还需要再监听连接情况
            }
        });

        // ble蓝牙连接状态监听
        BluetoothCommHelper.get().addBluetoothConnectedListener(this.getClass().getName(), (code, data) -> {
            switch (code) {
                case Constants.REQUEST_SUCCESS:
                    // 蓝牙连接上了不显示绿色, 只有设备准备好了, 再显示绿色
//                    setBluetoothState(true);
                    break;
                case Constants.REQUEST_FAILED:
                    setBluetoothState(false);
                    break;
            }
        });

    }

    public class Myhandler extends Handler {

        @SuppressLint("HandlerLeak")
        public Myhandler() {
            super();
        }

        @Override
        public void handleMessage(Message msg) {
            UserConfig.p(this, "更新流程：handler执行-");
            NavigationActivity.this.handleMessage(msg);
        }
    }

    @Override
    protected void onResume() {
        textView.setText(getTitle());
        classname = getIntent().getStringExtra("classname");
        rightbuttontext = getIntent().getStringExtra("morebutton");
        if (getIntent().getBooleanExtra("is_first", false)) {
            leftButton.setVisibility(View.GONE);
        }
        if (getIntent().getBooleanExtra("is_right", false)) {
            moreButton.setVisibility(View.GONE);
        }
        tvBack.setText(getIntent().getStringExtra(com.hrst.common.ui.interfaces.Constants.Common.KEY_BACK_STACK));
        super.onResume();
    }

    /**
     * 要用此函数做跳转，因为被跳转的leftbutton需要当前activity的标题做名称和类名做返回
     *
     * @param intent
     * @param classname 返回跳转的类名 默认当前当前类名
     */
    // ***
    @SuppressLint("NewApi")
    public void startActivity(Intent intent, String classname) {
        Window window;
        if (getParent() != null) {
            window = getParent().getWindow();
        } else {
            window = getWindow();
        }
        intent.putExtra("title", getTitle());
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_USER_ACTION);
        intent.putExtra("classname", classname);
        super.startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    @SuppressLint("NewApi")
    public void startActivity(Intent intent) {
        Window window;
        if (getParent() != null) {
            window = getParent().getWindow();
        } else {
            window = getWindow();
        }

        super.startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * 被所继承类调用设置主xml 因为标题栏的初始化有顺序要求
     */
    public abstract void setContentView();

    @Override
    public void setContentView(int layout) {
        thislayout = layout;
        mainLayout = View.inflate(this, thislayout, null);
        rlMaintitleLayout = (LinearLayout) mainLayout
                .findViewById(R.id.titlelayout);

        titleView = View.inflate(this, R.layout.titlelayout, null);
        if (rlMaintitleLayout != null) {
            rlMaintitleLayout.addView(titleView, lParams);
        }
        leftButton = (LinearLayout) titleView
                .findViewById(R.id.titleleftbutton);
        leftButton.setOnClickListener(this);
        tvBack = (TextView) titleView.findViewById(R.id.tv_back);
        moreButton = (LinearLayout) titleView
                .findViewById(R.id.titlerightbutton);
        moreButton.setOnClickListener(this);
        numberManageButton = (LinearLayout) titleView
                .findViewById(R.id.ll_numbermanage);
        numberManageButton.setOnClickListener(this);
        textView = (TextView) titleView.findViewById(R.id.titletextView1);
        textView.setOnClickListener(this);
        blueButton = (LinearLayout) titleView
                .findViewById(R.id.title_bltooth_layout);

        ibMorePic = (Button) titleView.findViewById(R.id.more_btn);
        ibNumberManage = (ImageButton) titleView
                .findViewById(R.id.ib_numbermanage);
        rlTitleMainView = (RelativeLayout) findViewById(R.id.titlerelative1);
        rlTitleDefoutView = (RelativeLayout) findViewById(R.id.titlerelative2);
        saveData_withPreferences = SaveData_withPreferences.getInstance(this);
        super.setContentView(mainLayout);

        // 初始化蓝牙标志状态
        setBluetoothState(BluetoothCommHelper.get().isConnected());

        //断蓝牙时点击进行蓝牙重连
        blueButton.setOnClickListener(v -> {
            isLoopSearch = true;
            MyAlertDialog.getInstance(this).showLoading("正在连接蓝牙...");
            startTryReConnect();
        });

        blueButton.setClickable(false);
    }

    @Override
    public void setContentView(View view) {
        mainLayout = view;
        rlMaintitleLayout = (LinearLayout) mainLayout
                .findViewById(R.id.titlelayout);

        titleView = View.inflate(this, R.layout.titlelayout, null);
        if (rlMaintitleLayout != null) {
            rlMaintitleLayout.addView(titleView, lParams);
        }
        super.setContentView(mainLayout);
    }

    @Override
    public void onClick(View v) {
        if (v == leftButton) {
            if ("场强分析".equals(textView.getText().toString().trim())) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                dialog.setMessage("是否返回\"目标制式界面\"")
                        .setNegativeButton("取消", new AlertDialog.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        })
                        .setPositiveButton("确定", new AlertDialog.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                onBackPressed();
                                setAnimation();
                                return;
                            }
                        })
                        .create()
                        .show();
            } else {
                onBackPressed();
                setAnimation();
            }
        } else if (v == moreButton) {
            Intent setIntent = new Intent(this, SettingActivity.class);
            setIntent.putExtra("tv_back", this.getTitle());
            startActivity(setIntent);
        }

    }

    // 设置返回动画
    public void setAnimation() {
        this.overridePendingTransition(R.anim.main_cut, R.anim.left_cut);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 这是我navigationbar的重点
     */
    @Override
    public void setTitle(CharSequence title) {
        textView.setText(title);
        super.setTitle(title);
    }

    /**
     * 设置自定义的标题
     */
    public void seothertitle(int featureId) {
        if (featureId == 0) {
            rlTitleMainView.removeAllViews();
            rlTitleMainView.addView(rlTitleDefoutView);
        } else {
            rlTitleMainView.removeAllViews();
            View view = View.inflate(this, featureId, null);
            rlTitleMainView.addView(view, lParams);
        }

    }

    /**
     * 尝试重新连接
     */
    protected void startTryReConnect() {
        myHandler.post(() -> {
            BluetoothCommHelper.get().search(bleSearchRequest, new SearchResponse() {
                @Override
                public void onSearchStarted() {
                }

                @Override
                public void onDeviceFounded(SearchResult device) {
                    Logger.d("result: " + device.getName() + " - " + device.toString() + " - rssi: " + device.rssi);
                    if (device.rssi != 0) {
                        if (device.getAddress().equals(BluetoothCommHelper.get().getCurrentDeviceMac())) {
                            if (device.device.getBondState() == BluetoothDevice.BOND_BONDED) {
                                BluetoothCommHelper.get().removeBond(device.device);
                                isLoopSearch = false;
                            }
                            BluetoothCommHelper.get().stopSearch();
                            // 延时5秒发送重连, 解除配对也需要时间
                            // 实战效果不行, 此机制可以改成监听解除配对状态来实现
                            BluetoothReconnectCmd reconnectCmd = new BluetoothReconnectCmd();
                            reconnectCmd.setCurDevice(device.device);
                            myHandler.postDelayed(() -> EventBus.getDefault().post(reconnectCmd), 5000);

                        }
                    }
                }

                @Override
                public void onSearchStopped() {
                    if (isLoopSearch) {
                        startTryReConnect();
                    }
                }

                @Override
                public void onSearchCanceled() {
                    isLoopSearch = false;
                }
            });
        });
    }
//	// 监听返回
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//			if (textView.getText().toString().equals("系统设置")) {
//				if (getIntent().getStringExtra("tv_back").equals("首页")) {
//					Intent intentMain = new Intent(this, SelectEntryActivity.class);
//					startActivity(intentMain);
//				}
//			}
//			finish();
//			setAnimation();
//		}
//		return super.onKeyDown(keyCode, event);
//	}

    /**
     * 设置标题蓝牙标志状态
     *
     * @param state true 连接中
     *              false 断开连接
     */
    public void setBluetoothState(boolean state) {
        // 连接后不可点击, 断开后可点击
        Logger.d("setBluetoothState: " + state);
        BluetoothCommHelper.get().setConnected(state);
        blueButton.setSelected(state);
        if (state){
            MyAlertDialog.getInstance(this).loadingDiss();
        }
    }

    public abstract void handleMessage(Message msg);

    protected void jumpToBluetoothActivity() {
        EventBus.getDefault().post(new JumpToBluetoothSelectCmd());
    }

    /**
     * 轮询心跳检测到的断开
     * 轮询检测在{@link hrst.sczd.agreement.sczd.McuDataDispatcher}
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onConnectBrokenNotify(ConnectBroken broken) {
        blueButton.setClickable(broken.isBroken());
        blueButton.setSelected(!broken.isBroken());
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void jumpToBluetoothPairActivity(JumpToBluetoothSelectCmd cmd) {
        if (!"蓝牙配对".equals(textView.getText().toString())) {
            finish();
        }
    }

}
