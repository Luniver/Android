package hrst.sczd.ui.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hrst.common.ui.InduceConfigure;
import com.hrst.common.ui.RuntimeStatus;
import com.hrst.common.util.ToastUtils;

import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import hrst.sczd.ExitApplication;
import hrst.sczd.R;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_PEAK_AVERAGE_RATION;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_PEAK_AVERAGE_RATION_ACK;
import hrst.sczd.agreement.sczd.vo.PDA_TO_MCU_REQUEST_SENSIBLE_VALID_STATISTIC_ACK;
import hrst.sczd.ui.base.NavigationActivity;
import hrst.sczd.ui.manager.SensibilityManager;
import hrst.sczd.utils.ToastUtil;

public class SensibilityActivity extends NavigationActivity implements RadioGroup.OnCheckedChangeListener, TextWatcher {

    private ListView listView;
    private SensibilityManager manager;
    private EditText etFrequncy;
    private RadioButton rbThresHoldAuto;
    private EditText etFjbCus;
    private Button btnFjbStart;
    private Button btnSensibleStart;
    private RadioGroup switchGroup;
    private RadioGroup fjbGroup;
    private EditText etTimes;
    private ProgressDialog progressDialog;
    private RelativeLayout contentContainer;
    private boolean couldSaveData = false;
    private TextView tvPhoneFormat;

    @Override
    public void setContentView() {
        ExitApplication.getInstance().addActivity(this);
        setContentView(R.layout.activity_sensibility);
        setTitle("灵敏度测试");
        initView();
        initData();
    }

