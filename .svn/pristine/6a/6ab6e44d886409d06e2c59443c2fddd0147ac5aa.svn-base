package hrst.sczd.ui.activity.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hrst.common.util.FileUtils;
import com.hrst.common.util.ToastUtils;
import com.orhanobut.logger.Logger;

import java.io.File;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import hrst.sczd.R;
import hrst.sczd.agreement.manager.InteractiveManager;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_DT_MODE_SETTING;
import hrst.sczd.agreement.sczd.vo.REQUEST_FIRM_UPGRADE;
import hrst.sczd.agreement.sczd.vo.REQUEST_FIRM_UPGRADE_ACK;
import hrst.sczd.agreement.sczd.vo.REQUEST_FREQUENCY_SETTING_ACK;
import hrst.sczd.agreement.sczd.vo.REQUEST_QUERY_FLASH_VERSION_ACK;
import hrst.sczd.agreement.sczd.vo.REQUEST_QUERY_VERSION;
import hrst.sczd.agreement.sczd.vo.REQUEST_QUERY_VERSION_ACK;
import hrst.sczd.agreement.sczd.vo.REQUEST_SYSTEM_SELF_CHECK;
import hrst.sczd.agreement.sczd.vo.REQUEST_SYSTEM_SELF_CHECK_ACK;
import hrst.sczd.utils.SaveData_withPreferences;
import hrst.sczd.utils.WindowUtils;
import hrst.sczd.utils.log.IOPath;


/**
 * 数传模式配置界面
 *
 * @author glj
 */
public class DtModeConfDialog implements OnClickListener, TextWatcher {

//	private static DtModeConfDialog dialog = null;

    /**
     * 固件升级超时处理
     */
    private static final int MSG_UPGRADE_OVERTIME = 0x24;
    private static final long OVERTIME = 3000;

    private Context context;

    private final String NAME = "DTMODECONFDIALOG";

    private Dialog dtModeConfDialog;

    private Button btn_dt_mode_conf_ok, btn_dt_mode_conf_cancel;

//	private Spinner sp_tran_mode;

    private EditText edt_dt_mode_conf_freq;

    private RadioGroup rg_select_dt_mode;

//    private RadioButton rb_close_dt, rb_open_dt;

    private RelativeLayout dtSettingContainer;
    private RelativeLayout rl433Container;
    private RelativeLayout rl868Container;

    private SaveData_withPreferences sPreferences;// 快速存储

    private TextView tvVersion;
    private File firmDir;
    private int currentFrameIndex = 0;
    private final int PACKET_LIMIT = 200;

    /**
     * 待升级固件元数据
     */
    private byte[] firmBinData;

    /**
     * 总帧数
     */
    private int totalFrames;

    /**
     * 进度展示
     */
    private ProgressDialog progressDialog;

    /**
     * 升级标志
     */
    private boolean isUpgrading;

    private Handler myHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPGRADE_OVERTIME:
                    // 不需要重传了, 未收到就重新升级
                    /*
                    ToastUtils.showToast("固件升级重传: " + currentFrameIndex + "帧");
                    if (resendCount < 3) {
                        InteractiveManager.getInstance().sendFirmUpgrade433(getCurrentUpgradeFrame());
                        myHandler.sendEmptyMessageDelayed(MSG_UPGRADE_OVERTIME, OVERTIME);
                    } else {
                        resendCount = 0;
                    }
                    resendCount++;
                    */
                    hideProgress();
                    resetStatus();
                    showResult(context.getString(R.string.upgrade_failed));
                    break;
            }
            return true;
        }
    });
    private int frequency;

    public DtModeConfDialog(Context cn) {
        context = cn;
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData();

        initView();
    }

    private void initData() {
        // 加载固件数据
        firmDir = new File(IOPath.FIRM_FILE_PATH);
        if (!firmDir.exists()) {
            firmDir.mkdirs();
        }
    }

    /**
     * 初始化弹窗
     */
    public void initView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_dt_mode_conf, null);
        sPreferences = SaveData_withPreferences.getInstance(context);
        btn_dt_mode_conf_ok = (Button) view.findViewById(R.id.btn_dt_mode_conf_ok);
        btn_dt_mode_conf_cancel = (Button) view.findViewById(R.id.btn_dt_mode_conf_cancel);
