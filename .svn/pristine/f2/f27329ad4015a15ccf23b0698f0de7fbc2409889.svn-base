package hrst.sczd.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hrst.common.util.FileUtils;
import com.hrst.common.util.ToastUtils;
import com.orhanobut.logger.Logger;

import java.io.File;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import hrst.sczd.ExitApplication;
import hrst.sczd.R;
import hrst.sczd.agreement.manager.InteractiveManager;
import hrst.sczd.agreement.sczd.vo.REQUEST_FIRM_UPGRADE;
import hrst.sczd.agreement.sczd.vo.REQUEST_FIRM_UPGRADE_ACK;
import hrst.sczd.agreement.sczd.vo.REQUEST_FREQUENCY_SETTING_ACK;
import hrst.sczd.agreement.sczd.vo.REQUEST_QUERY_VERSION;
import hrst.sczd.agreement.sczd.vo.REQUEST_QUERY_VERSION_ACK;
import hrst.sczd.agreement.sczd.vo.REQUEST_SYSTEM_SELF_CHECK;
import hrst.sczd.agreement.sczd.vo.REQUEST_SYSTEM_SELF_CHECK_ACK;
import hrst.sczd.ui.base.NavigationActivity;
import hrst.sczd.ui.manager.SkinSettingManager;
import hrst.sczd.utils.log.IOPath;

/**
 * Created by Sophimp on 2019/1/9.
 */

public class Module433ManagerActivity extends NavigationActivity implements TextWatcher {

    /**
     * 固件升级超时处理
     */
    private static final int MSG_UPGRADE_OVERTIME = 0x24;
    private static final long OVERTIME = 1500;

    private EditText etFrequency;
    private TextView tvVersion;
    private File firmDir;
    private int currentFrameIndex = 0;
    private final int PACKET_LIMIT = 100;

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

    private int resendCount;

    @Override
    public void setContentView() {
        ExitApplication.getInstance().addActivity(this);
        SkinSettingManager skinManager = new SkinSettingManager(this);
        if (skinManager.getCurrentSkinRes() == 0) {
            this.setTheme(android.R.style.Theme_Black_NoTitleBar);
        } else {
            this.setTheme(android.R.style.Theme_Light_NoTitleBar);
        }
        setContentView(R.layout.activity_module_433);
        setTitle("433模块管理");

        initView();

        initData();
    }

    private void initData() {
        // 加载固件数据
        firmDir = new File(IOPath.FIRM_FILE_PATH);
        if (!firmDir.exists()) {
            firmDir.mkdirs();
        }
    }

    private void initView() {
        etFrequency = findViewById(R.id.et_freq);
        etFrequency.addTextChangedListener(this);
        tvVersion = findViewById(R.id.tv_version);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case MSG_UPGRADE_OVERTIME:
                ToastUtils.showToast("固件升级重传: " + currentFrameIndex + "帧");
                if (resendCount < 3){
                    InteractiveManager.getInstance().sendFirmUpgrade433(getCurrentUpgradeFrame());
                    myHandler.sendEmptyMessageDelayed(MSG_UPGRADE_OVERTIME, OVERTIME);
                }else {
                    resendCount = 0;
                }
                resendCount++;
                break;
        }

    }

    /**
     * 频点设置
     */
    public void frequencySettingClick(View view) {
        if (TextUtils.isEmpty(etFrequency.getText())) {
            ToastUtils.showToast("请填写频点");
            return;
        }

        float frequency = Float.valueOf(etFrequency.getText().toString());
        if (frequency < 420 || frequency > 435) {
            ToastUtils.showToast("数值范围为:420 ~ 435 MHZ");
        }
        InteractiveManager.getInstance().sendFrequencySetting433(Integer.valueOf(etFrequency.getText().toString()).shortValue());
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
        if (firmDir.listFiles().length > 1) {
            ToastUtils.showToast("请只放一个升级包");
        } else if (firmDir.listFiles().length == 1) {
            InteractiveManager.getInstance().sendFirmUpgrade433(getCurrentUpgradeFrame());
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
        if (firmBinData != null) {
            obj = new REQUEST_FIRM_UPGRADE();
            byte[] curFrame = packageData(firmBinData);
            obj.setUsCurFrame_b(currentFrameIndex);
            obj.setContent_c(curFrame);
            obj.setUsTotalFrame_a(totalFrames);
        }

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
//    public void onSystemSelfCheckAck(REQUEST_SYSTEM_SELF_CHECK obj) {
    public void onSystemSelfCheckAck(REQUEST_SYSTEM_SELF_CHECK_ACK obj) {
        switch (0) {
//        switch (obj.getUcResult_a()) {
            case 0:
                ToastUtils.showToast("设置成功");
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
        if (currentFrameIndex != obj.getUcCurFrame_b()){
            resendCount = 0;
        }
        currentFrameIndex = obj.getUcCurFrame_b();
        Logger.d("goo ack current frame: " + obj.getUcCurFrame_b());
        if (currentFrameIndex < totalFrames && isUpgrading) {
            // 设置进度
            showProgress(currentFrameIndex);
            InteractiveManager.getInstance().sendFirmUpgrade433(getCurrentUpgradeFrame());
            myHandler.removeMessages(MSG_UPGRADE_OVERTIME);
            myHandler.sendEmptyMessageDelayed(MSG_UPGRADE_OVERTIME, OVERTIME);
        } else {
            hideProgress();
            resetStatus();
        }
    }

    /**
     * 升级超时处理
     */
    private void upgradeOvertime() {
    }

    @SuppressLint("DefaultLocale")
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onVersionQueryAck(REQUEST_QUERY_VERSION_ACK obj) {
        Logger.d("goo version: " + obj);
        switch (obj.getUcState_a()) {
            case 0:
                String platform = "-";
                if (obj.getUcPlatform_b() == 0) {
                    platform = "WXSC_ZK_SCZD";
                } else if (obj.getUcPlatform_b() == 1) {
                    platform = "WXSC_ZK_WRJ";
                } else if (obj.getUcPlatform_b() == 2) {
                    platform = "WXSC_ZK_LTEDB";
                } else if (obj.getUcPlatform_b() == 3) {
                    platform = "WXSC_ZK_LTECZ";
                }
                tvVersion.setText(String.format("当前版本: %s%d.%d", platform,
                        obj.getUcVersion_c() >> 4 & 0xF, obj.getUcVersion_c() & 0xF));
                /*
                if(((obj.getUcSequence_g() >> 7) & 0x1) == 1){
                    tvVersion.setText(R.string.need_to_upgrade);
                }else {
                    tvVersion.setText(String.format("当前版本: %s%d.%d_%d_%d_%d", platform,
                            obj.getUcVersion_c() >> 4 & 0xF, obj.getUcVersion_c() & 0xF,
                            obj.getUcMonth_d(), obj.getUcDay_f(), obj.getUcSequence_g() & 0x7f));
                }
                */
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

    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.setProgress(0);
            progressDialog.dismiss();
        }
    }

    public void showProgress(int progress_a) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
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
            if (txt.contains(".")) {
                if (txt.length() > 5) {
                    ToastUtils.showToast("小数点后不能超过两位");
                    s.delete(3, txt.length());
                }
            }
        }
    }
}
