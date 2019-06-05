package hrst.sczd.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.util.ArrayList;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import hrst.sczd.ExitApplication;
import hrst.sczd.R;
import hrst.sczd.agreement.sczd.vo.MCU_TO_PDA_FIRMWARE_STARTUP_ANOMALY;
import hrst.sczd.agreement.sczd.vo.MCU_TO_PDA_TEMPERATURE_AND_ELECTRICITY_STATE;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_DT_GAIN_SETTING_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_GAIN_SETTING_ACK;
import hrst.sczd.broadcast.SimStateBroadcast;
import hrst.sczd.ui.activity.fragment.fieldAnalysis.DevicesFragment;
import hrst.sczd.ui.activity.fragment.fieldAnalysis.FragmenDetection;
import hrst.sczd.ui.activity.fragment.fieldAnalysis.FragmentMSM;
import hrst.sczd.ui.activity.fragment.fieldAnalysis.TargetFragment;
import hrst.sczd.ui.adapter.MyFragmentPagerAdapter;
import hrst.sczd.ui.base.NavigationActivity;
import hrst.sczd.ui.manager.FieldAnalysisManager;
import hrst.sczd.ui.manager.SkinSettingManager;
import hrst.sczd.ui.model.CommunityModel;
import hrst.sczd.ui.model.SimCardAbsent;
import hrst.sczd.view.gifplay.MyAlertDialog;

/**
 * @author 刘兰
 * @version 1.1.1.3 修改者，修改日期，修改内容
 * @describe 场强分析
 * @date 2014.05.23
 */
public class FieldAnalysisActivity extends NavigationActivity implements OnPageChangeListener  {
    private static final String TAG = "goo-FieldAnalysisAct";
    Fragment devicesFragment = null;
    Fragment targetFragment = null;
    Fragment fragmentMsm = null, fragmentDetection = null;
    private ArrayList<Fragment> fragmentsList;
    private RadioButton rbDevices, rbTarget, rbInducedMode, rbCheckState;
    private ViewPager viewPager;
    ImageView ivHint;
    private Context context;
//    private CommunityModel model;

    // 跳转标示
    public final static int SHOW_MSM_CONTENT = 300;
    public final static int SHOW_COM = 100;

    private TelephonyManager tm;

    /**
     * 强场分析管理类
     */
    private FieldAnalysisManager manager;
    private SimStateBroadcast simStateBroadcast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ExitApplication.getInstance().addActivity(this);
        MyAlertDialog.getInstance(FieldAnalysisActivity.this).loadingDiss();
        init();
        initViewPager();
        Logger.d("goo sms field activity on create");
        CommunityModel.getInstance(this).clearAllData();
//        EventBus.getDefault().register(this);
        manager = new FieldAnalysisManager(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Logger.d("goo sms field activity on save instance state:" + outState.toString());
        super.onSaveInstanceState(outState);
    }

    /**
     * 设置布局，标题与视觉模式
     */
    @Override
    public void setContentView() {
        SkinSettingManager skinManager = new SkinSettingManager(this);
        if (skinManager.getCurrentSkinRes() == 0) {
            this.setTheme(android.R.style.Theme_Black_NoTitleBar);
        } else {
            this.setTheme(android.R.style.Theme_Light_NoTitleBar);
        }
        setContentView(R.layout.activity_fieldanalysis_sczd);
        setTitle("场强分析");
        tvBack.setText("蓝牙配对");
        moreButton.setVisibility(View.VISIBLE);
        blueButton.setVisibility(View.VISIBLE);

    }

    /**
     * 初始化控件
     */
    private void init() {
        viewPager = (ViewPager) findViewById(R.id.vPager);
        rbDevices = (RadioButton) findViewById(R.id.rb_devices);
        rbDevices.setChecked(true);
        rbTarget = (RadioButton) findViewById(R.id.rb_target);
        rbInducedMode = (RadioButton) findViewById(R.id.rb_induced_mode);
        rbCheckState = (RadioButton) findViewById(R.id.rb_check_state);
        ivHint = (ImageView) findViewById(R.id.hint_img);

        rbDevices.setOnClickListener(new MyOnClickListener(0));
        rbTarget.setOnClickListener(new MyOnClickListener(1));
        rbInducedMode.setOnClickListener(new MyOnClickListener(2));
        rbCheckState.setOnClickListener(new MyOnClickListener(3));

//        model = CommunityModel.getInstance(this);

        simStateBroadcast = new SimStateBroadcast();
        IntentFilter filter = new IntentFilter("android.intent.action.SIM_STATE_CHANGED");
//        registerReceiver(simStateBroadcast, filter);
    }

