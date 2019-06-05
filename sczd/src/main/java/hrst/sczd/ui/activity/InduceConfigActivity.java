package hrst.sczd.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hrst.common.ui.InduceConfigure;
import com.hrst.common.ui.database.dao.UsedRecordDao;
import com.hrst.common.ui.dialog.NumberDialogActivity;
import com.hrst.common.ui.errorlog.CrashHandlerCallBack;
import com.hrst.common.ui.interfaces.Constants;
import com.hrst.common.ui.utils.country.CharacterParserUtil;
import com.hrst.common.ui.utils.country.CountrySortModel;
import com.hrst.common.ui.utils.country.GetCountryNameSort;
import com.hrst.common.ui.view.gifplay.MyAlertDialog;
import com.hrst.common.util.ToastUtils;
import com.hrst.induce.annotation.TargetFormat;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import hrst.sczd.BuildConfig;
import hrst.sczd.R;
import hrst.sczd.agreement.manager.InteractiveManager;
import hrst.sczd.agreement.sczd.vo.MCU_TO_PDA_HEART;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_MODE_SETTING;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_MODE_SETTING_ACK;
import hrst.sczd.ui.base.NavigationActivity;
import hrst.sczd.ui.manager.InduceConfigManager;
import hrst.sczd.ui.model.ConnectBroken;

/**
 * create by Sophimp on 2018/7/16
 */
public class InduceConfigActivity extends NavigationActivity implements OnCheckedChangeListener, CrashHandlerCallBack {
    private static final String TAG = "goo-InduceConfigActivity";
    private TextView tvName, tvPhoneNumber, tv_msm_type;
    private CheckBox cbOpenCountry;
    private RadioGroup rgUnFindFormat;
    private RadioButton
            rbGSM,
            rbCDMA, rbLTE,
            rbTDS_CDMA, rbWCDMA;
    private static final int NUMBER_FOR_USER_INPUT = 0;

    public static Context context;

    //国家码控件
    private RelativeLayout relative_choseCountry;
    private TextView tv_countryName, tv_country;
    private GetCountryNameSort countryChangeUtil;
    private CharacterParserUtil characterParserUtil;
    private List<CountrySortModel> mAllCountryList;

    UsedRecordDao usedDao;

    private InduceConfigManager manager;

    private PDA_TO_MCU_REQUEST_MODE_SETTING modeSetting = new PDA_TO_MCU_REQUEST_MODE_SETTING();

    private Button nextButton;
    private boolean deviceIsReady = false;

    /**
     * 设备就绪, 初始化操作
     */
    private boolean firstHandle = false;
    private AlertDialog alertDialog;
    private boolean isNeedToUpgrade;
    private AlertDialog warnningDialog;
    private volatile boolean hasEntered = false;

    @Override
    public void setContentView() {
        // 初始化皮肤管理类
        this.setTheme(android.R.style.Theme_Black_NoTitleBar);
        setContentView(R.layout.activity_induce_config);
        setTitle("目标号码制式");
        moreButton.setVisibility(View.INVISIBLE);
        blueButton.setVisibility(View.VISIBLE);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
//		EventBus.getDefault().register(this);
//		bluetoothConnection = new BluetoothCommunication(this);
        usedDao = new UsedRecordDao(this);
        init();
        // 国家码
        initCountryList();
        setListener();
    }

    private void setListener() {

        relative_choseCountry.setOnClickListener(v -> {
            Intent intent = new Intent(InduceConfigActivity.this, CountryActivity.class);
            startActivityForResult(intent, 12);
        });
    }

    private void initCountryList() {
        String[] countryList = getResources().getStringArray(R.array.country_code_list_ch);

        for (int i = 0, length = countryList.length; i < length; i++) {
            String[] country = countryList[i].split("\\*");

            String countryName = country[0];
            String countryNumber = country[1];
            String countrySortKey = characterParserUtil.getSelling(countryName);
            CountrySortModel countrySortModel = new CountrySortModel(countryName, countryNumber,
                    countrySortKey);
            String sortLetter = countryChangeUtil.getSortLetterBySortKey(countrySortKey);
            if (sortLetter == null) {
                sortLetter = countryChangeUtil.getSortLetterBySortKey(countryName);
            }

            countrySortModel.sortLetters = sortLetter;
            mAllCountryList.add(countrySortModel);
        }

    }