//		sp_dt_mode = (Spinner) view.findViewById(R.id.sp_dt_mode);
//		sp_tran_mode = (Spinner) view.findViewById(R.id.sp_tran_mode);
        edt_dt_mode_conf_freq = (EditText) view.findViewById(R.id.edt_dt_mode_conf_freq);
        rg_select_dt_mode = (RadioGroup) view.findViewById(R.id.rg_select_dt_mode);
        // 433不需要关闭数传, 因为数传终端主要就是用来数传, 暂时先注释, 市场验证后, 可以去掉
//        rb_close_dt = (RadioButton) view.findViewById(R.id.rb_close_dt);
//        rb_open_dt = (RadioButton) view.findViewById(R.id.rb_open_dt);
        dtSettingContainer = view.findViewById(R.id.rl_dt_setting_containter);
        rl433Container = view.findViewById(R.id.rl_433_container);
        rl868Container = view.findViewById(R.id.rl_dt_868_container);
        edt_dt_mode_conf_freq.addTextChangedListener(this);
        tvVersion = view.findViewById(R.id.tv_version);
        readData();
        /*
        rg_select_dt_mode.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (group == rg_select_dt_mode) {

                    if (checkedId == rb_open_dt.getId()) {
                        dtSettingContainer.setVisibility(View.VISIBLE);
                    } else if (checkedId == rb_close_dt.getId()) {
                        dtSettingContainer.setVisibility(View.INVISIBLE);
                    }
                }

            }
        });
        */
        btn_dt_mode_conf_ok.setOnClickListener(this);
        btn_dt_mode_conf_cancel.setOnClickListener(this);

        view.findViewById(R.id.btn_system_self_check).setOnClickListener(this);
        view.findViewById(R.id.btn_version_query).setOnClickListener(this);
        view.findViewById(R.id.btn_mcu_firm_upgrade).setOnClickListener(this);
        view.findViewById(R.id.btn_frequency_setting).setOnClickListener(this);
        view.findViewById(R.id.btn_flash_version_query).setOnClickListener(this);

        dtModeConfDialog = WindowUtils.showDiyDialog(context, view);
    }

    public void show() {
        readData();
        resetStatus();
        dtModeConfDialog.show();
    }

    /**
     * 保存数据
     */
    private void saveData() {
//        sPreferences.saveDatas_Boolean(NAME, rb_open_dt.isChecked());
//		sPreferences.saveDatas(NAME+sp_dt_mode.getId(), sp_dt_mode.getSelectedItemPosition());
//		sPreferences.saveDatas(NAME+sp_tran_mode.getId(), sp_tran_mode.getSelectedItemPosition());
        sPreferences.saveDatas_String(NAME + edt_dt_mode_conf_freq.getId(), edt_dt_mode_conf_freq.getText().toString().trim());
    }

    /**
     * 读取数据
     */
    private void readData() {
        try {
            boolean open_dt = sPreferences.getData_Boolean(NAME);
            /*
            if (open_dt) {
                rb_open_dt.setChecked(true);
                dtSettingContainer.setVisibility(View.VISIBLE);
            } else {
                rb_close_dt.setChecked(true);
                dtSettingContainer.setVisibility(View.INVISIBLE);
            }
            */
            String dt_mode_conf_freq = sPreferences.getData_String(NAME + edt_dt_mode_conf_freq.getId());
            edt_dt_mode_conf_freq.setText(dt_mode_conf_freq);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return 0 868
     * 1 433
     * -1 未知
     */
    private int updateDtModeLayout() {
        if ((frequency > 420 && frequency < 435)) {
            rl868Container.setVisibility(View.INVISIBLE);
            rl433Container.setVisibility(View.VISIBLE);
            return 1;
        } else if ((frequency >= 452 && frequency <= 457) || (frequency >= 866 && frequency <= 870)) {
            rl868Container.setVisibility(View.VISIBLE);
            rl433Container.setVisibility(View.INVISIBLE);
            return 0;
        } else {
            return -1;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_frequency_setting:    // 频点下发
                frequencySettingClick(v);
                break;
            case R.id.btn_system_self_check:    // 433模块自检
                systemCheckClick(v);
                break;
            case R.id.btn_mcu_firm_upgrade:    // 433模块固件升级
                firmUpgradeClick(v);
                break;
            case R.id.btn_version_query:        // 433模块版本查询
                queryVersionClick(v);
                break;
            case R.id.btn_flash_version_query:        // 433模块版本查询
                queryFlashVersionClick(v);
                break;
            case R.id.btn_dt_mode_conf_ok:        // dialog 确定
                oncConfirmClick();
                break;
            case R.id.btn_dt_mode_conf_cancel:    // dialog 取消
                dtModeConfDialog.cancel();
                break;
        }
    }

    /**
     * 查询flash版本
     */
    private void queryFlashVersionClick(View v) {
        InteractiveManager.getInstance().sendQueryFlashVersion433();
    }

    /**
     * dialog 确定点击事件
     */
    private void oncConfirmClick() {
        // 关闭数传
//        if(rb_close_dt.isChecked()){
//            Logger.d("goo close dt mode");
//            sendDtModeToArm(0);
//        }
        //获取配置参数
        saveData();
        //关闭窗口
        dtModeConfDialog.cancel();
    }

    /**
     * 频点设置
     */
    public void frequencySettingClick(View view) {
        if (TextUtils.isEmpty(edt_dt_mode_conf_freq.getText())) {
            ToastUtils.showToast("请填写频点");
            return;
        }

        frequency = Integer.valueOf(edt_dt_mode_conf_freq.getText().toString());
        // 不管是868, 还是433 都需要向arm打开数传
        sendDtModeToArm(frequency);
        int dtMode = updateDtModeLayout();
        if (dtMode == 0) {
        } else if (dtMode == 1) {
            send433FrequencyCmd(frequency);
        } else {
            ToastUtils.showToast("433频率范围:420 ~ 435 MHZ \n868频率范围:452~457MHz, 866~870MHz!");
        }
    }

    private void sendDtModeToArm(int freq) {
        //设置实体类数据
        PDA_TO_MCU_DT_MODE_SETTING dtModeConf = new PDA_TO_MCU_DT_MODE_SETTING();
        dtModeConf.setfFreq_a(freq);
        // 0- 关闭 1-自主数传 2-数传模块(已弃用)
        // 如果频点为零， 关闭868数传
        dtModeConf.setUcDtMode_b((short) (freq == 0 ? 0 : 1));
        dtModeConf.setUcTranMode_c((short) 1);
        InteractiveManager.getInstance().sendDtModeSetting(dtModeConf);
    }

    private void send433FrequencyCmd(int freq) {
        InteractiveManager.getInstance().sendFrequencySetting433((short) freq);
    }

    /**
     * 系统自检
     */
    public void systemCheckClick(View view) {
        REQUEST_SYSTEM_SELF_CHECK obj = new REQUEST_SYSTEM_SELF_CHECK();
        InteractiveManager.getInstance().sendSystemSelfCheck433(obj);
    }

    /**
     * 固件升级
     */
    public void firmUpgradeClick(View view) {
        if (isUpgrading) {
            ToastUtils.showToast("正在升级, 请等待");
            return;
        } else {
            isUpgrading = true;
        }
        // 读取固件
        if (firmDir.listFiles().length == 0) {
            ToastUtils.showToast("未检测到升级包");
        } else if (firmDir.listFiles().length > 1) {
            ToastUtils.showToast("请只放一个升级包");
        } else if (firmDir.listFiles().length == 1) {
            InteractiveManager.getInstance().sendFirmUpgrade433(getCurrentUpgradeFrame());
            myHandler.sendEmptyMessageDelayed(MSG_UPGRADE_OVERTIME, OVERTIME);
            showProgress(currentFrameIndex);
        }
    }

    /**
     * 获取当前待升级帧数
     */
    private REQUEST_FIRM_UPGRADE getCurrentUpgradeFrame() {
        REQUEST_FIRM_UPGRADE obj = null;
        if (null == firmBinData) {
            firmBinData = FileUtils.file2byte(firmDir.listFiles()[0]);
            totalFrames = firmBinData.length / PACKET_LIMIT;
            if (totalFrames % PACKET_LIMIT > 0) {
                totalFrames += 1;
            }
        }
        // 开始升级
        obj = new REQUEST_FIRM_UPGRADE();
        byte[] curFrame = packageData(firmBinData);
        obj.setUsCurFrame_b(currentFrameIndex);
        obj.setContent_c(curFrame);
        obj.setUsTotalFrame_a(totalFrames);
        return obj;
    }

    /**
     * 分包操作
     */
    private byte[] packageData(byte[] data) {
        if (null == data) return null;
        byte[] result;
        // 一帧最大 1024 byte, 从零开始
        int srcIndex = currentFrameIndex * PACKET_LIMIT;
        int leftLen = data.length - srcIndex;
//        Logger.d("index: " + currentFrameIndex
//                + "\n srcIndex:" + srcIndex
//                + "\n dataLen" + data.length
//                + "\n leftLen: " + leftLen);
        // 每次传的数据包大小是固定的, 里面的数据不一定填满
        if (leftLen > PACKET_LIMIT) {
            // 分包
            result = new byte[PACKET_LIMIT];
            System.arraycopy(data, srcIndex, result, 0, result.length);
        } else {
            //最后一帧数据不为偶数时加ff
            if ((leftLen % 2) > 0) {
                result = new byte[leftLen + 1];
                result[leftLen] = (byte) 0xff;
            } else {
                result = new byte[leftLen];
            }
//            Logger.w("last frame length: " + leftLen
//                    + "\n index: " + currentFrameIndex
//                    + "\n startIndex: " + srcIndex
//                    + "\n result length: " + result.length);
            // 取包
            System.arraycopy(data, srcIndex, result, 0, leftLen);
        }
        return result;
    }

    /**
     * 版本查询
     */
    public void queryVersionClick(View view) {
        REQUEST_QUERY_VERSION obj = new REQUEST_QUERY_VERSION();
        InteractiveManager.getInstance().sendQueryVersion433(obj);
    }

    @Subscribe
    public void onFrequencySettingAck(REQUEST_FREQUENCY_SETTING_ACK obj) {
        switch (0) {
//        switch (obj.getUcResult_a()) {
            case 0:
                ToastUtils.showToast("设置成功");
                break;
            case 1:
                ToastUtils.showToast("频点越界");
                break;
            case 0XFF:
                ToastUtils.showToast("crc错误");
                break;
            default:
                ToastUtils.showToast("失败(未知错误)");
                break;
        }
    }

    @Subscribe
    public void onSystemSelfCheckAck(REQUEST_SYSTEM_SELF_CHECK_ACK obj) {
//        switch (0) {
        switch (obj.getUcResult_a()) {
            case 0:
                ToastUtils.showToast("系统正常");
                break;
            case 1:
                ToastUtils.showToast("无线芯片损坏");
                break;
            case 0XFF:
                ToastUtils.showToast("crc错误");
                break;
            default:
                ToastUtils.showToast("失败(未知错误)");
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onFirmUpgradeAck(REQUEST_FIRM_UPGRADE_ACK obj) {
        myHandler.removeMessages(MSG_UPGRADE_OVERTIME);
        currentFrameIndex = obj.getUcCurFrame_b();
        Logger.d("goo ack total frame: " + obj.getUcTotalFrame_a() + " current frame: " + obj.getUcCurFrame_b());
        // 如果升级出错, 全部重头开始升级
        if (obj.getState_c() != 0) {
            resetStatus();
            switch (obj.getState_c()) {
                case 1:
                    showResult(context.getString(R.string.upgrade_length_error));
                    break;
                case 2:
                    showResult(context.getString(R.string.upgrade_frame_error));
                    break;
                case 3:
                    showResult(context.getString(R.string.upgrade_flash_error));
                    break;
            }
            return;
        }
        if (obj.getUcCurFrame_b() < totalFrames) {
            // 设置进度
            showProgress(obj.getUcCurFrame_b());
            InteractiveManager.getInstance().sendFirmUpgrade433(getCurrentUpgradeFrame());
            myHandler.sendEmptyMessageDelayed(MSG_UPGRADE_OVERTIME, OVERTIME);
        } else {
            hideProgress();
            resetStatus();
            showResult(context.getString(R.string.upgrade_success));
        }
    }

    /**
     * 弹出升级结果
     *
     * @param msg 提示信息
     */
    private void showResult(String msg) {
        AlertDialog dialog =
                new AlertDialog.Builder(context)
                        .setTitle(R.string.tips)
                        .setMessage(msg)
                        .setCancelable(true)
                        .setPositiveButton(R.string.ok, (dialog1, which) -> {
                        })
                        .create();
        dialog.show();
    }

    @SuppressLint("DefaultLocale")
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onVersionQueryAck(REQUEST_QUERY_VERSION_ACK obj) {
        switch (obj.getUcState_a()) {
            case 0:
                String platform = "-";
                if (obj.getUcPlatform_b() == 0) {
                    platform = "WXSC_ZK_SCZD_";
                } else if (obj.getUcPlatform_b() == 1) {
                    platform = "WXSC_ZK_WRJ_";
                } else if (obj.getUcPlatform_b() == 2) {
                    platform = "WXSC_ZK_LTEDB_";
                } else if (obj.getUcPlatform_b() == 3) {
                    platform = "WXSC_ZK_LTECZ_";
                }

                Logger.d("goo version : " + obj.toString());
                if (((obj.getUcSequence_f() >> 7) & 0x1) == 1) {
                    tvVersion.setText(R.string.need_to_upgrade);
                } else {
                    tvVersion.setText(String.format("当前版本: %s%d.%d_%d月%d日_%d", platform,
                            obj.getUcVersion_c() >> 4 & 0xF, obj.getUcVersion_c() & 0xF,
                            obj.getUcMonth_d(), obj.getUcDay_e(), obj.getUcSequence_f() & 0x7f));
                }
//                tvVersion.setText(String.format("当前版本: %s%d.%d_", platform,
//                        obj.getUcVersion_c() >> 4 & 0xF, obj.getUcVersion_c() & 0xF));
                break;
            case 1:
                ToastUtils.showToast("查询有误");
                break;
            case 0XFF:
                ToastUtils.showToast("crc错误");
                break;
            default:
                ToastUtils.showToast("失败(未知错误)");
                break;
        }
    }

    @SuppressLint("DefaultLocale")
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onFlashVersionQueryAck(REQUEST_QUERY_FLASH_VERSION_ACK obj) {
        tvVersion.setText(String.format("flash版本: %d月%d日_%d", obj.getUcMonth_a(), obj.getUcDay_b(), obj.getUcSequence_c()));
    }

    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.setProgress(0);
            progressDialog.dismiss();
        }
    }

    public void showProgress(int progress_a) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgress(progress_a);
            progressDialog.setTitle("升级进度");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(totalFrames);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setButton(Dialog.BUTTON_NEGATIVE, "取消", (dialog, which) -> {
                progressDialog.setProgress(0);
                resetStatus();
            });
        } else {
            progressDialog.setProgress(progress_a);
        }
        progressDialog.show();
    }

    /**
     * 重置状态
     */
    private void resetStatus() {
        currentFrameIndex = 0;
        isUpgrading = false;
        myHandler.removeMessages(MSG_UPGRADE_OVERTIME);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String txt = s.toString().trim();
        if (!TextUtils.isEmpty(txt) && TextUtils.isDigitsOnly(txt)) {
            if (txt.length() > 3) {
                ToastUtils.showToast("433频率范围:420 ~ 435 MHZ \n868频率范围:452~457MHz, 866~870MHz!");
                s.delete(3, txt.length());
            }

            frequency = Integer.valueOf(txt);
            updateDtModeLayout();
        }
    }

    public void destory() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
