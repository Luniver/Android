package hrst.sczd.ui.activity.fragment.fieldAnalysis;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.hrst.common.annotation.SimOperator;
import com.hrst.common.ui.InduceConfigure;
import com.hrst.common.ui.database.dao.OperateRecordDao;
import com.hrst.common.ui.interfaces.Constants;
import com.hrst.common.util.DualSimUtils;
import com.hrst.common.util.PhoneUtil;
import com.hrst.common.util.SharedPreferencesUtils;
import com.hrst.common.util.ToastUtils;
import com.hrst.induce.InduceFactory;
import com.hrst.induce.annotation.SendMethod;
import com.hrst.induce.interfaces.InduceCallBack;
import com.hrst.induce.sms.HrstSmsMessage;
import com.hrst.induce.utils.InduceConstants;
import com.orhanobut.logger.Logger;

import hrst.sczd.R;
import hrst.sczd.ui.adapter.DropEditTextBaseAdapter;
import hrst.sczd.utils.PlayRingtone;
import hrst.sczd.utils.ToastUtil;
import hrst.sczd.view.DropEditText;
import hrst.sczd.view.animation.PushDownAnimation;

public class FragmenDetection extends Fragment implements View.OnClickListener {

    private static final String TAG = "FragmenDetection";

    // 开关机设置
    private TextView tvLight, tv_detection_result;
    private ImageView ivLight;
    private EditText ed_notification_phone
//	, ed_detection_outtime
            ;
    private DropEditText ed_detection_outtime;
    // 开关机收缩动画
    private LinearLayout ll_setting_detection, ll_result_detection;
    private RelativeLayout rl_setting_detection_more, rl_result_detection_more,
            rl_result_detection_more_all;
    private ImageView iv_detection_more, iv_detection_remove,
            iv_detection_result_more;
    private String notifation_phone;
    private String detection_outtime;

    private ScrollView sv_induce;

    private PlayRingtone mPlayRingtone;

    /**
     * 操作记录存储
     */
    private OperateRecordDao operateRecordDao;

    private InduceFactory induceFactory = InduceFactory.create();

    /**
     * 是否已显示过开机结果
     */
    private Button btnStartDetect;
    private Button btnstopDetect;

    private int timeout;
    private boolean isDevelivered;

    Handler uiHandler = new Handler(Looper.getMainLooper());

    private boolean isSendSuccess;

    /**
     * 设置界面布局
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_startorup_sczd, container, false);
        initView(view);
        initLisener();
        return view;
    }

    /**
     * 初始化控件
     *
     * @param view
     */
    private void initView(View view) {
        operateRecordDao = new OperateRecordDao(getContext());
        tvLight = (TextView) view.findViewById(R.id.tv_ligth);
        ivLight = (ImageView) view.findViewById(R.id.iv_light);
        ed_notification_phone = (EditText) view.findViewById(R.id.ed_notification_phone);
        ed_detection_outtime = (DropEditText) view.findViewById(R.id.ed_detection_outtime);
        tv_detection_result = (TextView) view.findViewById(R.id.tv_detection_result);
        // 开关机收缩动画
        ll_setting_detection = (LinearLayout) view.findViewById(R.id.ll_setting_detection);
        ll_result_detection = (LinearLayout) view.findViewById(R.id.ll_result_detection);
        rl_result_detection_more_all = (RelativeLayout) view.findViewById(R.id.rl_result_detection_more_all);
        rl_setting_detection_more = (RelativeLayout) view.findViewById(R.id.rl_setting_detection_more);
        rl_result_detection_more = (RelativeLayout) view.findViewById(R.id.rl_result_detection_more);
        iv_detection_more = (ImageView) view.findViewById(R.id.iv_detection_more);
        iv_detection_remove = (ImageView) view.findViewById(R.id.iv_detection_remove);
        iv_detection_result_more = (ImageView) view.findViewById(R.id.iv_detection_result_more);
        sv_induce = (ScrollView) view.findViewById(R.id.sv_induce);
        btnStartDetect = view.findViewById(R.id.btn_detection);
        btnstopDetect = view.findViewById(R.id.btn_enddetection);
    }