    /**
     * 界面获取焦点时执行
     */
    @Override
    protected void onResume() {
        Logger.d("goo FieldAnalysisActivity onResume");
//        if (sPreferences.getData_String(SettingUtils.TempHighValue) == null
//                || sPreferences.getData_String(SettingUtils.TempHighValue)
//                .equals("")) {
//            sPreferences.saveDatas_String(SettingUtils.TempHighValue, "65");
//        }
//        if (sPreferences.getData_String(SettingUtils.TempHighValue) == null
//                || sPreferences
//                .getData_String(SettingUtils.HostDeficiencyValue)
//                .equals("")) {
//            sPreferences.saveDatas_String(SettingUtils.HostDeficiencyValue,
//                    "20");
//        }
        super.onResume();
    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        fragmentsList = new ArrayList<Fragment>();
        devicesFragment = new DevicesFragment();
        targetFragment = new TargetFragment();
        fragmentMsm = new FragmentMSM();
        fragmentDetection = new FragmenDetection();

        fragmentsList.add(devicesFragment);
        fragmentsList.add(targetFragment);
        fragmentsList.add(fragmentMsm);
        fragmentsList.add(fragmentDetection);
        viewPager.setAdapter(new MyFragmentPagerAdapter(
                getSupportFragmentManager(), fragmentsList));
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setOnPageChangeListener(this);
    }

    /**
     * 获取电量和温度信息
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getTemperatureAndElectricity(MCU_TO_PDA_TEMPERATURE_AND_ELECTRICITY_STATE obj) {
        manager.showTemperatureAndElectricity(obj);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getDesGainSettingAck(PDA_TO_MCU_REQUEST_GAIN_SETTING_ACK obj) {
        if (obj.getcResult_a() == 0) {
            Toast.makeText(this, "目标增益下发成功", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "目标增益下发失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getDtGainSettingAck(PDA_TO_MCU_REQUEST_DT_GAIN_SETTING_ACK obj) {
        if (obj.getcResult_a() == 0){
            Toast.makeText(this, "数传增益下发成功", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "数传增益下发失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * sim 卡拔出, 退出当前页, 因为, sim卡状态改变会刷新当前activity多次
     * 消息发出在 ${@link hrst.sczd.broadcast.SimStateBroadcast } 中
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onSimCardAbsent(SimCardAbsent obj){
//        unregisterReceiver(simStateBroadcast);
        new AlertDialog.Builder(this)
                .setTitle("重要提示")
                .setMessage("SIM卡拔出, 请重新配置参数")
                .setPositiveButton("确定", (dialog, which) -> {
                    FieldAnalysisActivity.this.finish();
                })
                .show();
    }

    /**
     * 设备上报固件启动异常
     *
     * @param obj
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getFirmwareStartupAnomaly(MCU_TO_PDA_FIRMWARE_STARTUP_ANOMALY obj) {
//		manager.showFirmwareStartupAnomaly(obj);
    }

    /**
     * 返回按钮
     *
     * @param view
     */
    public void goBack(View view) {
        finish();
    }

    /**
     * 增益方式
     *
     * @param view
     */
    @SuppressLint("NewApi")
    public void gainWay(View view) {
        Intent gainIntent = new Intent();
        gainIntent.setClass(this, GainWayActivity.class);
        gainIntent.putExtra("tv_back", getTitle());
        startActivity(gainIntent);
        this.overridePendingTransition(R.anim.right_cut, R.anim.main_cut);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int arg0) {
        switch (arg0) {
            case 0:
                rbDevices.setChecked(true);
                rbTarget.setChecked(false);
                rbInducedMode.setChecked(false);
                rbCheckState.setChecked(false);
                break;
            case 1:
                rbTarget.setChecked(true);
                rbDevices.setChecked(false);
                rbInducedMode.setChecked(false);
                rbCheckState.setChecked(false);
                break;
            case 2:
                rbTarget.setChecked(false);
                rbDevices.setChecked(false);
                rbInducedMode.setChecked(true);
                rbCheckState.setChecked(false);
                break;
            case 3:
                rbTarget.setChecked(false);
                rbDevices.setChecked(false);
                rbInducedMode.setChecked(false);
                rbCheckState.setChecked(true);
                break;

            default:
                break;
        }
    }

    public class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        @Override
        public void onClick(View v) {
            if (viewPager != null) {
                viewPager.setCurrentItem(index);
            }
        }
    }

    @Override
    public void handleMessage(Message msg) {

    }

   // todo 以前的接口, 待移植业务
    public void setDetection(boolean booDetection) {
//        if (llIncude.getVisibility() == View.VISIBLE) {
            Drawable drawable = getResources().getDrawable(
                    R.drawable.hint_light);

            // / 这一步必须要做,否则不会显示.

            drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                    drawable.getMinimumHeight());

            rbCheckState.setCompoundDrawables(drawable, null, null, null);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sms_history:
//                startActivity(new Intent(FieldAnalysisActivity.this, SmsHistoryActivity.class));
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        Logger.d("sms field activity on start");
        super.onStop();
    }

    /**
     * 界面销毁时执行
     */
    @Override
    protected void onDestroy() {
        Logger.d("sms field activity on destroy");
        fragmentsList.clear();
        devicesFragment = null;
        targetFragment.onDestroy();
        targetFragment = null;
        fragmentMsm = null;
        fragmentDetection = null;
        super.onDestroy();
    }
}