    /**
     * 初始化需要操作的控件
     */
    private void init() {
        tvPhoneNumber = (TextView) this.findViewById(R.id.tv_phonenumber);
        tvName = (TextView) this.findViewById(R.id.tv_name);
        rgUnFindFormat = (RadioGroup) findViewById(R.id.rg_unfind_format);
        rgUnFindFormat.setOnCheckedChangeListener(this);
        rbGSM = (RadioButton) this.findViewById(R.id.rb_nofind_first);
        rbCDMA = (RadioButton) this.findViewById(R.id.rb_nofind_second);
        rbLTE = (RadioButton) this.findViewById(R.id.rb_nofind_third);
        rbTDS_CDMA = (RadioButton) this.findViewById(R.id.rb_nofind_four);
        rbWCDMA = (RadioButton) this.findViewById(R.id.rb_nofind_five);

        tv_msm_type = (TextView) findViewById(R.id.tv_msm_type);

        nextButton = findViewById(R.id.bt_next);
        nextButton.setOnClickListener(this);

        //国家码开关
        cbOpenCountry = (CheckBox) this.findViewById(R.id.cb_open_country);

        //国家码
        relative_choseCountry = (RelativeLayout) findViewById(R.id.rala_chose_country);
        tv_countryName = (TextView) findViewById(R.id.tv_chosed_country);
        tv_country = (TextView) findViewById(R.id.tv_country);

        mAllCountryList = new ArrayList<CountrySortModel>();
        countryChangeUtil = new GetCountryNameSort();
        characterParserUtil = new CharacterParserUtil();

        //国家码和名称
        String countryNum = tv_country.getText().toString();
        InduceConfigure.countryNum = countryNum;
        InduceConfigure.countryName = tv_countryName.getText().toString();
        manager = new InduceConfigManager(context, tvPhoneNumber, tvName, rbGSM, rbCDMA, rbLTE,
                rbTDS_CDMA, rbWCDMA, cbOpenCountry, tv_country, tv_countryName);
        manager.initData();
    }


    /**
     * 选择诱发短信格式
     *
     * @param v
     */
    public void selectSmsType(View v) {
        manager.selectSmsType(tv_msm_type);
    }