    private void initView() {
        contentContainer = findViewById(R.id.rl_content_container);
        listView = findViewById(R.id.lv_sensibility);
        etFrequncy = findViewById(R.id.et_freq);
        rbThresHoldAuto = findViewById(R.id.rb_auto);
        etFjbCus = findViewById(R.id.et_cus_fjb);
        btnFjbStart = findViewById(R.id.btn_fjb_start);
        btnSensibleStart = findViewById(R.id.btn_sensible_start);
        switchGroup = findViewById(R.id.rg_switch_group);
        fjbGroup = findViewById(R.id.rg_fjbmx_group);
        etTimes = findViewById(R.id.et_times);
        tvPhoneFormat = findViewById(R.id.tv_sys);
        tvPhoneFormat.setText(InduceConfigure.targetInduceFormat);
        // event
        switchGroup.setOnCheckedChangeListener(this);
        fjbGroup.setOnCheckedChangeListener(this);

        btnFjbStart.setOnClickListener(this);
        btnSensibleStart.setOnClickListener(this);

        etFjbCus.addTextChangedListener(this);
        etTimes.addTextChangedListener(this);
        etFrequncy.addTextChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            // 峰均比门限测试
            case R.id.btn_fjb_start:
                handleThresholdButtonClick();
                break;
            // 灵敏度百分比
            case R.id.btn_sensible_start:
                showProgress(1);
                enableSensibleStatistic(false);
                manager.startSensibleStatistic();
                break;
        }
    }

    private void handleThresholdButtonClick() {
        if (TextUtils.isEmpty(etFrequncy.getText().toString())) {
            ToastUtil.showToast(this, "频率的范围是1~9999");
            return;
        }
        if (TextUtils.isEmpty(etTimes.getText().toString())) {
            ToastUtil.showToast(this, "次数的范围是1~9999");
            return;
        }
        PDA_TO_MCU_REQUEST_PEAK_AVERAGE_RATION obj = new PDA_TO_MCU_REQUEST_PEAK_AVERAGE_RATION();
        // 下发为KHz
        obj.setFreqKhz_a((int) (Double.valueOf(etFrequncy.getText().toString()) * 1000));
        obj.setOpenOrclose_b((byte) 1);
        //LTE
        obj.setStandard_c((byte) 1);
        obj.setUsStatisticTimes_f(Integer.valueOf(etTimes.getText().toString()));
        if (rbThresHoldAuto.isChecked()) {
            obj.setStatisticMode_d((byte) 1);
        } else {
            obj.setStatisticMode_d((byte) 2);
            String threshold = etFjbCus.getText().toString();
            if (TextUtils.isEmpty(threshold)) {
                ToastUtil.showToast(this, "请填写门限值");
                return;
            }
            obj.setUcPeakAvgRatioThreshold_e(Integer.valueOf(threshold).shortValue());
        }
        manager.startThresholdStatistic(obj);
        showProgress(1);
        enableThresholdStatistic(false);
    }

    private void initData() {
        manager = new SensibilityManager(this);
//        EventBus.getDefault().register(this);

        switchGroup.check(RuntimeStatus.isSensibleStatisticOpen ? R.id.rb_statistic_open : R.id.rb_statistic_close);
        contentContainer.setVisibility(RuntimeStatus.isSensibleStatisticOpen ? View.VISIBLE : View.INVISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onThresholdAck(PDA_TO_MCU_REQUEST_PEAK_AVERAGE_RATION_ACK obj) {
        couldSaveData = true;
        manager.handleThresholdAck(obj);
    }

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onSensibleAck(PDA_TO_MCU_REQUEST_SENSIBLE_VALID_STATISTIC_ACK obj) {
        manager.handleSensibleAck(obj);
    }

    @Override
    public void handleMessage(Message msg) {

    }

    public void setAdapter(BaseAdapter adapter) {
        listView.setAdapter(adapter);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_statistic_close:
                RuntimeStatus.isSensibleStatisticOpen = false;
                contentContainer.setVisibility(View.INVISIBLE);
                manager.stopSensibleStatistic();
                break;
            case R.id.rb_statistic_open:
                RuntimeStatus.isSensibleStatisticOpen = true;
                contentContainer.setVisibility(View.VISIBLE);
                break;
            case R.id.rb_auto:
                btnFjbStart.setText(getString(R.string.start_statistic));
                etFjbCus.setEnabled(false);
                break;
            case R.id.rb_cus:
                btnFjbStart.setText(getString(R.string.set_threshold));
                etFjbCus.setEnabled(true);
                break;
        }
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
        if (txt.length() > 1) {
            float frequency = Float.valueOf(txt);
            if (txt.contains(".")){
                String decimal = txt.substring(txt.indexOf("."));
                if (decimal.length() > 3){
                    ToastUtils.showToast("小数点后不能超过两位");
                    txt = txt.substring(0, txt.length() - 1);
                    etFrequncy.setText(txt);
                    etFrequncy.setSelection(txt.length());
                    return;
                }
            }
            if (frequency > 9999) {
                ToastUtils.showToast("数值不能超过9999");
                txt = txt.substring(0, 4);
                if (etFrequncy.isFocused()) {
                    etFrequncy.setText(txt);
                    etFrequncy.setSelection(txt.length());
                }
                if (etTimes.isFocused()) {
                    etTimes.setText(txt);
                    etTimes.setSelection(txt.length());
                }
                if (etFjbCus.isFocused()) {
                    etFjbCus.setText(txt);
                    etFjbCus.setSelection(txt.length());
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
//        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 获取当前频率设置
     */
    public String getCurFrequency() {
        return etFrequncy.getText().toString().trim();
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
                enableThresholdStatistic(true);
                manager.stopSensibleStatistic();
                progressDialog.setProgress(0);
                progressDialog = null;
            });
        } else {
            progressDialog.setProgress(progress_a);
        }
        progressDialog.show();
    }

    public void enableThresholdStatistic(boolean enable) {
        btnFjbStart.setEnabled(enable);
    }

    public void enableSensibleStatistic(boolean enable) {
        btnSensibleStart.setEnabled(enable);
    }

    @Override
    public void onBackPressed() {
        // 没有数据可以保存
        if (!couldSaveData) {
            super.onBackPressed();
            return;
        }
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.setTitle("tips")
                .setMessage("是否保存此次测试数据到\n 手机存储/hrst/sczd/sensible/")
                .setNegativeButton("取消", (dialog1, which) -> SensibilityActivity.super.onBackPressed())
                .setPositiveButton("确定", (dialog12, which) -> {
                    manager.saveData2CsvFiles();
                    SensibilityActivity.super.onBackPressed();
                })
                .create()
                .show();
    }
}