    /**
     * 初始化监听
     */
    private void initLisener() {
        rl_setting_detection_more.setOnClickListener(this);
        rl_result_detection_more.setOnClickListener(this);
        iv_detection_remove.setOnClickListener(this);
        btnStartDetect.setOnClickListener(this);
        btnstopDetect.setOnClickListener(this);
        // 获取缓存通知号码
        notifation_phone = SharedPreferencesUtils.getString(Constants.Induce.KEY_NOTIFICATION_PHONE);
        if (notifation_phone.length() > 0) {
            ed_notification_phone.setText(notifation_phone);
        }
        //设置最多只能输入6位数字
        ed_detection_outtime.mEditText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        //设置初始值
        ed_detection_outtime.mEditText.setText(Constants.Induce.DEFAULT_DETECT_OVERTIME);
        //设置下拉的数据
        String[] mList = {"30000", "60000", "90000", "120000"};
        ed_detection_outtime.setAdapter(new DropEditTextBaseAdapter(getActivity(), mList));
        showText("");

        // 后续逻辑移动 onclick 事件中处理
    }

    /**
     * 开始播放声音
     *
     * @param rawId 音频文件
     */
    private void startAlert(int rawId) {
        if (mPlayRingtone != null) {
            mPlayRingtone.stopAlertRing();
            mPlayRingtone = null;
        }

        mPlayRingtone = new PlayRingtone(getActivity(), rawId);
        mPlayRingtone.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_detection:
                // 开始检测
                startDetect();
                break;
            case R.id.btn_enddetection:
                // 结束检测
                endDetect();
                break;
            case R.id.rl_setting_detection_more:
                animateDetectionSetting();
                break;
            case R.id.rl_result_detection_more:
                animateDetectionResult();
                break;
            case R.id.iv_detection_remove:
                removeDetectResult();
                break;
        }
    }

    /**
     * 清除检测结果
     */
    private void removeDetectResult() {
        if (TextUtils.isEmpty(tv_detection_result.getText())) {
            return;
        }
        new AlertDialog.Builder(getActivity())
                .setMessage("是否清除检测结果数据!")
                .setNegativeButton("取消", (dialog, which) -> {
                })
                .setPositiveButton("确定", (dialog, which) -> tv_detection_result.setText(""))
                .show();
    }

    /**
     * 检测结果栏动画
     */
    private void animateDetectionResult() {
        // 收缩开关机结果
        if (ll_result_detection.getVisibility() == View.GONE) {
            PushDownAnimation.putDownAnimation(ll_result_detection,
                    null, 500, ll_result_detection.getMeasuredHeight());
            iv_detection_result_more
                    .setBackgroundResource(R.drawable.up);
            PushDownAnimation.startAnimation(getActivity(),
                    iv_detection_result_more);
        } else {
            PushDownAnimation.putUpAnimation(ll_result_detection, null,
                    500);
            iv_detection_result_more
                    .setBackgroundResource(R.drawable.down);
            PushDownAnimation.startAnimation(getActivity(),
                    iv_detection_result_more);
        }
    }

    /**
     * 检测设置栏收缩动画
     */
    private void animateDetectionSetting() {
        // 收缩开关机设置
        if (ll_setting_detection.getVisibility() == View.GONE) {
            PushDownAnimation.putDownAnimation(ll_setting_detection,
                    rl_result_detection_more_all, 500, ll_setting_detection.getMeasuredHeight());
            iv_detection_more.setBackgroundResource(R.drawable.up);
            PushDownAnimation
                    .startAnimation(getActivity(), iv_detection_more);
        } else {
            PushDownAnimation.putUpAnimation(ll_setting_detection,
                    rl_result_detection_more_all, ll_setting_detection.getMeasuredHeight());
            iv_detection_more.setBackgroundResource(R.drawable.down);
            PushDownAnimation
                    .startAnimation(getActivity(), iv_detection_more);
        }
    }

    /**
     * 结束开关机检测
     */
    private void endDetect() {
        setLight(0);
        showText("此次开关机检测已终止！\n");
        induceFactory.stopInduce();
        setBtnState(false);
    }

    /**
     * 开始检测开关机
     */
    private void startDetect() {
        operateRecordDao.insertOperateRecord("检测",
                InduceConfigure.targetInduceFormat,
                InduceConfigure.targetPhone,
                "", "", "", "");
        // 检测是否满足诱发
        if (!canDetect()) {
            return;
        }
        isDevelivered = false;
        isSendSuccess = false;
        // 设置回调
        induceFactory.setInduceCallBack(new InduceCallBack() {
            @Override
            public void onInduceStart(int type) {
                showText("开始检测\n");
                setLight(1);
            }

            @Override
            public void onSend(String resutlCode) {
            }

            @Override
            public void onDelivered() {
                isDevelivered = true;
                Logger.d("sms is delivered!");
                // 开关机检测只有回执的时候, 才会认为开机, 否则发送成功, 也不认为成功
//                showText("目标号码：" + InduceConfigure.targetPhone + "已开机！\n");
                setLight(4);
            }

            @Override
            public void onInduceResult(int type, boolean isSuccess, String resultCode) {
                Logger.d("onInduceResult: " + "type: " + type + " success: " + isSuccess + " resultCode: " + resultCode);
                switch (resultCode) {
                    case InduceConstants.SMS_SENDDING_OK:
                        isSendSuccess = true;
                        uiHandler.postDelayed(() -> {
                            String msgContent;
                            if (isDevelivered){
                                // 开关机检测只有回执的时候, 才会认为开机, 否则发送成功, 也不认为成功
                                msgContent  = "目标号码：" + InduceConfigure.targetPhone + "已开机！\n";
                                setLight(4);
                                // 通知目标号码
                                if (!TextUtils.isEmpty(notifation_phone)){
                                    SmsManager.getDefault().sendTextMessage(notifation_phone, null, msgContent, null, null);
                                    ToastUtils.showToast("通知短信已送达");
                                }
                            }else {
                                msgContent = "目标号码：" + InduceConfigure.targetPhone + "已关机！\n";
                                setLight(5);
                            }
                            showText(msgContent);
                            setBtnState(false);
                        }, 8000);
                        break;
                    case InduceConstants.SMS_SENDDING_TIME_OUT:
                        showText("短信发送超时！\n");
                        setLight(2);
                        break;
                    default:
                        showText("目标号码：" + InduceConfigure.targetPhone + "已关机！\n");
                        setLight(5);
                        break;
                }
            }

            @Override
            public void onInduceFinish() {
                Logger.d("onInduceFinish");
//                showText("目标号码：" + InduceConfigure.targetPhone + "已关机！\n");
//                setLight(5);
                if (!isSendSuccess){
                    setBtnState(false);
                }
            }
        });

        // 开始检测
        induceFactory.addDetetionTask(InduceConfigure.targetPhone, buildSendMessage(), timeout, DualSimUtils.getDefaultCardId(InduceConfigure.targetPhone));
        induceFactory.startInduce();

        //按钮状态
        setBtnState(true);
    }

    /**
     * 构建发送信息
     */
    private HrstSmsMessage buildSendMessage() {
        // 获取需要诱发的短信内容
        HrstSmsMessage.Builder builder = new HrstSmsMessage.Builder()
                .setCountryCode(InduceConfigure.countryNum)
                .setSmsPduMode(InduceConfigure.pduMode)
                .setHasReceive(true)
                .setTargetFormat(InduceConfigure.targetInduceFormat)
                .setSendMethod(InduceConfigure.sendMethod);

        Logger.d("send message: " + builder.toString());
        // 常规短信需要回执
        builder.setHasReceive(SendMethod.METHOD_LTE_NORMAL.equals(InduceConfigure.sendMethod));
        return builder.build();
    }

    /**
     * 是否满足检测条件
     */
    private boolean canDetect() {
        if (induceFactory.isInducing()) {
            ToastUtils.showToast("正在诱发中, 请先停止诱发");
            return false;
        }
        switch (DualSimUtils.cardState) {
            case NO_CARD:
                ToastUtils.showToast("请装手机卡");
                return false;
            case ONLY_CARD_ONE:
                if (DualSimUtils.isTelecom(1)) {
                    if (!SimOperator.TELECOM.equals(InduceConfigure.targetPhoneOperator)) {
                        ToastUtils.showToast("电信卡只能诱发电信目标");
                        return false;
                    }
                }
                break;
            case ONLY_CARD_TWO:
                if (DualSimUtils.isTelecom(2)) {
                    if (!SimOperator.TELECOM.equals(InduceConfigure.targetPhoneOperator)) {
                        ToastUtils.showToast("电信卡只能诱发电信目标");
                        return false;
                    }
                }
                break;
        }
        if (!PhoneUtil.isMsgInduce()) {
            ToastUtil.showToast(getActivity(), "该手机型号暂不支持开关机检测功能!");
            return false;
        }
        notifation_phone = ed_notification_phone.getText().toString();
        detection_outtime = ed_detection_outtime.getText();
        if (TextUtils.isEmpty(detection_outtime)) {
            ToastUtil.showToast(getActivity(), "等待时间不能为空！");
            return false;
        }
        timeout = Integer.parseInt(detection_outtime);
        if (timeout > 999999 || timeout < 20000) {
            ToastUtil.showToast(getActivity(), R.string.detection_out_tip);
            return false;
        }
        if (detection_outtime.contains("-")) {
            ToastUtil.showToast(getActivity(), R.string.negative_tip);
            return false;
        }
        if (TextUtils.isEmpty(notifation_phone)) {
            notifation_phone = "";
        } else {
            if (!PhoneUtil.isPhoneNumber(notifation_phone)) {
                ToastUtils.showToast("请输入正确的通知手机号");
                return false;
            }
        }
        if (notifation_phone.equals(InduceConfigure.targetPhone)) {
            ToastUtil.showToast(getActivity(), R.string.notify_cant_target);
            return false;
        }
        SharedPreferencesUtils.put(Constants.Induce.KEY_NOTIFICATION_PHONE, notifation_phone);
        return true;
    }

    /**
     * 显示文本内容
     *
     * @param str
     */
    private void showText(String str) {
        tv_detection_result.append(str);
        sv_induce.fullScroll(ScrollView.FOCUS_DOWN);
    }

    /**
     * 设置指示灯状态
     */
    private void setLight(int state) {
        switch (state) {
            case 0:
                ivLight.setBackgroundResource(R.drawable.gray_light);
                tvLight.setText("未启动");
                break;
            case 1:
                ivLight.setBackgroundResource(R.drawable.gray_light);
                tvLight.setText("正在发送...");
                break;
            case 2:
                ivLight.setBackgroundResource(R.drawable.gray_light);
                tvLight.setText("发送失败");
                break;
            case 3:
                ivLight.setBackgroundResource(R.drawable.gray_light);
                tvLight.setText("正在检测...");
                break;
            case 4:
                ivLight.setBackgroundResource(R.drawable.green_light);
                tvLight.setText("开机");
                break;
            case 5:
                ivLight.setBackgroundResource(R.drawable.red_light);
                tvLight.setText("关机");
                break;
            case 6:
                ivLight.setBackgroundResource(R.drawable.red_light);
                tvLight.setText("检测失败");
            default:
                break;
        }
    }

    /**
     * 在退出这个页面之前保存数据
     */
    @Override
    public void onPause() {
        super.onPause();
    }

    /**
     * 设置按钮状态
     *
     * @param isDetecting 正在检测 true 反之 false
     */
    private void setBtnState(boolean isDetecting) {
        btnStartDetect.setEnabled(!isDetecting);
        btnstopDetect.setEnabled(isDetecting);
    }

    @Override
    public void onDestroy() {
        operateRecordDao.closeDataBase();
        super.onDestroy();
    }
}