    /**
     * 设备断开连接通知
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onConnectBroke(ConnectBroken obj) {
        if (obj.isBroken()) {
            deviceIsReady = false;
        }
    }

    /**
     * 接收MCU心跳信息 (放在蓝牙连接页面)
     *
     * @param obj
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getMcuHeart(MCU_TO_PDA_HEART obj) {

        isNeedToUpgrade = false;

        byte state = obj.getcHeartState_a();

        /*
            -3: 制式切换异常
            -2: 硬件版本异常
            -1: 状态错误
            0：未定义
            1：设备初始化中
            2：设备配置中
            3：设备运行中，表示设备可以正常使用 */
        switch (state) {
            case -3:
            case -2:
            case -1:    // 状态错误
                // 状态错误, 需要跳转升级固件
                isNeedToUpgrade = true;
                // 闪烁
                ToastUtils.showToast(getString(R.string.device_status_error));
                break;
            case 0:
            case 1:
            case 2:
                deviceIsReady = false;
                firstHandle = true;
                break;
            case 3:
                deviceIsReady = true;
                if (firstHandle) {
                    firstHandle = false;
                    // 硬切换不会返回制式下发成功信息
//                    myHandler.removeCallbacks(settingOvertimeCheck);
                    ToastUtils.showToast(getString(R.string.device_status_ready));
                    MyAlertDialog.getInstance(this).loadingDiss();
                    if (null == alertDialog) {
                        alertDialog = new AlertDialog.Builder(this)
                                .setTitle(R.string.check_hint)
                                .setMessage(R.string.device_status_ready)
                                .setPositiveButton(R.string.yes, (which, btn) -> {
                                    alertDialog.dismiss();
                                })
                                .create();
                    }
                    alertDialog.show();
                    // 发送授时
                    InteractiveManager.getInstance().sendTiming();
                }
                break;
            default:
                break;
        }
    }

    @Subscribe
    public void onModeSetting(PDA_TO_MCU_REQUEST_MODE_SETTING_ACK obj) {
        /*
         * 链路配置失败：-4
         * 资源配置失败：-3
         * 设备未就绪：-2
         * 设置失败：-1
         * 设置成功: 0
         * 无需切换：1
         * 资源配置成功：3
         * 链路配置成功：4
         */
        int res = obj.getcResult_a();
        switch (res) {
            case -6:    // 硬切换出错
                ToastUtils.showToast("硬切换制式出错");
                break;
            case -5:
                ToastUtils.showToast("当前设备不支持此制式切换");
                MyAlertDialog.getInstance(this).loadingDiss();
                nextButton.setEnabled(true);
                break;
            case -4:
                ToastUtils.showToast("链路配置失败：");
                break;
            case -3:
                ToastUtils.showToast("资源配置失败 ");
                break;
            case -2:
                ToastUtils.showToast("设备未就绪");
                break;
            case -1:
                ToastUtils.showToast("设置失败");
                MyAlertDialog.getInstance(this).loadingDiss();
                nextButton.setEnabled(true);
                break;
            case 0:
                ToastUtils.showToast("制式下发成功");
                jumpToNext();
                break;
            case 1:
                ToastUtils.showToast("无需切换");
                jumpToNext();
                break;
            case 3:
                ToastUtils.showToast("资源配置成功");
                break;
            case 4:
                ToastUtils.showToast("链路配置成功");
                break;
            case 6:
                // 硬切换, 设备会断电重启, 需提示等待60秒, 然后定时搜索蓝牙, 搜索到后自动连接
                MyAlertDialog.getInstance(this).loadingDiss();
                MyAlertDialog.getInstance(this).showLoading(getString(R.string.change_format_refresh_60s));
                isLoopSearch = true;
                startTryReConnect();
                break;
        }
    }


    /**
     * 点击输入号码监听方法
     *
     * @param view
     */
    public void inputNumber(View view) {
        Intent intentNumber = new Intent();
        intentNumber.putExtra("number", tvPhoneNumber.getText().toString());
        intentNumber.putExtra("name", tvName.getText().toString());
        intentNumber.setClass(this, NumberDialogActivity.class);
        startActivityForResult(intentNumber, NUMBER_FOR_USER_INPUT);
    }

    /**
     * 号码管理控件的监听方法
     *
     * @param view
     */
    @SuppressLint("NewApi")
    public void numberManger(View view) {
        Intent intentNumberManage = new Intent();
        // 直接复制过去改个包名是行不通的
//		intentNumberManage.setClass(this, hrst.singlepawn.numbermanage.NumberManageActivity.class);
        intentNumberManage.setClass(this, NumberManageActivity.class);
        intentNumberManage.putExtra(Constants.Common.KEY_BACK_STACK, "目标号码制式");
        startActivityForResult(intentNumberManage, NUMBER_FOR_USER_INPUT);
        this.overridePendingTransition(R.anim.right_cut, R.anim.main_cut);
    }

    /**
     * 跳转到蓝牙配对的按钮监听方法
     */
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == nextButton.getId()) {
            // 调试代码
//            jumpToNext();

            // 下发配置后, 等待设置回调, 配置成功, 才能进入下一步

            if (!manager.onNext(usedDao)) return;

            // 如果是状态错误, 需要跳转到升级界面升级
            if (isNeedToUpgrade) {
                jumpToUpgrade();
                return;
            }

            if (!deviceIsReady) {
                ToastUtils.showToast("设备未就绪, 请稍等");
                return;
            }

            InteractiveManager.getInstance().sendRequestModeSetting(modeSetting);

            MyAlertDialog.getInstance(this).showLoading(getString(R.string.change_format_refresh_30s));

            nextButton.setEnabled(false);

            // 如果60s后还没有反应, 设置超时, 80秒是预留空间
            // 现在不再设置超时取消了
//            myHandler.postDelayed(settingOvertimeCheck, 80000);
        }
    }

    /**
     * 跳转到升级界面
     */
    private void jumpToUpgrade() {

        if (warnningDialog == null) {

            warnningDialog = new AlertDialog.Builder(this)
                    .setTitle("警告")
                    .setMessage("当前设备状态错误, 需要排查或者升级固件")
                    .setPositiveButton(R.string.yes, ((dialog, which) -> startActivity(new Intent(this, AboutUsActivity.class))))
                    .setNegativeButton(R.string.no, (((dialog, which) -> {
                        warnningDialog.dismiss();
                        // 下发切换制式命令
                        InteractiveManager.getInstance().sendRequestModeSetting(modeSetting);
                        MyAlertDialog.getInstance(this).showLoading(getString(R.string.change_format_refresh_60s));
                    })))
                .create();
        }
        warnningDialog.show();

    }

    private Runnable settingOvertimeCheck = new Runnable() {
        @Override
        public void run() {
            MyAlertDialog.getInstance(InduceConfigActivity.this).loadingDiss();
            nextButton.setEnabled(true);
            ToastUtils.showToast("配置超时");
        }
    };

    public void jumpToNext() {

        MyAlertDialog.getInstance(this).loadingDiss();
        nextButton.setEnabled(true);
        // 移除超时检测
//        myHandler.removeCallbacks(settingOvertimeCheck);

        if (!deviceIsReady) {
            // 等待下一次点击再检查使用
            return;
        }

        synchronized (this){
            if (hasEntered){
                // 可能会多次进入
                return;
            } else {
                hasEntered = true;
            }
        }

        Intent intentBlueTooth = new Intent(context, FieldAnalysisActivity.class);
        intentBlueTooth.putExtra(Constants.Common.KEY_BACK_STACK, "目标号码制式");
        context.startActivity(intentBlueTooth);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case NUMBER_FOR_USER_INPUT:
                    // 获取号码制式的方法
                    manager.userInput(data);
                    break;
                case 12:
                    //设置国家码信息
                    manager.setCountryInfo(data);
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (group.equals(rgUnFindFormat)) {
            /* LTE_STANDARD    = (0x01)
            * ,GSM_STANDARD   = (0x02)
            * ,CDMA_STANDARD  = (0x03)
            * ,WCDMA_STANDARD = (0x04)
            * ,TD_STANDARD    = (0x05)
            */
            if (checkedId == rbGSM.getId()) {
                InduceConfigure.targetInduceFormat = TargetFormat.TARGET_FORMAT_GSM;
                modeSetting.setcMode_a((byte) 0x03);
            } else if (checkedId == rbCDMA.getId()) {
                InduceConfigure.targetInduceFormat = TargetFormat.TARGET_FORMAT_CDMA;
                modeSetting.setcMode_a((byte) 0x02);
            } else if (checkedId == rbTDS_CDMA.getId()) {
                InduceConfigure.targetInduceFormat = TargetFormat.TARGET_FORMAT_TDSCDMA;
                modeSetting.setcMode_a((byte) 0x05);
            } else if (checkedId == rbWCDMA.getId()) {
                InduceConfigure.targetInduceFormat = TargetFormat.TARGET_FORMAT_WCDMA;
                modeSetting.setcMode_a((byte) 0x04);
            } else if (checkedId == rbLTE.getId()) {
                InduceConfigure.targetInduceFormat = TargetFormat.TARGET_FORMAT_LTE;
                modeSetting.setcMode_a((byte) 0x01);
            }
        }
    }

    @Override
    protected void onDestroy() {
        usedDao.closeDataBase();
//		EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void goOnUpLoad() {
        // 还有文件未上传，继续上传
        //uoloadError();
    }

    @Override
    protected void onResume() {
//        String countryNum = tv_country.getText().toString();
//        InduceConfigure.countryNum = countryNum.substring(0, countryNum.length());
//        InduceConfigure.countryName = tv_countryName.getText().toString();
        UsedRecordDao.setStartTime();
        hasEntered = false;
        super.onResume();
    }

    @Override
    public void handleMessage(Message msg) {

    }

}
