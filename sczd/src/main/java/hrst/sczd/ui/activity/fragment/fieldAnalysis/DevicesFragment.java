package hrst.sczd.ui.activity.fragment.fieldAnalysis;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hrst.common.ui.InduceConfigure;
import com.hrst.common.ui.interfaces.Constants;
import com.hrst.common.util.SharedPreferencesUtils;
import com.hrst.common.util.ToastUtils;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import hrst.sczd.R;
import hrst.sczd.agreement.sczd.vo.FPGA_TO_PDA_HEART;
import hrst.sczd.agreement.sczd.vo.MCU_TO_PDA_HEART;
import hrst.sczd.agreement.sczd.vo.MCU_TO_PDA_TEMPERATURE_AND_ELECTRICITY_STATE;
import hrst.sczd.ui.manager.DevicesManager;
import hrst.sczd.ui.manager.SkinSettingManager;
import hrst.sczd.view.LightView;

/**
 * @author 刘兰、沈月美
 * @version 1.1.1.3 修改者，修改日期，修改内容
 * @describe 场强分析--设备信息fragment类
 * @date 2014.05.23
 */
@SuppressLint("ValidFragment")
public class DevicesFragment extends Fragment {
    private static final String TAG = "goo-DevicesFragment";
    private TextView tvTargetphone;
    private TextView tvTargetName;
    private TextView tvTargetFormat;
    private TextView tvTemperature;
    private TextView tvElectricity;
    private LightView ivFpga, ivMcu;
    private AlertDialog netWorkDialog;
    private Button btnRegExit;
    private SkinSettingManager skinManager;
    private View view;
    /**
     * 记录出现弹窗的时间
     */
    private long oldTime;

    public boolean isShowDialog = false;

    private DevicesManager manager;
    private TextView tvDeviceStatus;

    /**
     * mcu 和 fpga 是否重新启动
     */
    private boolean isReboot = false;

    /**
     * 构造方法
     */
    public DevicesFragment() {
    }

    /**
     * 设置布局，标题与视觉模式
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = initView(inflater);
        getTargetInfo();
//        long startTime = System.currentTimeMillis();
        initData();
//        Log.d(TAG, "onCreateView: init time:" + (System.currentTimeMillis() - startTime));
        return view;
    }

    private View initView(LayoutInflater inflater) {
        View view = inflater.inflate(R.layout.fragment_devices_sczd, null);
        tvTargetphone = (TextView) view.findViewById(R.id.tv_targetphone_s);
        tvTargetName = (TextView) view.findViewById(R.id.tv_targetname_s);
        tvTargetFormat = (TextView) view.findViewById(R.id.tv_targetformat_s);
        tvTemperature = (TextView) view.findViewById(R.id.tv_temperature_s);
        tvElectricity = (TextView) view.findViewById(R.id.tv_electricity_s);
        ivFpga = (LightView) view.findViewById(R.id.iv_fpga_s);
        ivMcu = (LightView) view.findViewById(R.id.iv_mcu_s);
        tvDeviceStatus = view.findViewById(R.id.tv_info_s);
        skinManager = new SkinSettingManager(this.getActivity());
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 通过SharedPreferences文件获取目标信息
     */
    private void getTargetInfo() {
        tvTargetphone.setText(InduceConfigure.targetPhone);
        tvTargetName.setText(SharedPreferencesUtils.getString(Constants.Induce.KEY_TARGET_NAME));
        tvTargetFormat.setText(InduceConfigure.targetInduceFormat);

    }

    /**
     * 初始化数据
     */
    private void initData() {
        EventBus.getDefault().register(this);
        manager = new DevicesManager(getActivity());
        manager.init(tvTemperature, tvElectricity, ivFpga, ivMcu);
    }

    /**
     * 获取电量和温度信息
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getTemperatureAndElectricity(MCU_TO_PDA_TEMPERATURE_AND_ELECTRICITY_STATE obj) {
        manager.showTemperatureAndElectricity(obj);
    }

    /**
     * 获取Mcu心跳信息
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getMcuHeart(MCU_TO_PDA_HEART obj) {
        manager.showMcuHeart(obj);
        // 系统正常: 0
        // MCU启动中:1
        // FPGA不在线:2
        byte state = obj.getcHeartState_a();

        switch (state) {
            case -1:    // 状态错误
                // 闪烁
                ToastUtils.showToast(getString(R.string.device_status_error));
                ivMcu.setState(2);
                break;
            case 0:
                break;
            case 1:
                isReboot = true;
                ivMcu.setState(0);
                ToastUtils.showToast(getString(R.string.device_status_init));
                tvDeviceStatus.setText(getString(R.string.device_status_init));
                break;
            case 2:
                isReboot = true;
                ivMcu.setState(0);
                ToastUtils.showToast(getString(R.string.device_status_setting));
                tvDeviceStatus.setText(getString(R.string.device_status_setting));
//				MyAlertDialog.getInstance(context).showLoading("设备配置中");
                break;
            case 3:
                ivMcu.setState(1);
//                ToastUtils.showToast(getString(R.string.device_status_finished));
                tvDeviceStatus.setText(getString(R.string.device_status_finished));
//                if (isReboot) {
//                    new AlertDialog.Builder(getContext())
//                            .setTitle("提示")
//                            .setMessage("请重新配置参数")
//                            .setPositiveButton("确定", null)
//                            .show();
//                }
                isReboot = false;
                break;
            default:
                break;
        }
    }

    /**
     * 获取Fpga心跳信息
     */
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void getFpgaHeart(FPGA_TO_PDA_HEART obj) {
        manager.showFpgaHeart(obj);
    }

    /**
     * 界面销毁时执行
     */
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        manager.destory();
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 当单兵设备异常时，提示弹框
     *
     * @param message 提示内容
     * @param title   弹框标题
     */
    public void HintDialog(String message, String title) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialog_reg, null);
        TextView tvRegTitle = (TextView) view.findViewById(R.id.tv_reg_title);
        TextView tvRegMessage = (TextView) view.findViewById(R.id.tv_reg_message);
        tvRegTitle.setText(title);
        tvRegMessage.setText(message);
        netWorkDialog = new AlertDialog.Builder(getActivity()).create();
        netWorkDialog.setView(view);
        btnRegExit = (Button) view.findViewById(R.id.btn_reg_exit);
        btnRegExit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                isShowDialog = false;
                netWorkDialog.dismiss();
            }
        });
        if (!netWorkDialog.isShowing()) {
            netWorkDialog.show();
        }
    }

    /**
     * 判断是否显示弹窗
     *
     * @return
     */
    public boolean isShowDialog() {
        long nowTime = System.currentTimeMillis();
        long time = nowTime / 1000;
        if ((time - oldTime) > 30) {
            oldTime = time;
            return true;
        }
        return false;
    }
}
