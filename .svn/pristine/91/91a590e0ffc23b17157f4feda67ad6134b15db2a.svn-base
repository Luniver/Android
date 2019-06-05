package hrst.sczd.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.hrst.common.ui.RuntimeStatus;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import hrst.sczd.ExitApplication;
import hrst.sczd.R;
import hrst.sczd.agreement.manager.InteractiveManager;
import hrst.sczd.agreement.sczd.vo.MCU_TO_PDA_U_DISK_INSERTION;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_CHECK_TF_KEEP_NUMBER_SIZE_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_KEEP_NUMBER_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_U_DISK_SIZE_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_U_DISK_STATE_ACK;
import hrst.sczd.ui.base.NavigationActivity;
import hrst.sczd.ui.manager.DebugDataAcquisitionManager;
import hrst.sczd.ui.manager.SkinSettingManager;
import hrst.sczd.utils.ToastUtil;

/**
 * 调试数据获取
 *
 * @author glj 2018-01-24
 */
public class DebugDataAcquisitionActivity extends NavigationActivity implements
        OnCheckedChangeListener {

    private DebugDataAcquisitionManager manager;

    /**
     * U盘显示layout
     */
    private LinearLayout uDiskLlayout;

    /**
     * 文本:当前制式
     */
    private TextView currentModeTxt;

    /**
     * 文本:TF可用空间
     */
    private TextView tfAvailableSpaceSizeTxt;

    /**
     * 文本:TF总空间
     */
    private TextView tfTotalSpaceSizeTxt;

    /**
     * 文本:U盘可用空间
     */
    private TextView uAvailableSpaceSizeTxt;

    /**
     * 文本:U盘总空间
     */
    private TextView uTotalSpaceSizeTxt;

    /**
     * 输入文本:帧数
     */
    private EditText framesNumEdt;

    /**
     * 按钮:获取
     */
    private Button getBtn;

    /**
     * rg:获取模式
     */
    private RadioGroup acquisitionModeRg;

    /**
     * rb:常规
     */
    private RadioButton routineRb;

    /**
     * rb:同步
     */
    private RadioButton synchronizationRb;

    /**
     * TF右箭头
     */
    private ImageView tfRightImg;

    /**
     * U盘可用空间进度条
     */
    private ProgressBar uProgress;

    /**
     * TF可用空间进度条
     */
    private ProgressBar tfProgress;

    /**
     * 选择获取数据模式 默认常规
     */
    private int selectMode = 0;

    /**
     * 当前帧数
     */
    private int framesNum;

    private ProgressDialog progressDialog;
    private RadioGroup acqusitionSwith;
    private LinearLayout acquisitionContainer;

    @Override
    public void setContentView() {
        ExitApplication.getInstance().addActivity(this);
        SkinSettingManager skinManager = new SkinSettingManager(this);
        if (skinManager.getCurrentSkinRes() == 0) {
            this.setTheme(android.R.style.Theme_Black_NoTitleBar);
        } else {
            this.setTheme(android.R.style.Theme_Light_NoTitleBar);
        }
        setContentView(R.layout.activity_bebug_data_acquisition);
        setTitle("调试数据获取");
        initView();
        initData();
        initListener();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        uDiskLlayout = $(R.id.llayout_u_disk);
        currentModeTxt = $(R.id.txt_current_mode);
        tfAvailableSpaceSizeTxt = $(R.id.txt_tf_available_space_size);
        tfTotalSpaceSizeTxt = $(R.id.txt_tf_total_space_size);
        uAvailableSpaceSizeTxt = $(R.id.txt_u_available_space_size);
        uTotalSpaceSizeTxt = $(R.id.txt_u_total_space_size);
        framesNumEdt = $(R.id.edt_frames_num);
        getBtn = $(R.id.btn_get);
        acquisitionModeRg = $(R.id.rg_acquisition_mode);
        acqusitionSwith = $(R.id.rg_acquisition_switch);
        acquisitionContainer = $(R.id.ll_save_container);
        routineRb = $(R.id.rb_routine);
        synchronizationRb = $(R.id.rb_synchronization);
        tfRightImg = $(R.id.img_tf_right);
        uProgress = $(R.id.progress_u);
        tfProgress = $(R.id.progress_tf);
    }

    /**
     * 初始化数据
     */
    private void initData() {
//		EventBus.getDefault().register(this);
        manager = new DebugDataAcquisitionManager(this);
        // 请求U盘状态
        InteractiveManager.getInstance().sendRequestUDiskState();
        // 请求TF大小
        InteractiveManager.getInstance().sendCheckTfKeepNumberSize();
        // 请求U盘大小
        InteractiveManager.getInstance().sendRequestUDiskSize();

//		InteractiveManager.getInstance(context).sendRequestUDiskFirmwareName();
//		InteractiveManager.getInstance(context).sendRequestClearTfKeepNumber();
//		InteractiveManager.getInstance(context).sendRequestUDiskFirmwareName();
//		InteractiveManager.getInstance(context).sendRequestVersion();
        manager.showCurrentMode(currentModeTxt);
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        acquisitionModeRg.setOnCheckedChangeListener(this);
        acqusitionSwith.setOnCheckedChangeListener(this);
        acqusitionSwith.check(RuntimeStatus.isDataCapturing ? R.id.rb_open : R.id.rb_close);
        getBtn.setOnClickListener(this);
        tfRightImg.setOnClickListener(this);
        framesNumEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String num = s.toString().trim();
                if (num.length() > 0) {
                    framesNum = Integer.valueOf(num);
                    if (!manager.checkParam(framesNum)) {
                        int end = (framesNum + "").length() - 1;
                        framesNumEdt.setText(num.substring(0, end));
                        framesNumEdt.setSelection(end);
                    }
                }else {
                    framesNum = 0;
                }
//                if (num.length() > 3) {
//                    Toast.makeText(context, getString(R.string.valid_integer), Toast.LENGTH_SHORT).show();
//                    framesNumEdt.setText(num.substring(0, 3));
//                    framesNumEdt.setSelection(3);
//                    return;
//                }
            }
        });
    }

    public void hideProgress() {
        if (progressDialog != null) {
            progressDialog.setProgress(0);
            progressDialog.dismiss();
        }
    }

    public void showProgress(int progress_a) {
        final int MAX_PROGRESS = 100;
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgress(10);
            progressDialog.setTitle("测试进度");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(MAX_PROGRESS);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.setButton(Dialog.BUTTON_NEGATIVE, "取消", (dialog, which) -> {
                progressDialog.setProgress(0);
                progressDialog = null;
            });
        } else {
            progressDialog.setProgress(progress_a);
        }
        progressDialog.show();
    }

    public void getTFSizeClick(View v) {
        //请求TF大小
        InteractiveManager.getInstance().sendCheckTfKeepNumberSize();
    }

    public void getUDiskSizeClick(View v) {
        // 请求U盘大小
        InteractiveManager.getInstance().sendRequestUDiskSize();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            // 获取
            case R.id.btn_get:
                manager.getModeInfo(selectMode, framesNum);
                break;

            // TF右箭头
            case R.id.img_tf_right:
                manager.toU_DiskKeepNum();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_open:
                RuntimeStatus.isDataCapturing = true;
                acquisitionContainer.setVisibility(View.VISIBLE);
                selectMode = routineRb.isChecked() ? 0x0 : 0x1;
                break;
            case R.id.rb_close:
                RuntimeStatus.isDataCapturing = false;
                acquisitionContainer.setVisibility(View.GONE);
                selectMode = 0xff;
                manager.getModeInfo(selectMode, framesNum);
                break;
            // 常规
            case R.id.rb_routine:
                selectMode = 0;
                break;
            // 同步
            case R.id.rb_synchronization:
                selectMode = 1;
                break;
        }
    }

    @Override
    public void handleMessage(Message msg) {

    }

    /**
     * 获取U盘容量信息
     *
     * @param obj
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getUDiskSizeInfo(PDA_TO_MCU_REQUEST_U_DISK_SIZE_ACK obj) {
        manager.showU_DiskSizeInfo(obj, uProgress, uAvailableSpaceSizeTxt,
                uTotalSpaceSizeTxt);
        ToastUtil.showToast(this, "U盘大小: " + obj.getUDiskRestSize_b() + "/" + obj.getUDiskSize_a());

    }

    /**
     * 获取TF容量信息
     *
     * @param obj
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getTFSizeInfo(PDA_TO_MCU_CHECK_TF_KEEP_NUMBER_SIZE_ACK obj) {
        ToastUtil.showToast(this, "tf size: " + obj.getTFTotalCapacity_a() + "/" + obj.getTFUsedCapacity_b());
        manager.showTFSizeInfo(obj, tfProgress, tfAvailableSpaceSizeTxt,
                tfTotalSpaceSizeTxt);
    }

    /**
     * 获取存数应答
     *
     * @param obj
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getKeepNumberAck(PDA_TO_MCU_REQUEST_KEEP_NUMBER_ACK obj) {
        manager.showKeepNumberAck(obj);
    }

    /**
     * U 盘状态
     *
     * @param obj
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getU_DiskState(PDA_TO_MCU_U_DISK_STATE_ACK obj) {
        ToastUtil.showToast(this, "u 盘state: " + obj.getcUDiskState_a());
    }

    /**
     * U盘插入/拔出提示
     *
     * @param obj
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getU_DiskInsertion(MCU_TO_PDA_U_DISK_INSERTION obj) {
        manager.showU_DiskInsertion(obj, uDiskLlayout);
    }

    /**
     * 初始化View工具
     *
     * @param id 绑定的Id
     * @return
     */
    private <T> T $(int id) {
        T view = (T) findViewById(id);
        return view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//		EventBus.getDefault().unregister(this);
    }

}
